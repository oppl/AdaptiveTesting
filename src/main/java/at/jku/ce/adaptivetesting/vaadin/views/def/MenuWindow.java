package at.jku.ce.adaptivetesting.vaadin.views.def;

/**
 * Created by Peter
 */

import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.views.Views;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;

public class MenuWindow extends Window {

	private static final long serialVersionUID = -7005783695341501851L;
	private GridLayout gLayout = new GridLayout(7, 1);
	Button adminMenu, resultDlMenu, log, start;

	public MenuWindow() {
		super("Menü");
		center();
		setContent(gLayout);
		gLayout.setMargin(true);

		adminMenu = new Button("Item Administration", (Button.ClickListener) event -> {
			Navigator navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.ADMIN.toString());
			close();
		});

		resultDlMenu = new Button("Test-Ergebnis Download", (Button.ClickListener) event -> {
			Navigator navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.RESULTSDL.toString());
			close();
		});

		log = new Button("Live-Log", (Button.ClickListener) event -> {
			Navigator navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.LOG.toString());
			close();
		});

		start = new Button("Startseite", (Button.ClickListener) event -> {
			Navigator navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.DEFAULT.toString());
			close();
		});

		gLayout.addComponent(adminMenu,0, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),1, 0);
		gLayout.addComponent(resultDlMenu,2, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),3, 0);
		gLayout.addComponent(log,4, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),5, 0);
		gLayout.addComponent(start,6, 0);
	}
	public void deactivateAdminMenuButton() {
		adminMenu.setEnabled(false);
	}

	public void deactivateResultDlMenuButton() {
		resultDlMenu.setEnabled(false);
	}

	public void deactivateLogButton() {
		log.setEnabled(false);
	}

	public void deactivateStartButton() {
		start.setEnabled(false);
	}
}
