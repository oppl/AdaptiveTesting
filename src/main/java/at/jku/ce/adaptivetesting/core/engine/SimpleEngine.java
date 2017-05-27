package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.File;
import java.util.*;
import javax.script.ScriptException;
import at.jku.ce.adaptivetesting.core.StudentData;
import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.engine.EngineException;
import at.jku.ce.adaptivetesting.core.engine.HistoryEntry;
import at.jku.ce.adaptivetesting.core.engine.ICurrentQuestionChangeListener;
import at.jku.ce.adaptivetesting.core.engine.IEngine;
import at.jku.ce.adaptivetesting.core.engine.IResultFiredListener;
import at.jku.ce.adaptivetesting.core.engine.ResultFiredArgs;
import at.jku.ce.adaptivetesting.r.RProvider;

/**
 * The worker class
 *
 * @author Florian
 *
 */
public class SimpleEngine implements IEngine {
	private Stack<HistoryEntry> history = new Stack<>();
	private List<IResultFiredListener> resultFiredListeners = new ArrayList<>();
	private List<ICurrentQuestionChangeListener> currentQuestionChangeListeners = new ArrayList<>();
	private final float[] upperBounds;
	private final List<IQuestion<? extends AnswerStorage>>[] bags;
	private int questionNumber;
	private IQuestion<? extends AnswerStorage> question;
	private String r_itemdiff, r_libFolder;
	private RProvider rProvider;
	private StudentData student;

	/**
	 * Using -1.6f, -0.2f, 1.2f, 2.5f as explicit upper bounds
	 *
	 * @throws EngineException
	 */
	public SimpleEngine() throws EngineException {
		//this(-1.6f, -0.2f, 1.2f, 2.5f);
		this(-2.715000f, -2.246250f, -2.049900f, -1.631000f,
				-1.440625f, -1.316250f, -1.169125f, -0.832000f,
				-0.710075f, -0.551250f, -0.443000f, -0.358900f,
				-0.202650f, -0.079700f, 0.169000f, 0.251200f,
				0.310700f, 0.411500f, 0.526175f, 0.610500f,
				0.744375f, 0.805600f, 0.844675f, 0.927600f,
				1.027125f, 1.170350f, 1.252075f, 1.377600f,
				1.515100f, 1.600750f, 1.716900f, 1.946600f,
				2.041800f, 2.262850f, 2.430500f, 2.619900f,
				2.921600f, 3.103350f, 3.794900f, 4.937000f);
	}

	/**
	 *
	 * @param upperBounds
	 *            : All upper bounds. Additionally a upper bound "+INF" is
	 *            created. All upper bounds including
	 * @throws EngineException
	 */

