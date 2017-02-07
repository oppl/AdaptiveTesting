package at.jku.ce.adaptivetesting.topic.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlOpenAnswerKeywordQuestion;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlProfitQuestion;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

import java.util.Vector;

/**
 * Created by oppl on 07/02/2017.
 */
public class OpenAnswerKeywordQuestion extends VerticalLayout implements
        IQuestion<OpenAnswerKeywordDataStorage> {

    private static final long serialVersionUID = 6373936654529246422L;
    private OpenAnswerKeywordDataStorage solution;
    private float difficulty = 0;
    private TextArea answer;
    private Label question;
    private Image questionImage = null;

    private String id;

    public OpenAnswerKeywordQuestion(OpenAnswerKeywordDataStorage solution, Float difficulty,
                                     String questionText, Image questionImage, String id) {
        this(solution, OpenAnswerKeywordDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImage, id);
    }

    public OpenAnswerKeywordQuestion(OpenAnswerKeywordDataStorage solution,
                                     OpenAnswerKeywordDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
        // super(1, 2);
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;
        answer = new TextArea("Bitte geben Sie Ihre Antwort ein:");
        answer.setColumns(50);
        answer.setRows(10);
        if (prefilled.getEnteredText() != null) {
            answer.setValue(prefilled.getEnteredText());
            answer.setEnabled(false);
        }
        else if (prefilled.getAnswers().size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Ihre Antwort muss folgende Schlüsselwörter enthalten:\n");
            for (String[] keywords: prefilled.getAnswers()) {
                buffer.append("Eines der folgdenden Wörter: ");
                for (String keyword: keywords) {
                    buffer.append(keyword);
                    if (!keywords[keywords.length-1].equals(keyword)) buffer.append(" ODER ");
                }
                if (!prefilled.getAnswers().lastElement().equals(keywords)) buffer.append(" UND\n");
            }
            answer.setValue(buffer.toString());
            answer.setEnabled(false);
        }
        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (questionImage != null) addComponent(this.questionImage);
        addComponent(answer);
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
    public OpenAnswerKeywordDataStorage getSolution() {
        return solution;
    }

    @Override
    public OpenAnswerKeywordDataStorage getUserAnswer() {
        OpenAnswerKeywordDataStorage userAnswer = new OpenAnswerKeywordDataStorage();
        userAnswer.setEnteredText(answer.getValue());
        userAnswer.setAnswers(solution.getAnswers());
        return userAnswer;
    }

    @Override
    public double checkUserAnswer() {
        String userAnswer = answer.getValue();
        for (String[] requriedKeyword: solution.getAnswers()) {
            boolean variantFound = false;
            for (String variant: requriedKeyword) {
                if (StringUtils.contains(userAnswer,variant)) variantFound = true;
            }
            if (!variantFound) return 0.0d;
        }

        return 1.0d;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public XmlQuestionData<OpenAnswerKeywordDataStorage> toXMLRepresentation() {
        return new XmlOpenAnswerKeywordQuestion(getSolution(), getQuestionText(),
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
        addComponent(answer);
    }
}