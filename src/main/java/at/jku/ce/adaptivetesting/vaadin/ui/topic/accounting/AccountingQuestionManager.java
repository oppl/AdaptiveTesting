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
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import at.jku.ce.adaptivetesting.xml.topic.accounting.*;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.vaadin.ui.QuestionManager;
import com.vaadin.shared.ui.label.ContentMode;

public class AccountingQuestionManager extends QuestionManager {

	private static final long serialVersionUID = -4764723794449575244L;
	private String studentIDCode = new String();
	private Map studentGradesLastYear;
	private Map studentGradesLastTest;
	private String studentClass = new String();
	private String studentGender = new String();
	private StudentData student;

	public AccountingQuestionManager(String quizName) {
		super(quizName);
		Button openKontenplan = new Button("Kontenplan öffnen");
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
		Button openCompanyDescription = new Button("Unternehmensbeschreibung öffnen");
		openCompanyDescription.addClickListener(e -> {
			Window window = new Window("Unternehmensbeschreibung");
			window.setWidth("80%");
			window.setHeight("80%");
			VerticalLayout vl = assembleCompanyDescription();
			Button close = new Button("Schließen");
			close.addClickListener( e1 -> {
				window.close();
			});
			vl.setMargin(true);
			vl.setSpacing(true);
			vl.addComponent(close);
			window.setContent(vl);
			window.center();
			window.setResizable(false);
			getUI().addWindow(window);
		});
		addHelpButton(openKontenplan);
		addHelpButton(openCompanyDescription);
	}

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

		Label gradeLastYear = new Label("<p/>Welche Note hattest du im letzten Zeugnis in ...", ContentMode.HTML);
		TextField gradeLastYearRW = new TextField("Rechungswesen");
		TextField gradeLastYearBWL = new TextField("BWL/BVW");
		TextField gradeLastYearD = new TextField("Deutsch");
		TextField gradeLastYearE = new TextField("Englisch");
		TextField gradeLastYearM = new TextField("Mathematik");

		Label gradeLastTest = new Label("<p/>Welche Note hattest du auf die letzte Schularbeit aus ...", ContentMode.HTML);
		TextField gradeLastTestRW = new TextField("Rechungswesen");
		TextField gradeLastTestBWL = new TextField("BWL/BVW");
		TextField gradeLastTestD = new TextField("Deutsch");
		TextField gradeLastTestE = new TextField("Englisch");
		TextField gradeLastTestM = new TextField("Mathematik");

		Label classNameLabel = new Label("<p/>Welche Klasse besuchst du?",ContentMode.HTML);
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

	public void displayCompanyInfo(Component[] components) {
		// Create second page
		VerticalLayout layout = assembleCompanyDescription();
		layout.setSpacing(true);
		Button cont = new Button("Weiter", e -> {
			removeAllComponents();
			for (Component c : components) {
				addComponent(c);
			}
			super.startQuiz(student);
		});
		layout.addComponent(cont);
//		layout.setComponentAlignment(components[0], Alignment.MIDDLE_CENTER);
		addComponent(layout);

	}

	private VerticalLayout assembleCompanyDescription() {
		VerticalLayout layout = new VerticalLayout();

		addComponent(layout);
		Label label = new Label(
				"Stelle dir vor, du hast dich mit dem Handel von Tablet-PCs aller Art selbständig gemacht und dazu das Einzelunternehmen „World of Tabs e. U.“ vor zwei Jahren gegründet. Da du dich in Rechnungswesen schon gut auskennst, beschließt du selbst die Buchhaltung zu führen.<p/>",
				ContentMode.HTML);
		Label companyData = new Label("<table><tr><td>Firmenname:</td><td>World of Tabs</td></tr><tr><td>Adresse:</td><td>Unterfeld 15</td></tr><tr><td></td><td>4541 Adlwang</td></tr><tr><td>E-mail:</td><td>office@worldtabs.at</td></tr><tr><td>Internet:</td><td>www.worldtabs.at</td></tr><tr><td>UID-Nummer:</td><td>ATU32589716</td></tr></table><p/>", ContentMode.HTML);
		Label descr = new Label("<i>World of Tabs dient im Folgenden als Modellunternehmen, <b>aus dessen Sicht</b> du die Aufgabenstellungen bearbeiten sollst. Wir bitten dich die Aufgaben <b>alleine, ohne Hilfe</b> von Mitschüler/inne/n oder Lehrer/inne/n zu lösen. Du kannst den Kontenplan und einen Taschenrechner verwenden.</i><p/>",ContentMode.HTML);
		Label disclaimer = new Label("<b>Wichtig ist, dass du im Folgenden bei der Angabe der Kontennummer und des Kontennamens die genaue Nummer bzw. Bezeichnung verwendest. Bspw. wird eine Aufgabe falsch gewertet, wenn du die Nummer 30 anstatt 33 für das Lieferverbindlichkeiten wählst oder du den Kontennamen \"Lieferverbindlichkeiten\" anstatt \"AATech\" (bei personifiziertem Lieferantenkonto) für den Lieferanten wählst.<b>",ContentMode.HTML);
		layout.addComponent(HtmlLabel.getCenteredLabel("h1", "Unternehmensbeschreibung"));// Title of the quiz
		layout.addComponent(companyData);
		layout.addComponent(label);
		layout.addComponent(descr);
		layout.addComponent(disclaimer);
		return layout;
	}

