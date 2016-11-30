package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.StudentData;

import java.util.List;

public class ResultFiredArgs {
	// TODO implement
	public final boolean outOfQuestions;
	public final List<HistoryEntry> history;
	public final double skillLevel;
	public final double delta;
	public final StudentData student;

	public ResultFiredArgs(boolean outOfQuestions, List<HistoryEntry> history,
			double skillLevel, double delta, StudentData student) {
		super();
		this.outOfQuestions = outOfQuestions;
		this.history = history;
		this.skillLevel = skillLevel;
		this.delta = delta;
		this.student = student;
	}

}
