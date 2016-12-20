package at.jku.ce.adaptivetesting.topic.bpmn;

import at.jku.ce.adaptivetesting.html.HtmlLabel;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

public class BPMNUserAnswer {
	
	/**
	 * topic: modeling
	 * class represents UserAnswer (also prepared for polytome answer)
	 * created by David Graf 06-2016
	 */
	
	private int elementId;
	private ComboBox answerSelector;
	private Label htmlBorder;
		
	public BPMNUserAnswer() {
		this(0,new ComboBox("Choose the right answer:"), new HtmlLabel());		
	}

	public BPMNUserAnswer(int id, ComboBox answerSelector, Label htmlBorder) {
		this.elementId = id;
		this.answerSelector = answerSelector;
		this.setHtmlBorder(htmlBorder);
	}

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public ComboBox getAnswerSelector() {
		return answerSelector;
	}

	public void setAnswerSelector(ComboBox answerSelector) {
		this.answerSelector = answerSelector;
	}

	public Label getHtmlBorder() {
		return htmlBorder;
	}

	public void setHtmlBorder(Label htmlBorder) {
		this.htmlBorder = htmlBorder;
	}
}
