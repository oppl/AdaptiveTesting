package at.jku.ce.adaptivetesting.vaadin.views.test.accounting;

import at.jku.ce.adaptivetesting.questions.accounting.*;

import java.util.List;

public class AccountingQuestionKeeperProvider {
    private static AccountingQuestionKeeper accKeeper = new AccountingQuestionKeeper();

    public void initialize() {
        accKeeper.initialize();
    }

    public int getSize() {
        return accKeeper.getSize();
    }

    public List<ProfitQuestion> getProfitList() throws Exception {
        return accKeeper.getProfitList();
    }

    public List<MultipleTaskTableQuestion> getMultipleTaskTableList() throws Exception {
        return accKeeper.getMultipleTaskTableList();
    }

    public List<AccountingQuestion> getAccountingList() throws Exception {
        return accKeeper.getAccountingList();
    }

    public List<MultiAccountingQuestion> getMultiAccountingList() throws Exception {
        return accKeeper.getMultiAccountingList();
    }

    public List<MultipleChoiceQuestion> getMultipleChoiceList() throws Exception {
        return accKeeper.getMultipleChoiceList();
    }

    public List<OpenAnswerKeywordQuestion> getOpenAnswerKeywordList() throws Exception {
        return accKeeper.getOpenAnswerKeywordList();
    }
}
