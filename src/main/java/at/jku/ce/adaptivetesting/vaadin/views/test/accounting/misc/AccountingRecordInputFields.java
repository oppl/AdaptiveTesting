package at.jku.ce.adaptivetesting.vaadin.views.test.accounting.misc;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingDataProvider;
import at.jku.ce.adaptivetesting.questions.accounting.util.AccountRecordData;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class AccountingRecordInputFields extends GridLayout {

	private static final long serialVersionUID = 8543708812084425332L;
	private final AccountNumberInputField accountNumberInputField;
	private final ComboBox ddAccountNames;
	private final CurrencyTextBox currencyTextBox;

	public AccountingRecordInputFields(AccountRecordData data) {
		super(3, 1);
		AccountingDataProvider dataProvider = AccountingDataProvider
				.getInstance();
		setSpacing(true);
		// Add AccountNumber
		accountNumberInputField = new AccountNumberInputField();
		accountNumberInputField.setCaption("Kontennr.:");
		accountNumberInputField.setWidth("4em");
		addComponent(accountNumberInputField, 0, 0);
		// Add DD AccountNames
		ddAccountNames = new ComboBox("Kontenname:");
		ddAccountNames.addItems((Object[]) AccountingDataProvider.getInstance()
				.getAllAccountNames());
		ddAccountNames.setWidth("20em");
		addComponent(ddAccountNames, 1, 0);
		// Add Curreny field
		currencyTextBox = new CurrencyTextBox();
		currencyTextBox.setCaption("Betrag (€):");
		currencyTextBox.setWidth("8em");
		addComponent(currencyTextBox, 2, 0);
		// set default values from AccountRecordData
		if (data.accountName != null) {
			if (!dataProvider.containsString(data.accountName)) {
				Notification.show('"' + data.accountName
						+ "\" ist kein zulässiger Kontenname",
						Type.WARNING_MESSAGE);
			}
			ddAccountNames.setValue(data.accountName);
			ddAccountNames.setEnabled(data.accountName == null
					|| data.accountName.length() == 0);
		}
		if (data.accountNumber > 0) {
			if (!dataProvider.containsNumber(data.accountNumber)) {
				Notification.show('"' + data.accountNumber
						+ "\" ist keine zulässige Kontennummer",
						Type.WARNING_MESSAGE);
			}
			accountNumberInputField.setValue(Integer
					.toString(data.accountNumber));
			accountNumberInputField.setEnabled(false);
		}
		if (data.value >= 0.01f) {
			currencyTextBox.setValue(Float.toString(data.value));
			currencyTextBox.setEnabled(false);
		}
	}

	public AccountRecordData getAccountRecordData() {
		return new AccountRecordData((String) ddAccountNames.getValue(),
				currencyTextBox.getNumericValue(),
				accountNumberInputField.getAccountNumber());
	}

	public void setAccountRecordData(AccountRecordData data) {
		AccountingDataProvider dataProvider = AccountingDataProvider
				.getInstance();
		if (data.accountName != null) {
			if (!dataProvider.containsString(data.accountName)) {
				Notification.show('"' + data.accountName
								+ "\" ist kein zulässiger Kontenname",
						Type.WARNING_MESSAGE);
			}
			ddAccountNames.setValue(data.accountName);
			ddAccountNames.setEnabled(data.accountName == null
					|| data.accountName.length() == 0);
		}
		if (data.accountNumber > 0) {
			if (!dataProvider.containsNumber(data.accountNumber)) {
				Notification.show('"' + data.accountNumber
								+ "\" ist keine zulässige Kontennummer",
						Type.WARNING_MESSAGE);
			}
			accountNumberInputField.setValue(Integer
					.toString(data.accountNumber));
			accountNumberInputField.setEnabled(false);
		}
		if (data.value >= 0.01f) {
			currencyTextBox.setValue(Float.toString(data.value));
			currencyTextBox.setEnabled(false);
		}
	}
}
