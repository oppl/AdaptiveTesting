package at.jku.ce.adaptivetesting.vaadin.views;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */

public enum Views {
	DEFAULT("default"), TEST("test"), RESULT("result"), LOG("log"), ADMIN("admin"), RESULTSDL("download");
	private String string;

	Views(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}

}
