package at.jku.ce.adaptivetesting.vaadin.ui;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.Calendar;
import java.util.GregorianCalendar;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class MainUI extends VerticalLayout implements View {

	private static final long serialVersionUID = 4966805861748123750L;

	public MainUI() {
		// Make the web-app large
		setSizeFull();
		// Set the layout for the bottom
		// Create a 3rd party licence button with a click listener
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
				// Disable sender
				event.getButton().setEnabled(false);
			}

		});
		Label copyright = new HtmlLabel("<i>© Reisisoft & JKU 2017 - "
				+ new GregorianCalendar().get(Calendar.YEAR) + "</i>");
		/* Button openLog = new Button("Open Log", (ClickListener) event -> {
			Navigator navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.Log.toString());

		});*/
		// Add the flowLayout at position 1 (second element) -> centered
		// Add everthing to flowlayout
		GridLayout southLayout = new GridLayout(3, 1);
		southLayout.setWidth("100%");
		southLayout.addComponent(licences, 0, 0);
		// southLayout.addComponent(openLog, 1, 0);
		southLayout.addComponent(copyright, 2, 0);
		// Add southlayout to the main Layout
		addComponent(southLayout);
		setComponentAlignment(southLayout, Alignment.BOTTOM_CENTER);
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
		VaadinUI.setCurrentPageTitle(event);
	}
}
