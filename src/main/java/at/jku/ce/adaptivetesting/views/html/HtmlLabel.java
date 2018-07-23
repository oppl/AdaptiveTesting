package at.jku.ce.adaptivetesting.views.html;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class HtmlLabel extends Label {
	public HtmlLabel() {
		this("");
	}

	public HtmlLabel(String content) {
		super(content, ContentMode.HTML);
	}

	public static HtmlLabel getCenteredLabel(String text) {
		return getCenteredLabel("div", text);
	}

	public static HtmlLabel getCenteredLabel(String tag, String text) {
		return new HtmlLabel(HtmlUtils.center(tag, text));
	}

	private static final long serialVersionUID = 3371602359798072688L;

}
