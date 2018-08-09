package at.jku.ce.adaptivetesting.views.def;

/**
 * Created by Peter
 */

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.*;

public class ErrorInfoWindow extends Window {

    private static final long serialVersionUID = -7005783695341501851L;
    private VerticalLayout vLayout = new VerticalLayout();
    private GridLayout gLayout = new GridLayout(3, 1);
    Button followLink, cancel;

    public ErrorInfoWindow(String errorID, String errorMessage) {
        super("SQL-Syntax Fehler");
        center();
        setContent(vLayout);
        vLayout.addComponent(new HtmlLabel("<p align=\"center\" style=\"color:red\">" + errorMessage + "</p>"));
        gLayout.setMargin(true);
        followLink = new Button("Fehlerdetails anzeigen", (Button.ClickListener) event -> {
            followLink.getUI().getPage().open("https://www.techonthenet.com/oracle/errors/" + errorID.toLowerCase() + ".php",
                    "_blank", false);
            LogHelper.logInfo("Displayed error details");
            close();
        });

        cancel = new Button("Abbrechen");
        cancel.addClickListener( e -> {
            LogHelper.logInfo("No error details needed");
            close();
        });

        gLayout.addComponent(followLink, 0, 0);
        gLayout.addComponent(new HtmlLabel("&ensp;"), 1, 0);
        gLayout.addComponent(cancel, 2, 0);
        vLayout.addComponent(gLayout);
    }
}
