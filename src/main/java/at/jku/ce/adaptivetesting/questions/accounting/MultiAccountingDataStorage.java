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
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (AccountRecordData[] ard: soll) {
            sb.append("Soll Variante "+i+": ");
            for (int a = 0;a<ard.length;a++) {
                sb.append(ard[a]);
                if (a<ard.length-1) sb.append(", ");
            }
            sb.append("; ");
            i++;
        }
        i = 1;
        sb.append("  |  ");
        for (AccountRecordData[] ard: haben) {
            sb.append("Haben Variante "+i+": ");
            for (int a = 0;a<ard.length;a++) {
                sb.append(ard[a]);
                if (a<ard.length-1) sb.append(", ");
            }
            sb.append(" - ");
            i++;
        }
		return sb.toString();

    }
}
