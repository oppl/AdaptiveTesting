package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.engine.StudentData;
import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.questions.math.MultipleChoiceMathQuestion;
import at.jku.ce.adaptivetesting.questions.math.SimpleMathQuestion;
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
        verticalLayout.addComponent(new HtmlLabel("<h1>Anleitung</h1><br>" +
                "Bitte beantworte die gestellten Aufgaben. Durch den Button \"Nächste Frage\" kommst " +
                "du zur nächsten Aufgabe, deine Ergebnise werden automatisch ausgewertet. Bitte beachte, dass " +
                "du nicht zu bereits bearbeiteten Fragen zurückgehen kannst! Klicke auf den Button um den Test zu starten." +
                "<br>"));


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

            List<SimpleMathQuestion> simpleMathList = QuestionProvider.getSimpleMathList();

            simpleMathList.forEach(q -> addQuestion(q));

            List<MultipleChoiceMathQuestion> multiMathList = QuestionProvider.getMultiChoiceMathList();

            multiMathList.forEach(q -> addQuestion(q));

        } catch (JAXBException | IOException e1) {
            LogHelper.logThrowable(e1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
