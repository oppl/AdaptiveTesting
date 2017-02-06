package at.jku.ce.adaptivetesting.xml.topic.accounting;

import at.jku.ce.adaptivetesting.topic.accounting.MultipleChoiceDataStorage;
import at.jku.ce.adaptivetesting.topic.accounting.ProfitDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 06/02/2017.
 */
@XmlRootElement(name = "multipleChoiceQuestionDataStorage")
public class XmlMultipleChoiceQuestion extends XmlQuestionData<MultipleChoiceDataStorage>{

    private static final long serialVersionUID = -7011780466232380923L;

    public XmlMultipleChoiceQuestion() {
     }

    public XmlMultipleChoiceQuestion(MultipleChoiceDataStorage solution, String questionText,
                                 float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultipleChoiceDataStorage> getDataStorageClass() {
            return MultipleChoiceDataStorage.class;
        }

}

