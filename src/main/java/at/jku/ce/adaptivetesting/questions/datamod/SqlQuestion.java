package at.jku.ce.adaptivetesting.questions.datamod;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.views.def.ErrorInfoWindow;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import at.jku.ce.adaptivetesting.views.test.datamod.TableWindow;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;

import java.io.*;
import java.sql.SQLSyntaxErrorException;

/**
 * Created by Peter
 */

public class SqlQuestion extends VerticalLayout implements IQuestion<SqlDataStorage>, Cloneable {

    private static final long serialVersionUID = 6373936654529246422L;
    private SqlDataStorage solution, userAnswer;
    private float difficulty = 0;
    private ExpandingTextArea answer;
    private Label question, infoTop, infoBottom;
    private Image questionImage = null;
    private String id;
    private int tries, defaultTries;
    private HtmlLabel triesLeft;

    public SqlQuestion(SqlDataStorage solution, Float difficulty, String questionText, Image questionImage, String id) {
        this(solution, SqlDataStorage.getEmptyDataStorage(), difficulty, questionText, questionImage, id);
    }

    public SqlQuestion(SqlDataStorage solution, SqlDataStorage userAnswer, float difficulty, String questionText, Image questionImage, String id) {
        this.difficulty = difficulty;
        this.id = id;
        this.questionImage = questionImage;
        this.userAnswer = userAnswer;
        this.solution = solution;
        defaultTries = solution.getTries();
        tries = solution.getTries();

        answer = new ExpandingTextArea("Bitte gib deine Antwort ein:");
        answer.setWidth(100, Unit.PERCENTAGE);

        if (userAnswer.getAnswerQuery() != null) {
            answer.setValue(userAnswer.getAnswerQuery());
            answer.setEnabled(false);
        }
        infoTop = new HtmlLabel();
        //Shows Question ID
        setText(infoTop, "<p>" + id + "</p>" + solution.getInfoTop());
        //setText(infoTop, solution.getInfoTop());
        addComponent(infoTop);

        solution.createTableWindows(this);

        infoBottom = new HtmlLabel();
        setText(infoBottom, solution.getInfoBottom());
        addComponent(infoBottom);

        question = new HtmlLabel();
        setQuestionText(question, questionText);
        addComponent(question);

        if (questionImage != null) addComponent(this.questionImage);

        triesLeft = new HtmlLabel("<p style=\"color:#339933\">Du kannst die Query Diagnose max. <b>" + defaultTries +
                "</b> Mal ausführen! [<b>" + defaultTries + "</b> Versuch(e) übrig]</p>");
        addComponent(triesLeft);
        addComponent(answer);
        setSpacing(true);
    }

    @Override
    public String getQuestionID() {
        return id;
    }

    public SqlQuestion clone() throws CloneNotSupportedException {
        SqlQuestion objClone = (SqlQuestion)super.clone();
        return objClone;
    }

    @SuppressWarnings("unchecked")
    public SqlQuestion cloneThroughSerialize(SqlQuestion t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializeToOutputStream(t, bos);
        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (SqlQuestion)ois.readObject();
    }

