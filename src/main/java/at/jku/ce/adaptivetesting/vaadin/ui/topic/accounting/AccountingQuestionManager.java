package at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import at.jku.ce.adaptivetesting.core.StudentData;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.topic.accounting.*;
import at.jku.ce.adaptivetesting.vaadin.ui.VaadinUI;
import at.jku.ce.adaptivetesting.xml.topic.accounting.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.vaadin.ui.QuestionManager;
import at.jku.ce.adaptivetesting.vaadin.ui.Calculator;
import com.vaadin.shared.ui.label.ContentMode;
import org.w3c.dom.html.HTMLLabelElement;

public class AccountingQuestionManager extends QuestionManager {

	private static final long serialVersionUID = -4764723794449575244L;
	private String studentIDCode = new String();
	private Map studentGradesLastYear;
	private Map studentGradesLastTest;
	private String studentClass = new String();
	private String studentGender = new String();
	private StudentData student;
	private final String imageFolder = VaadinServlet.getCurrent().getServletConfig().
			getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";

	public AccountingQuestionManager(String quizName) {
		super(quizName);

		//graphical Interface Kontenplan
		Button openKontenplan = new Button("Kontenplan");
		openKontenplan.addClickListener(e -> {
			openKontenplan.setEnabled(false);
			// Create Window with layout
			Window window = new Window("Kontenplan");
			GridLayout layout = new GridLayout(1, 1);
			layout.addComponent(AccountingDataProvider.getInstance()
					.toHtmlTable());
			layout.setSizeFull();
			window.setContent(layout);
			window.center();
			window.setWidth("60%");
			window.setHeight("80%");
			window.setResizable(false);
			window.addCloseListener(e1 -> openKontenplan.setEnabled(true));
			getUI().addWindow(window);

		});

		//graphical Interface Unternehmensbeschreibung
		Button openCompanyDescription = new Button("Unternehmensbeschreibung");
		openCompanyDescription.addClickListener(e -> {
			Window window = new Window("Unternehmensbeschreibung");
			window.setWidth("80%");
			window.setHeight("80%");
			VerticalLayout vl = assembleCompanyDescription();
			/*Button close = new Button("Schließen");
			close.addClickListener( e1 -> {
				window.close();
			});*/
			vl.setMargin(true);
			vl.setSpacing(true);
			//vl.addComponent(close);
			window.setContent(vl);
			window.center();
			window.setResizable(false);
			getUI().addWindow(window);
		});

		//graphical Interface Personalverrechnungstabelle
		Button openPersBilling = new Button("Personalverrechnungstabelle");
		openPersBilling.addClickListener(e -> {
			Window window = new Window("Personalverrechnungstabelle");
			window.setWidth("80%");
			window.setHeight("80%");
			VerticalLayout vl = assemblePersBilling();

			/*Button close = new Button("Schließen");
			close.addClickListener( e1 -> {
				window.close();
			});*/
			vl.setMargin(true);
			vl.setSpacing(true);
			//vl.addComponent(close);
			window.setContent(vl);
			window.center();
			window.setResizable(false);
			getUI().addWindow(window);
		});

		//graphical Interface Taschenrechner
		Button openCalculator = new Button("Taschenrechner");
		openCalculator.addClickListener(e -> {
			//Calculator Calculator = new Calculator();
			//getUI().addWindow(Calculator.getWindow());
			Notification.show("Zur Zeit nicht verfügbar:\nBitte eigenen Taschenrechner verwenden.");
		});

		addHelpButton(openKontenplan);
		addHelpButton(openCompanyDescription);
		addHelpButton(openPersBilling);
		addHelpButton(openCalculator);
	}

	//just a layout used for the Personalverrechnungstabelle.jpg
	private VerticalLayout assemblePersBilling() {

		VerticalLayout layout = new VerticalLayout();

		layout.setSizeFull();

		Image image = new Image("",
				new FileResource(new File(imageFolder + "Personalverrechnungstabelle.jpg")));
		image.setWidth("80%");
		layout.addComponent(image);
		layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

		// Für was ist das zweite Image?

		/*Image image_2 = new Image("",
		 		new FileResource(new File (imageFolder + "index.jpg")));
		layout.addComponent(image_2);
		layout.setComponentAlignment(image_2, Alignment.MIDDLE_CENTER);*/

		layout.setHeight(null);
		return layout;
	}

