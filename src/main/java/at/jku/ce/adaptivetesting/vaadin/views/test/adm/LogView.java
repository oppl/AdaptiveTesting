package at.jku.ce.adaptivetesting.vaadin.views.test.adm;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

import java.io.*;
import java.nio.charset.Charset;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.vaadin.views.def.DefaultView;
import at.jku.ce.adaptivetesting.vaadin.views.def.MenuWindow;
import com.google.common.io.Files;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

public class LogView extends VerticalLayout implements View {
	Label log;

	public LogView() {
		setMargin(true);
		setSpacing(true);

		GridLayout gridLayout = new GridLayout(10, 1);
		gridLayout.setWidth("100%");

		Button logDownload = new Button("Download Log-File");
		logDownload.addClickListener( e -> {
			LogHelper.logInfo("Downloaded Log-File");
		});

		Button logUpdate = new Button("Update");
		logUpdate.addClickListener( e -> {
			update();
		});

		Button menu = new Button("Menü");
		menu.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 32642854872179636L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				LogHelper.logInfo("Opened MenuWindow");
				MenuWindow menuWindow = new MenuWindow();

				menuWindow.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 7874342882437355680L;

					@Override
					public void windowClose(Window.CloseEvent e) {
						event.getButton().setEnabled(true);
					}
				});
				menuWindow.deactivateLogButton();
				getUI().addWindow(menuWindow);
				event.getButton().setEnabled(false);
			}
		});

		gridLayout.addComponent(logDownload, 0, 0);
		gridLayout.addComponent(logUpdate, 1, 0);
		gridLayout.addComponent(menu, 9, 0);
		gridLayout.setComponentAlignment(menu, Alignment.BOTTOM_RIGHT);

		Resource res = new FileResource(new File(DefaultView.Servlet.getLogFileName()));
		FileDownloader fd = new FileDownloader(res);
		fd.extend(logDownload);

		log = new Label();
		log.setContentMode(ContentMode.PREFORMATTED);
		addComponent(log);

		addComponent(gridLayout);
		Label empty = new Label("");
		empty.setHeight("1em");
		addComponent(empty);
	}

	private static final long serialVersionUID = 5066697583930352899L;

	@Override
	public void enter(ViewChangeEvent event) {
		update();
	}

	private void update() {
		String fileContent;
		try {
			fileContent = Files.toString(new File(DefaultView.Servlet.getLogFileName()), Charset.defaultCharset());
		} catch (IOException e) {
			LogHelper.logThrowable(e);
			fileContent = "Could not load file due to:"
					+ e.getClass().getName() + " " + e.getMessage();
		}
		log.setValue(fileContent);
		// Scroll Down
		getUI().scrollIntoView(getComponent(getComponentCount()-1));
	}
}