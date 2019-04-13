package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mathQuestionDataStorage")
public class MathQuestionXml extends XmlQuestionData<MathDataStorage> {
    private static final long serialVersionUID = -7011780466232381923L;

    public MathQuestionXml() {
    }

    public MathQuestionXml(MathDataStorage solution, String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MathDataStorage> getDataStorageClass() {
        return MathDataStorage.class;
    }
}
