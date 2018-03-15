package at.jku.ce.adaptivetesting.vaadin.views;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import at.jku.ce.adaptivetesting.core.LogHelper;

import com.google.common.io.Files;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class LogView extends Label implements View {
	private File file;

	public LogView(File file) {
		this.file = file;
		setContentMode(ContentMode.PREFORMATTED);

	}

	private static final long serialVersionUID = 5066697583930352899L;

	@Override
	public void enter(ViewChangeEvent event) {
		String fileContent;
		try {
			fileContent = Files.toString(file, Charset.defaultCharset());
		} catch (IOException e) {
			LogHelper.logThrowable(e);
			fileContent = "Could not load file due to:"
					+ e.getClass().getName() + " " + e.getMessage();
		}
		setValue(fileContent);
	}
}