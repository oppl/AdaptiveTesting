package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.questions.math.MultipleChoiceMathQuestion;
import at.jku.ce.adaptivetesting.questions.math.SimpleMathQuestion;

import java.util.List;

public class MathQuestionKeeperProvider {
    private static MathQuestionKeeper mathKeeper = new MathQuestionKeeper();

    public void initialize() {
        mathKeeper.initialize();
    }

    public int getSize() {
        return mathKeeper.getSize();
    }

    public List<MathQuestion> getMathList() throws Exception {
        return mathKeeper.getMathList();
    }

    public List<SimpleMathQuestion> getSimpleMathList() throws Exception {
        return mathKeeper.getSimpleMathList();
    }

    public List<MultipleChoiceMathQuestion> getMultiChoiceMathList() throws Exception {
        return mathKeeper.getMultiChoiceMathList();
    }
}
