package at.jku.ce.adaptivetesting.questions.accounting;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by oppl on 06/02/2017.
 */
@XmlRootElement(name = "multipleTaskTableQuestionDataStorage")
public class MultipleTaskTableQuestionXml extends
        XmlQuestionData<MultipleTaskTableDataStorage> {

    private static final long serialVersionUID = 3262285204313858211L;

    public MultipleTaskTableQuestionXml() {
    }

    public MultipleTaskTableQuestionXml(MultipleTaskTableDataStorage solution,
                                        String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultipleTaskTableDataStorage> getDataStorageClass() {
        return MultipleTaskTableDataStorage.class;
    }

}