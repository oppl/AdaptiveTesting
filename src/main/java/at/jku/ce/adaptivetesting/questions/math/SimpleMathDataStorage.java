package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleMathDataStorage extends AnswerStorage {
    private static final long serialVersionUID = -8179746363246548456L;
    @XmlElement(name = "answerElements")
    private HashMap<String,String> answerElements = new HashMap<>();

    @XmlElement(name = "questionType")
    private String questionType;

    @XmlElement(name = "correctSelection")
    private Vector<String> correctSelection = new Vector<>();

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

    public Vector<String> getCorrectSelection (){
        if(correctSelection == null || correctSelection.size() == 0){
            return new Vector<String>();
        } else {
            return correctSelection;
        }
    }

    public void setCorrectSelection (Vector<String> correctSelection){
        this.correctSelection = correctSelection;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (String s: answerElements.keySet()) {
            if(correctSelection != null && correctSelection.size() > 0) {
                buffer.append(s + " " + correctSelection.get(i) + " " + answerElements.get(s));
            } else {
                buffer.append(s + " " + answerElements.get(s));
            }
            i++;
        }
        return buffer.toString();
    }
}

