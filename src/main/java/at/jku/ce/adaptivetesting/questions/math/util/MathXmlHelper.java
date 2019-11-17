package at.jku.ce.adaptivetesting.questions.math.util;

import at.jku.ce.adaptivetesting.questions.math.*;
import com.vaadin.ui.Image;

public class MathXmlHelper {
    public static MathQuestion fromXml(MathQuestionXml xml, String id) {
        return new MathQuestion(
                xml.getDifficulty(), xml.getQuestion()
                .replace("\\n", " <br />"), xml.getMaterialNr(), xml.getQuestionType(), null, id);
    }

    public static SimpleMathQuestion fromXml(SimpleMathQuestionXml xml, String id) {
        return new SimpleMathQuestion(xml.getDataStorage(), xml.getDifficulty(),
                xml.getQuestion().replace("\\n", " <br />"), null, id);
    }

    public static MultipleChoiceMathQuestion fromXml(MultipleChoiceMathQuestionXml xml, String id) {
        return new MultipleChoiceMathQuestion(xml.getDataStorage(), xml.getDifficulty(),
                xml.getQuestion().replace("\\n", " <br />"), null, null, id);
    }
}
