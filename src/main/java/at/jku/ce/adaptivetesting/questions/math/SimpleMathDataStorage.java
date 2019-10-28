package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleMathDataStorage extends AnswerStorage {
    private static final long serialVersionUID = -8179746363246548456L;
    @XmlElement(name = "answerElements")
    private HashMap<String,String> answerElements = new HashMap<>();

    public SimpleMathDataStorage() {
    }

    public static SimpleMathDataStorage getEmptyDataStorage() {
        return new SimpleMathDataStorage();
    }

    public HashMap<String, String> getAnswerElements() {
        return answerElements;
    }

    public void setAnswerElements(HashMap<String, String> answerElements) {
        this.answerElements = answerElements;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (String s: answerElements.keySet()) {
            buffer.append(s + ": " + answerElements.get(s));
        }
        return buffer.toString();
    }
}

