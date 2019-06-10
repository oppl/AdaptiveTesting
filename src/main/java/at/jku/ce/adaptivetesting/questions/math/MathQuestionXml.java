package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mathQuestionDataStorage")
public class MathQuestionXml extends XmlQuestionData<MathDataStorage> {
    private static final long serialVersionUID = -7011780466232381923L;

    @XmlElement(name = "materialNr")
    private String materialNr;

    @XmlElement(name = "questionType")
    private int questionType;

    public MathQuestionXml() {
    }

    public MathQuestionXml(MathDataStorage solution, String questionText, float difficulty, String materialNr, int questionType) {
        super(solution, questionText, difficulty);
        if (materialNr == null || materialNr.length() == 0) {
            materialNr = "";
        }
        this.setMaterialNr(materialNr);
        this.setQuestionType(questionType);
    }

    @Override
    public Class<MathDataStorage> getDataStorageClass() {
        return MathDataStorage.class;
    }

    public String getMaterialNr() {
        return materialNr;
    }

    public void setMaterialNr(String materialNr) {
        this.materialNr = materialNr;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }
}
