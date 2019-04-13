package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.questions.math.MathQuestion;

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
}
