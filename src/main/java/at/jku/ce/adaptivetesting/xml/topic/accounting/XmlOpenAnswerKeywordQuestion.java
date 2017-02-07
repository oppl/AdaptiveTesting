package at.jku.ce.adaptivetesting.xml.topic.accounting;

import at.jku.ce.adaptivetesting.topic.accounting.OpenAnswerKeywordDataStorage;
import at.jku.ce.adaptivetesting.topic.accounting.ProfitDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 07/02/2017.
 */
@XmlRootElement(name = "openAnswerKeywordQuestionDataStorage")
public class XmlOpenAnswerKeywordQuestion extends XmlQuestionData<OpenAnswerKeywordDataStorage> {

    private static final long serialVersionUID = -7011780466232381923L;

    public XmlOpenAnswerKeywordQuestion() {

    }

    public XmlOpenAnswerKeywordQuestion(OpenAnswerKeywordDataStorage solution, String questionText,
                             float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<OpenAnswerKeywordDataStorage> getDataStorageClass() {
        return OpenAnswerKeywordDataStorage.class;
    }
}
