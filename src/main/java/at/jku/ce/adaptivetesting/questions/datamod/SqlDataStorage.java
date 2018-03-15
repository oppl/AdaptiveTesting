package at.jku.ce.adaptivetesting.questions.datamod;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Vector;

/**
 * Created by Peter, 13.03.2018
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlDataStorage extends AnswerStorage {

    private static final long serialVersionUID = -8179746363246548567L;

    @XmlElement(name = "keyword")
    private Vector<String[]> answers = new Vector<>();

    @XmlTransient
    private String enteredText = null;

    public static SqlDataStorage getEmptyDataStorage() {
        return new SqlDataStorage();
    }

    public SqlDataStorage() {
    }

    public Vector<String[]> getAnswers() {
        return answers;
    }

    public void setAnswers(Vector<String[]> answers) {
        this.answers = answers;
    }

    public void addAnswer(String[] answer) {
        answers.add(answer);
    }

    public String getEnteredText() {
        return enteredText;
    }

    public void setEnteredText(String enteredText) {
        this.enteredText = enteredText;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Expected Keywords:<ul> ");
        for (String[] answer: answers) {
            buffer.append("<li>");
            for (String variant: answer) {
                buffer.append(variant+", ");
            }
            buffer.append("</li>");
        }
        buffer.append("</ul>");
        if (enteredText != null) buffer.append("<p>"+enteredText+"</p>");
        return buffer.toString();
    }

}