package at.jku.ce.adaptivetesting.views.test.accounting.misc;

import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("vaadin")
public class AccountingKontenplan extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Kontenplan");
        GridLayout layout = new GridLayout(1, 1);
        layout.addComponent(AccountingDataProvider.getInstance()
                .toHtmlTable());
        layout.setSizeFull();
        setContent(layout);
    }
}