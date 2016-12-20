package at.jku.ce.adaptivetesting.vaadin.ui.topic.bpmn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.core.StudentData;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.topic.accounting.ProfitQuestion;
import at.jku.ce.adaptivetesting.topic.bpmn.BPMNDataStorage;
import at.jku.ce.adaptivetesting.topic.bpmn.BPMNQuestion;
import at.jku.ce.adaptivetesting.vaadin.ui.QuestionManager;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import at.jku.ce.adaptivetesting.xml.topic.bpmn.BPMNXmlHelper;
import at.jku.ce.adaptivetesting.xml.topic.bpmn.XmlBPMNQuestion;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;


public class BPMNQuestionManager extends QuestionManager {

	/**
	 * topic: modeling
	 * class for read xml questions to java object and handle questions
	 * created by David Graf 06-2016
	 */
	
	private static final long serialVersionUID = -4764723794449575244L;
	
	public BPMNQuestionManager(String quizName) {
		super(quizName);
		
		// button for help of bpmn notation
		Button openBPMN = new Button("BPMN Notation");
		openBPMN.addClickListener(e -> {
			
			final String path = VaadinUI.Servlet.getQuestionFolderName()+"/help/BPMN2_0_Poster_DE.pdf";

		    Resource pdf = new FileResource(new File(path));
		    setResource("help", pdf);
		    ResourceReference rr = ResourceReference.create(pdf, this, "help");
		    Page.getCurrent().open(rr.getURL(), "blank_");
			
		});
		addHelpButton(openBPMN);
	}
	
	@Override
	public void startQuiz(StudentData student) {
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
		Label label = new Label(
		"This Adaptive Testing System is a Beta Application to assess abstraction modelling skills in Business Process Modelling." + "<br>" 
		+ "Please consider that there are only a few Pilot Items implemented." + "<br>"  
		+ "The Items were implemented as part of a bachelor thesis." + "<br>"  + "<br>"
		+ "Try to answer following questions!" + "<br>" + "<br>" ,
					ContentMode.HTML);
		Button cont = new Button("Continue", e -> {
			removeAllComponents();
			for (Component c : components) {
				addComponent(c);
			}
			super.startQuiz(null);
		});
		layout.addComponent(components[0]);// Title of the quiz
		layout.addComponent(label);
		layout.addComponent(cont);
		layout.setComponentAlignment(components[0], Alignment.MIDDLE_CENTER);

	}
	
	public int loadQuestions(File containingFolder) throws JAXBException,
			IOException {
		assert containingFolder.exists() && containingFolder.isDirectory();
		JAXBContext bpmnJAXB = JAXBContext.newInstance(
				XmlBPMNQuestion.class, BPMNDataStorage.class);

		Unmarshaller bpmnUnmarshaller = bpmnJAXB
				.createUnmarshaller();
		
		final List<BPMNQuestion> bpmnList = new ArrayList<>();
		
		String bpmnRootElement = XmlBPMNQuestion.class
				.getAnnotation(XmlRootElement.class).name();
		
		File[] questions = containingFolder.listFiles(f -> f
				.isFile()
				&& (f.canRead() || f.setReadable(true))
				&& f.getName().endsWith("_bpmn.xml"));
		
		// read all questions
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
			if (fileAsString.contains(bpmnRootElement)) {
				LogHelper.logInfo("Question detected as "
						+ ProfitQuestion.class.getName());
				// Profit Question
				XmlBPMNQuestion question = (XmlBPMNQuestion)bpmnUnmarshaller
						.unmarshal(new StringReader(fileAsString));
				bpmnList.add(BPMNXmlHelper.fromXml(question));
			}  else {
				throw new IllegalArgumentException(
						"Question type not supported. File: " + f);
			}
			LogHelper.logInfo("Loaded question with filename:" + f.getName());
		}
		// Add question to the question manager
		bpmnList.forEach(q -> addQuestion(q));
		return questions.length;
		}
	
	@Override
	public void loadQuestions() {
		try {
			loadQuestions(new File(VaadinUI.Servlet.getQuestionFolderName()));
		} catch (JAXBException | IOException e1) {
			Notification.show("Questions could not be loaded - FATAL error",
					e1.getMessage() + e1.getMessage(), Type.ERROR_MESSAGE);
			LogHelper.logThrowable(e1);
		}

	}

}
