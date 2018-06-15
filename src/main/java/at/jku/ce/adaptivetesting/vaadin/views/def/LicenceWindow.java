package at.jku.ce.adaptivetesting.vaadin.views.def;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

import at.jku.ce.adaptivetesting.core.ProductData;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.core.html.HtmlLink;

import com.vaadin.ui.Label;
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
			"http://www.gnu.org/licenses/gpl.html", "GPL-3", true);

	public LicenceWindow() {
		super("Lizenzen");
		center();
		ProductData productData = new ProductData();
		// Layout for the window
		setContent(vLayout);
		vLayout.setMargin(true);
		Label empty = new Label("");
		empty.setHeight("1em");
		vLayout.addComponent(new HtmlLabel("<b>" + productData.getProductShort() + "</b>"));
		vLayout.addComponent(new Label("Version " + productData.getVersion()));
		// Add the 3rd party licences
		addLibraryLicence(new HtmlLink("https://vaadin.com/",
				"Vaadin Framework 7.7.4", true), apache2);
		addLibraryLicence(new HtmlLink("https://github.com/jbytecode/rcaller",
				"RCaller 3.0", true), lgpl3);
		addLibraryLicence(new HtmlLink("https://cran.r-project.org/web/packages/catR/index.html",
				"catR 3.4", true), gpl3);
		addLibraryLicence(new HtmlLink("http://jdbi.org/",
				"JDBI 3.3.0", true), apache2);
		addLibraryLicence(new HtmlLink("https://vaadin.com/directory/component/expandingtextarea",
				"ExpandingTextArea 1.2.0 (Vaadin Add-on)", true), apache2);
		vLayout.addComponent(empty);
		vLayout.addComponent(new HtmlLabel("Reisisoft, 2014"));
		vLayout.addComponent(new HtmlLabel(productData.getCompanyShort() + " - Institut für WIINF/CE, 2014-2018"));
		vLayout.addComponent(new HtmlLabel("Peter Baumann, BSc, 2018"));
	}

	public void addLibraryLicence(HtmlLink library, HtmlLink licence) {
		vLayout.addComponent(new HtmlLabel(library + " [" + licence + ']'));
	}

}
