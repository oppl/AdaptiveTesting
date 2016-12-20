package at.jku.ce.adaptivetesting.topic.bpmn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "modelelement")
@XmlAccessorType(XmlAccessType.FIELD)
public class BPMNElement implements Serializable {

	/**
	 * topic: modeling
	 * represents an element of the model
	 * created by David Graf 06-2016
	 */
	
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id")
    protected int id;
	@XmlElement(name = "type")
    protected String type;
	@XmlElement(name = "x")
    protected int x;
	@XmlElement(name = "y")
    protected int y;
	@XmlElement(name = "w")
    protected int w;
	@XmlElement(name = "h")
    protected int h;

	public BPMNElement () {
		this(0,"",0,0,0,0);
	}

	public BPMNElement(int id, String type, int x, int y, int h, int w) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

}
