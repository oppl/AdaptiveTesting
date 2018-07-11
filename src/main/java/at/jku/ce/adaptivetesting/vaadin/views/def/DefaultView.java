package at.jku.ce.adaptivetesting.vaadin.views.def;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import at.jku.ce.adaptivetesting.core.TestVariants;
import at.jku.ce.adaptivetesting.core.ProductData;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.core.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.views.*;
import at.jku.ce.adaptivetesting.vaadin.views.test.accounting.AccountingTestView;
import at.jku.ce.adaptivetesting.vaadin.views.test.adm.AdminView;
import at.jku.ce.adaptivetesting.vaadin.views.test.adm.LogView;
import at.jku.ce.adaptivetesting.vaadin.views.test.adm.ResultsDownloadView;
import at.jku.ce.adaptivetesting.vaadin.views.test.datamod.DatamodTestView;
import at.jku.ce.adaptivetesting.vaadin.views.test.TestView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
@Theme("vaadin")
@PreserveOnRefresh
@Title("Seite wird geladen...")
public class DefaultView extends UI {
	private Navigator navigator;
	private TestView manager;
	protected static final String PASSWD = "cat2018ce";

	public DefaultView() {
		navigator = new Navigator(this, this);

		navigator.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				if ((event.getNewView() instanceof AdminView ||
						event.getNewView() instanceof LogView ||
						event.getNewView() instanceof ResultsDownloadView) &&
						!(DefaultViewFooter.USERPWD.equals(PASSWD))) {
					createAndShowNotification("Zugang verweigert",
							"Bitte geben Sie das Passwort ein, um die Seite anzugeigen.",
							Notification.Type.ERROR_MESSAGE);
					String viewName = event.getNewView().getClass().getName();
					LogHelper.logError("Someone tried to enter " +
							viewName.substring(viewName.lastIndexOf('.')+1, viewName.length()) +
							" without authorization");
					return false;
				} else {
					return true;
				}
			}
			@Override
			public void afterViewChange(ViewChangeEvent event) {
			}
		});

		// Create Welcome Screen
		DefaultViewFooter mainScreen = new DefaultViewFooter();
		mainScreen.setMargin(true);
		Button start = new Button("Start", e -> {
			navigator.navigateTo(Views.TEST.toString());
		});
		start.setWidth(25, Unit.PERCENTAGE);
		start.setHeight(35, Unit.PERCENTAGE);
		start.setEnabled(false);
		Label empty = new Label("");
		empty.setHeight("1em");

		mainScreen.addComponent(empty);
		mainScreen.addComponent(new HtmlLabel(HtmlUtils.center("h1", productData.toString())));
		mainScreen.addComponent(new HtmlLabel(HtmlUtils.center("h2", "Bitte wähle einen Test aus und drücke auf den <b>" +
				start.getCaption() + "</b> Button, um damit zu beginnen!")));
		mainScreen.addComponent(new HtmlLabel(HtmlUtils.center("h3",
				"<i>Hinweis: Während des Tests darf die <b>Zurück-Taste</b> nicht zur Navigation " +
						"verwendet werden!</b>")));
		// Question view
		navigator.addView(Views.DEFAULT.toString(), mainScreen);

		String defaultValue = "-- Bitte auswählen --";
		String test1 = TestVariants.RW.toString();
		String test2 = TestVariants.SQL.toString();

		ComboBox box = new ComboBox("Testauswahl");
		box.addItems(defaultValue, test1, test2);
		box.addValueChangeListener(new Property.ValueChangeListener() {

			boolean testSelected = false;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				String testTypeFolder = "";

				if (event.getProperty().getValue().equals(test1)) {
					if (ConnectionProvider.connectionEstablished()) {
						ConnectionProvider.closeConnection();
					}
					manager = new AccountingTestView(test1);
					testTypeFolder = TestVariants.RW.getFolderName();
					testSelected = true;
					start.setEnabled(true);

				} else if (event.getProperty().getValue().equals(test2)) {

					manager = new DatamodTestView(test2);
					testTypeFolder = TestVariants.SQL.getFolderName();
					testSelected = true;
					start.setEnabled(true);

				} else if (event.getProperty().getValue().equals(defaultValue)) {
					start.setEnabled(false);
				}
				if (testSelected) {
					// initial load of questions for the student quiz
					manager.loadQuestions();
					// add views
					navigator.addView(Views.TEST.toString(), manager);
					navigator.addView(Views.ADMIN.toString(), new AdminView(manager, testTypeFolder));
					navigator.addView(Views.RESULTSDL.toString(), new ResultsDownloadView(manager, testTypeFolder));
					mainScreen.enableMenuButton();
					testSelected = false;
				}
			}
		});
		box.setWidth(20, Unit.PERCENTAGE);
		box.setValue(defaultValue);
		box.setNullSelectionAllowed(false);
		box.setFilteringMode(FilteringMode.CONTAINS);
		box.setEnabled(true);

		mainScreen.addComponent(box);
		mainScreen.setComponentAlignment(box, Alignment.MIDDLE_CENTER);
		mainScreen.addComponent(start);
		mainScreen.setComponentAlignment(start, Alignment.MIDDLE_CENTER);

		mainScreen.addComponent(empty);
		mainScreen.addComponent(empty);

		navigator.addView(Views.LOG.toString(), new LogView());
		navigator.setErrorView(mainScreen);

		LogHelper.logInfo("Startup completed");
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = DefaultView.class)
	public static class Servlet extends VaadinServlet {
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			// Get the question folder as defined in WEB-INF/web.xml
			questionFolderName = getServletConfig().getServletContext().getInitParameter(questionFolderKey);
			File fQf = new File(questionFolderName);
			boolean isWorking = fQf.exists() && fQf.isDirectory() || fQf.mkdirs();
			if (!isWorking) {
				questionFolderName = null;
			}
			// Get the result folder as defined in WEB-INF/web.xml
			resultFolderName = getServletConfig().getServletContext().getInitParameter(resultFolderKey);
			File fRf = new File(resultFolderName);
			isWorking = fRf.exists() && fRf.isDirectory() || fRf.mkdirs();
			if (!isWorking) {
				resultFolderName = null;
			}
			// Get the result folder as defined in WEB-INF/web.xml
			resourcesFolderName = getServletConfig().getServletContext().getInitParameter(resourcesFolderKey);
			File fRef = new File(resourcesFolderName);
			isWorking = fRef.exists() && fRef.isDirectory() || fRef.mkdirs();
			if (!isWorking) {
				resourcesFolderName = null;
			}
			// Get the image folder as defined in WEB-INF/web.xml
			imageFolderName = getServletConfig().getServletContext().getInitParameter(imageFolderKey);
			File fIf = new File(imageFolderName);
			isWorking = fIf.exists() && fIf.isDirectory() || fIf.mkdirs();
			if (!isWorking) {
				imageFolderName = null;
			}
			// Get the log location as defined in WEB-INF/web.xml
			logLocation = getServletConfig().getServletContext().getInitParameter(logLocKey);
			File fLog = new File(logLocation);
			try {
				isWorking = fLog.exists() && fLog.isFile() || fLog.createNewFile();
			} catch (IOException e) {
				isWorking = false;
			}
			if (!isWorking) {
				logLocation = null;
			}
		}

		private static String
				questionFolderName = null,
				resultFolderName = null,
				resourcesFolderName = null,
				imageFolderName = null,
				logLocation = null;
		private final static String questionFolderKey = "at.jku.ce.adaptivetesting.questionfolder";
		private final static String resultFolderKey = "at.jku.ce.adaptivetesting.resultfolder";
		private final static String resourcesFolderKey = "at.jku.ce.adaptivetesting.resourcesfolder";
		private final static String imageFolderKey = "at.jku.ce.adaptivetesting.imagefolder";
		private final static String logLocKey = "at.jku.ce.adaptivetesting.logfilepath";

		/**
		 * Gets the question folder name
		 *
		 * @return NULL if not set, or the String is not a valid folder / a
		 *         folder at this location could not be created.
		 */
		public static String getQuestionFolderName() {
			return questionFolderName;
		}

		/**
		 * Gets the result folder name
		 *
		 * @return NULL if not set, or the String is not a valid folder / a
		 *         folder at this location could not be created.
		 */
		public static String getResultFolderName() {
			return resultFolderName;
		}

		/**
		 * Gets the resources folder name
		 *
		 * @return NULL if not set, or the String is not a valid folder / a
		 *         folder at this location could not be created.
		 */
		public static String getResourcesFolderName() {
			return resourcesFolderName;
		}

		/**
		 * Gets the Log file name
		 * @return NULL if not set, or the String is not a valid file / a file
		 *         at this location could not be created.
		 */
		public static String getLogFileName() {
			return logLocation;
		}
	}

	private static ProductData productData = new ProductData();

	public static ProductData getProductData() {
		return productData;
	}

	public static void setCurrentPageTitle(ViewChangeEvent e) {
		Page.getCurrent().setTitle(
				(e.getViewName().length() == 0 ? Views.DEFAULT.toString() : e
						.getViewName())
						+ " - "
						+ DefaultView.getProductData().getProductShort()
						+ " v"
						+ DefaultView.getProductData().getVersion());
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().addUriFragmentChangedListener(
				new Page.UriFragmentChangedListener() {
					public void uriFragmentChanged(
							Page.UriFragmentChangedEvent source) {
						LogHelper.logInfo(source.getUriFragment());
						if (source.getUriFragment() == null) return;
						if (source.getUriFragment().equals("admin")) {
							navigator.navigateTo(Views.ADMIN.toString());
						}
						if (source.getUriFragment().equals("log")) {
							navigator.navigateTo(Views.LOG.toString());
						}
						if (source.getUriFragment().equals("download")) {
							navigator.navigateTo(Views.RESULTSDL.toString());
						}
						if (source.getUriFragment().equals("result")) {
							navigator.navigateTo(Views.RESULT.toString());
						}
					}
				});
	}

	private void createAndShowNotification(String caption, String description, Notification.Type type) {
		description += "<span style=\"position:fixed;top:0;left:0;width:100%;height:100%\"></span>";
		Notification notif = new Notification(caption, description, type, true);
		notif.setDelayMsec(-1);
		notif.show(Page.getCurrent());
	}
}