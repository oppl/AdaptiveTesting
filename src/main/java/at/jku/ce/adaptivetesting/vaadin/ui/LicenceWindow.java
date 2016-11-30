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

	public LicenceWindow() {
		super("Licences");
		center();
		// Layout for the window
		setContent(vLayout);
		vLayout.setMargin(true);
		vLayout.addComponent(new HtmlLabel("Originalversion erstellt 2014 von Florian Reisinger."));
		// Add the 3rd party licences
		addLibraryLicence(new HtmlLink("https://vaadin.com/",
				"Vaadin Framework", true), apache2);
		addLibraryLicence(new HtmlLink("http://www.mhsatman.com/rcaller.php",
				"RCaller 2.0", true), lgpl3);

	}

	public void addLibraryLicence(HtmlLink library, HtmlLink licence) {
		vLayout.addComponent(new HtmlLabel(library + " ( " + licence + ')'));
	}

}
