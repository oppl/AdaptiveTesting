package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
//import com.sun.jdi.FloatValue;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

import java.io.*;

/**
 * Created by oppl on 07/02/2017.
 */
public class OpenAnswerKeywordQuestion extends VerticalLayout implements
        IQuestion<OpenAnswerKeywordDataStorage>, Cloneable {

    private static final long serialVersionUID = 6373936654529246422L;
    private OpenAnswerKeywordDataStorage solution;
    private float difficulty = 0;
    private ExpandingTextArea answer;
    private Label question;
    private Image questionImage = null;

    private String id;

    public OpenAnswerKeywordQuestion(OpenAnswerKeywordDataStorage solution, Float difficulty,
                                     String questionText, Image questionImage, String id) {
        this(solution, OpenAnswerKeywordDataStorage.getEmptyDataStorage(), difficulty,
                questionText, questionImage, id);
    }

    public OpenAnswerKeywordQuestion(OpenAnswerKeywordDataStorage solution,
                                     OpenAnswerKeywordDataStorage prefilled, float difficulty, String questionText, Image questionImage, String id) {
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;
        answer = new ExpandingTextArea("Bitte geben Sie Ihre Antwort ein:");
        answer.setWidth(100, Unit.PERCENTAGE);

        if (prefilled.getEnteredText() != null) {
            answer.setValue(prefilled.getEnteredText());
            answer.setEnabled(false);
        }
        else if (prefilled.getAnswers().size() > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Ihre Antwort muss folgende Schlüsselwörter enthalten:\n");
            for (String[] keywords: prefilled.getAnswers()) {
                buffer.append("Eines der folgenden Wörter: ");
                for (String keyword: keywords) {
                    buffer.append(keyword);
                    if (!keywords[keywords.length-1].equals(keyword)) buffer.append(" ODER ");
                }
                if (!prefilled.getAnswers().lastElement().equals(keywords)) buffer.append(" UND\n");
            }
            answer.setValue(buffer.toString());
            answer.setEnabled(false);
        }
        question = new HtmlLabel();
        setQuestionText(questionText);

        this.solution = solution;
        addComponent(question);
        if (questionImage != null) addComponent(this.questionImage);
        addComponent(answer);
        setSpacing(true);
    }

    @Override
    public String    getQuestionID() {
        return id;
    }

    public OpenAnswerKeywordQuestion clone() throws CloneNotSupportedException {
        OpenAnswerKeywordQuestion objClone = (OpenAnswerKeywordQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public OpenAnswerKeywordQuestion cloneThroughSerialize(OpenAnswerKeywordQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (OpenAnswerKeywordQuestion)ois.readObject();
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
    public OpenAnswerKeywordDataStorage getSolution() {
        return solution;
    }

    @Override
    public OpenAnswerKeywordDataStorage getUserAnswer() {
        OpenAnswerKeywordDataStorage userAnswer = new OpenAnswerKeywordDataStorage();
        userAnswer.setEnteredText(answer.getValue());
        userAnswer.setAnswers(solution.getAnswers());
        return userAnswer;
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);
        String userAnswer = answer.getValue();
        // change user's input to match xxx(x),yy

        // error handling
        if (userAnswer.equals("")) {
            LogHelper.logInfo("No answer: The text input was empty");
            return 0.0d;
        }
        boolean numberanswer = false;
        String[] userAnswerParts;
        try {
            // convert TextNumbers into Double values
            userAnswer = TextNumberToDouble (userAnswer);
            // check if useranswer is a number
            if(userAnswer.contains(",")) userAnswer = userAnswer.replaceAll(",", ".");
            Double.valueOf(userAnswer);

            if (userAnswer.charAt(0) != '+' && userAnswer.charAt(0) != '-')
                userAnswer = "+" + userAnswer;

            if (userAnswer.contains(".")) {
                userAnswerParts = userAnswer.split("\\.");
                if (userAnswerParts[1].charAt(0) == '-') userAnswerParts[0] = "00";
                if (userAnswerParts[1].length() > 2) userAnswerParts[1] = userAnswerParts[1].substring(0, 2);
                userAnswer = userAnswerParts[0] + "," + userAnswerParts[1];
            }
            else {
                userAnswerParts = new String[2];
                userAnswer = userAnswer + ",00";
            }
            numberanswer = true;
        } catch (NumberFormatException e) {
            //LogHelper.logInfo("The input was not a number");
            if(userAnswer.contains(" "))
                userAnswer = userAnswer.replaceAll(" ", ",");
            if(userAnswer.contains(";"))
                userAnswer = userAnswer.replaceAll(";", ",");
            if(userAnswer.contains("."))
                userAnswer = userAnswer.replaceAll(".", ",");

            userAnswerParts = userAnswer.split(",");
            for (int i = 0; i < userAnswerParts.length; i++) {
                try {
                    userAnswerParts[i] = userAnswerParts[i].toLowerCase();
                    userAnswerParts[i] = userAnswerParts[i].substring(0, 1).toUpperCase() +
                            userAnswerParts[i].substring(1, userAnswerParts[i].length());
                } catch (Exception ex) {
                    userAnswerParts[i] = "_empty";
                }
            }
        }

        for (String[] requriedKeyword: solution.getAnswers()) {

            int nrOfKeywords = 0;
            for (int i = 0; i < requriedKeyword.length; i++) {
                if (!requriedKeyword[i].equals("")) nrOfKeywords++;
            }
            boolean[]variantFoundParts = new boolean[nrOfKeywords];

            boolean variantFound = false;
            if (numberanswer) {
                for (int k = 0; k < nrOfKeywords; k++) {
                    // check if the userAnswer contains the variant
                    if (StringUtils.equalsIgnoreCase(userAnswer, requriedKeyword[k]))
                        variantFound = true;
                }
            } else {
                int x = 0;
                for (int i = 0; i < userAnswerParts.length; i++) {
                    for (int j = 0; j < nrOfKeywords; j++) {
                        if (StringUtils.equalsIgnoreCase(userAnswerParts[i],
                                requriedKeyword[j].replaceAll(" ", ""))) {
                            variantFoundParts[x] = true;
                            x++;
                            break;
                        }
                    }
                }
                for (int i = 0; i < variantFoundParts.length; i++) {
                    variantFound = true && variantFoundParts[i];
                }
            }
            if (!variantFound) {
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

    @Override
    public XmlQuestionData<OpenAnswerKeywordDataStorage> toXMLRepresentation() {
        return new OpenAnswerKeywordQuestionXml(getSolution(), getQuestionText(),
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
        addComponent(answer);
    }

    private String TextNumberToDouble (String userAnswer) {
        switch (userAnswer.toLowerCase()) {
            case "eins":
                userAnswer = "1.00";
                break;
            case "zwei":
                userAnswer = "2.00";
                break;
            case "drei":
                userAnswer = "3.00";
                break;
            case "vier":
                userAnswer = "4.00";
                break;
            case "fünf": case "fuenf":
                userAnswer = "5.00";
                break;
            case "sechs":
                userAnswer = "6.00";
                break;
            case "sieben":
                userAnswer = "7.00";
                break;
            case "acht":
                userAnswer = "8.00";
                break;
            case "neun":
                userAnswer = "9.00";
                break;
            case "zehn":
                userAnswer = "10.00";
                break;
            case "elf":
                userAnswer = "11.00";
                break;
            case "zwölf": case "zwoelf":
                userAnswer = "12.00";
                break;
            case "dreizehn":
                userAnswer = "13.00";
                break;
            case "vierzehn":
                userAnswer = "14.00";
                break;
            case "fünfzehn": case "fuenfzehn":
                userAnswer = "15.00";
                break;
            case "sechszehn":
                userAnswer = "16.00";
                break;
            case "siebzehn":
                userAnswer = "17.00";
                break;
            case "achtzehn":
                userAnswer = "18.00";
                break;
            case "neunzehn":
                userAnswer = "19.00";
                break;
            case "zwanzig":
                userAnswer = "20.00";
                break;
        }
        return userAnswer;
    }
}