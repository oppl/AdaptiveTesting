package at.jku.ce.adaptivetesting.vaadin.ui;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.Arrays;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class MockQuestion<Question extends IQuestion<T> & Component, T extends AnswerStorage>
		extends VerticalLayout implements IQuestion<T> {

	private static final long serialVersionUID = 8387557147527424813L;
	private final Question question;
	private String questionText;

	public MockQuestion(Question question) {
		this.question = question;

		TextArea textArea = new TextArea("Question text");
		textArea.setSizeFull();

		// Download the result
		Button button = new Button("Display current user's solution");

		button.setSizeFull();
		button.addClickListener(e -> {
			Window window = new Window("Current user solution");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			window.setContent(layout);
			// Update questionText
			textArea.addTextChangeListener(ev -> questionText = ev.getText());
			textArea.setTextChangeEventMode(TextChangeEventMode.EAGER);
			Label label;
			try {
				label = new Label(toXML());
			} catch (Exception e1) {
				label = new Label(
						"<h1>Error parsing XML</h1><p>" + e1.getMessage()
								+ Arrays.toString(e1.getStackTrace()),
						ContentMode.HTML);
				LogHelper.logThrowable(e1);
			}
			layout.addComponent(label);
			window.center();
			getUI().addWindow(window);
		});
		// Add components to the UI
		addComponent(textArea);
		addComponent(question);
		addComponent(button);
	}

	@Override
	public T getSolution() {
		return question.getSolution();
	}

	@Override
	public T getUserAnswer() {
		return question.getUserAnswer();
	}

	@Override
	public double checkUserAnswer() {
		return question.checkUserAnswer();
	}

	@Override
	public float getDifficulty() {
		return question.getDifficulty();
	}

	@Override
	public XmlQuestionData<T> toXMLRepresentation() {
		XmlQuestionData<T> xml = question.toXMLRepresentation();
		xml.setQuestion(questionText != null ? questionText : "");
		xml.setDataStorage(getUserAnswer());
		return xml;
	}

	@Override
	public double getMaxPoints() {
		return question.getMaxPoints();
	}

	@Override
	public String getQuestionText() {
		return questionText;
	}
}
