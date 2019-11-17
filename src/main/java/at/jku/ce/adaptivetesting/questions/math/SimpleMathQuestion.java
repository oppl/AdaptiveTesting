package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleMathQuestion extends VerticalLayout implements
        IQuestion<SimpleMathDataStorage>, Cloneable {

    private String id;
    private static final long serialVersionUID = 5932474069705038565L;
    private float difficulty = 0;
    private SimpleMathDataStorage solution;
    private Label question;
    private List<Image> questionImages;
    private GridLayout inputGrid;
    private int numberOfInputFields = 0;

    public SimpleMathQuestion() {
        this(SimpleMathDataStorage.getEmptyDataStorage(), 0f, "", null, "");
    }

    @Override
    public String getQuestionID() {
        return id;
    }

    public SimpleMathQuestion(SimpleMathDataStorage solution, float difficulty, String questionText, List<Image> questionImages, String id) {
        this.difficulty = difficulty;
        this.solution = solution;
        this.id = id;
        this.question = new HtmlLabel();
        setQuestionText(questionText);
        this.questionImages = questionImages;
        // Fill grid
        numberOfInputFields = solution.getAnswerElements().size();
        inputGrid = new GridLayout(1, numberOfInputFields);

        int i = 0;
        for (Map.Entry<String, String> entry : this.solution.getAnswerElements().entrySet()) {
            // inputGrid.addComponent(new Label(entry.getKey()), 0, i); // TODO : ? lösen
            inputGrid.addComponent(new TextField(entry.getKey()), 0, i);
            i++;
        }
        addComponent(question);
        if (questionImages != null) {
            for (Image image : this.questionImages) {
                addComponent(image);
            }
        }
        addComponent(inputGrid);
    }

    @Override
    public SimpleMathDataStorage getSolution() {
        return solution;
    }

    public SimpleMathQuestion clone() throws CloneNotSupportedException {
        SimpleMathQuestion objClone = (SimpleMathQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public SimpleMathQuestion cloneThroughSerialize(SimpleMathQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (SimpleMathQuestion)ois.readObject();
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
    public SimpleMathDataStorage getUserAnswer() {
        SimpleMathDataStorage dataStorage = new SimpleMathDataStorage();
        HashMap<String,String> userAnswers = new HashMap<>();
        for (int i = 0; i < numberOfInputFields; i++){
            userAnswers.put(
                    // ((Label) inputGrid.getComponent(0, i)).getValue(),
                    ((TextField) inputGrid.getComponent(0, i)).getCaption(),
                    ((TextField) inputGrid.getComponent(0, i)).getValue()
            );
        }
        dataStorage.setAnswerElements(userAnswers);
        return dataStorage;
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);
        SimpleMathDataStorage user = getUserAnswer(), solution = getSolution();

        double points = 0.0d;

        for (Map.Entry<String, String> entry : solution.getAnswerElements().entrySet()) {
            if(user.getAnswerElements().get(entry.getKey()).equals(entry.getValue())){
                points = points + (1.0d / numberOfInputFields);
            }
        }
        return points;
    }

    @Override
    public double performQueryDiagnosis() {
        return 0;
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public XmlQuestionData<SimpleMathDataStorage> toXMLRepresentation() {
        return new SimpleMathQuestionXml(getSolution(), getQuestionText(),
                getDifficulty());
    }

    @Override
    public double getMaxPoints() {
        return 1d;
    }

    @Override
    public String getQuestionText() {
        return question.getValue();
    }

    public void setQuestionText(String questionText) {
        question.setValue("<br />" + questionText + "<br />");
    }

    public void setQuestionImages(List<Image> questionImages) {
        if (questionImages == null) return;
        this.questionImages = questionImages;
        removeAllComponents();
        addComponent(question);
        for (Image image : this.questionImages) {
            addComponent(image);
        }
        addComponent(inputGrid);
    }

}

