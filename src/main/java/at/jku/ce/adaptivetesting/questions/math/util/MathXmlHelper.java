package at.jku.ce.adaptivetesting.questions.math.util;

import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.questions.math.MathQuestionXml;

public class MathXmlHelper {
    public static MathQuestion fromXml(MathQuestionXml xml, String id) {
        return new MathQuestion(
                xml.getDifficulty(), xml.getQuestion()
                .replace("\\n", " <br />"), xml.getMaterialNr(), xml.getQuestionType(), null, id);
    }
}
