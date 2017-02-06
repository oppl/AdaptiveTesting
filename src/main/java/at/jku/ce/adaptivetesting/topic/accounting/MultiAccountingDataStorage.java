package at.jku.ce.adaptivetesting.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.core.AnswerStorage;

import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "accountingRecord")
public class MultiAccountingDataStorage extends AnswerStorage {
    private static final long serialVersionUID = -8179746363246548456L;
    @XmlElement(name = "debit")
    private Vector<AccountRecordData[]> soll = new Vector<>();
    @XmlElement(name = "credit")
    private Vector<AccountRecordData[]> haben = new Vector<>();

    public static MultiAccountingDataStorage getEmptyDataStorage() {
        MultiAccountingDataStorage ds = new MultiAccountingDataStorage();
        AccountRecordData[] accountRecordDatas = new AccountRecordData[3];
        for (int i = 0; i < accountRecordDatas.length; i++) {
            accountRecordDatas[i] = new AccountRecordData();
        }
        ds.addSoll(accountRecordDatas);
        accountRecordDatas = new AccountRecordData[3];
        for (int i = 0; i < accountRecordDatas.length; i++) {
            accountRecordDatas[i] = new AccountRecordData();
        }
        ds.addHaben(accountRecordDatas);
        return ds;
    }

    public MultiAccountingDataStorage() {
    }

    public Vector<AccountRecordData[]> getSoll() {
        return soll;
    }

    public Vector<AccountRecordData[]> getHaben() {
        return haben;
    }

    public void addSoll(AccountRecordData[] data) {
        if (data != null) {
            soll.add(data);
        }
    }

    public void addHaben(AccountRecordData[] data) {
        if (data != null) {
            haben.add(data);
        }
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean html) {
        String nl = html ? "<br>" : System.getProperty("line.separator");
        //todo: implement new toString method
/*		int max = soll.length > haben.length ? soll.length : haben.length;
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
		return sb.toString();*/
        return "";
    }
}
