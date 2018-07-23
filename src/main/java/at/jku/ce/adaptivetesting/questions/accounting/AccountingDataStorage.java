package at.jku.ce.adaptivetesting.questions.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.questions.accounting.util.AccountRecordData;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "accountingRecord")
public class AccountingDataStorage extends AnswerStorage {
	private static final long serialVersionUID = -8179746363246548456L;
	@XmlElement(name = "debit")
	private AccountRecordData[] soll = new AccountRecordData[0];
	@XmlElement(name = "credit")
	private AccountRecordData[] haben = new AccountRecordData[0];

	public static AccountingDataStorage getEmptyDataStorage() {
		AccountingDataStorage ds = new AccountingDataStorage();
		AccountRecordData[] accountRecordDatas = new AccountRecordData[3];
		for (int i = 0; i < accountRecordDatas.length; i++) {
			accountRecordDatas[i] = new AccountRecordData();
		}
		ds.setSoll(accountRecordDatas);
		accountRecordDatas = new AccountRecordData[3];
		for (int i = 0; i < accountRecordDatas.length; i++) {
			accountRecordDatas[i] = new AccountRecordData();
		}
		ds.setHaben(accountRecordDatas);
		return ds;
	}

	public AccountingDataStorage() {
	}

	public AccountRecordData[] getSoll() {
		return soll;
	}

	public AccountRecordData[] getHaben() {
		return haben;
	}

	public void setSoll(AccountRecordData[] data) {
		if (data != null) {
			soll = data;
		}
	}

	public void setHaben(AccountRecordData[] data) {
		if (data != null) {
			haben = data;
		}
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean html) {
		String nl = html ? "<br>" : System.getProperty("line.separator");
		int max = soll.length > haben.length ? soll.length : haben.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < max; i++) {
			if (i < soll.length) {
				sb.append(soll[i]);
			}
			sb.append("  |  ");
			if (i < haben.length) {
				sb.append(haben[i]);
			}
			sb.append(nl);
		}
		return sb.toString();
	}
}
