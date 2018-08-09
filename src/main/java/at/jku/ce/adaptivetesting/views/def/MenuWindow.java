package at.jku.ce.adaptivetesting.views.def;

/**
 * Created by Peter
 */

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.views.Views;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class MenuWindow extends Window {

	private static final long serialVersionUID = -7005783695341501851L;
	private GridLayout gLayout = new GridLayout(9, 1);
	Button adminMenu, resultDlMenu, log, start, deletePw;
	Navigator navigator;

	public MenuWindow() {
		super("Navigationsmenü");
		center();
		setContent(gLayout);
		gLayout.setMargin(true);

		adminMenu = new Button("Item Administration", (Button.ClickListener) event -> {
			navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.ADMIN.toString());
			close();
		});

		resultDlMenu = new Button("Test-Ergebnis Download", (Button.ClickListener) event -> {
			navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.RESULTSDL.toString());
			close();
		});

		log = new Button("Live-Log", (Button.ClickListener) event -> {
			navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.LOG.toString());
			close();
		});

		start = new Button("Startseite", (Button.ClickListener) event -> {
			navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.DEFAULT.toString());
			close();
		});

		deletePw = new Button("Passwort löschen", (Button.ClickListener) event -> {
			DefaultViewFooter.USERPWD = "";
			createAndShowNotification("Admin Modus deaktiviert",
					"<p style=\"color:#339933\">Das eingegebene Passwort wurde gelöscht!<br><br>" +
							"Navigationsmenü, Item Administration, Test-Ergebnis Download<br>" +
							"und Live-Log sind nun systemweit gesperrt.</p>", Notification.Type.HUMANIZED_MESSAGE);
			LogHelper.logInfo("password was deleted by user");
			navigator = getUI().getNavigator();
			assert navigator != null;
			navigator.navigateTo(Views.DEFAULT.toString());
			close();
		});
		deletePw.addStyleName("friendly");

		gLayout.addComponent(adminMenu,0, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),1, 0);
		gLayout.addComponent(resultDlMenu,2, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),3, 0);
		gLayout.addComponent(log,4, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),5, 0);
		gLayout.addComponent(start,6, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),7, 0);
		gLayout.addComponent(deletePw,8, 0);
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

	private void createAndShowNotification(String caption, String description, Notification.Type type) {
		description += "<span style=\"position:fixed;top:0;left:0;width:100%;height:100%\"></span>";
		Notification notif = new Notification(caption, description, type, true);
		notif.setDelayMsec(-1);
		notif.show(Page.getCurrent());
	}
}
