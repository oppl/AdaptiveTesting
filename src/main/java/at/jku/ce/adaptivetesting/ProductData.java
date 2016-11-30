package at.jku.ce.adaptivetesting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public class ProductData {
	private final int mjVers = 0;
	private final int minVers = 5;
	private final int micVers = 2;
	private final String company = "JKU";
	private final String product = "Adaptive Testing Plattform";

	public int getMajor() {
		return mjVers;
	}

	public int getMinor() {
		return minVers;
	}

	public int getMicro() {
		return micVers;
	}

	public int getMjVers() {
		return mjVers;
	}

	public int getMinVers() {
		return minVers;
	}

	public int getMicVers() {
		return micVers;
	}

	public String getCompany() {
		return company;
	}

	public String getProduct() {
		return product;
	}

	@Override
	public String toString() {
		return getCompany() + ' ' + getProduct() + " v. " + getVersion();
	}

	public String getVersion() {
		return getMajor() + "." + getMinor() + '.' + getMicro();
	}

}