	//data of the students with the question asked at the beginning
	@Override
	public void startQuiz(StudentData student) {
		LogHelper.logInfo("loading private questions");
		// Remove everything from the layout, save it for displaying after
		// clicking OK
		final Component[] components = new Component[getComponentCount()];
		for (int i = 0; i < components.length; i++) {
			components[i] = getComponent(i);


		}
		removeAllComponents();
		// Create first page
		VerticalLayout layout = new VerticalLayout();
		addComponent(layout);


		ComboBox gender = new ComboBox("Geschlecht");
		String[] genderItems = {"männlich", "weiblich"};
		gender.addItems(genderItems);
		//gender.setSizeFull();
		gender.setEnabled(true);

		Label gradeLastYear = new Label("<p/>Welche Note hatten Sie im letzten Zeugnis in ...", ContentMode.HTML);
		TextField gradeLastYearRW = new TextField("Rechungswesen");
		TextField gradeLastYearBWL = new TextField("BWL/BVW");
		TextField gradeLastYearD = new TextField("Deutsch");
		TextField gradeLastYearE = new TextField("Englisch");
		TextField gradeLastYearM = new TextField("Mathematik");

		Label gradeLastTest = new Label("<p/>Welche Note hatten Sie auf die letzte Schularbeit aus ...", ContentMode.HTML);
		TextField gradeLastTestRW = new TextField("Rechungswesen");
		TextField gradeLastTestBWL = new TextField("BWL/BVW");
		TextField gradeLastTestD = new TextField("Deutsch");
		TextField gradeLastTestE = new TextField("Englisch");
		TextField gradeLastTestM = new TextField("Mathematik");

		Label classNameLabel = new Label("<p/>Welche Klasse besuchen Sie?",ContentMode.HTML);
		TextField className = new TextField("(z.B. 4A)");

		Label studentCode = new Label("<p/>Damit deine Antworten mit späteren Fragebogenergebnissen verknüpft werden können, ist es notwendig, einen anonymen Benutzernamen anzulegen. Erstelle deinen persönlichen Code nach folgendem Muster:",ContentMode.HTML);
		TextField studentCodeC1 = new TextField("Tag und Monat der Geburt (DDMM), z.B. \"1008\" für Geburtstag am 10. August");
		TextField studentCodeC2 = new TextField("Zwei Anfangsbuchstaben des Vornamens, z.B. \"St\" für \"Stefan\"");
		TextField studentCodeC3 = new TextField("Zwei Anfangsbuchstaben des Vornamens der Mutter,, z.B. \"Jo\" für \"Johanna\"");

		Label thankYou = new Label("<p/>Danke für die Angaben.<p/>", ContentMode.HTML);
		Button cont = new Button("Weiter", e -> {
			removeAllComponents();
			studentIDCode = new String(studentCodeC1.getValue()+studentCodeC2.getValue()+studentCodeC3.getValue());
			studentGender = (gender.getValue() == null) ? new String("undefined") : gender.getValue().toString();
			studentClass = className.getValue();

			studentGradesLastYear = new HashMap();
			studentGradesLastYear.put("RW",gradeLastYearRW.getValue());
			studentGradesLastYear.put("BWL",gradeLastYearBWL.getValue());
			studentGradesLastYear.put("D",gradeLastYearD.getValue());
			studentGradesLastYear.put("E",gradeLastYearE.getValue());
			studentGradesLastYear.put("M",gradeLastYearM.getValue());

			studentGradesLastTest = new HashMap();
			studentGradesLastTest.put("RW",gradeLastTestRW.getValue());
			studentGradesLastTest.put("BWL",gradeLastTestBWL.getValue());
			studentGradesLastTest.put("D",gradeLastTestD.getValue());
			studentGradesLastTest.put("E",gradeLastTestE.getValue());
			studentGradesLastTest.put("M",gradeLastTestM.getValue());

			this.student = new StudentData(studentIDCode, studentGender,studentClass,studentGradesLastYear,studentGradesLastTest);
			LogHelper.logInfo("StudentData: " + this.student.toString());

			displayCompanyInfo(components);
		});

		layout.addComponent(HtmlLabel.getCenteredLabel("h1", "Fragen zu deiner Person"));// Title of the quiz

		layout.addComponent(gender);

		layout.addComponent(gradeLastYear);
		layout.addComponent(gradeLastYearRW);
		layout.addComponent(gradeLastYearBWL);
		layout.addComponent(gradeLastYearD);
		layout.addComponent(gradeLastYearE);
		layout.addComponent(gradeLastYearM);

		layout.addComponent(gradeLastTest);
		layout.addComponent(gradeLastTestRW);
		layout.addComponent(gradeLastTestBWL);
		layout.addComponent(gradeLastTestD);
		layout.addComponent(gradeLastTestE);
		layout.addComponent(gradeLastTestM);

		layout.addComponent(classNameLabel);
		layout.addComponent(className);

		layout.addComponent(studentCode);
		layout.addComponent(studentCodeC1);
		layout.addComponent(studentCodeC2);
		layout.addComponent(studentCodeC3);

		layout.addComponent(thankYou);
		layout.addComponent(cont);
//		layout.setComponentAlignment(components[0], Alignment.MIDDLE_CENTER);
	}

