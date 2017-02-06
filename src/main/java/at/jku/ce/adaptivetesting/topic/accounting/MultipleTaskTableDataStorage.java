package at.jku.ce.adaptivetesting.topic.accounting;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by oppl on 06/02/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MultipleTaskTableDataStorage extends AnswerStorage {

    private static final long serialVersionUID = -8179746363246548467L;

    @XmlElement(name = "answerColumns")
    private HashMap<Integer,String> answerColumns = new HashMap<>();

    @XmlElement(name = "tasks")
    private HashMap<Integer, String> tasks = new HashMap<>();

    @XmlElement(name = "correctAnswers")
    private HashMap<Integer, Float> correctAnswers = new HashMap<>();

    public static MultipleTaskTableDataStorage getEmptyDataStorage() {
        return new MultipleTaskTableDataStorage();
    }

    public MultipleTaskTableDataStorage() {
    }

    public HashMap<Integer, String> getAnswerColumns() {
        return answerColumns;
    }

    public void addAnswerColumns(Integer id, String answerColumn) {
        if (id.intValue() > 9 || id.intValue() < 0) return;
        this.answerColumns.put(id, answerColumn);
    }

    public void setAnswerColumns(HashMap<Integer, String> answerColumns) {
        this.answerColumns = answerColumns;
    }

    public HashMap<Integer, String> getTasks() {
        return tasks;
    }

    public void addTask(Integer id, String task) {
        if (id.intValue() < 1) return;
        this.tasks.put(id,task);
    }

    public void setTasks(HashMap<Integer, String> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, Float> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(HashMap<Integer, Float> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void addCorrectAnswer(Integer id, Float correctAnswer) {
        if (id.intValue()<10) return;
        this.correctAnswers.put(id,correctAnswer);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<table><th><td>Task</td>");
        for (Integer i: answerColumns.keySet()) {
            buffer.append("<td>"+answerColumns.get(i)+"</td>");
        }
        buffer.append("</th>");
        for (Integer i: tasks.keySet()) {
            buffer.append("<tr><td>"+tasks.get(i)+"</td>");
            for (Integer a: answerColumns.keySet()) {
                Float answer = correctAnswers.get(new Integer(i.intValue()*10+a.intValue()));
                if (answer != null) {
                    buffer.append("<td>"+new DecimalFormat("######.00").format(answer.floatValue())+"</td>");
                }
                else buffer.append("<td></td>");
            }
            buffer.append("</tr>");
        }
        buffer.append("</table>");
        return buffer.toString();
    }
}
