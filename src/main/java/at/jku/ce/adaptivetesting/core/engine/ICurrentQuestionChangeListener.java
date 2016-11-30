package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.AnswerStorage;

public interface ICurrentQuestionChangeListener {

	public void questionChanged(IQuestion<? extends AnswerStorage> question);

}