	//second page
	public void displayCompanyInfo(Component[] components) {
		// Create second page
		VerticalLayout layout = assembleCompanyDescription();
		layout.setSpacing(true);
		Button cont = new Button("Weiter", e -> {

			removeAllComponents();
			quizRules(components);
		});
		layout.addComponent(cont);
//		layout.setComponentAlignment(components[0], Alignment.MIDDLE_CENTER);
		addComponent(layout);
	}

	//describes what happens, when you click on the weiter button
	public void quizRules(Component[] components){

		VerticalLayout layout = assemleRules();
		layout.setSpacing(true);
		Button cont = new Button("Weiter", e -> {

			removeAllComponents();
			for (Component c : components) {
				addComponent(c);
			}
			super.startQuiz(student);
		});

		layout.addComponent(cont);
		addComponent(layout);
	}

	//vertical layout of the description of the ausgangssituation
	private VerticalLayout assembleCompanyDescription() {
		VerticalLayout layout = new VerticalLayout();
		addComponent(layout);

		Label initSit = new Label("<table>" +
				"<tbody>" +
				"<caption style=\"font-size:25px\"><strong>Ausgangssituation</strong></caption>" +
				"<tr>" +
				"<td colspan=\\\"4\\\"><strong><br>Sie sind als selbständiger " +
				"Steuerberater und Buchhalter tätig.<br>" +
				"Zu deinen Kunden gehören die unten angeführten Unternehmen.<br>" +
				"Für diese übernehmen Sie die Buchhaltung, d.h. Sie verbuchen die " +
				"angeführten Geschäftsfälle aus deren Sicht.</strong></td>" +
				"</tr>" +
				"</tbody>" +
				"</table>", ContentMode.HTML);
		layout.addComponent(initSit);

		Label contentTable = new Label("<table>" +
				"<tbody>" +
				"<tr>" +
				"<td colspan=\"4\"><strong><br>Unternehmensbeschreibungen</strong></td>" +
				"</tr>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<tr>" +
				"<td>Firmenname:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td><strong>World of Tabs GmbH</strong></td>" +
				"<td><strong>Restaurant Sommer</strong></td>" +
				"<td><strong>ATM GmbH</strong></td>" +
				"</tr>" +
				"<tr>" +
				"<td>Adresse:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>Unterfeld 15</td>" +
				"<td>Am Berg 5</td>" +
				"<td>Altenbergstr. 7</td>" +
				"</tr>" +
				"<tr>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>4541 Adlwang</td>" +
				"<td>4452 Ternberg</td>" +
				"<td>4040 Linz</td>" +
				"</tr>" +
				"<tr>" +
				"<td>E-Mail:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>office@worldtabs.at</td>" +
				"<td>office@summer.at</td>" +
				"<td>office@atm.at</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Internet:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>www.worldtabs.at</td>" +
				"<td>www.sommer.at</td>" +
				"<td>www.atm.at</td>" +
				"</tr>" +
				"<tr>" +
				"<td>UID-Nummer:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>ATU32589716</td>" +
				"<td>ATU89716325</td>" +
				"<td>ATU58932761</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Unternehmens-<br>gegenstand:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>Handel mit Tablets<br>und Zubehör</td>" +
				"<td>Restaurant mit<br>regionaler Küche</td>" +
				"<td>Metallbearbeitung: Bohren,<br>Fräsen, Stanzen, Lasern, …</td>" +
				"</tr>" +
				"<tr>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan=\"4\"><strong><br>Hinweise zu den laufenden " +
				"und den Um- und Nachbuchungen</strong></td>" +
				"</tr>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<tr>" +
				"<td>Buchung:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">Sofern nichts Anderes angeführt ist, wird " +
				"erfolgsorientiert und nicht <br> bestandsorientiert gebucht. " +
				"D.h., sofern nichts anderes angeführt ist, wird<br>darauf " +
				"abgezielt, den Gewinn niedrig zu halten, um Steuerabgaben " +
				"gering<br>zu halten.</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Abschlussjahr:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">2016</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Abschreibung:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">Halbjahres- bzw. Ganzjahresabschreibung</td>" +
				"</tr><tr>" +
				"<td>Skonto:</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">Wird stets in Anspruch genommen.</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Zeitliche:<br> Abgrenzung</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">Die Abgrenzung erfolgt am Jahresende im Rahmen " +
				"der Um- und<br>Nachbuchungen.</td>" +
				"</tr>" +
				"<tr>" +
				"<td>Geringwertig <br> Wirtschaftsgüter</td>" +
				"<td>&emsp;&emsp;&emsp;</td>" +
				"<td colspan=\"3\">Sofern nichts Anderes angeführt ist, wird für " +
				"geringwertige <br> Wirtschaftsgüter mit Anschaffungs- bzw. " +
				"Herstellungskosten bis 400,00<br>die Bewertungsfreiheit nach " +
				"§ 13 EStG (Sofortabschreibung) in Anspruch<br>genommen.</td>" +
				"</tr>" +
				"</tbody>" +
				"</table>", ContentMode.HTML);
		layout.addComponent(contentTable);
		/*
		Label descr = new Label("<i>World of Tabs dient im Folgenden als Modellunternehmen, <b>aus dessen Sicht</b> Sie die Aufgabenstellungen bearbeiten sollen. Wir bitten dich die Aufgaben <b>alleine, ohne Hilfe</b> von Mitschüler/inne/n oder Lehrer/inne/n zu lösen. Sie können den Kontenplan und einen Taschenrechner verwenden.</i><p/>",ContentMode.HTML);
		Label disclaimer = new Label("<b>Wichtig ist, dass Sie im Folgenden bei der Angabe der Kontennummer und des Kontennamens die genaue Nummer bzw. Bezeichnung verwenden. Bspw. wird eine Aufgabe falsch gewertet, wenn Sie die Nummer 30 anstatt 33 für das Lieferverbindlichkeiten wählen oder Sie den Kontennamen \"Lieferverbindlichkeiten\" anstatt \"AATech\" (bei personifiziertem Lieferantenkonto) für den Lieferanten wählen.<b>",ContentMode.HTML);
		layout.addComponent(HtmlLabel.getCenteredLabel("h1", "Unternehmensbeschreibung"));// Title of the quiz
		*/
		//layout.addComponent(descr);
		//layout.addComponent(disclaimer);
		return layout;
	}

