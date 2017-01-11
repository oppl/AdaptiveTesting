package at.jku.ce.adaptivetesting.vaadin.ui.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public enum Views {
	DEFAULT("Start"), TEST("Test"), RESULT("Result"), Log("Log"), Admin("Admin");
	private String string;

	Views(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}

}