	@SuppressWarnings("unchecked")
	public SimpleEngine (float... upperBounds) throws EngineException {
		Arrays.sort(upperBounds);
		this.upperBounds = upperBounds;
		LogHelper.logInfo(String.valueOf("Number of question categories: " + upperBounds.length));
		bags = new List[upperBounds.length + 1];
		for (int i = 0; i < bags.length; i++) bags[i] = new ArrayList<>();
		rProvider = new RProvider();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * IEngine#addQuestionToPool
	 * (IQuestion)
	 */
	@Override
	public void addQuestionToPool(IQuestion<? extends AnswerStorage> question) {
		if (question != null) {
			questionNumber++;
		}
		for (int i = 0; i < upperBounds.length; i++) {
			if (question.getDifficulty() <= upperBounds[i]) {
				bags[i].add(question);
				return;
			}
		}
		// If the difficulty is higher than all upper bounds specified
		bags[upperBounds.length].add(question);
	}

	@Override
	public List<IQuestion<? extends AnswerStorage>> getQuestions() {
		List<IQuestion<? extends AnswerStorage>> questions = new LinkedList<>();
		for (int i = 0; i < bags.length; i++) {
			questions.addAll(bags[i]);
		}
		return questions;
	}

	public boolean removeQuestion(IQuestion<? extends AnswerStorage> question) {
		for (int i = 0; i < bags.length; i++) {
			boolean found = bags[i].remove(question);
			if (found) return true;
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see IEngine#
	 * addQuestionChangeListener
	 * (at.reisisoft.jku.ce.adaptivelearning.core.engine
	 * .ICurrentQuestionChangeListener)
	 */
	@Override
	public void addQuestionChangeListener(
			ICurrentQuestionChangeListener questionChangeListener) {
		assert questionChangeListener != null;
		currentQuestionChangeListeners.add(questionChangeListener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IEngine#
	 * addResultFiredListener
	 * (IResultFiredListener)
	 */
	@Override
	public void addResultFiredListener(IResultFiredListener resultFiredListener) {
		assert resultFiredListener != null;
		resultFiredListeners.add(resultFiredListener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IEngine#
	 * removeQuestionChangeListener
	 * (at.reisisoft.jku.ce.adaptivelearning.core.engine
	 * .ICurrentQuestionChangeListener)
	 */
	@Override
	public void removeQuestionChangeListener(
			ICurrentQuestionChangeListener questionChangeListener) {
		assert questionChangeListener != null;
		currentQuestionChangeListeners.remove(questionChangeListener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IEngine#
	 * removeResultFiredListener
	 * (IResultFiredListener)
	 */
	@Override
	public void removeResultFiredListener(
			IResultFiredListener resultFiredListener) {
		assert resultFiredListener != null;
		resultFiredListeners.remove(resultFiredListener);
	}

	private void fireQuestionChangeListener(
			IQuestion<? extends AnswerStorage> question) {
		for (ICurrentQuestionChangeListener listener : currentQuestionChangeListeners) {
			listener.questionChanged(question);
		}
	}

	private void fireResultListener(ResultFiredArgs args) {
		for (IResultFiredListener listener : resultFiredListeners) {
			try {
				listener.resultFired(args);
			} catch (EngineException e) {
				LogHelper.logThrowable(e);
			}
		}
	}

	/**
	 *
	 * @param question
	 *            A question, answered by the user
	 * @return True if the question is answered correctly, false if not
	 */
	private double addQuestionToHistory(
			IQuestion<? extends AnswerStorage> question) {
		HistoryEntry entry = new HistoryEntry(question);
		history.push(entry);
		return entry.points;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * IEngine#requestCalculation
	 * ()
	 */
	@Override
	public void requestCalculation() throws EngineException {
		addQuestionToHistory(question);
		// Do R
		try {
			RCaller caller = rProvider.getRCaller();
			// Get r code
			RCode rCode = rProvider.getRCode();
			// Add script
			rCode.addRCode(getRScript());
			// Execute R code and get result
			String rNameReturn = "next_item_parm";
			// [0] -> next item's difficulty [1] -> current competence
			// [2] ->Delta to result
			double[] result = rProvider.execute(caller, rCode, rNameReturn)
					.getAsDoubleArray(rNameReturn);
			LogHelper.logInfo("StudentID: "+ student.getStudentIDCode() + " - Calculation result:\tNext item: " + result[0]
					+ "\tCurrent competence:\t" + result[1] + "\tDelta:\t"
					+ result[2]);
			// Get array position of difficulty
			IQuestion<? extends AnswerStorage> nextQuestion = getQuestion(getArrayPositionFromWantedDifficulty(result[0]));
			if (nextQuestion == null || result[2] < 0.5d) {
				ResultFiredArgs args = new ResultFiredArgs(
						nextQuestion == null,
						Collections.unmodifiableList(history), result[1],
						result[2], student);
				fireResultListener(args);
				return;
			}
			question = nextQuestion;
			fireQuestionChangeListener(nextQuestion);
		} catch (ScriptException e) {
			throw new EngineException(e);
		}
	}

	private int getArrayPositionFromWantedDifficulty(double wantedDifficulty) {
		for (int i = 0; i < upperBounds.length - 1; i++) {
			if (wantedDifficulty > upperBounds[i]) {
				return i;
			}
		}
		return upperBounds.length - 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see IEngine#start()
	 */
	@Override
	public void start() throws EngineException {
		initR();
		history.clear();
		question = getQuestion((upperBounds.length + 1) / 2 - 1);
		fireQuestionChangeListener(question);

	}

	private int setClass(int schoolClass) {
		float category = 0.0f;
		for (int i = 0; i < upperBounds.length; i++) {
			if (i == schoolClass) category = upperBounds[schoolClass];
		}
		LogHelper.logInfo("Set initial question category to: " + String.valueOf(category));
		return schoolClass;
	}

	/*
 	* (non-Javadoc)
 	*
 	* @see IEngine#start(StudentData student)
 	*/
	@Override
	public void start(StudentData student) throws EngineException {
		initR();
		history.clear();
		String studentClass = student.getStudentClass();
		int schoolClass;
		try {
			schoolClass = Integer.parseInt(studentClass.substring(0, 1));
			if (schoolClass > 0 && schoolClass < 6)
				LogHelper.logInfo("Student class: " + String.valueOf(schoolClass));
			else
				LogHelper.logError("Invalid student class: " + String.valueOf(schoolClass));
		} catch (Exception e) {
			LogHelper.logError(e.toString());
			schoolClass = 0;
		}
		switch(schoolClass) {
			case 1:
				// 1. class (9. grade) - bag 3
				question = getQuestion(setClass(2));
				break;
			case 2:
				// 2. class (10. grade) - bag 5
				question = getQuestion(setClass(4));
				break;
			case 3:
				// 3. class (11. grade) - bag 7
				question = getQuestion(setClass(6));
				break;
			case 4: case 5:
				// 4. / 5. class (12. / 13. grade) - bag 10
				question = getQuestion(setClass(9));
				break;
				// no choice or invalid grade (bag 3)
			default:
				question = getQuestion(setClass(2));
		}
		LogHelper.logInfo(String.valueOf(question.getDifficulty()));
		fireQuestionChangeListener(question);
	}

	private void initR() throws EngineException {
		if (questionNumber <= 0) {
			return;
		}
		// Create the r_itemdiff String
		StringBuilder sb = new StringBuilder("item_diff <- c(");
		int bag = 0, item = 0;
		// Get first item
		while (bags[bag].size() == 0) {
			bag++;
		}
		sb.append(bags[bag].get(0).getDifficulty());
		if (bags[bag].size() > 1) {
			item = 1;
		} else {
			bag++;
		}
		while (bag < bags.length) {
			while (item < bags[bag].size()) {
				sb.append(',').append(bags[bag].get(item).getDifficulty());
				// Go to next item
				item++;
			}
			// Go to next bag
			bag++;
			item = 0;
		}
		// Set R-variable itemdiff
		r_itemdiff = sb.append(')').toString();
		// Get library home
		File path = new File(System.getProperty("java.io.tmpdir"), "r_lib");
		if (!path.exists()) {
			path.mkdirs();
		}
		r_libFolder = path.getPath().replace("\\", "\\\\");
		// Set up needed R libraries
		String nl = System.getProperty("line.separator");
		String cmd = "options(repos=structure(c(CRAN=\"http://cran.r-project.org/\")))"
				+ nl + "install.packages(\"catR\",lib=\"" + r_libFolder + "\")";
		try {
			RProvider rProvider = new RProvider();
			RCaller rCaller = rProvider.getRCaller();
			RCode rCode = rProvider.getRCode();
			rProvider.run(rCaller, rCode);
		} catch (ScriptException e) {
			throw new EngineException(e);
		}
	}

	/**
	 *
	 * @param difficulty
	 *            A upper bound specified during initialisation
	 * @return Question
	 */
	private IQuestion<? extends AnswerStorage> getQuestion(float difficulty) {
		return getQuestion(Arrays.asList(upperBounds).indexOf(difficulty));
	}

	private String getRScript() {
		// Newline
		String nl = System.getProperty("line.separator");
		// Get R matrix (input)
		StringBuilder sb = new StringBuilder("double <- c(");
		Iterator<String> iterator = history
				.stream()
				.map(e -> e.question.getDifficulty() + ","
						+ (Math.abs(e.points) < 0.01 ? "0" : "1")).iterator();
		if (iterator.hasNext()) {
			sb.append(iterator.next());
		}
		while (iterator.hasNext()) {
			sb.append(',').append(iterator.next());
		}
		String inputMatrix = sb.append(")").toString();
		return "library(catR, lib.loc=\""
				+ r_libFolder
				+ "\")"
				+ nl
				+ r_itemdiff
		+ nl
		+ inputMatrix
		+ nl
		+ "itembank <- unname(as.matrix(cbind(1, item_diff, 0, 1)))"
		+ nl
		+ "stellen <- 1:length(double)"
		+ nl
		+ "ungerade <- stellen[which(stellen %% 2 != 0)]"
		+ nl
		+ "gerade <- stellen[which(stellen %% 2 == 0)]"
		+ nl
		+ "response_pattern <- double[gerade]"
		+ nl
		+ "pre_items_diff <- as.matrix(double[ungerade],length(response_pattern)) "
		+ nl
		+ "previous_items <- as.matrix(rep(0, length(response_pattern), length(response_pattern)))"
		+ nl
		+ "for(i in 1:length(response_pattern)) {"
		+ nl
		+ "for(j in 1:nrow(itembank)) {"
		+ nl
		+ "if(pre_items_diff[i,1]==itembank[j,2]) (previous_items[i,1] <-j)"
		+ nl
		+ "}"
		+ nl
		+ "}"
		+ nl
		+ "select_next_item <- nextItem(itembank, x = response_pattern, out = previous_items, criterion = \"MPWI\")"
		+ nl
		+ "next_item_parm <- c(itembank[select_next_item$item,2], "
		+ nl
		+ "thetaEst(itembank[previous_items,], response_pattern),"
		+ nl
		+ "eapSem(thetaEst(itembank[previous_items,], response_pattern), itembank[previous_items,], response_pattern))";
	}

	/**
	 *
	 * @param arrayIndex
	 *            An index of the upperBoundArray +1
	 * @return Question
	 */
	private IQuestion<? extends AnswerStorage> getQuestion(int arrayIndex) {
		if (arrayIndex < 0 || arrayIndex > bags.length || questionNumber == 0) {
			return null; // Invalid index or no question available
		}
		List<IQuestion<? extends AnswerStorage>> list = bags[arrayIndex];
		int listSize = list.size();
		if (listSize == 0) {
			int nextArrayIndex = (arrayIndex + 1) % bags.length;
			return getQuestion(nextArrayIndex);
		}
		int index = (int) Math.round(Math.random() * (listSize - 1));
		IQuestion<? extends AnswerStorage> question = list.get(index);
		list.remove(index);
		questionNumber--;
		return question;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * IEngine#resetQuestions()
	 */
	@Override
	public void resetQuestions() {
		for (List<?> bag : bags) {
			bag.clear();
		}
		questionNumber = 0;
	}

	@Override
	public void setStudentData(StudentData student) {
		this.student = student;
	}
}
