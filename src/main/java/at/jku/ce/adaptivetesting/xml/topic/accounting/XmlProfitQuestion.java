package at.jku.ce.adaptivetesting.xml.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.topic.accounting.ProfitDataStorage;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

@XmlRootElement(name = "profitQuestionDataStorage")
public class XmlProfitQuestion extends XmlQuestionData<ProfitDataStorage> {

	private static final long serialVersionUID = -7011780466232380923L;

	public XmlProfitQuestion() {

	}

	public XmlProfitQuestion(ProfitDataStorage solution, String questionText,
			float difficulty) {
		super(solution, questionText, difficulty);
	}

	@Override
	public Class<ProfitDataStorage> getDataStorageClass() {
		return ProfitDataStorage.class;
	}

}
