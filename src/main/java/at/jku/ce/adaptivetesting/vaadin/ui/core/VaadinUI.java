package at.jku.ce.adaptivetesting.vaadin.ui.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import at.jku.ce.adaptivetesting.ProductData;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.ui.LogView;
import at.jku.ce.adaptivetesting.vaadin.ui.MainUI;
import at.jku.ce.adaptivetesting.vaadin.ui.QuestionManager;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingQuestionManager;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("vaadin")
@PreserveOnRefresh
@Title("Loading page...")
public class VaadinUI extends UI {
	private Navigator navigator;

	public VaadinUI() {
		// Set up the Navigator
		navigator = new Navigator(this, this);

		// Create Welcome Screen
		MainUI mainScreen = new MainUI();
		mainScreen.setMargin(true);
		Button start = new Button("Start", e -> {
			navigator.navigateTo(Views.TEST.toString());
		});
		start.setWidth("40%");
		start.setHeight("40%");
		//mainScreen.addComponent(new HtmlLabel(HtmlUtils.center("h1", "Willkommen zur " + productData)));
		mainScreen
				.addComponent(new HtmlLabel(HtmlUtils.center("h2",
						"Bitte klicke den <b>" + start.getCaption()
								+ "</b> Button um mit dem Rechnungswesentest zu beginnen!")));
		mainScreen.addComponent(start);
		mainScreen.setComponentAlignment(start, Alignment.MIDDLE_CENTER);

		navigator.addView(Views.DEFAULT.toString(), mainScreen);
		// Question view
		// Change this to the questionManager you like
		final QuestionManager manager = new AccountingQuestionManager(
				"Rechnungswesentest");
		navigator.addView(Views.TEST.toString(), manager);
		navigator.addView(Views.Log.toString(),
				new LogView(new File(Servlet.getLogFileName())));
		navigator.setErrorView(mainScreen);
		LogHelper.logInfo("Startup completed");
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = VaadinUI.class)
	public static class Servlet extends VaadinServlet {
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			// Get the question folder as defined in WEB-INF/web.xml
			questionFolderName = getServletConfig().getServletContext()
					.getInitParameter(questionFolderKey);
			File fQf = new File(questionFolderName);
			boolean isWorking = fQf.exists() && fQf.isDirectory()
					|| fQf.mkdirs();
			if (!isWorking) {
				questionFolderName = null;
			}
			// Get the log location as defined in WEB-INF/web.xml
			logLocation = getServletConfig().getServletContext()
					.getInitParameter(logLocKey);
			File fLog = new File(logLocation);
			try {
				isWorking = fLog.exists() && fLog.isFile()
						|| fLog.createNewFile();
			} catch (IOException e) {
				isWorking = false;
			}
			if (!isWorking) {
				logLocation = null;
			}
		}

		private static String questionFolderName = null, logLocation = null;
		private final static String questionFolderKey = "at.jku.ce.adaptivetesting.questionfolder";
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
		 * Gets the Log location
		 *
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
						+ VaadinUI.getProductData().getProduct()
						+ " v"
						+ VaadinUI.getProductData().getVersion());

	}

	@Override
	protected void init(VaadinRequest request) {

	}
}