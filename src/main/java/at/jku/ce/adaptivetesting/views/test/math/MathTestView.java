package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.engine.StudentData;
import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.views.Views;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.views.test.TestView;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MathTestView extends TestView {
    private static final long serialVersionUID = -4764723794449575244L;
    private String studentIDCode = new String();
    private String studentGender = new String();
    private String studentExperience = new String();
    private StudentData student;
    private final String imageFolder = VaadinServlet.getCurrent().getServletConfig().
            getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";
    private MathQuestionKeeperProvider QuestionProvider;
    //private int tries;

    public MathTestView(String quizName) {
        super(quizName);
        QuestionProvider = new MathQuestionKeeperProvider();

        Button cancel = new Button("Test abbrechen");
        cancel.addClickListener(e -> {
            getUI().getNavigator().navigateTo(Views.DEFAULT.toString());
            LogHelper.logInfo("The test has been canceled by the student");
        });
        addHelpButton(cancel);
    }

    //data of the students with the question asked at the beginning
    @Override
    public void startQuiz(StudentData student) {
        // Remove everything from the layout, save it for displaying after clicking OK
        final Component[] components = new Component[getComponentCount()];
        for (int i = 0; i < components.length; i++) {
            components[i] = getComponent(i);
        }
        removeAllComponents();
        // Create first page
        VerticalLayout layout = new VerticalLayout();
        addComponent(layout);

        String defaultValue = "-- Bitte auswählen --";
        ComboBox gender = new ComboBox("Geschlecht");
        String[] genderItems = {defaultValue, "männlich", "weiblich"};
        gender.addItems(genderItems);
        gender.setWidth(15, Sizeable.Unit.PERCENTAGE);
        gender.setValue(defaultValue);
        gender.setNullSelectionAllowed(false);
        gender.setFilteringMode(FilteringMode.CONTAINS);
        gender.setEnabled(true);

        ComboBox experience = new ComboBox("Erfahrung mit Mathematik und Geometrie");
        String[] experienceItems = {defaultValue, "Anfänger", "Fortgeschritten", "Profi"};
        experience.addItems(experienceItems);
        experience.setWidth(15, Sizeable.Unit.PERCENTAGE);
        experience.setValue(defaultValue);
        experience.setNullSelectionAllowed(false);
        experience.setFilteringMode(FilteringMode.CONTAINS);
        experience.setEnabled(true);

        Label studentCode = new Label("<p/>Damit deine Antworten mit späteren Fragebogenergebnissen verknüpft werden können, ist es notwendig, einen anonymen Benutzernamen anzulegen. Erstelle deinen persönlichen Code nach folgendem Muster:", ContentMode.HTML);
        TextField studentCodeC1 = new TextField("Tag und Monat der Geburt (DDMM), z.B. \"1008\" für Geburtstag am 10. August");
        TextField studentCodeC2 = new TextField("Zwei Anfangsbuchstaben des Vornamens, z.B. \"St\" für \"Stefan\"");
        TextField studentCodeC3 = new TextField("Zwei Anfangsbuchstaben des Vornamens der Mutter,, z.B. \"Jo\" für \"Johanna\"");

        Label thankYou = new Label("<p/>Danke für die Angaben.<p/>", ContentMode.HTML);
        Button cont = new Button("Weiter", e -> {
            removeAllComponents();
            studentIDCode = new String(studentCodeC1.getValue() + studentCodeC2.getValue() + studentCodeC3.getValue());
            studentGender = (gender.getValue() == null) ? new String("undefined") : gender.getValue().toString();
            studentExperience = (experience.getValue() == null) ? new String("undefined") : experience.getValue().toString();

            this.student = new StudentData(studentIDCode, quizName, studentGender, studentExperience);
            LogHelper.logInfo("StudentData: " + this.student.toString());

            quizRules(components);
        });

        layout.addComponent(HtmlLabel.getCenteredLabel("h1", "Fragen zu deiner Person"));// Title of the quiz
        layout.addComponent(gender);
        layout.addComponent(experience);
        layout.addComponent(studentCode);
        layout.addComponent(studentCodeC1);
        layout.addComponent(studentCodeC2);
        layout.addComponent(studentCodeC3);
        layout.addComponent(thankYou);
        layout.addComponent(cont);
    }

    public void quizRules(Component[] components){

        VerticalLayout verticalLayout = new VerticalLayout();

        Image image_1 = new Image("Abb. 1", new FileResource(new File(imageFolder + "datamod_tutorial_img_1.png")));
        Image image_2_1 = new Image("Abb. 2.1", new FileResource(new File(imageFolder + "datamod_tutorial_img_2_1.png")));
        Image image_2_2 = new Image("Abb. 2.2", new FileResource(new File(imageFolder + "datamod_tutorial_img_2_2.png")));
        Image image_3 = new Image("Abb. 3", new FileResource(new File(imageFolder + "datamod_tutorial_img_3.png")));
        Image image_4_1 = new Image("Abb. 4.1", new FileResource(new File(imageFolder + "datamod_tutorial_img_4_1.png")));
        Image image_4_2 = new Image("Abb. 4.2", new FileResource(new File(imageFolder + "datamod_tutorial_img_4_2.png")));
        Image image_5_1 = new Image("Abb. 5.1", new FileResource(new File(imageFolder + "datamod_tutorial_img_5_1.png")));
        Image image_5_2 = new Image("Abb. 5.2", new FileResource(new File(imageFolder + "datamod_tutorial_img_5_2.png")));
        Image image_6_1 = new Image("Abb. 6.1", new FileResource(new File(imageFolder + "datamod_tutorial_img_6_1.png")));
        Image image_6_2 = new Image("Abb 6.2", new FileResource(new File(imageFolder + "datamod_tutorial_img_6_2.png")));

        verticalLayout.addComponent(new HtmlLabel("<h1>Anleitung</h1><br>" +
                "In Abb. 1 siehst du wie die Testbeispiele aufgebaut sind.<br><br>" +
                "Fragen können in deutscher- oder englischer Sprache gestellt werden. Der oberste Teil des Beispiels<br>" +
                "besteht aus einem ein- oder mehrzeiligen Erklärungstext, gefolgt von Schemainformationen. In der<br>" +
                "folgenden Abbildung kann man z.B. erkennen, dass die Tabelle <i>Hotel</i> aus drei Spalten besteht, wobei<br>" +
                "die Spalte <b>HNo</b> der Primärschlüssel ist. Fremdschlüssel werden kursiv dargestellt.<br><br>"));
        verticalLayout.addComponent(image_1);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Im unteren Teil werden weiterführende Informationen zu den Zusammenhängen der Tabellen untereinander gegeben.<br><br>" +
                "Klickt man z.B. auf den Button mit dem Namen <i>Hotel</i>, werden die<br><br>"));
        verticalLayout.addComponent(image_2_1);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Tabelleninformationen zu dieser Tabelle in einem Fenster angezeigt.<br>" +
                "(Es können alle Fenster aller Tabellen gleichzeitig offen sein und am Bildschirm verschoben werden)<br><br>"));
        verticalLayout.addComponent(image_2_2);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Braucht man bei der Query-Eingabe Hilfe kann mit dem Button <b>SQL Tutorial</b> das w3schools.com SQL Tutorial<br>" +
                "konsultiert werden. Wie in Abb. 1 ersichtlich, befindet sich dieser im unteren Teil jedes Testbeispiels.<br><br>"));
        verticalLayout.addComponent(image_3);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Da zum Lösen mancher Aufgaben relativ lange SQL-Queries eingegeben werden müssen, hast du die Möglichkeit<br>" +
                "deine Eingaben vor den Abgaben ausgiebig testen zu können. Durch Drücken des <b>Query Diagnose</b> Buttons<br>" +
                "erhältst du entsprechendes Feedback. Für jede Diagnose wird dir ein Versuch abgezogen. Hast du keine Versuche<br>" +
                "mehr übrig, wird deine zuletzt eingegebene Antwort gewertet und das nächste Beispiel geladen. Die maximale<br>" +
                "Anzahl an Versuchen für das gerade bearbeitete Beispiel wird durch den grünen Hinweistext (siehe Abb. 1)<br>" +
                "verdeutlicht. Für das obige Beispiel hast du z.B. max. 3 Versuche um es zu lösen.<br><br>" +
                "<h2>Mögliches Feedback vom CAT System<br><br>"));
        verticalLayout.addComponent(image_4_1);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Die Eingabe ist syntaktisch korrekt, aber das erhaltene Ergebnis stimmt nicht mit dem Geforderten überein.<br><br>"));
        verticalLayout.addComponent(image_4_2);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Es wird ein Fehler, sowie die durch deine Eingabe erhalte Ergebnistabelle angezeigt.<br>(Ein Versuch wird abgezogen)<br><br>"));
        verticalLayout.addComponent(image_5_1);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Die Eingabe weist einen Syntaxfehler auf.<br><br>"));
        verticalLayout.addComponent(image_5_2);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "In diesem Fall werden die Exceptions der ORACLE Datenbank angezeigt. Diese beinhalten Fehlernummern und<br>" +
                "werden rein in Englisch ausgegeben. (Ein Versuch wird Abgezogen)<br><br>"));
        verticalLayout.addComponent(image_6_1);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Die Eingabe ist syntaktisch korrekt und stimmt mit dem geforderten Ergebnis überein.<br><br>"));
        verticalLayout.addComponent(image_6_2);
        verticalLayout.addComponent(new HtmlLabel("<br>" +
                "Es wird angezeigt beim wievielten Versuch du die Aufgabe gelöst hast. Das erhaltene Ergebnis wird ebenfalls<br>" +
                "angezeigt. Mehrmaliges Drücken des <b>Query Diagnose</b> Buttons bei bereits richtigem Antwort beeinflusst<br>" +
                "die Anzahlt der Versuche nicht mehr.<br><br>"));

        Button cont = new Button("Test beginnen", e -> {

            removeAllComponents();
            for (Component c : components) {
                addComponent(c);
            }
            super.startQuiz(student);
        });

        verticalLayout.addComponent(cont);
        addComponent(verticalLayout);
    }

    @Override
    public void loadQuestions() {
        ConnectionProvider.initialize();
        try {
            QuestionProvider.initialize();

            List<MathQuestion> mathList = QuestionProvider.getMathList();

            mathList.forEach(q -> addQuestion(q));

        } catch (JAXBException | IOException e1) {
            LogHelper.logThrowable(e1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
