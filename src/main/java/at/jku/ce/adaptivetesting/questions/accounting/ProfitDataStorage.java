package at.jku.ce.adaptivetesting.questions.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.questions.accounting.util.ProfitPossibleAnswers;

@XmlAccessorType(XmlAccessType.FIELD)
public class ProfitDataStorage extends AnswerStorage {

	private static final long serialVersionUID = 3127620913952575646L;
	@XmlElement(name = "answer")
	private ProfitPossibleAnswers value;

	public static ProfitDataStorage getEmptyDataStorage() {
		return new ProfitDataStorage();
	}

	public ProfitDataStorage() {
		this(null);
	}

	public ProfitDataStorage(ProfitPossibleAnswers value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value == null ? "<keine Auswahl>" : value.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value == null ? 0 : value.hashCode());
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
		ProfitDataStorage other = (ProfitDataStorage) obj;
		return value == other.value;
	}

	public ProfitPossibleAnswers getValue() {
		return value;
	}

	public void setValue(ProfitPossibleAnswers value) {
		if (value != null) {
			this.value = value;
		}
	}

}
