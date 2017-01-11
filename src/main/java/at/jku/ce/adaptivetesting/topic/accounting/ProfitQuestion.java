package at.jku.ce.adaptivetesting.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlProfitQuestion;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ProfitQuestion extends VerticalLayout implements
		IQuestion<ProfitDataStorage> {

	private static final long serialVersionUID = 6373936654529246422L;
	private ProfitDataStorage solution;
	private float difficulty = 0;
	private ComboBox answerSelector;
	private Label question;

	private String id;

	public ProfitQuestion(ProfitDataStorage solution, Float difficulty,
			String questionText, String id) {
		this(solution, ProfitDataStorage.getEmptyDataStorage(), difficulty,
				questionText, id);
	}

	public ProfitQuestion(ProfitDataStorage solution,
			ProfitDataStorage prefilled, float difficulty, String questionText, String id) {
		// super(1, 2);
		this.difficulty = difficulty;
		this.id = id;
		answerSelector = new ComboBox("Wählen Sie die richtige Antwort:");
		answerSelector.addItems((Object[]) ProfitPossibleAnswers.values());
		answerSelector.setSizeFull();
		answerSelector.setValue(prefilled.getValue());
		answerSelector.setEnabled(prefilled.getValue() == null);
		question = new HtmlLabel();
		setQuestionText(questionText);
		this.solution = solution;
		addComponent(question);
		addComponent(answerSelector);
		setSpacing(true);
	}

	@Override
	public String getQuestionID() {
		return id;
	}

	@Override
	public String getQuestionText() {
		return question.getValue();
	}

	public void setQuestionText(String questionText) {
		question.setValue("<br />" + questionText + "<br />");
	}

	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public ProfitDataStorage getSolution() {
		return solution;
	}

	@Override
	public ProfitDataStorage getUserAnswer() {
		return new ProfitDataStorage(
				(ProfitPossibleAnswers) answerSelector.getValue());
	}

	@Override
	public double checkUserAnswer() {
		return solution.equals(getUserAnswer()) ? 1d : 0d;
	}

	@Override
	public float getDifficulty() {
		return difficulty;
	}

	@Override
	public XmlQuestionData<ProfitDataStorage> toXMLRepresentation() {
		return new XmlProfitQuestion(getSolution(), getQuestionText(),
				getDifficulty());
	}

	@Override
	public double getMaxPoints() {
		return 1d;
	}

}
