package at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting;

import at.jku.ce.adaptivetesting.topic.accounting.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class KeeperOfQuestionsProvider {

    private static KeeperOfQuestions keeper = new KeeperOfQuestions("Rechnungswesentest");

    public int getSize() {
        return keeper.getSize();
    }

    public List<ProfitQuestion> getProfitList() throws Exception {
        return keeper.getProfitList();
    }

    public List<MultipleTaskTableQuestion> getMultipleTaskTableList() throws Exception {
        return keeper.getMultipleTaskTableList();
    }

    public List<AccountingQuestion> getAccountingList() throws Exception {
        return keeper.getAccountingList();
    }

    public List<MultiAccountingQuestion> getMultiAccountingList() throws Exception {
        return keeper.getMultiAccountingList();
    }

    public List<MultipleChoiceQuestion> getMultipleChoiceList() throws Exception {
        return keeper.getMultipleChoiceList();
    }

    public List<OpenAnswerKeywordQuestion> getOpenAnswerKeywordList() throws Exception {
        return keeper.getOpenAnswerKeywordList();
    }
}
