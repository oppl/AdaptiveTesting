package at.jku.ce.adaptivetesting.views.html;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public class HtmlUtils {
	public static String center(String tag, String text) {
		StringBuilder sb = new StringBuilder();
		sb.append('<').append(tag).append(" style='text-align: center;'>");
		sb.append(text);
		sb.append("</").append(tag).append('>');
		return sb.toString();
	}

	public static String center(String text) {
		return center("div", text);
	}
}
