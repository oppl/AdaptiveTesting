package at.jku.ce.adaptivetesting.questions.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

@XmlRootElement(name = "multiAccountingQuestionDataStorage")
public class MultiAccountingQuestionXml extends
        XmlQuestionData<MultiAccountingDataStorage> {

    private static final long serialVersionUID = 3262285204313858211L;

    public MultiAccountingQuestionXml() {
    }

    public MultiAccountingQuestionXml(MultiAccountingDataStorage solution,
                                      String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<MultiAccountingDataStorage> getDataStorageClass() {
        return MultiAccountingDataStorage.class;
    }

}
