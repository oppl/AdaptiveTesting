package at.jku.ce.adaptivetesting.topic.bpmn;

import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;

import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dataStorage")
public class BPMNDataStorage extends AnswerStorage {

	/**
	 * topic: modeling
	 * data of xml question
	 * created by David Graf 06-2016
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "questiontype")
	public String questionType;
	@XmlElement(name = "model")
	public String model;
	@XmlElement(name = "element")
	public BPMNElement[] elements = new BPMNElement[0];
	@XmlElement(name = "errorelement")
	private int errorelement;
	@XmlElement(name = "errortype")
	private BPMNPossibleAnswers errortype;
	@XmlElement(name = "supporttext")
	public String supporttext;
	
	public static BPMNDataStorage getEmptyDataStorage() {
		return new BPMNDataStorage();
	}

	public BPMNDataStorage() {
		this(null);
	}

	public BPMNDataStorage(BPMNPossibleAnswers value) {
		this.errortype = value;
	}
	
	public Object getValue() {
		return errortype == null ? "NULL" : errortype.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (errortype == null ? 0 : errortype.hashCode());
		return result;
	}

	// compare answer of user with correct answer
	public boolean equals(BPMNDataStorage obj, int id) {
		return obj.errortype != null && this.errortype != null && this.errortype.name.equals(obj.errortype.name) && this.errorelement == id;
	}

	public void setValue(BPMNPossibleAnswers value) {
		if (value != null) {
			this.errortype = value;
		}
	}
	
	public String getSupporttext() {
		return supporttext;
	}

	public Resource getImageSource() {
		FileResource resource = new FileResource(new File(VaadinUI.Servlet.getQuestionFolderName()+"/images/"+model));
		return resource;
	}
	
	public BPMNElement[] getModelElements () {
		return elements;	
	}
	
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

}
