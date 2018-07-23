package at.jku.ce.adaptivetesting.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

public class ProductData {
	private final int mjVers = 1;
	private final int minVers = 1;
	private final int micVers = 2;
	private final String company = "Johannes Kepler Universität";
	private final String companyShort = "JKU";
	private final String product = "Plattform für computerisiertes adaptives Testen (CAT)";
	private final String productShort = "CAT Plattform";

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

	public String getCompanyShort() {
		return companyShort;
	}

	@Override
	public String toString() {
		return getProduct();
	}

	public String getVersion() {
		return getMajor() + "." + getMinor() + '.' + getMicro();
	}
}
