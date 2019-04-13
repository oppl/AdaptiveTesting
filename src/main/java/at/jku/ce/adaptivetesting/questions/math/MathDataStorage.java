package at.jku.ce.adaptivetesting.questions.math;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MathDataStorage extends AnswerStorage {
    private static final long serialVersionUID = -8179746363246548456L;
    @XmlElement(name = "info")
    private String info = "";

    public static MathDataStorage getEmptyDataStorage() {
        MathDataStorage ds = new MathDataStorage();
        ds.setInfo("Beispieltext");
        return ds;
    }

    public MathDataStorage() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Infotext: <p>" + info + "</p>");
        return buffer.toString();
    }
}
