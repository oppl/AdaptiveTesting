package at.jku.ce.adaptivetesting.vaadin.ui;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import at.jku.ce.adaptivetesting.core.*;
import at.jku.ce.adaptivetesting.core.engine.engines.SimpleEngine;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import at.jku.ce.adaptivetesting.core.engine.EngineException;
import at.jku.ce.adaptivetesting.core.engine.ICurrentQuestionChangeListener;
import at.jku.ce.adaptivetesting.core.engine.IEngine;
import at.jku.ce.adaptivetesting.core.engine.IResultFiredListener;
import at.jku.ce.adaptivetesting.core.engine.ResultFiredArgs;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.ui.core.Views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;

public abstract class QuestionManager extends VerticalLayout implements
		ICurrentQuestionChangeListener, IResultFiredListener, View {

	private static final long serialVersionUID = -4764723794449575244L;
	private SingleComponentLayout questionHolder = new SingleComponentLayout();
	private IEngine iEngine;
	private GridLayout southLayout = new GridLayout(3, 1);
	private HorizontalLayout helperRow = new HorizontalLayout();
	private final Button next;
	private Component helpComponent = null;
	private Label title;
	private Class<? extends IResultView> resultViewClass = null;
	private int questionNo;

	public QuestionManager(String quizName) {
		this(quizName, null);
	}

	public QuestionManager(String quizName, IEngine engine) {
		setMargin(true);
		questionNo = 0;
		title = HtmlLabel.getCenteredLabel("h1", quizName);
		addComponent(title);
		addComponent(questionHolder);

		addComponent(southLayout);

		next = new Button("Nächste Frage");
		next.addClickListener(e -> {
			e.getButton().setEnabled(false);
			try {
				iEngine.requestCalculation();
			} catch (EngineException e1) {
				Notification.show("Die nächste Frage konnte nicht ausgewählt werden.",
						"Bitte wende dich an den Lehrenden.", Type.ERROR_MESSAGE);
				LogHelper.logThrowable(e1);
			}
		});
		southLayout.addComponent(next, 2, 0);
		helperRow.setSpacing(true);
		southLayout.addComponent(helperRow,1,0);
		southLayout.setSizeFull();
		southLayout.setMargin(true);
		// Ensure we have an engine
		if (engine == null) {
			try {
				iEngine = new SimpleEngine();
			} catch (EngineException e1) {
				Notification.show("Test-System konnte nicht gestartet werden", "Bitte wende dich an den Lehrenden.",
						Type.ERROR_MESSAGE);
				LogHelper.logThrowable(e1);

			}
		} else {
			iEngine = engine;
		}
		// Register to engine events
		iEngine.addQuestionChangeListener(this);
		iEngine.addResultFiredListener(this);
		setResultView(VaadinResultView.class);
	}

	/**
	 *
	 * @param question
	 *            A question which is a component as well
	 */
	public <QuestionComponent extends IQuestion<? extends AnswerStorage> & Component & Sizeable> void addQuestion(
			QuestionComponent question) {
		iEngine.addQuestionToPool(question);
	}

	public IEngine getEngine() {
		return iEngine;
	}
	protected final void addHelpButton(Component c) {
		assert c != null;
		helpComponent = c;
		helperRow.addComponent(helpComponent);
	}

	@Override
	public void resultFired(ResultFiredArgs args) throws EngineException {
		IResultView result;
		if (resultViewClass == null) {
			String msg = "You forget to set the result view";
			LogHelper.logError(msg);
			throw new NullPointerException(msg);
		}
		Constructor<? extends IResultView> resultConstructor;
		try {
			resultConstructor = resultViewClass.getConstructor(
					ResultFiredArgs.class, String.class);
			result = resultConstructor.newInstance(args, title.getValue());
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NullPointerException e) {
			LogHelper.logInfo(resultViewClass.getName()
					+ " does not implement the constructors of "
					+ IResultView.class.getName());
			throw new EngineException(e);
		}

		// Add it to the navigator
		Navigator navigator = getUI().getNavigator();
		assert navigator != null;
		// Cast cannot fail, as the setResultView takes care, that it is a View
		// as well
		navigator.addView(Views.RESULT.toString(), (View) result);
		navigator.navigateTo(Views.RESULT.toString());

	}

	// As QuestionManager only listens to the engine, every Question is a
	// Component as well
	@Override
	public void questionChanged(IQuestion<? extends AnswerStorage> question) {
		// This cast won't fail as every question in the engine is a Component
		// as well
		if (question != null) {
			questionNo++;
			Label newTitel = HtmlLabel.getCenteredLabel("h1", "Aufgabe "+questionNo);
			replaceComponent(title,newTitel);
			title = newTitel;
			Component c = (Component) question;
			questionHolder.addComponent(c);
			Sizeable s = (Sizeable) question;
			s.setSizeFull();
		}
		next.setEnabled(true);
	}

	/**
	 * Loads questions to the {@code iEngine}
	 */
	public abstract void loadQuestions();

	public void startQuiz(StudentData student) {
		questionNo = 0;
		iEngine.resetQuestions();
		iEngine.setStudentData(student);
		loadQuestions();
		try {
			iEngine.start(student);
		} catch (EngineException e) {
			Notification.show("Das Test-System konnte nicht gestartet werden", "Bitte wenden Sie sich an den Lehrenden.", Type.ERROR_MESSAGE);
			LogHelper.logThrowable(e);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		startQuiz(new StudentData("anonymous"));
		VaadinUI.setCurrentPageTitle(event);
	}

	public <RView extends View & IResultView> void setResultView(
			Class<? extends RView> class1) {
		resultViewClass = class1;
	}
}
