package at.jku.ce.adaptivetesting.topic.bpmn;

public enum BPMNPossibleAnswers {
	
	/**
	 * topic: modeling
	 * possible answers
	 * created by David Graf 06-2016
	 */	
	
	WordSyntax("syntactic","Element is not in BPMN Notation"), 
	SentenceSyntax("syntactic","Uncorrect use of an element"), 
	TextSyntax("syntactic","Uncorrect data flow between elements"),
	Correctness("semantic","Element is semantic not correct "),
	Relevance("semantic","Element is not relevant "),
	Completeness("semantic","Informal description and model does not match"),
	Unambiguousness("pragmatic","Redundant or inconsistent flow "),
	Understandability("pragmatic","Naming not suitable ");
	
	String name;
	String questionType;

	BPMNPossibleAnswers(String questionType, String name) {
		this.questionType = questionType;
		this.name = name;	
	}

	@Override
	public String toString() {
		return name;
	}
	
}