	//vertical layout of the description of the Bearbeitungshinweise
	private VerticalLayout assemleRules() {

		VerticalLayout layout = new VerticalLayout();
		addComponent(layout);
		Label hints_top = new Label("<table>" +
				"<tbody>" +
				"<caption style=\"font-size:25px\"><strong>Bearbeitungshinweise</strong></caption>" +
				"<tr>" +
				"<td valign=\"top\"><br>1.</td>" +
				"<td><br>Wir bitten Sie die Aufgaben <strong>alleine, ohne Hilfe</strong> " +
				"von anderen Personen oder Unterlagen<br> zu lösen.</td>" +
				"</tr>" +
				"<tr>" +
				"<td valign=\"top\">2.</td>" +
				"<td>Sie können den <strong>Kontenplan</strong> und einen <strong>" +
				"Taschenrechner</strong> verwenden.</td>" +
				"</tr>" +
				"<tr>" +
				"<td valign=\"top\">3.</td>" +
				"<td>Wichtig ist, dass Sie bei der Angabe der <strong>" +
				"Kontennummer</strong> und des <strong>Kontennamens</strong> die<br><strong>" +
				"genaue Nummer bzw. Bezeichnung</strong> verwenden. Bspw. wird eine Aufgabe " +
				"falsch<br>gewertet, <u>wenn Sie die Nummer 30 anstatt 33 für das " +
				"Lieferverbindlichkeiten wählen oder<br> Sie den Kontennamen \"" +
				"Lieferverbindlichkeiten\" anstatt \"AATech\"</u> (bei personifiziertem<br>" +
				"Lieferantenkonto) für den Lieferanten wählen.</td>" +
				"</tr>" +
				"<tr>" +
				"<td valign=\"top\">4.</td>" +
				"<td>Die <strong>folgende Abbildung</strong> zeigt, wie das in diesem Test verwendete Onlineformular " +
				"ausgefüllt<br>werden soll, um einen Geschäftsfall korrekt zu verbuchen.</td>" +
				"</tr>" +
				"</tbody>" +
				"</table>", ContentMode.HTML);
		layout.addComponent(hints_top);

		// Image as a file resource
		Image image = new Image("",
				new FileResource(new File(imageFolder + "Musterbeispiel_Buchungssatz.jpg")));
		image.setWidth("60%");
		layout.addComponent(image);

		Label hints_bottom = new Label("<table>" +
				"<tbody>" +
				"<tr>" +
				"<td colspan=\"2\"><strong><br>Viel Erfolg!</strong></td>" +
				"</tr>" +
				"</tbody>" +
				"</table>", ContentMode.HTML);
		layout.addComponent(hints_bottom);

		return layout;
	}

