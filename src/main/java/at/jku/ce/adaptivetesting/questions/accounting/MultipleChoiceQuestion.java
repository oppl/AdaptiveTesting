package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import com.vaadin.ui.*;

import java.io.*;
import java.util.*;

/**
 * Created by oppl on 06/02/2017.
 */
public class MultipleChoiceQuestion extends VerticalLayout implements
        IQuestion<MultipleChoiceDataStorage>, Cloneable {

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
        multipleChoiceDataStorage.setAnswerOptions(solution.getAnswerOptions());
        multipleChoiceDataStorage.setCorrectAnswers(checkedBoxes);
        return multipleChoiceDataStorage;
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);
        MultipleChoiceDataStorage answer = getUserAnswer();
        MultipleChoiceDataStorage solution = getSolution();
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
    public float getDifficulty() {
        return difficulty;
    }

    public MultipleChoiceQuestion clone() throws CloneNotSupportedException {
        MultipleChoiceQuestion objClone = (MultipleChoiceQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public MultipleChoiceQuestion cloneThroughSerialize(MultipleChoiceQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (MultipleChoiceQuestion)ois.readObject();
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
    public XmlQuestionData<MultipleChoiceDataStorage> toXMLRepresentation() {
        return new MultipleChoiceQuestionXml(getSolution(), getQuestionText(),
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
