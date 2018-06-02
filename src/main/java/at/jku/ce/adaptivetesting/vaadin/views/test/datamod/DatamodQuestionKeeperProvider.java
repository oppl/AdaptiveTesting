package at.jku.ce.adaptivetesting.vaadin.views.test.datamod;

import at.jku.ce.adaptivetesting.questions.datamod.*;

import java.util.List;

/**
 * Created by Peter
 */

public class DatamodQuestionKeeperProvider {
    private static DatamodQuestionKeeper dmKeeper = new DatamodQuestionKeeper();

    public void initialize() {
    dmKeeper.initialize();
}

    public int getSize() {
    return dmKeeper.getSize();
}

    public List<SqlQuestion> getSqlList() throws Exception {
        return dmKeeper.getSqlList();
    }
}
