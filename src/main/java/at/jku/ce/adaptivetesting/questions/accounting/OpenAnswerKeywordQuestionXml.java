package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 07/02/2017.
 */
@XmlRootElement(name = "openAnswerKeywordQuestionDataStorage")
public class OpenAnswerKeywordQuestionXml extends XmlQuestionData<OpenAnswerKeywordDataStorage> {

    private static final long serialVersionUID = -7011780466232381923L;

    public OpenAnswerKeywordQuestionXml() {

    }

    public OpenAnswerKeywordQuestionXml(OpenAnswerKeywordDataStorage solution, String questionText,
                                        float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<OpenAnswerKeywordDataStorage> getDataStorageClass() {
        return OpenAnswerKeywordDataStorage.class;
    }
}
