package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import at.jku.ce.adaptivetesting.questions.accounting.util.ProfitPossibleAnswers;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.*;

import java.io.*;
import java.util.*;

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
    private ComboBox answerSelector;

    public SimpleMathQuestion() {
        this(SimpleMathDataStorage.getEmptyDataStorage(), SimpleMathDataStorage.getEmptyDataStorage(), 0f, "", null, "");
    }

    public SimpleMathQuestion(SimpleMathDataStorage solution, float difficulty, String questionText, List<Image> questionImages, String id) {
        this(solution, SimpleMathDataStorage.getEmptyDataStorage(), difficulty, questionText, questionImages, id);
    }

    @Override
    public String getQuestionID() {
        return id;
    }

    public SimpleMathQuestion(SimpleMathDataStorage solution, SimpleMathDataStorage prefilled, float difficulty, String questionText, List<Image> questionImages, String id) {
        this.difficulty = difficulty;
        this.solution = solution;
        this.id = id;
        this.question = new HtmlLabel();
        setQuestionText(questionText);
        this.questionImages = questionImages;
        // Fill grid
        numberOfInputFields = solution.getAnswerElements().size();
        inputGrid = new GridLayout(3, numberOfInputFields);

        int i = 0;
        for (Map.Entry<String, String> entry : this.solution.getAnswerElements().entrySet()) {
            if (solution.getQuestionType().equals("SelectionQuestion")){
                inputGrid.addComponent(new Label(entry.getKey()), 0, i);
                answerSelector = new ComboBox();
                answerSelector.setCaptionAsHtml(true);
                answerSelector.addItem("=");
                answerSelector.addItem("<");
                answerSelector.addItem(">");
                answerSelector.addItem("<=" );
                answerSelector.addItem(">=");
                answerSelector.setInputPrompt("Wählen Sie aus");
                answerSelector.setNullSelectionAllowed(false);
                answerSelector.setTextInputAllowed(false);
                answerSelector.setValue("=");
                if(prefilled.getAnswerElements().get(entry.getKey()) != null){
                    TextField f = new TextField();
                    f.setValue(prefilled.getAnswerElements().get(entry.getKey()));
                    answerSelector.setValue(prefilled.getCorrectSelection().get(i));
                    inputGrid.addComponent(answerSelector, 1, i);
                    inputGrid.addComponent(f, 2, i);
                } else {
                    inputGrid.addComponent(answerSelector, 1, i);
                    inputGrid.addComponent(new TextField(), 2, i);
                }
            } else {
                // inputGrid.addComponent(new Label(entry.getKey()), 0, i); // TODO : ? lösen
                TextField f = new TextField(entry.getKey());
                f.setCaptionAsHtml(true);
                if(prefilled.getAnswerElements().get(entry.getKey()) != null){
                    f.setValue(prefilled.getAnswerElements().get(entry.getKey()));
                }
                inputGrid.addComponent(f, 0, i);
            }
            i++;
        }
        addComponent(question);
        if (questionImages != null) {
            for (Image image : this.questionImages) {
                addComponent(image);
                setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            }
        }

        Label l = new Label("    ");
        l.setVisible(true);
        addComponent(l);

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
        Vector<String> correctSelection = new Vector<>();

        if (solution.getQuestionType().equals("SelectionQuestion")){
            for (int i = 0; i < numberOfInputFields; i++) {
                userAnswers.put(
                        // ((Label) inputGrid.getComponent(0, i)).getValue(),
                        ((Label) inputGrid.getComponent(0, i)).getValue(),
                        ((TextField) inputGrid.getComponent(2, i)).getValue()
                );
                correctSelection.add((String) ((ComboBox) inputGrid.getComponent(1, i)).getValue());
            }
        } else {
            for (int i = 0; i < numberOfInputFields; i++) {
                userAnswers.put(
                        // ((Label) inputGrid.getComponent(0, i)).getValue(),
                        ((TextField) inputGrid.getComponent(0, i)).getCaption(),
                        ((TextField) inputGrid.getComponent(0, i)).getValue()
                );
            }
        }
        dataStorage.setAnswerElements(userAnswers);
        dataStorage.setCorrectSelection(correctSelection);
        return dataStorage;
    }

    @Override
    public double checkUserAnswer() {
        double points = 0.0d;
        LogHelper.logInfo("Questionfile: " + id);
        SimpleMathDataStorage solution = getSolution();

        if (solution.getQuestionType().equals("SelectionQuestion")){
            for (int i = 0; i < numberOfInputFields; i++) {
                String entryText = ((Label) inputGrid.getComponent(0, i)).getValue();
                String selectedValue = (String) ((ComboBox) inputGrid.getComponent(1, i)).getValue();
                String enteredValue = ((TextField) inputGrid.getComponent(2, i)).getValue();
                if (selectedValue.equals(solution.getCorrectSelection().get(i)) && enteredValue.toLowerCase().contains(solution.getAnswerElements().get(entryText).toLowerCase())) {
                    points = points + 1.0d / numberOfInputFields;
                }
            }
        } else {
            SimpleMathDataStorage user = getUserAnswer();
            for (Map.Entry<String, String> userAnswerElement : user.getAnswerElements().entrySet()) {
                userAnswerElement.setValue(userAnswerElement.getValue().replaceAll("\\s", ""));
                userAnswerElement.setValue(userAnswerElement.getValue().replace('.', ','));
            }

            for (Map.Entry<String, String> entry : solution.getAnswerElements().entrySet()) {
                if (entry.getValue().startsWith("[")
                        && entry.getValue().endsWith("]")
                        && entry.getValue().contains(";")) {
                    String values = entry.getValue().substring(1, entry.getValue().length() - 1);
                    values = values.replace(',', '.');
                    String[] v = values.split(";");
                    double floor = Double.parseDouble(v[0]);
                    double ceiling = Double.parseDouble(v[1]);
                    String userAnswer = user.getAnswerElements().get(entry.getKey());
                    if (userAnswer != null && userAnswer.length() != 0) {
                        userAnswer = userAnswer.replace(',', '.');
                        try {
                            double userAnswerNumber = Double.parseDouble(userAnswer);
                            if (userAnswerNumber >= floor && userAnswerNumber <= ceiling) {
                                points = points + (1.0d / numberOfInputFields);
                            }
                        } catch (NumberFormatException e) {
                            LogHelper.logInfo("Entered answer was not a number!");
                        }
                    }
                } else if (entry.getValue().contains(";")) {
                    String[] answerParts = entry.getValue().split(";");
                    for (String answerPart : answerParts) {
                        if (user.getAnswerElements().get(entry.getKey()).toLowerCase().contains(answerPart.toLowerCase())) {
                            points = points + (1.0d / numberOfInputFields) / answerPart.length();
                        }
                    }
                } else if (user.getAnswerElements().get(entry.getKey()).toLowerCase().contains(entry.getValue().toLowerCase())) {
                    points = points + (1.0d / numberOfInputFields);
                }
            }
        }
        LogHelper.logInfo("You achieved " + points + " points!");
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
            setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        }

        Label l = new Label("    ");
        l.setVisible(true);
        addComponent(l);

        addComponent(inputGrid);
    }

}

