package at.jku.ce.adaptivetesting.xml.topic.bpmn;


import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.topic.bpmn.BPMNDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

@XmlRootElement(name = "bpmnQuestionDataStorage")
public class XmlBPMNQuestion extends XmlQuestionData<BPMNDataStorage> {

	/**
	 * topic: modeling
	 * created by: David Graf 06-2016
	 */
	
	private static final long serialVersionUID = 1L;

	public XmlBPMNQuestion() {
	}

	public XmlBPMNQuestion(BPMNDataStorage solution,
			String questionText, float difficulty) {
		super(solution, questionText, difficulty);
	}

	@Override
	public Class<BPMNDataStorage> getDataStorageClass() {
		return BPMNDataStorage.class;
	}
	
}
