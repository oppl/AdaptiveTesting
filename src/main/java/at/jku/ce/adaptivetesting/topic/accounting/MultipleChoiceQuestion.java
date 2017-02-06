package at.jku.ce.adaptivetesting.topic.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputFields;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputGrid;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlAccountingQuestion;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlMultipleChoiceQuestion;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlProfitQuestion;
import com.vaadin.ui.*;

import java.util.*;

/**
 * Created by oppl on 06/02/2017.
 */
public class MultipleChoiceQuestion extends VerticalLayout implements
        IQuestion<MultipleChoiceDataStorage> {

    private static final long serialVersionUID = 6373936654529246423L;
    private MultipleChoiceDataStorage solution;
    private float difficulty = 0;
    private Vector<CheckBox> answerSelector;
    private Label question;
    private Image questionImage = null;

    private String id;

    public MultipleChoiceQuestion(MultipleChoiceDataStorage solution, Float difficulty,
                          String questionText, Image questionImage, String id) {
        this(solution, MultipleChoiceDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImage, id);
    }

    public MultipleChoiceQuestion(MultipleChoiceDataStorage solution,
                                  MultipleChoiceDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
        // super(1, 2);
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;
        answerSelector = new Vector<>();

        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (questionImage != null) addComponent(this.questionImage);

        addComponent(new Label("Wählen Sie die richtige(n) Option(en):"));
        HashMap<Integer,String> answerOptions = solution.getAnswerOptions();
        for (Integer i: answerOptions.keySet()) {
            CheckBox checkBox = new CheckBox(answerOptions.get(i));
            checkBox.setData(i);
            if (prefilled.getCorrectAnswers().contains(i)) {
                checkBox.setValue(true);
            }
            if (prefilled.getCorrectAnswers().size()!=0) checkBox.setEnabled(false);
            answerSelector.add(checkBox);
            addComponent(checkBox);
        }
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
    public MultipleChoiceDataStorage getSolution() {
        return solution;
    }

    @Override
    public MultipleChoiceDataStorage getUserAnswer() {
        MultipleChoiceDataStorage multipleChoiceDataStorage = MultipleChoiceDataStorage.getEmptyDataStorage();
        Vector<Integer> checkedBoxes = new Vector<>();
        for (CheckBox checkBox: answerSelector) {
            if (checkBox.getValue() == true) checkedBoxes.add((Integer) checkBox.getData());
        }
        multipleChoiceDataStorage.setCorrectAnswers(checkedBoxes);
        return multipleChoiceDataStorage;
    }

    @Override
    public double checkUserAnswer() {
        MultipleChoiceDataStorage answer = getUserAnswer();
        MultipleChoiceDataStorage solution = getSolution();
        for (Integer checked: answer.getCorrectAnswers()) {
            if (!solution.getCorrectAnswers().contains(checked)) return 0.0d;
        }
        for (Integer unchecked: solution.getCorrectAnswers()) {
            if (!answer.getCorrectAnswers().contains(unchecked)) return 0.0d;
        }
        return 1.0d;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public XmlQuestionData<MultipleChoiceDataStorage> toXMLRepresentation() {
        return new XmlMultipleChoiceQuestion(getSolution(), getQuestionText(),
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
        for (CheckBox checkBox: answerSelector) addComponent(checkBox);
    }

}
