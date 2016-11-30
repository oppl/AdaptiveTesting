package at.jku.ce.adaptivetesting.xml;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.LogHelper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "questionData")
public abstract class XmlQuestionData<DataStorage extends AnswerStorage>
		implements Serializable {

	private static final long serialVersionUID = 5422318817914536294L;
	@XmlElement(name = "dataStorage", nillable = true)
	private DataStorage dataStorage;
	@XmlElement(name = "question")
	private String question;
	@XmlElement(name = "difficulty")
	private float difficulty;

	public XmlQuestionData() {
		this(null, "", 0);
	}

	public XmlQuestionData(DataStorage dataStorage, String question,
			float difficulty) {
		if (dataStorage == null) {
			try {
				dataStorage = getDataStorageClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				Class<DataStorage> class1 = getDataStorageClass();
				LogHelper
				.logError(class1.getName()
						+ " does not implement an empty constructor as required by"
						+ AnswerStorage.class.getName());
				LogHelper.logThrowable(e);
			}
		}
		this.dataStorage = dataStorage;
		this.difficulty = difficulty;
		if (question == null || question.length() == 0) {
			question = "";
		}
		this.question = question;

	}

	public DataStorage getDataStorage() {
		return dataStorage;
	}

	public void setDataStorage(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public float getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}

	public abstract Class<DataStorage> getDataStorageClass();

	// public static void main(String[] args) throws JAXBException {
	// ByteArrayOutputStream byteArrayOutputStream = new
	// ByteArrayOutputStream();
	// JAXBContext context = JAXBContext.newInstance(XMLQuestionData.class);
	// Marshaller marshaller = context.createMarshaller();
	// marshaller.marshal(new XMLQuestionData(), byteArrayOutputStream);
	// System.out.println(new String(byteArrayOutputStream.toByteArray()));
	// }

}
