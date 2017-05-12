package at.jku.ce.adaptivetesting.vaadin.ui;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.html.HtmlLink;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LicenceWindow extends Window {

	private static final long serialVersionUID = -7005783695341501851L;
	private VerticalLayout vLayout = new VerticalLayout();
	private final HtmlLink apache2 = new HtmlLink(
			"http://www.apache.org/licenses/LICENSE-2.0.html",
			"Apache 2.0 Lizenz", true);
	private final HtmlLink lgpl3 = new HtmlLink(
			"http://www.gnu.org/licenses/lgpl.html", "LGPL v3", true);
	private final HtmlLink gpl3 = new HtmlLink(
			"https://cran.r-project.org/web/licenses/GPL v3", "GPL-3", true);


	public LicenceWindow() {
		super("Licences");
		center();
		// Layout for the window
		setContent(vLayout);
		vLayout.setMargin(true);
		// Original Version Info
		vLayout.addComponent(new HtmlLabel("Originalversion erstellt durch Reisisoft (2014)"));
		vLayout.addComponent(new HtmlLabel("Weiterentwickelt durch JKU"));
		// Add the 3rd party licences
		addLibraryLicence(new HtmlLink("https://vaadin.com/",
				"Vaadin Framework", true), apache2);
		addLibraryLicence(new HtmlLink("https://github.com/jbytecode/rcaller",
				"RCaller 3.0", true), lgpl3);
		addLibraryLicence(new HtmlLink("https://cran.r-project.org/web/packages/catR/index.html",
				"catR 3.4", true), gpl3);
	}

	public void addLibraryLicence(HtmlLink library, HtmlLink licence) {
		vLayout.addComponent(new HtmlLabel(library + " ( " + licence + ')'));
	}

}
