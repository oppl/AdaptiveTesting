package at.jku.ce.adaptivetesting.xml.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.topic.accounting.AccountingDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

@XmlRootElement(name = "accountingtQuestionDataStorage")
public class XmlAccountingQuestion extends
		XmlQuestionData<AccountingDataStorage> {

	private static final long serialVersionUID = 3262285204313858210L;

	public XmlAccountingQuestion() {
	}

	public XmlAccountingQuestion(AccountingDataStorage solution,
			String questionText, float difficulty) {
		super(solution, questionText, difficulty);
	}

	@Override
	public Class<AccountingDataStorage> getDataStorageClass() {
		return AccountingDataStorage.class;
	}

}
