package at.jku.ce.adaptivetesting.topic.bpmn;


import java.util.ArrayList;
import java.util.List;

import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.bpmn.XmlBPMNQuestion;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BPMNQuestion extends VerticalLayout implements
	IQuestion<BPMNDataStorage> {

	/**
	 * topic: modeling
	 * class for handle a question
	 * created by David Graf 06-2016
	 */
	
	private static final long serialVersionUID = 1L;
	
	private float difficulty = 0;
	private BPMNDataStorage solution;
	private Label question;
	private Label supporttext;
	private Label coord;
	private Image modelImage;
	private BPMNUserAnswer userAnswer = new BPMNUserAnswer(); // represents a user answer
	Label htmlBorder = userAnswer.getHtmlBorder();
	ComboBox answerSelector = userAnswer.getAnswerSelector();
	private List<BPMNPossibleAnswers> possibleAnswersType = new ArrayList<BPMNPossibleAnswers>();
		
	public BPMNQuestion(BPMNDataStorage solution, Float difficulty,
			String question) {
		this(solution, BPMNDataStorage.getEmptyDataStorage(), difficulty,
				question);
	}

	public BPMNQuestion(BPMNDataStorage solution,
			BPMNDataStorage prefilled, float difficulty, String questionText) {
		this.difficulty = difficulty;
		this.solution = solution;
		questionInit(prefilled);	
		setQuestionText(questionText);
	}
	
	private void questionInit(BPMNDataStorage prefilled) {

		question = new HtmlLabel();
		
		// evaluate questionType -> syntatic, semantic, pragmatic
		evaluateQuestionType();
		
		supporttext = new HtmlLabel();
		supporttext.setValue("<br />" + solution.getSupporttext() + "<br />");
				
		// only for testing - print click coord
		coord = new HtmlLabel();
		coord.setValue("<br /> 0 x 0<br />");
									
		modelImage = new Image("",solution.getImageSource());

		// clickListener
		modelImage.addClickListener(e -> {
			getCoord(e);		
			handleAnswer(e);				
		});
	
		//add Components for UI
		addComponent(question);
		addComponent(modelImage);
		addComponent(supporttext);
		//addComponent(coord);
		
		answerSelector.setSizeFull();
		addComponent(answerSelector);
		addComponent(htmlBorder);

		setSpacing(true);
		
	}

	private void evaluateQuestionType() {

		BPMNPossibleAnswers[] possibleAnswers = BPMNPossibleAnswers.values();
						
		for (BPMNPossibleAnswers a : possibleAnswers) {
			if (a.questionType.equals(solution.getQuestionType())) {
				possibleAnswersType.add(a);
			}
		}	
	}

	private void handleAnswer(ClickEvent event) {
		int rx = event.getRelativeX();
		int ry = event.getRelativeY();
		BPMNElement[] elements = solution.getModelElements();
		
		// check if click is on a valid element
		for (BPMNElement e : elements) {
			if (Math.abs(rx-e.getX())<(e.getW()/2) && Math.abs(ry-e.getY())<(e.getH()/2)) {
								
				userAnswer.setElementId(e.getId());
				
				String path = "VAADIN/themes/vaadin/Images/"+e.type+".png";
				htmlBorder.setValue("<img src=\""+path+"\" style=\"position:absolute;left:"+(e.getX()-e.getW()/2)+"px;top:"+(e.getY()+70)+"px;width:"+e.getW()+"px;height:"+e.getH()+"px;\">");		
				
				answerSelector.addItems((Object[]) possibleAnswersType.toArray());
					
				answerSelector.setEnabled(true);		
			}
		}
	}

	// method only for testoutput of click coord
	private void getCoord(ClickEvent e) {
		coord.setValue("<br />"+ e.getRelativeX() + "x" + e.getRelativeY() + "<br />");
	}

	@Override
	public String getQuestionText() {
		return question.getValue();
	}
	
	private void setQuestionText(String questionText) {
		question.setValue("<br />" + questionText + "<br />");
	}
	
	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public BPMNDataStorage getSolution() {
		return solution;
	}

	@Override
	public BPMNDataStorage getUserAnswer() {
		return new BPMNDataStorage(
				(BPMNPossibleAnswers) userAnswer.getAnswerSelector().getValue());
	}

	@Override
	public double checkUserAnswer() {
		return solution.equals(getUserAnswer(),userAnswer.getElementId()) ? 1d : 0d;
	}

	@Override
	public double getMaxPoints() {
		return 1d;
	}

	@Override
	public float getDifficulty() {
		return difficulty;
	}

	@Override
	public XmlQuestionData<BPMNDataStorage> toXMLRepresentation() {
		return new XmlBPMNQuestion(getSolution(), getQuestionText(),
				getDifficulty());
	}
}
