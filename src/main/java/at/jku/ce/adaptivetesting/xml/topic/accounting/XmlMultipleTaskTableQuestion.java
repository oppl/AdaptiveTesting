package at.jku.ce.adaptivetesting.xml.topic.accounting;

import at.jku.ce.adaptivetesting.topic.accounting.MultiAccountingDataStorage;
import at.jku.ce.adaptivetesting.topic.accounting.MultipleTaskTableDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 06/02/2017.
 */
@XmlRootElement(name = "multipleTaskTableQuestionDataStorage")
public class XmlMultipleTaskTableQuestion extends
        XmlQuestionData<MultipleTaskTableDataStorage> {

    private static final long serialVersionUID = 3262285204313858211L;

    public XmlMultipleTaskTableQuestion() {
    }

    public XmlMultipleTaskTableQuestion(MultipleTaskTableDataStorage solution,
                                      String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultipleTaskTableDataStorage> getDataStorageClass() {
        return MultipleTaskTableDataStorage.class;
    }

}