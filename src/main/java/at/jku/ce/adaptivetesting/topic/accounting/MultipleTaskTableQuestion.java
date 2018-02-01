package at.jku.ce.adaptivetesting.topic.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlMultipleTaskTableQuestion;
import com.vaadin.ui.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by oppl on 06/02/2017.
 */
public class MultipleTaskTableQuestion extends VerticalLayout implements
        IQuestion<MultipleTaskTableDataStorage>, Cloneable {

    private static final long serialVersionUID = 6373936654529246432L;
    private MultipleTaskTableDataStorage solution;
    private float difficulty = 0;
    private Table answerTable;
    private Set<TextField> entryBoxes;
    private Label question;
    private Image questionImage = null;

    private String id;

    public MultipleTaskTableQuestion(MultipleTaskTableDataStorage solution, Float difficulty,
                                     String questionText, Image questionImage, String id) {
        this(solution, MultipleTaskTableDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImage, id);
    }

    public MultipleTaskTableQuestion(MultipleTaskTableDataStorage solution,
                                     MultipleTaskTableDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
        // super(1, 2);
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;

        this.solution = solution;

        answerTable = new Table();
        answerTable.addStyleName("components-inside");

        answerTable.addContainerProperty("Aufgabe", HtmlLabel.class,null);
        answerTable.setColumnWidth("Aufgabe",300);
        for (Integer columnTitleId: solution.getAnswerColumns().keySet()) {
            answerTable.addContainerProperty(solution.getAnswerColumns().get(columnTitleId), TextField.class, null);
            answerTable.setColumnWidth(solution.getAnswerColumns().get(columnTitleId),200);
        }

        entryBoxes = new HashSet<>();
        for (Integer taskId: solution.getTasks().keySet()) {

            Vector<Object> tableRow = new Vector<>();

            tableRow.add(new HtmlLabel(solution.getTasks().get(taskId)));

            for (Integer columnId: solution.getAnswerColumns().keySet()) {
                TextField entry = new TextField();
                entry.setData(new Integer(taskId.intValue()*10+columnId.intValue()));
                Float prefilledAnswer = prefilled.getCorrectAnswers().get(new Integer(taskId.intValue()*10+columnId.intValue()));
                if (prefilledAnswer != null) {
                    entry.setValue(new DecimalFormat("######.00").format(prefilledAnswer.floatValue()));
                }
                tableRow.add(entry);
                entryBoxes.add(entry);
            }

            answerTable.addItem(tableRow.toArray(),
                    taskId);
        }
        answerTable.setPageLength(answerTable.size());
        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (questionImage != null) addComponent(this.questionImage);
        addComponent(answerTable);
        setSpacing(true);
    }

    @Override
    public String getQuestionID() {
        return id;
    }

    public MultipleTaskTableQuestion clone() throws CloneNotSupportedException {
        MultipleTaskTableQuestion objClone = (MultipleTaskTableQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public MultipleTaskTableQuestion cloneThroughSerialize(MultipleTaskTableQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (MultipleTaskTableQuestion)ois.readObject();
    }

    private static void serializeToOutputStream(Serializable ser, OutputStream os)
            throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(ser);
            oos.flush();
        } finally {
            oos.close();
        }
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
    public MultipleTaskTableDataStorage getSolution() {
        return solution;
    }

    @Override
    public MultipleTaskTableDataStorage getUserAnswer() {
        MultipleTaskTableDataStorage userAnswer = new MultipleTaskTableDataStorage();
        userAnswer.setAnswerColumns(solution.getAnswerColumns());
        userAnswer.setTasks(solution.getTasks());
        for (TextField entryBox: entryBoxes) {
            Float entry = null;
            try {
                String entryText = entryBox.getValue().replace(',','.');
                entry = Float.parseFloat(entryText);
            }
            catch (Exception e) {
//                Notification.show("\""+entryBox.getValue()+"\" ist keine zulässige Eingabe!", Notification.Type.ERROR_MESSAGE);
            }
            if (entry!=null) userAnswer.addCorrectAnswer((Integer) entryBox.getData(),entry);
        }
        return userAnswer;
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);
        MultipleTaskTableDataStorage userAnswer = getUserAnswer();
        if (userAnswer.getCorrectAnswers().keySet().size() != this.solution.getCorrectAnswers().keySet().size()) return 0d;
        for (Integer answerID: userAnswer.getCorrectAnswers().keySet()) {
            int answer = Math.round(userAnswer.getCorrectAnswers().get(answerID).floatValue()*100);
            int solution = Math.round(this.solution.getCorrectAnswers().get(answerID).floatValue()*100);
            if (answer != solution) {
                LogHelper.logInfo("Incorrect answer");
                return 0d;
            }
        }
        LogHelper.logInfo("Correct answer");
        return 1d;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public XmlQuestionData<MultipleTaskTableDataStorage> toXMLRepresentation() {
        return new XmlMultipleTaskTableQuestion(getSolution(), getQuestionText(),
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
        addComponent(answerTable);
    }

}
