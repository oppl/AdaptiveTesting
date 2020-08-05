package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

    @XmlRootElement(name = "multipleChoiceMathQuestionDataStorage")
public class MultipleChoiceMathQuestionXml extends XmlQuestionData<MultipleChoiceMathDataStorage> {

    private static final long serialVersionUID = -7011780466232380923L;

    public MultipleChoiceMathQuestionXml() {
    }

    public MultipleChoiceMathQuestionXml(MultipleChoiceMathDataStorage solution, String questionText,
                                     float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultipleChoiceMathDataStorage> getDataStorageClass() {
        return MultipleChoiceMathDataStorage.class;
    }
}