package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.IResultView;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.engine.HistoryEntry;
import at.jku.ce.adaptivetesting.core.engine.ResultFiredArgs;
import at.jku.ce.adaptivetesting.questions.math.MathDataStorage;
import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.questions.math.MultipleChoiceMathQuestion;
import at.jku.ce.adaptivetesting.questions.math.SimpleMathQuestion;
import at.jku.ce.adaptivetesting.views.def.DefaultView;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.views.test.datamod.TableWindow;
import com.github.rcaller.exception.ExecutionException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
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
                : "Ihr Kompetenzniveau bestimmt wurde.")));

        addComponent(HtmlLabel
                .getCenteredLabel("Im Folgenden sehen Sie die Fragen und die von Ihnen gegebenen Antworten in zeitlich absteigender Reihenfolge.<br>" +
                        "Mit einem Klick auf den Button Ergebnis können Sie Detailinformationen zur jeweiligen Frage anzeigen."));
        addComponent(HtmlLabel
                .getCenteredLabel("Die Zahl in der ersten Spalte bezieht sich dabei auf deren Schwierigkeitsgrad.<br/>" +
                        "Je größer sie ist, desto höher ist auch die Schwierigkeit der Frage.<br>"));

        // Create HTML table of the history
        Table table = new Table();
        // final String showResult = "Ergebnis";
        final String solution = "Korrekte Antwort", userAnswer = "Deine Antwort";
        table.addContainerProperty("#", Integer.class, null);
        table.addContainerProperty("Schwierigkeitsgrad", Float.class, null);
        table.addContainerProperty("Resultat", String.class, null);
        table.addContainerProperty(userAnswer, Button.class, null);
        table.addContainerProperty(solution, Button.class, null);
        // table.addContainerProperty(showResult, Button.class, null);
        // List<HistoryEntry> entries = Lists.reverse(args.history);
        List<HistoryEntry> entries = new ArrayList<HistoryEntry>(args.history);
        Collections.reverse(entries);
        int nr = entries.size();
        for (HistoryEntry entry : entries) {
            Button qAnswer = null, qSolution = null;
            if (entry.question instanceof Component && entry.question != null) {
                try {
                    Class<? extends AnswerStorage> dataStorageClass = entry.question
                            .getSolution().getClass();
                    Constructor<? extends IQuestion> constructor;
                    Component iQuestionSolution;
                    Component iQuestionUser;

                    if(entry.question instanceof MathQuestion) {
                        constructor = entry.question
                                .getClass()
                                .getConstructor(
                                        float.class, String.class, String.class, int.class, Image.class, String.class);
                        iQuestionSolution = (Component) constructor
                                .newInstance(entry.question.getDifficulty(),
                                        entry.question.getQuestionText() + "\n\n\n\n Das Anzeigen der Lösung zu diesem Fragentyp ist leider nicht möglich.", "", 1, null, "");
                        iQuestionUser = (Component) constructor
                                .newInstance(entry.question.getDifficulty(),
                                        entry.question.getQuestionText() + "\n\n\n\n Das Anzeigen der Benutzerantwort zu diesem Fragentyp ist leider nicht möglich.", "", 1, null, "");
                    } else if(entry.question instanceof SimpleMathQuestion) {
                        constructor = entry.question
                                .getClass()
                                .getConstructor(dataStorageClass, dataStorageClass,
                                        float.class, String.class, List.class, String.class);
                        iQuestionSolution = (Component) constructor
                                .newInstance(entry.question.getSolution(),
                                        entry.question.getSolution(),
                                        entry.question.getDifficulty(),
                                        entry.question.getQuestionText(),
                                        null, "");
                        iQuestionUser = (Component) constructor
                                .newInstance(entry.question.getSolution(),
                                        entry.question.getUserAnswer(),
                                        entry.question.getDifficulty(),
                                        entry.question.getQuestionText(), null, "");
                    } else if (entry.question instanceof MultipleChoiceMathQuestion) {
                        constructor = entry.question
                                .getClass()
                                .getConstructor(dataStorageClass, dataStorageClass,
                                        float.class, String.class, List.class, List.class, String.class);
                        iQuestionSolution = (Component) constructor
                                .newInstance(entry.question.getSolution(),
                                        entry.question.getSolution(),
                                        entry.question.getDifficulty(),
                                        entry.question.getQuestionText(),
                                        null, null, "");
                        iQuestionUser = (Component) constructor
                                .newInstance(entry.question.getSolution(),
                                        entry.question.getUserAnswer(),
                                        entry.question.getDifficulty(),
                                        entry.question.getQuestionText(), null, null, "");
                    } else {
                        iQuestionSolution = null;
                        iQuestionUser = null;
                    }
                    // The following casts can not fail, because the question is
                    // a component as well


                    // Create the 2 needed click listeners
                    Button.ClickListener clickListenerSol = event -> {
                        Window window = new Window(solution);
                        event.getButton().setEnabled(false);
                        window.addCloseListener(e -> event.getButton()
                                .setEnabled(true));
                        VerticalLayout vl = new VerticalLayout();
                        vl.addComponent(iQuestionSolution);
                        vl.setMargin(true);
                        window.setContent(vl);
                        window.center();
                        window.setWidth("90%");
                        window.setHeight("80%");
                        if (iQuestionSolution instanceof Sizeable) {
                            Sizeable sizeable = iQuestionSolution;
                            sizeable.setSizeFull();
                        }
                        getUI().addWindow(window);
                    };

                    Button.ClickListener clickListenerUA = event -> {
                        Window window = new Window(userAnswer);
                        event.getButton().setEnabled(false);
                        window.addCloseListener(e -> event.getButton()
                                .setEnabled(true));
                        VerticalLayout vl = new VerticalLayout();
                        vl.addComponent(iQuestionUser);
                        vl.setMargin(true);
                        window.setContent(vl);
                        window.center();
                        window.setWidth("90%");
                        window.setHeight("80%");
                        if (iQuestionUser instanceof Sizeable) {
                            Sizeable sizeable = iQuestionUser;
                            sizeable.setSizeFull();
                        }
                        getUI().addWindow(window);
                    };

                    // Create the two buttons
                    qAnswer = new Button(userAnswer, clickListenerUA);
                    qSolution = new Button(solution, clickListenerSol);

                } catch (Exception e) {
                    // Ignore this line in the table
					/*LogHelper
					.logInfo("1 entry's solution/ user answer are missing on the final screen."
							+ entry.question.getClass().getName()
							+ " does not implement the constructors required by"
							+ IQuestion.class.getName());*/
                    LogHelper.logError(e.toString());
                }
            }

            table.addItem(new Object[] { new Integer(nr), entry.question.getDifficulty(),
                    isCorrect(entry.points, entry.question.getMaxPoints()), qAnswer, qSolution}, null);
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

        addComponent(HtmlLabel.getCenteredLabel("h2",
                "Dein Kompetenzniveau ist: <b>" + args.skillLevel + "</b>"));
        addComponent(HtmlLabel.getCenteredLabel("Delta:  " + args.delta));
        if(args.skillLevel <= -2.4) {
            addComponent(HtmlLabel.getCenteredLabel("h3",
                    "<b>A: Werte bis -2,4:</b>"));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Aufgrund dieses Testergebnisses ist leider keine fundierte Aussage über die mathematischen Fähigkeiten möglich."));
        }
        if(args.skillLevel > -2.4 && args.skillLevel <= -0.679) {
            addComponent(HtmlLabel.getCenteredLabel("h3",
                    "<b>B: Werte über -2,4 bis -0,679:</b>"));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Das Testergebnis deutet darauf hin, dass du leichte Aufgaben lösen kannst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Leichte Aufgaben sind etwa Beispiele, die mit einem Schritt und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Mindestens 4 von 5 Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
        }
        if(args.skillLevel > -0.679 && args.skillLevel <= -0.161) {
            addComponent(HtmlLabel.getCenteredLabel("h3",
                    "<b>C: Werte über -0,679 bis -0,161:</b>"));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Das Testergebnis deutet darauf hin, dass du leichte bis mittelschwere Aufgaben lösen kannst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Leichte Aufgaben sind etwa Beispiele, die mit einem Schritt und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Mindestens 4 von 5 Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Mittelschwere Aufgaben zeichnen sich dadurch aus, dass sie mit mehreren Schritten und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Etwas mehr als die Hälfte der Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
        }
        if(args.skillLevel > -0.161 && args.skillLevel <= 0.425) {
            addComponent(HtmlLabel.getCenteredLabel("h3",
                    "<b>D: Werte über -0,161 bis 0,425:</b>"));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Das Testergebnis deutet darauf hin, dass du leichte, mittelschwere und schwere Aufgaben lösen kannst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Leichte Aufgaben sind etwa Beispiele, die mit einem Schritt und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Mindestens 4 von 5 Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Mittelschwere Aufgaben zeichnen sich dadurch aus, dass sie mit mehreren Schritten und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Etwas mehr als die Hälfte der Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Schwere Aufgaben sind etwa Beispiele, bei denen mehrere Schritte zur Lösung notwendig sind und vertraute Verfahren und Formeln leicht adaptiert werden müssen. Ungefähr 1 von 4 Schüler/innen konnte diese Aufgaben bei der Zentralmatura korrekt lösen."));
        }
        if(args.skillLevel > 0.425) {
            addComponent(HtmlLabel.getCenteredLabel("h3",
                    "<b>E: Werte über 0,425:</b>"));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Das Testergebnis deutet darauf hin, dass du leichte, mittelschwere, schwere und sehr schwere Aufgaben lösen kannst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Leichte Aufgaben sind etwa Beispiele, die mit einem Schritt und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Mindestens 4 von 5 Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Mittelschwere Aufgaben zeichnen sich dadurch aus, dass sie mit mehreren Schritten und vertrauten Verfahren bzw. Formeln gelöst oder begründet werden können. Etwas mehr als die Hälfte der Schüler/innen haben diese Aufgaben bei der Zentralmatura korrekt gelöst."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Schwere Aufgaben sind etwa Beispiele, bei denen mehrere Schritte zur Lösung notwendig sind und vertraute Verfahren und Formeln leicht adaptiert werden müssen. Ungefähr 1 von 4 Schüler/innen konnte diese Aufgaben bei der Zentralmatura korrekt lösen."));
            addComponent(HtmlLabel.getCenteredLabel(
                    "Eine sehr schwere Aufgabe zeichnet sich durch die Notwendigkeit von mehreren Lösungsschritten mit Hilfe von adaptierten vertrauten Verfahren bzw. Formeln und einer höheren innermathematischen Reflexion und Rechenfertigkeit aus. Diese Art von Aufgaben werden nur von sehr wenigen Schüler/innen korrekt gelöst."));
        }
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
