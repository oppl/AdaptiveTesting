package at.jku.ce.adaptivetesting.views.test.accounting.misc;

import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;

@Theme("vaadin")
public class AccountingCalculatorWindow extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Taschenrechner");
        AccountingCalculator accountingCalculator = new AccountingCalculator();
        setContent(accountingCalculator);
    }
}