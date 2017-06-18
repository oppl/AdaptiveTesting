package at.jku.ce.adaptivetesting.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlProfitQuestion;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ProfitQuestion extends VerticalLayout implements
		IQuestion<ProfitDataStorage> {

	private static final long serialVersionUID = 6373936654529246422L;
	private ProfitDataStorage solution;
	private float difficulty = 0;
	private ComboBox answerSelector;
	private Label question;
	private Image questionImage = null;

	private String id;

	public ProfitQuestion(ProfitDataStorage solution, Float difficulty,
			String questionText, Image questionImage, String id) {
		this(solution, ProfitDataStorage.getEmptyDataStorage(), difficulty,
				questionText, questionImage, id);
	}

	public ProfitQuestion(ProfitDataStorage solution,
						  ProfitDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
		// super(1, 2);
		this.difficulty = difficulty;
		this.id = id;
		this.questionImage = questionImage;
		answerSelector = new ComboBox("Wählen Sie die richtige Antwort:");
		answerSelector.addItems((Object[]) ProfitPossibleAnswers.values());
		answerSelector.setSizeFull();
		answerSelector.setValue(prefilled.getValue());
		answerSelector.setEnabled(prefilled.getValue() == null);
		question = new HtmlLabel();
		setQuestionText(questionText);

		this.solution = solution;
		addComponent(question);
		if (questionImage != null) addComponent(this.questionImage);
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
		LogHelper.logInfo("Questionfile: " + id);
		if ((solution.equals(getUserAnswer()) ? 1d : 0d) == 1d) {
			LogHelper.logInfo("Correct answer");
			return 1.0d;
		} else {
			LogHelper.logInfo("Incorrect answer");
			return 0.0d;
		}
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

	public Image getQuestionImage() {
		return questionImage;
	}

	public void setQuestionImage(Image questionImage) {
		if (questionImage == null) return;
		this.questionImage = questionImage;
		removeAllComponents();
		addComponent(question);
		addComponent(this.questionImage);
		addComponent(answerSelector);

	}

}
