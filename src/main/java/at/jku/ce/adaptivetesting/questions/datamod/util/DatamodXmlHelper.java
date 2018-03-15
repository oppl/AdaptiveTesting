package at.jku.ce.adaptivetesting.questions.datamod.util;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.questions.datamod.*;
import at.jku.ce.adaptivetesting.questions.datamod.SqlQuestionXml;

public final class DatamodXmlHelper {
	public static SqlQuestion fromXml(SqlQuestionXml xml, String id) {
		return new SqlQuestion(xml.getDataStorage(),
				xml.getDifficulty(), xml.getQuestion()
				.replace("\\n", " <br />"), null, id);

	}
}
