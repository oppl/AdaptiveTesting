package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;

import java.util.List;

public interface IEngine {

	/**
	 *
	 * @param question
	 *            The question to add
	 */
    void addQuestionToPool(IQuestion<? extends AnswerStorage> question);

	/**
	 *
	 * @param questionChangeListener
	 *            A listener, which listens to change of the question
	 */
    void addQuestionChangeListener(
            ICurrentQuestionChangeListener questionChangeListener);

	/**
	 *
	 * @param resultFiredListener
	 *            A listener, which listens to the result of a test run
	 */
    void addResultFiredListener(IResultFiredListener resultFiredListener);

	/**
	 *
	 * @param questionChangeListener
	 *            A listener, which listens to change of the question
	 */
    void removeQuestionChangeListener(
            ICurrentQuestionChangeListener questionChangeListener);

	/**
	 *
	 * @param resultFiredListener
	 *            A listener, which listens to the result of a test run
	 */
    void removeResultFiredListener(
            IResultFiredListener resultFiredListener);

	/**
	 * Signals that the user has finished his input and that the question gets
	 * evaluated
	 *
	 * @throws EngineException
	 */
    void requestCalculation() throws EngineException;

	/**
	 * Starts the test
	 *
	 */
    void start();

	/**
	 * Starts the test
	 *
	 */
	void start(StudentData student);

	/**
	 * Deletes all questions stored in the Engine
	 */
    void resetQuestions();

	void setStudentData(StudentData student);

	List<IQuestion<? extends AnswerStorage>> getQuestions();

	IQuestion<? extends AnswerStorage> getQuestion();

	boolean removeQuestion(IQuestion<? extends AnswerStorage> question);
}