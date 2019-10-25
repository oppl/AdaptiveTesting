package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.IResultView;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.engine.HistoryEntry;
import at.jku.ce.adaptivetesting.core.engine.ResultFiredArgs;
import at.jku.ce.adaptivetesting.questions.math.MathDataStorage;
import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.views.def.DefaultView;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.views.test.datamod.TableWindow;
import com.github.rcaller.exception.ExecutionException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathResultView extends VerticalLayout implements View, IResultView {
    private static final long serialVersionUID = -6619938011293967055L;
    private final String imageFolder = VaadinServlet.getCurrent().getServletConfig().
            getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";
    private String resultsFolder;

    public MathResultView(ResultFiredArgs args, String title, String resultsFolder) {
        this.resultsFolder = resultsFolder;
        setSpacing(true);
        addComponent(new HtmlLabel(title));
        //addComponent(HtmlLabel.getCenteredLabel("h2", "Test abgeschlossen"));
        addComponent(HtmlLabel.getCenteredLabel("Der Test wurde beendet, da "
                + (args.outOfQuestions ? "keine weiteren Fragen verfügbar sind."
                : "dein Kompetenzniveau bestimmt wurde.")));

        addComponent(HtmlLabel
                .getCenteredLabel("Im Folgenden siehst du die Fragen und die von dir gegebenen Antworten in zeitlich absteigender Reihenfolge.<br>" +
                        "Mit einem Klick auf den Button Ergebnis kannst du Detailinformationen zur jeweiligen Frage anzeigen."));
        addComponent(HtmlLabel
                .getCenteredLabel("Die Zahl in der ersten Spalte bezieht sich dabei auf deren Schwierigkeitsgrad.<br/>" +
                        "Je größer sie ist, desto höher ist auch die Schwierigkeit der Frage.<br>"));

        // Create HTML table of the history
        Table table = new Table();
        // final String showResult = "Ergebnis";
        table.addContainerProperty("#", Integer.class, null);
        table.addContainerProperty("Schwierigkeitsgrad", Float.class, null);
        table.addContainerProperty("Resultat", String.class, null);
        // table.addContainerProperty(showResult, Button.class, null);
        // List<HistoryEntry> entries = Lists.reverse(args.history);
        List<HistoryEntry> entries = new ArrayList<HistoryEntry>(args.history);
        Collections.reverse(entries);
        int nr = entries.size();
        for (HistoryEntry entry : entries) {
            /*
            if (entry.question instanceof Component && entry.question != null) {
                try {
                    Class<? extends AnswerStorage> dataStorageClass = entry.question.getSolution().getClass();
                    Constructor<? extends IQuestion> constructor = entry.question.getClass().getConstructor(
                            dataStorageClass, dataStorageClass, float.class, String.class, Image.class, String.class);

                    Component iQuestionSolution = (Component) constructor.newInstance(
                            entry.question.getSolution(),
                            entry.question.getUserAnswer(),
                            entry.question.getDifficulty(),
                            entry.question.getQuestionText(),
                            null,"");
                    MathQuestion mathQuestion = (MathQuestion)entry.question;

                    MathDataStorage userAnswer = mathQuestion.getUserAnswer();
                    TableWindow userAnswerTableEmbedded = new TableWindow();
                    userAnswerTableEmbedded.drawResultTable(userAnswer.getInfo());

                    MathDataStorage solution = mathQuestion.getSolution();
                    TableWindow answerTableEmbedded = new TableWindow();
                    answerTableEmbedded.drawResultTable(solution.getInfo());



                } catch (Exception e) {
                    LogHelper.logError(e.toString());
                }
            }
            */
            table.addItem(new Object[] { new Integer(nr), entry.question.getDifficulty(),
                    isCorrect(entry.points, entry.question.getMaxPoints())}, null);
            nr--;
        }
        int size = table.size();
        if (size > 10) {
            size = 10;
        }
        table.setPageLength(size);
        table.setWidthUndefined();
        addComponent(table);
        setComponentAlignment(table, Alignment.MIDDLE_CENTER);

        addComponent(HtmlLabel.getCenteredLabel("h3",
                "Dein Kompetenzniveau ist: <b>" + args.skillLevel + "</b>"));
        addComponent(HtmlLabel.getCenteredLabel("Delta:  " + args.delta));
        addComponent(HtmlLabel.getCenteredLabel("h3",
                "<b>Vielen Dank fürs Mitmachen!\n</b>"));
        storeResults(args);

        //Image image = new Image("", new FileResource(new File(imageFolder + "datamod_Kompetenzmodell.png")));

        //addComponent(image);
        //setComponentAlignment(image, Alignment.MIDDLE_CENTER);
    }

    private void storeResults(ResultFiredArgs args) {
        File resultFile;
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String fileName = new String(args.student.getStudentIDCode()+ "_" + dtf.format(now) + ".csv");
            resultFile = new File(new File(resultsFolder),fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));
            writer.write(args.student.toString()+"\n");
            writer.write(Double.toString(args.skillLevel)+"\n");
            writer.write(Double.toString(args.delta)+"\n");
            writer.write(Boolean.toString(args.outOfQuestions)+"\n");
            writer.write(args.history.size()+"\n");
            for (HistoryEntry entry : args.history) {
                writer.write(
                        entry.question.getQuestionText() + ";" +
                                entry.question.getDifficulty() + ";" +
                                entry.question.getSolution().toString() + ";" +
                                entry.question.getUserAnswer().toString() + ";" +
                                isCorrect(entry.points, entry.question.getMaxPoints()) + "\n");
            }
            writer.close();
        } catch (Exception var9) {
            throw new ExecutionException("Can not create a temporary file for storing the results: " + var9.toString());
        }
    }

    private String isCorrect(double points, double maxPoints) {
        return points + " / " + maxPoints + " (" + 100 * points / maxPoints + "% )";
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        DefaultView.setCurrentPageTitle(event);
    }
}
