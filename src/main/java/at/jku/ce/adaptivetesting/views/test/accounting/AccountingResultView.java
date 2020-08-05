package at.jku.ce.adaptivetesting.views.test.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.IResultView;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.engine.HistoryEntry;
import at.jku.ce.adaptivetesting.core.engine.ResultFiredArgs;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;

import at.jku.ce.adaptivetesting.views.def.DefaultView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.github.rcaller.exception.ExecutionException;

public class AccountingResultView extends VerticalLayout implements View, IResultView {

	private static final long serialVersionUID = -6619938011293967055L;
	private final String imageFolder = VaadinServlet.getCurrent().getServletConfig().
			getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";
	private String resultsFolder;

	public AccountingResultView(ResultFiredArgs args, String title, String resultsFolder) {
		this.resultsFolder = resultsFolder;
		setSpacing(true);
		addComponent(new HtmlLabel(title));
		//addComponent(HtmlLabel.getCenteredLabel("h2", "Test abgeschlossen"));
		addComponent(HtmlLabel.getCenteredLabel("Der Test wurde beendet, da "
				+ (args.outOfQuestions ? "keine weiteren Fragen verfügbar sind."
						: "Ihr Kompetenzniveau bestimmt wurde.")));

		addComponent(HtmlLabel
				.getCenteredLabel("Im Folgenden sehen Sie die Fragen und die gegebenen Antworten in zeitlich absteigender Reihenfolge."));
		addComponent(HtmlLabel
				.getCenteredLabel("Die Zahl in der ersten Spalte bezieht sich auf die Schwierigkeit der jeweiligen Frage.<br/>Negative Zahlen stehen für leichtere Fragen, positive Zahlen kennzeichnen schwierigere Fragen."));

		// Create HTML table of the history
		Table table = new Table();
		final String solution = "Korrekte Antwort", userAnswer = "Deine Antwort";
		table.addContainerProperty("#", Integer.class, null);
		table.addContainerProperty("Schwierigkeitsgrad", Float.class, null);
		table.addContainerProperty("Resultat", String.class, null);
		table.addContainerProperty(userAnswer, Button.class, null);
		table.addContainerProperty(solution, Button.class, null);
		//List<HistoryEntry> entries = Lists.reverse(args.history);
		List<HistoryEntry> entries = new ArrayList<HistoryEntry>(args.history);
		Collections.reverse(entries);
		int nr = entries.size();
		for (HistoryEntry entry : entries) {
			Button qAnswer = null, qSolution = null;
			if (entry.question instanceof Component && entry.question != null) {
				try {
					Class<? extends AnswerStorage> dataStorageClass = entry.question
							.getSolution().getClass();
					Constructor<? extends IQuestion> constructor = entry.question
							.getClass()
							.getConstructor(dataStorageClass, dataStorageClass,
									float.class, String.class, Image.class, String.class);
					// The following casts can not fail, because the question is
					// a component as well
					Component iQuestionSolution = (Component) constructor
							.newInstance(entry.question.getSolution(),
									entry.question.getSolution(),
									entry.question.getDifficulty(),
									entry.question.getQuestionText(),
									null,"");
					Component iQuestionUser = (Component) constructor
							.newInstance(entry.question.getSolution(),
									entry.question.getUserAnswer(),
									entry.question.getDifficulty(),
									entry.question.getQuestionText(),null,"");

					// Create the 2 needed click listeners
					ClickListener clickListenerSol = event -> {
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

					ClickListener clickListenerUA = event -> {
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
					isCorrect(entry.points, entry.question.getMaxPoints()),
					qAnswer, qSolution }, null);
			nr--;
		}
		int size = table.size();
		if (size > 10) {
			size = 10;
		}
		table.setPageLength(size);
		addComponent(table);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);

		addComponent(HtmlLabel.getCenteredLabel("h3",
				"Dein Kompetenzniveau ist: <b>" + args.skillLevel + "</b>"));
		addComponent(HtmlLabel.getCenteredLabel("Delta:  " + args.delta));
		storeResults(args);

		Image image = new Image("", new FileResource(new File(imageFolder + "accounting_Kompetenzmodell.png")));

		addComponent(image);
		setComponentAlignment(image, Alignment.MIDDLE_CENTER);

		/*Link link = new Link("Umfrage Adaptive Testing",
				new ExternalResource("https://docs.google.com/forms/d/e/1FAIpQLSdg0GyIhMymJaLB6hCSkutV41WqJs09qCUSn9DMmSYJ3Lu_Pg/viewform?c=0&w=1"));
		link.setTargetName("_blank");
		link.setDescription("Der Fragebogen soll Schwächen der Performance und Usability der Test-Software aufzeigen. " +
				"Die Auswertung wird zur Verbessung eventueller Schwachstellen herangezogen. " +
				"Damit die Ergebnisse des Fragebogens denen des Tests zugeordnet werden können, " +
				"ist es notwendig, den zuvor erstellen anonymen Benutzernamen anzugeben.");
		addComponent(link);
		setComponentAlignment(link, Alignment.MIDDLE_CENTER);*/
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
		return points + " / " + maxPoints + " (" + 100 * points / maxPoints
				+ "% )";
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DefaultView.setCurrentPageTitle(event);
	}

}
