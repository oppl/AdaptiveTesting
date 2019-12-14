package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
//import com.sun.jdi.FloatValue;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Label l = new Label("    ");
        l.setVisible(true);
        addComponent(l);

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
        boolean numberanswerfound = false;

        // Remove all white space
        userAnswer = userAnswer.replaceAll("\\s","");

        // convert TextNumbers into Double values
        userAnswer = TextNumberToDouble (userAnswer);

        String regexNumber_DotSep = "([0-9]{1,3}(((,[0-9]{3}){0,})|[0-9]{0,}))?(\\.[0-9]{1,2}){1}",
                    regexNumber_ColonSep = 			  "([0-9]{1,3}(((\\.[0-9]{3}){0,})|[0-9]{0,}))?(,[0-9]{1,2}){1}";

        Pattern regexNumber_DotSep_pattern = Pattern.compile(regexNumber_DotSep);
        Matcher regexNumber_DotSep_matcher = regexNumber_DotSep_pattern.matcher(userAnswer);
        if(regexNumber_DotSep_matcher.find()){
            String old_number_part = regexNumber_DotSep_matcher.group(0);
            String new_number_part = regexNumber_DotSep_matcher.group(0);
            boolean digitsFollowing = false;
            if (userAnswer.indexOf(old_number_part) + old_number_part.length() < userAnswer.length()) {
                if (Character.isDigit(userAnswer.charAt(userAnswer.indexOf(old_number_part) + old_number_part.length()))) {
                    digitsFollowing = true;
                }
            }
            if(!digitsFollowing) {
                int number_index = userAnswer.indexOf(old_number_part);
                if (number_index > 0) {
                    if (userAnswer.charAt(number_index - 1) != '+' && userAnswer.charAt(number_index - 1) != '-') {
                        new_number_part = "+" + old_number_part;
                    } else {
                        old_number_part = userAnswer.charAt(number_index - 1) + old_number_part;
                        new_number_part = old_number_part;
                    }
                } else {
                    new_number_part = "+" + old_number_part;
                }
                new_number_part = new_number_part.replace(",", "").replace('.', ',');
                userAnswer = userAnswer.replace(old_number_part, new_number_part);
                numberanswerfound = true;
            }
        }

        Pattern regexNumber_ColonSep_pattern = Pattern.compile(regexNumber_ColonSep);
        Matcher regexNumber_ColonSep_matcher = regexNumber_ColonSep_pattern.matcher(userAnswer);
        if(regexNumber_ColonSep_matcher.find() && !numberanswerfound){
            String old_number_part = regexNumber_ColonSep_matcher.group(0);
            String new_number_part = regexNumber_ColonSep_matcher.group(0);
            boolean digitsFollowing = false;
            if (userAnswer.indexOf(old_number_part) + old_number_part.length() < userAnswer.length()) {
                if (Character.isDigit(userAnswer.charAt(userAnswer.lastIndexOf(old_number_part) + old_number_part.length()))) {
                    digitsFollowing = true;
                }
            }
            if(!digitsFollowing) {
                int number_index = userAnswer.indexOf(old_number_part);
                if (number_index > 0) {
                    if (userAnswer.charAt(number_index - 1) != '+' && userAnswer.charAt(number_index - 1) != '-') {
                        new_number_part = "+" + old_number_part;
                    } else {
                        old_number_part = userAnswer.charAt(number_index - 1) + old_number_part;
                        new_number_part = old_number_part;
                    }
                } else {
                    new_number_part = "+" + old_number_part;
                }
                new_number_part = new_number_part.replace(".", "");
                userAnswer = userAnswer.replace(old_number_part, new_number_part);
                numberanswerfound = true;
            }
        }

        String regexNumber_DotSep_nondecimal = "([0-9]{1,3}(((,[0-9]{3}){0,})|[0-9]{0,}))",
                regexNumber_ColonSep_nondecimal = 			  "([0-9]{1,3}(((\\.[0-9]{3}){0,})|[0-9]{0,}))";

        Pattern regexNumber_DotSep_pattern_nondecimal = Pattern.compile(regexNumber_DotSep_nondecimal);
        Matcher regexNumber_DotSep_matcher_nondecimal = regexNumber_DotSep_pattern_nondecimal.matcher(userAnswer);
        if(regexNumber_DotSep_matcher_nondecimal.find() && !numberanswerfound && !userAnswer.contains(".")){
            String old_number_part = regexNumber_DotSep_matcher_nondecimal.group(0);
            String new_number_part = regexNumber_DotSep_matcher_nondecimal.group(0);
            int number_index = userAnswer.indexOf(old_number_part);
            if(number_index > 0){
                if (userAnswer.charAt(number_index-1) != '+' && userAnswer.charAt(number_index-1) != '-') {
                    new_number_part = "+" + old_number_part;
                } else {
                    old_number_part = userAnswer.charAt(number_index-1) + old_number_part;
                    new_number_part = old_number_part;
                }
            } else {
                new_number_part = "+" + old_number_part;
            }
            new_number_part = new_number_part.replace(",", "");
            new_number_part = new_number_part + ",00";
            userAnswer = userAnswer.replace(old_number_part, new_number_part);
            numberanswerfound = true;
        }
        Pattern regexNumber_ColonSep_pattern_nondecimal = Pattern.compile(regexNumber_ColonSep_nondecimal);
        Matcher regexNumber_ColonSep_matcher_nondecimal = regexNumber_ColonSep_pattern_nondecimal.matcher(userAnswer);
        if(regexNumber_ColonSep_matcher_nondecimal.find() && !numberanswerfound && !userAnswer.contains(",")){
            String old_number_part = regexNumber_ColonSep_matcher_nondecimal.group(0);
            String new_number_part = regexNumber_ColonSep_matcher_nondecimal.group(0);
            int number_index = userAnswer.indexOf(old_number_part);
            if(number_index > 0){
                if (userAnswer.charAt(number_index-1) != '+' && userAnswer.charAt(number_index-1) != '-') {
                    new_number_part = "+" + old_number_part;
                } else {
                    old_number_part = userAnswer.charAt(number_index-1) + old_number_part;
                    new_number_part = old_number_part;
                }
            } else {
                new_number_part = "+" + old_number_part;
            }
            new_number_part = new_number_part.replace(".", "");
            new_number_part = new_number_part + ",00";
            userAnswer = userAnswer.replace(old_number_part, new_number_part);
            numberanswerfound = true;
        }
        for (String[] requriedKeyword: solution.getAnswers()) {
            int nrOfKeywords = 0;
            for (int i = 0; i < requriedKeyword.length; i++) {
                if (!requriedKeyword[i].equals("")) nrOfKeywords++;
            }

            boolean variantFound = false;
            for (int k = 0; k < nrOfKeywords; k++) {
                // check if the userAnswer contains the variant
                if (userAnswer.toLowerCase().contains(requriedKeyword[k].toLowerCase().replaceAll(" ", "")))
                    variantFound = true;
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