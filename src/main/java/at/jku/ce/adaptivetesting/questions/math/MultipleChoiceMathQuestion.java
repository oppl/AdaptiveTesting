package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.*;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;

public class MultipleChoiceMathQuestion extends VerticalLayout implements
        IQuestion<MultipleChoiceMathDataStorage>, Cloneable {

    private static final long serialVersionUID = 6373936654529246428L;
    private MultipleChoiceMathDataStorage solution;
    private float difficulty = 0;
    private Vector<CheckBox> answerSelector;
    private Label question;
    private List<Image> questionImages;
    private List<Image> answerOptionImages;

    private String id;

    public MultipleChoiceMathQuestion(MultipleChoiceMathDataStorage solution, Float difficulty,
                                  String questionText, List<Image> questionImages, List<Image> answerOptionImages, String id) {
        this(solution, MultipleChoiceMathDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImages, answerOptionImages, id);
    }

    public MultipleChoiceMathQuestion(MultipleChoiceMathDataStorage solution,
                                  MultipleChoiceMathDataStorage prefilled, float difficulty, String questionText, List<Image> questionImages, List<Image> answerOptionImages, String id) {
        this.difficulty = difficulty;
        this.id = id;
        this.questionImages = questionImages;
        this.answerOptionImages = answerOptionImages;
        answerSelector = new Vector<>();

        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (this.questionImages != null) {
            int i = 1;
            for(Image image : this.questionImages) {
                addComponent(image);
                image.setCaption("<font size=\"2\">Abbildung " + i + "</font>");
                image.setCaptionAsHtml(true);
                setComponentAlignment(image, Alignment.MIDDLE_CENTER);
                i++;
            }
        }

        Label l = new Label("    ");
        l.setVisible(true);
        addComponent(l);

        addComponent(new Label("Wähle die richtige(n) Option(en):"));
        HashMap<Integer,String> answerOptions = solution.getAnswerOptions();
        GridLayout multipleChoiceAnswers;
        if(solution.getQuestionType().equals("FillIn") && answerOptions.size() == 6){
            multipleChoiceAnswers = new GridLayout(2, answerOptions.size() + 2);
        } else {
            multipleChoiceAnswers = new GridLayout(2, answerOptions.size());
        }
        // multipleChoiceAnswers.setSizeFull();
        // multipleChoiceAnswers.setSpacing(true);
        int answerNumber = 0;
        int position = 0;
        if(solution.getQuestionType().equals("FillIn") && answerOptions.size() == 6){
            multipleChoiceAnswers.addComponent(new Label ("Lücke 1:"), 0, position);
            position++;
        }
        for (Integer i: answerOptions.keySet()) {
            if(solution.getQuestionType().equals("FillIn") && answerOptions.size() == 6 && position == 4){
                multipleChoiceAnswers.addComponent(new Label ("Lücke 2:"), 0, position);
                position++;
            }
            CheckBox checkBox = new CheckBox(answerOptions.get(i));
            checkBox.setCaptionAsHtml(true);
            checkBox.setData(i);
            if (prefilled.getCorrectAnswers().contains(i)) {
                checkBox.setValue(true);
            }
            if (prefilled.getCorrectAnswers().size()!=0) checkBox.setEnabled(false);
            answerSelector.add(checkBox);
            multipleChoiceAnswers.addComponent(checkBox, 0, position);
            multipleChoiceAnswers.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
            if (answerOptionImages != null && answerNumber < this.answerOptionImages.size()){
                multipleChoiceAnswers.addComponent(this.answerOptionImages.get(answerNumber), 1, position);
                multipleChoiceAnswers.setComponentAlignment(this.answerOptionImages.get(answerNumber), Alignment.MIDDLE_CENTER);
            }
            position++;
            answerNumber++;
        }
        addComponent(multipleChoiceAnswers);
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

    public List<Image> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<Image> questionImages, List<Image> answerOptionImages) {
        if (questionImages == null) return;
        this.questionImages = questionImages;
        this.answerOptionImages = answerOptionImages;
        removeAllComponents();
        addComponent(question);
        int i = 1;
        for (Image image : this.questionImages) {
            addComponent(image);
            image.setCaption("<font size=\"2\">Abbildung " + i + "</font>");
            image.setCaptionAsHtml(true);
            setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            i++;
        }

        Label l = new Label("    ");
        l.setVisible(true);
        addComponent(l);

        GridLayout multipleChoiceAnswers;
        if(solution.getQuestionType().equals("FillIn") && getUserAnswer().getAnswerOptions().size() == 6){
            multipleChoiceAnswers = new GridLayout(2, getUserAnswer().getAnswerOptions().size() + 2);
        } else {
            multipleChoiceAnswers = new GridLayout(2, getUserAnswer().getAnswerOptions().size());
        }
        // multipleChoiceAnswers.setSizeFull();
        // multipleChoiceAnswers.setSpacing(true);
        int answerNumber = 0;
        int position = 0;
        if(solution.getQuestionType().equals("FillIn") && getUserAnswer().getAnswerOptions().size() == 6){
            multipleChoiceAnswers.addComponent(new Label ("Lücke 1:"), 0, position);
            position++;
        }
        for (CheckBox checkBox: answerSelector){
            if(solution.getQuestionType().equals("FillIn") && getUserAnswer().getAnswerOptions().size() == 6 && position == 4){
                multipleChoiceAnswers.addComponent(new Label ("Lücke 2:"), 0, position);
                position++;
            }
            multipleChoiceAnswers.addComponent(checkBox, 0, position);
            multipleChoiceAnswers.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
            if(this.answerOptionImages != null && answerNumber < this.answerOptionImages.size()) {
                multipleChoiceAnswers.addComponent(this.answerOptionImages.get(answerNumber), 1, position);
                multipleChoiceAnswers.setComponentAlignment(this.answerOptionImages.get(answerNumber), Alignment.MIDDLE_CENTER);
            }
            position++;
            answerNumber++;
        }
        addComponent(multipleChoiceAnswers);
    }

}