    private static void serializeToOutputStream(Serializable ser, OutputStream os) throws IOException {
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

    public void setText(Label label, String text) {
        label.setValue(text);
    }

    public void setQuestionText(Label label, String text) {
        label.setValue("<p style=\"color:#0099ff\">" + text + "</p>");
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public SqlDataStorage getSolution() {
        solution.setAnswer(solution.getAnswerQuery());
        return solution;
    }

    @Override
    public SqlDataStorage getUserAnswer() {
        String userAnswerQuery = answer.getValue();
        userAnswer.setAnswerQuery(userAnswerQuery);
        userAnswer.setAnswer(userAnswerQuery);
        userAnswer.setTries(tries);
        return userAnswer;
    }

    public double performQueryDiagnosis() {
        LogHelper.logInfo("Questionfile: " + id);

        if (answer.getValue().equals("")) {
            createAndShowNotification("Keine Antwort eingegeben:" , "Das Textfeld ist leer!<br>", Notification.Type.WARNING_MESSAGE);
            LogHelper.logInfo("Diagnosis: No answer (The text input was empty)");
            //running a query diagnosis on an empty answer does not decrease the amount of available tries
            return -1.0d;
        }
        double check;
        try {
            check = ConnectionProvider.compareResults(answer.getValue(), this.solution.getAnswerQuery());
        } catch (Exception ex) {
            try {
                String errorMessage = ex.getCause().getMessage();
                if (errorMessage.equals("Invalid SQL type: sqlKind = UNINITIALIZED")) {
                    showSqlKindUnitializedError();
                } else {
                    String errorId = errorMessage.substring(0, errorMessage.indexOf(":")).replace("-", "");
                    ErrorInfoWindow errorInfoWindow = new ErrorInfoWindow(errorId, errorMessage);
                    getUI().addWindow(errorInfoWindow);
                }
                LogHelper.logError(errorMessage);
                // a syntax error is often not related to the comprehension of the task
                return -1.0d;
            } catch (NullPointerException ex1) {
                showSqlKindUnitializedError();
                LogHelper.logError("Invalid SQL type: sqlKind = UNINITIALIZED");
                return  -1.0d;
            }
        }
        TableWindow tableWindow = new TableWindow();
        tableWindow.setSizeUndefined();
        tableWindow.drawResultTable(answer.getValue());
        this.getUI().addWindow(tableWindow);

        if (check == 1.0d) {
            createAndShowNotification("<p style=\"color:#339933\">Ergebnis korrekt:</p>" , "Super gemacht!.<br>" +
                    "<p style=\"font-size:small\" \"color:#339933\">(Beim " + (1 + (defaultTries - tries)) +
                    ". Versuch geschafft)</p>", Notification.Type.HUMANIZED_MESSAGE);
            LogHelper.logInfo("Diagnosis: Correct answer");
            return 1.0d;
        } else {
            createAndShowNotification("Ergebnis inkorrekt:", "Abfragergebnis stimmt nicht mit der Lösung überein.<br>" +
                    "<p style=\"font-size:small\">(" + (tries-1) + " Versuche übrig)</p>", Notification.Type.ERROR_MESSAGE);
            LogHelper.logInfo("Diagnosis: Incorrect answer");
            return 0.0d;
        }
    }

    private void showSqlKindUnitializedError() {
        createAndShowNotification("SQL-Syntax Fehler",
                "SQL Abfragen müssen mit \"SELECT\" oder \"(SELECT\" beginnen", Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public double checkUserAnswer() {
        LogHelper.logInfo("Questionfile: " + id);

        if (answer.getValue().equals("")) {
            LogHelper.logInfo("Submit: No answer (The text input was empty)");
            //submitting an empty answer is considered as wrong answer
            return 0.0d;
        }
        double check;
        try {
            check = ConnectionProvider.compareResults(answer.getValue(), solution.getAnswerQuery());
        } catch (Exception ex) {
            LogHelper.logError(ex.getMessage());
            return 0.0d;
        }
        if (check == 1.0d) {
            LogHelper.logInfo("Submit: Correct answer");
            return 1.0d;
        } else {
            LogHelper.logInfo("Submit: Incorrect answer");
            return 0.0d;
        }
    }

    @Override
    public float getDifficulty() {
        return difficulty;
    }

    @Override
    public XmlQuestionData<SqlDataStorage> toXMLRepresentation() {
        return new SqlQuestionXml(getSolution(), getQuestionText(),
                getDifficulty());
    }

    public int getDefaultTries() {
        return defaultTries;
    }

    public int getTries() {
        return tries;
    }

    public void decreaseTries() {
        tries--;
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

    // Custom Notification that stays on-screen until user presses it
    private void createAndShowNotification(String caption, String description, Notification.Type type) {
        description += "<span style=\"position:fixed;top:0;left:0;width:100%;height:100%\"></span>";
        Notification notif = new Notification(caption, description, type, true);
        notif.setDelayMsec(-1);
        notif.show(Page.getCurrent());
    }

    private double submitInfo (double checkValue) {
        if (checkValue == 1.0d) {
            createAndShowNotification("<p style=\"color:#339933\">Das Ergebnis war korrekt!</p>" , "", Notification.Type.HUMANIZED_MESSAGE);
        } else if (checkValue == 0.0d) {
            createAndShowNotification("Das Ergebnis war inkorrekt!", "", Notification.Type.ERROR_MESSAGE);
        }
        return checkValue;
    }

    public void updateTriesLeftLabel() {
        triesLeft.setValue("<p style=\"color:#339933\">Du kannst die Query Diagnose max. <b>" +
                defaultTries + "</b> Mal ausführen! [<b>" + getTries() + "</b> Versuch(e) übrig]</p>");
    }
}