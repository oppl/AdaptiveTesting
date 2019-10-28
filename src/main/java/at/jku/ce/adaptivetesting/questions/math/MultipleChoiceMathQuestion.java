package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class MultipleChoiceMathQuestion extends VerticalLayout implements
        IQuestion<MultipleChoiceMathDataStorage>, Cloneable {

    private static final long serialVersionUID = 6373936654529246428L;
    private MultipleChoiceMathDataStorage solution;
    private float difficulty = 0;
    private Vector<CheckBox> answerSelector;
    private Label question;
    private Image questionImage = null;

    private String id;

    public MultipleChoiceMathQuestion(MultipleChoiceMathDataStorage solution, Float difficulty,
                                  String questionText, Image questionImage, String id) {
        this(solution, MultipleChoiceMathDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImage, id);
    }

    public MultipleChoiceMathQuestion(MultipleChoiceMathDataStorage solution,
                                  MultipleChoiceMathDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;
        answerSelector = new Vector<>();

        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (questionImage != null) addComponent(this.questionImage);

        addComponent(new Label("Wähle die richtige(n) Option(en):"));
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
    public MultipleChoiceMathDataStorage getSolution() {
        return solution;
    }

    @Override
    public MultipleChoiceMathDataStorage getUserAnswer() {
        MultipleChoiceMathDataStorage multipleChoiceMathDataStorage = MultipleChoiceMathDataStorage.getEmptyDataStorage();
        Vector<Integer> checkedBoxes = new Vector<>();
        for (CheckBox checkBox: answerSelector) {
            if (checkBox.getValue() == true) checkedBoxes.add((Integer) checkBox.getData());
        }
        multipleChoiceMathDataStorage.setAnswerOptions(solution.getAnswerOptions());
        multipleChoiceMathDataStorage.setCorrectAnswers(checkedBoxes);
        return multipleChoiceMathDataStorage;
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);
        MultipleChoiceMathDataStorage answer = getUserAnswer();
        MultipleChoiceMathDataStorage solution = getSolution();
        for (Integer checked: answer.getCorrectAnswers()) {
            if (!solution.getCorrectAnswers().contains(checked)) {
                LogHelper.logInfo("Incorrect answer");
                return 0.0d;
            }
        }
        for (Integer unchecked: solution.getCorrectAnswers()) {
            if (!answer.getCorrectAnswers().contains(unchecked)) {
                LogHelper.logInfo("Incorrect answer");
                return 0.0d;
            }
        }
        LogHelper.logInfo("Correct answer");
        return 1.0d;
    }

    @Override
    public double performQueryDiagnosis() {
        return 0;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    public MultipleChoiceMathQuestion clone() throws CloneNotSupportedException {
        MultipleChoiceMathQuestion objClone = (MultipleChoiceMathQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public MultipleChoiceMathQuestion cloneThroughSerialize(MultipleChoiceMathQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (MultipleChoiceMathQuestion)ois.readObject();
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
    public XmlQuestionData<MultipleChoiceMathDataStorage> toXMLRepresentation() {
        return new MultipleChoiceMathQuestionXml(getSolution(), getQuestionText(),
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
