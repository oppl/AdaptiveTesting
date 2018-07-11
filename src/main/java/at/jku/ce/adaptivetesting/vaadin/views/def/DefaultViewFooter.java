package at.jku.ce.adaptivetesting.vaadin.views.def;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

import java.io.File;

import at.jku.ce.adaptivetesting.core.LogHelper;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class DefaultViewFooter extends VerticalLayout implements View {

	private static final long serialVersionUID = 4966805861748123750L;
	private final String imageFolder = VaadinServlet.getCurrent().getServletConfig().
			getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";
	private Button menu;
	public static String USERPWD = "";

	public DefaultViewFooter() {
		setSizeFull();
		final Button licences = new Button("Lizenzen");
		licences.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 32642854872179636L;

			@Override
			public void buttonClick(ClickEvent event) {
				LogHelper.logInfo("Opened LicenceWindow");
				LicenceWindow licenceWindow = new LicenceWindow();
				licenceWindow.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 7874342882437355680L;

					@Override
					public void windowClose(CloseEvent e) {
						event.getButton().setEnabled(true);
					}
				});
				getUI().addWindow(licenceWindow);
				event.getButton().setEnabled(false);
			}
		});
		menu = new Button("Navigationsmenü");
		menu.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 32642854872179636L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (USERPWD.equals(DefaultView.PASSWD)) {
					loadMenuWindow(event);
				} else {
					LogHelper.logInfo("Opened PasswordWindow");
					PasswordWindow passwordWindow = new PasswordWindow();
					getUI().addWindow(passwordWindow);
				}
			}
		});
		menu.setEnabled(false);

		Link ce_jku = new Link(null,
				new ExternalResource("https://www.jku.at/institut-fuer-wirtschaftsinformatik-communications-engineering/"));
		ce_jku.setIcon(new FileResource(new File(imageFolder + "ce_jku_copyright_logo.png")));
		ce_jku.setTargetName("_blank");

		GridLayout southLayout = new GridLayout(10, 1);
		southLayout.setWidth("100%");
		southLayout.addComponent(licences, 0, 0);
		southLayout.addComponent(menu, 1, 0);
		southLayout.addComponent(ce_jku, 9, 0);
		southLayout.setComponentAlignment(ce_jku, Alignment.BOTTOM_RIGHT);
		addComponent(southLayout);
	}

	@Override
	public void addComponent(Component c) {
		int size = getComponentCount();
		if (size == 0) {
			super.addComponent(c);
		} else {
			addComponent(c, size - 1);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DefaultView.setCurrentPageTitle(event);
	}

	public void enableMenuButton () {
		menu.setEnabled(true);
	}

	private void loadMenuWindow (ClickEvent event) {
		LogHelper.logInfo("Opened MenuWindow");
		MenuWindow menuWindow = new MenuWindow();
		menuWindow.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 7874342882437355680L;

			@Override
			public void windowClose(Window.CloseEvent e) {
				event.getButton().setEnabled(true);
			}
		});
		menuWindow.deactivateStartButton();
		getUI().addWindow(menuWindow);
		event.getButton().setEnabled(false);
	}
}