	public int loadQuestions(File containingFolder) throws JAXBException,
			IOException {
		assert containingFolder.exists() && containingFolder.isDirectory();
		JAXBContext accountingJAXB = JAXBContext.newInstance(
				XmlAccountingQuestion.class, AccountingDataStorage.class);
		JAXBContext multiAccountingJAXB = JAXBContext.newInstance(
				XmlMultiAccountingQuestion.class, MultiAccountingDataStorage.class);
		JAXBContext profitJAXB = JAXBContext.newInstance(
				XmlProfitQuestion.class, ProfitDataStorage.class);
		JAXBContext multipleChoiceJAXB = JAXBContext.newInstance(
				XmlMultipleChoiceQuestion.class, MultipleChoiceDataStorage.class);
		JAXBContext multipleTaskTableJAXB = JAXBContext.newInstance(
				XmlMultipleTaskTableQuestion.class, MultipleTaskTableDataStorage.class);
		JAXBContext openAnswerKeywordJAXB = JAXBContext.newInstance(
				XmlOpenAnswerKeywordQuestion.class, OpenAnswerKeywordDataStorage.class);


		Unmarshaller accountingUnmarshaller = accountingJAXB
				.createUnmarshaller();
		Unmarshaller multiAccountingUnmarshaller = multiAccountingJAXB
				.createUnmarshaller();
		Unmarshaller profitUnmarshaller = profitJAXB.createUnmarshaller();
		Unmarshaller multipleChoiceUnmarshaller = multipleChoiceJAXB.createUnmarshaller();
		Unmarshaller multipleTaskTableUnmarshaller = multipleTaskTableJAXB.createUnmarshaller();
		Unmarshaller openAnswerKeywordUnmarshaller = openAnswerKeywordJAXB.createUnmarshaller();

		final List<AccountingQuestion> accountingList = new ArrayList<>();
		final List<MultiAccountingQuestion> multiAccountingList = new ArrayList<>();
		final List<ProfitQuestion> profitList = new ArrayList<>();
		final List<MultipleChoiceQuestion> multipleChoiceList = new ArrayList<>();
		final List<MultipleTaskTableQuestion> multipleTaskTableList = new ArrayList<>();
		final List<OpenAnswerKeywordQuestion> openAnswerKeywordList = new ArrayList<>();

		String accountingRootElement = XmlAccountingQuestion.class
				.getAnnotation(XmlRootElement.class).name();
		String multiAccountingRootElement = XmlMultiAccountingQuestion.class
				.getAnnotation(XmlRootElement.class).name();
		String profitRootElement = XmlProfitQuestion.class.getAnnotation(
				XmlRootElement.class).name();
		String multipleChoiceRootElement = XmlMultipleChoiceQuestion.class.getAnnotation(
				XmlRootElement.class).name();
		String multipleTaskTableRootElement = XmlMultipleTaskTableQuestion.class.getAnnotation(
				XmlRootElement.class).name();
		String openAnswerKeywordRootElement = XmlOpenAnswerKeywordQuestion.class.getAnnotation(
				XmlRootElement.class).name();

		File[] questions = containingFolder.listFiles(f -> f
				.isFile()
				&& (f.canRead() || f.setReadable(true))
				&& f.getName().endsWith(".xml"));

		// read all questions
		LogHelper.logInfo("Found "+questions.length+" potential questions");
		int successfullyLoaded = 0;
		for (File f : questions) {
			LogHelper.logInfo("Loading question with filename: " + f.getName());
			BufferedReader reader = null;
			StringBuilder sb = new StringBuilder();
			try {
				reader = new BufferedReader(new InputStreamReader(
						new BOMInputStream(new FileInputStream(f),
								ByteOrderMark.UTF_8), "UTF8"));

				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
			String fileAsString = sb.toString().replaceAll("& ", "&amp; ");
			File image = checkImageAvailable(containingFolder, f.getName());
			if (fileAsString.contains(profitRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ ProfitQuestion.class.getName());
				// Profit Question
				XmlProfitQuestion question = (XmlProfitQuestion) profitUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				ProfitQuestion pq = AccountingXmlHelper.fromXml(question, f.getName());
				if (image!=null) pq.setQuestionImage(new Image("",new FileResource(image)));
				profitList.add(pq);
				successfullyLoaded++;
			} else if (fileAsString.contains(accountingRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ AccountingQuestion.class.getName());
				// Accounting Question
				XmlAccountingQuestion question = (XmlAccountingQuestion) accountingUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				AccountingQuestion aq = AccountingXmlHelper.fromXml(question, f.getName());
				if (image != null) aq.setQuestionImage(new Image("", new FileResource(image)));
				accountingList.add(aq);
				successfullyLoaded++;
			} else if (fileAsString.contains(multiAccountingRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ MultiAccountingQuestion.class.getName());
				// Accounting Question
				XmlMultiAccountingQuestion question = (XmlMultiAccountingQuestion) multiAccountingUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				MultiAccountingQuestion maq = AccountingXmlHelper.fromXml(question, f.getName());
				if (image != null) maq.setQuestionImage(new Image("", new FileResource(image)));
				multiAccountingList.add(maq);
				successfullyLoaded++;
			}
			else if (fileAsString.contains(multipleChoiceRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ MultipleChoiceQuestion.class.getName());
				// Accounting Question
				XmlMultipleChoiceQuestion question = (XmlMultipleChoiceQuestion) multipleChoiceUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				MultipleChoiceQuestion mq = AccountingXmlHelper.fromXml(question, f.getName());
				if (image!=null) mq.setQuestionImage(new Image("",new FileResource(image)));
				multipleChoiceList.add(mq);
				successfullyLoaded++;
			}
			else if (fileAsString.contains(multipleTaskTableRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ MultipleTaskTableQuestion.class.getName());
				// Accounting Question
				XmlMultipleTaskTableQuestion question = (XmlMultipleTaskTableQuestion) multipleTaskTableUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				MultipleTaskTableQuestion mtt = AccountingXmlHelper.fromXml(question, f.getName());
				if (image!=null) mtt.setQuestionImage(new Image("",new FileResource(image)));
				multipleTaskTableList.add(mtt);
				successfullyLoaded++;
			}
			else if (fileAsString.contains(openAnswerKeywordRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ OpenAnswerKeywordQuestion.class.getName());
				// Accounting Question
				XmlOpenAnswerKeywordQuestion question = (XmlOpenAnswerKeywordQuestion) openAnswerKeywordUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				OpenAnswerKeywordQuestion oak = AccountingXmlHelper.fromXml(question, f.getName());
				if (image!=null) oak.setQuestionImage(new Image("",new FileResource(image)));
				openAnswerKeywordList.add(oak);
				successfullyLoaded++;
			}
			else {
				LogHelper.logInfo("QuestionManager: item type not supported for "+f.getName()+", ignoring file.");
//				throw new IllegalArgumentException(
//						"Question type not supported. File: " + f);
				continue;
			}
			LogHelper.logInfo("Loaded question with filename:" + f.getName());
		}
		// Add question to the question manager
		accountingList.forEach(q -> addQuestion(q));
		profitList.forEach(q -> addQuestion(q));
		multipleChoiceList.forEach(q -> addQuestion(q));
		multiAccountingList.forEach(q -> addQuestion(q));
		multipleTaskTableList.forEach(q -> addQuestion(q));
		openAnswerKeywordList.forEach(q -> addQuestion(q));
		LogHelper.logInfo("Successfully loaded "+successfullyLoaded+" questions.");

		/*		MultipleChoiceDataStorage mds = new MultipleChoiceDataStorage();
		HashMap<Integer,String> answerOptions = new HashMap<>();
		answerOptions.put(new Integer(1),"gewinnerhöhend");
		answerOptions.put(new Integer(2),"gewinnerniedrigend");
		answerOptions.put(new Integer(3),"beeinflusst nicht");
		mds.setAnswerOptions(answerOptions);
		Vector<Integer> correctAnswers = new Vector<>();
		correctAnswers.add(new Integer(1));
		correctAnswers.add(new Integer(2));
		mds.setCorrectAnswers(correctAnswers);
		XmlMultipleChoiceQuestion xml = new XmlMultipleChoiceQuestion(mds,"test",1.0f);
		Marshaller jaxbMarshaller = multipleChoiceJAXB.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(xml, System.out);

		MultiAccountingDataStorage ads = new MultiAccountingDataStorage();
		AccountRecordData[] ard1 = new AccountRecordData[3];
		ard1[0] = new AccountRecordData("test1-1",1.0f,10);
		ard1[1] = new AccountRecordData("test1-2",1.0f,10);
		ard1[2] = new AccountRecordData("test1-3",1.0f,10);
		AccountRecordData[] ard2 = new AccountRecordData[3];
		ard2[0] = new AccountRecordData("test2-1",1.0f,10);
		ard2[1] = new AccountRecordData("test2-2",1.0f,10);
		ard2[2] = new AccountRecordData("test2-3",1.0f,10);
		ads.addHaben(ard1);
		ads.addHaben(ard2);
		ads.addSoll(ard1);
		ads.addSoll(ard2);
		XmlMultiAccountingQuestion xml = new XmlMultiAccountingQuestion(ads,"test",1.0f);
		Marshaller jaxbMarshaller = multiAccountingJAXB.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(xml, System.out);

		MultipleTaskTableDataStorage tts = new MultipleTaskTableDataStorage();
		tts.addAnswerColumns(new Integer(1),"USt");
		tts.addAnswerColumns(new Integer(2),"Nettobetrag");
		tts.addAnswerColumns(new Integer(3), "Bruttobetrag");
		tts.addTask(new Integer(1),"531,33 inkl. 20 % USt");
		tts.addTask(new Integer(2),"1255,40 brutto (20 %)");
		tts.addTask(new Integer(3),"163,50 + 10 % USt");
		tts.addTask(new Integer(4),"800 exkl. 10 % USt");
		tts.addCorrectAnswer(new Integer(11),86.89f);
		tts.addCorrectAnswer(new Integer(12),434.44f);
		tts.addCorrectAnswer(new Integer(13),521.33f);
		tts.addCorrectAnswer(new Integer(21),209.23f);
		tts.addCorrectAnswer(new Integer(22),1046.17f);
		tts.addCorrectAnswer(new Integer(23),1255.40f);
		tts.addCorrectAnswer(new Integer(31),16.35f);
		tts.addCorrectAnswer(new Integer(32),163.50f);
		tts.addCorrectAnswer(new Integer(33),179.85f);
		tts.addCorrectAnswer(new Integer(41),80.00f);
		tts.addCorrectAnswer(new Integer(42),800.00f);
		tts.addCorrectAnswer(new Integer(43),880.00f);
		XmlMultipleTaskTableQuestion xml = new XmlMultipleTaskTableQuestion(tts,"Berechnen Sie:",1.0f);
		Marshaller jaxbMarshaller = multipleTaskTableJAXB.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(xml, System.out);

		OpenAnswerKeywordDataStorage oak = new OpenAnswerKeywordDataStorage();
		Vector<String> requiredKeyword = new Vector<>();
		requiredKeyword.add("Test11");
		requiredKeyword.add("Test12");
		oak.addAnswer(requiredKeyword.toArray(new String[]{}));
		Vector<String> requiredKeyword1 = new Vector<>();
		requiredKeyword1.add("Test21");
		requiredKeyword1.add("Test22");
		requiredKeyword1.add("Test23");
		oak.addAnswer(requiredKeyword1.toArray(new String[]{}));
		XmlOpenAnswerKeywordQuestion xml = new XmlOpenAnswerKeywordQuestion(oak,"Dies ist ein Test.",0.5f);
		Marshaller jaxbMarshaller = openAnswerKeywordJAXB.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(xml, System.out);*/

		return questions.length;
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
		}
	}
}