	public int loadQuestions(File containingFolder) throws Exception {
		// Add question to the question manager

		KeeperOfQuestionsProvider prov = new KeeperOfQuestionsProvider();

		List<AccountingQuestion> accountingList = prov.getAccountingList();
		List<MultiAccountingQuestion> multiAccountingList = prov.getMultiAccountingList();
		List<ProfitQuestion> profitList = prov.getProfitList();
		List<MultipleChoiceQuestion> multipleChoiceList = prov.getMultipleChoiceList();
		List<MultipleTaskTableQuestion> multipleTaskTableList = prov.getMultipleTaskTableList();
		List<OpenAnswerKeywordQuestion> openAnswerKeywordList = prov.getOpenAnswerKeywordList();

		accountingList.forEach(q -> addQuestion(q));
		multiAccountingList.forEach(q -> addQuestion(q));
		profitList.forEach(q -> addQuestion(q));
		multipleChoiceList.forEach(q -> addQuestion(q));
		multipleTaskTableList.forEach(q -> addQuestion(q));
		openAnswerKeywordList.forEach(q -> addQuestion(q));

		LogHelper.logInfo("!!!!!!!!!!!:"+ accountingList.isEmpty()  + accountingList.size());
		LogHelper.logInfo("Successfully loaded existing question(s).");

		return prov.getSize();
	}

	private File checkImageAvailable(File containingFolder, String fileName) {
		String imageName = fileName.replace(".xml",".png");
		File image = new File(containingFolder,imageName);
		if (image.exists()) {
			return image;
		}
		else {
			imageName = fileName.replace(".xml",".jpg");
			image = new File(containingFolder,imageName);
			if (image.exists()) {
				return image;
			}
			else {
				return null;
			}
		}
	}

	@Override
	public void loadQuestions() {
		try {

			loadQuestions(new File(VaadinUI.Servlet.getQuestionFolderName()));

		} catch (JAXBException | IOException e1) {
			LogHelper.logThrowable(e1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void questionLoadedInfo(File file, int counter, String questionType) {
		counter++;
		LogHelper.logInfo("(" + counter + ") Loading questionfile: " + file.getName());
		LogHelper.logInfo("Type: " + questionType);
	}
}
