package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by oppl on 06/02/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class MultipleChoiceDataStorage extends AnswerStorage {

    private static final long serialVersionUID = -8179746363246548457L;
    @XmlElement(name = "answerOptions")
    private HashMap<Integer,String> answerOptions = new HashMap<>();

    @XmlElement(name = "correctAnswers")
    private Vector<Integer> correctAnswers = new Vector<>();

    public MultipleChoiceDataStorage() {
    }

    public static MultipleChoiceDataStorage getEmptyDataStorage() {
        return new MultipleChoiceDataStorage();
    }

    public HashMap<Integer, String> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(HashMap<Integer, String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public Vector<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Vector<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Integer i: answerOptions.keySet()) {
            if (correctAnswers.contains(i)) {
                buffer.append("[x] "+answerOptions.get(i)+"<br/>");
            }
            else {
                buffer.append("[ ] "+answerOptions.get(i)+"<br/>");
            }
        }
        return buffer.toString();
    }
}
