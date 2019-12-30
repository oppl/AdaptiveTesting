package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
public class MultipleChoiceMathDataStorage extends AnswerStorage {

    private static final long serialVersionUID = -8179746363246548457L;
    @XmlElement(name = "answerOptions")
    private HashMap<Integer,String> answerOptions = new HashMap<>();

    @XmlElement(name = "correctAnswers")
    private Vector<Integer> correctAnswers = new Vector<>();

    @XmlElement(name = "questionType")
    private String questionType;

    public MultipleChoiceMathDataStorage() {
    }

    public static at.jku.ce.adaptivetesting.questions.math.MultipleChoiceMathDataStorage getEmptyDataStorage() {
        return new at.jku.ce.adaptivetesting.questions.math.MultipleChoiceMathDataStorage();
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

    public String getQuestionType (){
        if(questionType == null || questionType.length() == 0){
            return "";
        } else {
            return questionType;
        }
    }

    public void setQuestionType (String questionType){
        this.questionType = questionType;
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
