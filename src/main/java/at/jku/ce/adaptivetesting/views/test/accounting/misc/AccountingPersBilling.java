package at.jku.ce.adaptivetesting.views.test.accounting.misc;

import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.io.File;

@Theme("vaadin")
public class AccountingPersBilling extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Personalverrechnungstabelle");
        VerticalLayout vl = assemblePersBilling();
        vl.setMargin(true);
        vl.setSpacing(true);
        vl.setSizeFull();
        setContent(vl);
    }

    private VerticalLayout assemblePersBilling() {
        String imageFolder = VaadinServlet.getCurrent().getServletConfig().
                getServletContext().getInitParameter("at.jku.ce.adaptivetesting.imagefolder") + "/";
        VerticalLayout layout = new VerticalLayout();

        layout.setSizeFull();

        Image image = new Image("",
                new FileResource(new File(imageFolder + "Personalverrechnungstabelle.jpg")));
        image.setWidth("80%");
        layout.addComponent(image);
        layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

        layout.setHeight(null);
        return layout;
    }
}