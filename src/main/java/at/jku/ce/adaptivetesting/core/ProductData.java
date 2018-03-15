package at.jku.ce.adaptivetesting.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

public class ProductData {
	private final int mjVers = 1;
	private final int minVers = 1;
	private final int micVers = 2;
	private final String company = "JKU";
	private final String product = "Computerized Adaptive Testing (CAT) Platform";
	private final String productShort = "CAT Platform";

	public int getMajor() {
		return mjVers;
	}

	public int getMinor() {
		return minVers;
	}

	public int getMicro() {
		return micVers;
	}

	public String getCompany() {
		return company;
	}

	public String getProduct() {
		return product;
	}

	public String getProductShort() {
		return productShort;
	}

	@Override
	public String toString() {
		return getProduct();
	}

	public String getVersion() {
		return getMajor() + "." + getMinor() + '.' + getMicro();
	}
}
