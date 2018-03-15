package at.jku.ce.adaptivetesting.vaadin.views.test.accounting.util;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.SingleComponentLayout;

import com.vaadin.ui.*;

public abstract class AccountingRecordInputGrid extends SingleComponentLayout {
	public enum Side {
		Right, Left
	}

	private static final long serialVersionUID = 3423260539583285740L;
	// Create all necessary layouts
	private VerticalLayout outer = new VerticalLayout();
	private GridLayout inner = new GridLayout(2, 2);
	private GridLayout right = new GridLayout(1, 8);
	private GridLayout left = new GridLayout(1, 8);
	private Label questionText = new HtmlLabel();
	private Image questionImage = null;

	public AccountingRecordInputGrid() {
		// Make the layout
		setSizeFull();
		outer.setSizeFull();
		addComponent(outer);
		outer.addComponent(questionText);
		outer.addComponent(inner);
		outer.setSpacing(true);
		inner.addComponent(new Label("In den Eingabeboxen die vier Stellen der Kontonummer eingeben!"), 0,0);
		inner.addComponent(left, 0, 1);
		inner.addComponent(right, 1, 1);
		// Make layout size full
		inner.setSizeFull();
		right.setSizeFull();
		left.setSizeFull();
		left.addComponent(new HtmlLabel("<b>Soll</b>"), 0, 0);
		right.addComponent(new HtmlLabel("<b>Haben</b>"), 0, 0);
	}

	public void addComponent(Component component, Side side, int position) {
		assert side != null;
		GridLayout current = side == Side.Right ? right : left;
		current.addComponent(component, 0, position + 1);
	}

	protected void setQuestionText(String text) {
		questionText.setValue("<br />" + text + "<br />");
	}

	protected String getQuestionText() {
		return questionText.getValue();
	}

	public Image getQuestionImage() {
		return questionImage;
	}

	public void setQuestionImage(Image questionImage) {
		this.replaceComponent(this.questionImage, questionImage);
		if (questionImage == null) return;
		this.questionImage = questionImage;
		outer.removeAllComponents();
		outer.addComponent(questionText);
		outer.addComponent(questionImage);
		outer.addComponent(inner);
		removeAllComponents();
		addComponent(outer);
	}
}
