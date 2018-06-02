package at.jku.ce.adaptivetesting.vaadin.views.def;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

/**
 * Created by Peter
 */

public class PasswordWindow extends Window {

	private static final long serialVersionUID = -7005783695341501851L;
	private GridLayout gLayout = new GridLayout(3, 1);
	private String password = "";
	private PasswordField pwField;
	private Button confirm;

	public PasswordWindow() {
		super("Passworteingabe");
		center();
		setContent(gLayout);
		gLayout.setMargin(true);

		pwField = new PasswordField();
		pwField.setMaxLength(10);

		pwField.addValueChangeListener(event -> password = (String) event.getProperty().getValue());
		confirm = new Button("Bestätigen", (Button.ClickListener) event -> {
			DefaultViewFooter.USERPWD = password;
			if(password.equals(DefaultView.PASSWD)) {
				createAndShowNotification("Passwort korrekt",
						"<p style=\"color:#339933\">Du bist eingeloggt. Das Menü ist nun freigegeben.</p>", Notification.Type.HUMANIZED_MESSAGE);
				LogHelper.logInfo("entered correct password");
				close();
			} else {
				createAndShowNotification("Passwort inkorrekt",
						"Bitte erneut eingeben.", Notification.Type.ERROR_MESSAGE);
				LogHelper.logError("entered incorrect password");
			}
		});
		gLayout.addComponent(pwField,0, 0);
		gLayout.addComponent(new HtmlLabel("&ensp;"),1, 0);
		gLayout.addComponent(confirm,2, 0);
	}

	private void createAndShowNotification(String caption, String description, Notification.Type type) {
		description += "<span style=\"position:fixed;top:0;left:0;width:100%;height:100%\"></span>";
		Notification notif = new Notification(caption, description, type, true);
		notif.setDelayMsec(-1);
		notif.show(Page.getCurrent());
	}
}
