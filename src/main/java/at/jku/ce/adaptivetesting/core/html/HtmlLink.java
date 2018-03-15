package at.jku.ce.adaptivetesting.core.html;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public class HtmlLink {
	private boolean isOpenNewWindow;
	private String url, text;

	public HtmlLink(String url, String text, boolean openInNewWindow) {
		isOpenNewWindow = openInNewWindow;
		this.url = url;
		this.text = text;
	}

	public HtmlLink(String url, String text) {
		this(url, text, false);
	}

	public HtmlLink(String url) {
		this(url, url);
	}

	public boolean isOpenNewWindow() {
		return isOpenNewWindow;
	}

	public void setOpenNewWindow(boolean isOpenNewWindow) {
		this.isOpenNewWindow = isOpenNewWindow;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href='").append(url).append("'");
		if (isOpenNewWindow) {
			sb.append("target='_blank'");
		}
		sb.append('>');
		sb.append(text);
		sb.append("</a>");
		return sb.toString();
	}
}
