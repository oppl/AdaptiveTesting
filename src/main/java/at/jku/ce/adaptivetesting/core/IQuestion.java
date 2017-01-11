package at.jku.ce.adaptivetesting.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import at.jku.ce.adaptivetesting.xml.XmlQuestionData;

/**
 *
 * @author Florian
 *
 *         Every type implementing this needs a Constructor taking its
 *         {@link DataStorage} type twice (solution and pre-filled question),
 *         {@link float} (Question difficulty) and a {@link String} (Actual
 *         question text)
 *
 * @param <DataStorage>
 *            A object, which can store the question's answer
 */
public interface IQuestion<DataStorage extends AnswerStorage> {
	/**
	 *
	 * @return The right answer to the question
	 */
	DataStorage getSolution();

	/**
	 *
	 * @return The answer made by the user. A question, which is read to be
	 *         displayed to the user must return NULL
	 */
	DataStorage getUserAnswer();

	/**
	 *
	 * @return Validates the user answer. The returned value is the amount of
	 *         points gained for this answer must be lower then getMaxPoints()
	 */
	double checkUserAnswer();

	/**
	 *
	 * @return The maximum number of points you can get with this question
	 */
	double getMaxPoints();

	/**
	 *
	 * @return The difficulty. -INF the easiest, +INF the hardest
	 */
	float getDifficulty();

	/**
	 *
	 * @return A representation of the question, which can be parsed to XML
	 */
	XmlQuestionData<DataStorage> toXMLRepresentation();

	/**
	 *
	 * @return Gets the text of the question
	 */
	String getQuestionText();

	String getQuestionID();

	/**
	 *
	 * @return XML as String
	 * @throws JAXBException
	 *             Exception creating XML
	 */
	default String toXML() throws JAXBException {
		XmlQuestionData<DataStorage> xmlRepresentation = toXMLRepresentation();
		Class<? extends AnswerStorage> dataStorageClass = xmlRepresentation
				.getDataStorageClass();
		JAXBContext context = JAXBContext.newInstance(
				xmlRepresentation.getClass(), dataStorageClass);
		Marshaller marshaller = context.createMarshaller();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(xmlRepresentation, byteArrayOutputStream);
		try {
			return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Should never happen, as "UTF-8" is hard coded
			return "FAILED " + e.getMessage();
		}
	}
}
