package at.jku.ce.adaptivetesting.xml.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.topic.accounting.AccountingQuestion;
import at.jku.ce.adaptivetesting.topic.accounting.MultipleChoiceQuestion;
import at.jku.ce.adaptivetesting.topic.accounting.ProfitQuestion;

public final class AccountingXmlHelper {
	public static AccountingQuestion fromXml(XmlAccountingQuestion xml, String id) {
		return new AccountingQuestion(xml.getDataStorage(),
				xml.getDifficulty(), xml.getQuestion()
						.replace("\\n", " <br />"), null, id);
	}

	public static ProfitQuestion fromXml(XmlProfitQuestion xml, String id) {
		return new ProfitQuestion(xml.getDataStorage(), xml.getDifficulty(),
				xml.getQuestion().replace("\\n", " <br />"), null, id);

	}

	public static MultipleChoiceQuestion fromXml(XmlMultipleChoiceQuestion xml, String id) {
		return new MultipleChoiceQuestion(xml.getDataStorage(), xml.getDifficulty(),
				xml.getQuestion().replace("\\n", " <br />"), null, id);

	}

}
