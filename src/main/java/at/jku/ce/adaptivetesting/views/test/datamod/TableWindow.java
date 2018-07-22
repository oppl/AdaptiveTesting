package at.jku.ce.adaptivetesting.views.test.datamod;

import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

/**
 * Created by Peter
 */

public class TableWindow extends Window {

	private static final long serialVersionUID = -7005783695341501851L;
	private GridLayout gLayout = new GridLayout(2, 1);

	public TableWindow(String tableName) {
		super("Tabelle: " + tableName);
		doBefore();
		gLayout.addComponent(ConnectionProvider.drawTable(tableName),0, 0);
		doAfter();
	}

	public TableWindow() {
		super("Ergebnistabelle");
		doBefore();
	}

	public void drawResultTable(String sql) {
		gLayout.addComponent(ConnectionProvider.drawResultTable(sql),0, 0);
		doAfter();
	}

	private void doBefore() {
		center();
		setContent(gLayout);
		gLayout.setMargin(true);

	}
	private void doAfter() {
		HtmlLabel spacer = new HtmlLabel();
		spacer.setValue("&ensp;");
		gLayout.addComponent(spacer,1, 0);
	}

	public GridLayout getgLayout() {
		return gLayout;
	}
}
