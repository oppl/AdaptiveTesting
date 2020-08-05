package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "simpleMathQuestionDataStorage")
public class SimpleMathQuestionXml extends
        XmlQuestionData<SimpleMathDataStorage> {

    private static final long serialVersionUID = 3262285204313858210L;

    public SimpleMathQuestionXml() {
    }

    public SimpleMathQuestionXml(SimpleMathDataStorage solution,
                                 String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<SimpleMathDataStorage> getDataStorageClass() {
        return SimpleMathDataStorage.class;
    }

}
