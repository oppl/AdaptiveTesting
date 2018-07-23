package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 06/02/2017.
 */
@XmlRootElement(name = "multipleChoiceQuestionDataStorage")
public class MultipleChoiceQuestionXml extends XmlQuestionData<MultipleChoiceDataStorage>{

    private static final long serialVersionUID = -7011780466232380923L;

    public MultipleChoiceQuestionXml() {
    }

    public MultipleChoiceQuestionXml(MultipleChoiceDataStorage solution, String questionText,
                                     float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultipleChoiceDataStorage> getDataStorageClass() {
            return MultipleChoiceDataStorage.class;
        }
}

