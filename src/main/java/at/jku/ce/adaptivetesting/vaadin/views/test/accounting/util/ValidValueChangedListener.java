package at.jku.ce.adaptivetesting.vaadin.views.test.accounting.util;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public interface ValidValueChangedListener<T> {

	void accept(T changedValue);
}
