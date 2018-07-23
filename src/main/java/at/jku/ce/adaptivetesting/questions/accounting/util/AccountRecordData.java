package at.jku.ce.adaptivetesting.questions.accounting.util;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.LogHelper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "accountRecordData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountRecordData implements Serializable {
	private static final long serialVersionUID = -6679031880487687770L;

	@Override
	public String toString() {
		return "(" + accountNumber + ") "
				+ (accountName == null ? "???" : accountName) + " " + value
				+ '€';
	}

	@XmlElement(name = "accountName")
	public final String accountName;
	@XmlElement(name = "amount")
	public final float value;
	@XmlElement(name = "accountNumber")
	public final int accountNumber;

	public AccountRecordData() {
		this("", 0, 0);
	}

	public AccountRecordData(String accountname, float value, int accountNumber) {
		accountName = accountname;
		this.value = value;
		this.accountNumber = accountNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		result = prime * result
				+ (accountName == null ? 0 : accountName.hashCode());
		result = prime * result + Math.round(value * 100);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AccountRecordData other = (AccountRecordData) obj;


		if (accountNumber != other.accountNumber) {
			LogHelper.logInfo("Wrong Accountnumber "+accountNumber+" "+other.accountNumber);
			return false;
		}
		if (accountName == null) {
			if (other.accountName != null) {
				LogHelper.logInfo("Accountname missing "+other.accountName);
				return false;
			}
		} else if (!accountName.equals(other.accountName)) {
			LogHelper.logInfo("Wrong Accountname "+accountName+ " "+other.accountName);
			return false;
		}
		if (Math.round(value * 100) != Math.round(other.value * 100)) {
			LogHelper.logInfo("Wrong Amount "+value+" "+other.value);
			return false;
		}
		return true;
	}
}
