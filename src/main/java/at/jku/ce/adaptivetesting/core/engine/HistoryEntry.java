package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;

public class HistoryEntry {

	public final double points;
	public final IQuestion<? extends AnswerStorage> question;

	public HistoryEntry(IQuestion<? extends AnswerStorage> question) {
		points = question.checkUserAnswer();
		this.question = question;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(points);
		result = prime * result + (int) (temp ^ temp >>> 32);
		result = prime * result + (question == null ? 0 : question.hashCode());
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
		HistoryEntry other = (HistoryEntry) obj;
		if (Double.doubleToLongBits(points) != Double
				.doubleToLongBits(other.points)) {
			return false;
		}
		if (question == null) {
			if (other.question != null) {
				return false;
			}
		} else if (!question.equals(other.question)) {
			return false;
		}
		return true;
	}

}
