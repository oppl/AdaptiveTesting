package at.jku.ce.adaptivetesting.xml.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.topic.accounting.AccountingDataStorage;
import at.jku.ce.adaptivetesting.topic.accounting.MultiAccountingDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

@XmlRootElement(name = "multiAccountingQuestionDataStorage")
public class XmlMultiAccountingQuestion extends
        XmlQuestionData<MultiAccountingDataStorage> {

    private static final long serialVersionUID = 3262285204313858211L;

    public XmlMultiAccountingQuestion() {
    }

    public XmlMultiAccountingQuestion(MultiAccountingDataStorage solution,
                                 String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultiAccountingDataStorage> getDataStorageClass() {
        return MultiAccountingDataStorage.class;
    }

}
