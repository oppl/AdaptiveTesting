package at.jku.ce.adaptivetesting.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputFields;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputGrid;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlAccountingQuestion;
import com.vaadin.ui.Image;

public class AccountingQuestion extends AccountingRecordInputGrid implements
		IQuestion<AccountingDataStorage>, Cloneable {

	private String id;
	private static final long serialVersionUID = 5932474069705038565L;
	private float difficulty = 0;
	private AccountingDataStorage solution;
	private AccountingRecordInputFields[] soll, haben;

	public AccountingQuestion() {
		this(AccountingDataStorage.getEmptyDataStorage(), 0f, "", null, "");
	}

	public AccountingQuestion(AccountingDataStorage solution, float difficulty,
			String question, Image questionImage, String id) {
		this(solution, AccountingDataStorage.getEmptyDataStorage(), difficulty,
				question, questionImage, id);
	}

	@Override
	public String getQuestionID() {
		return id;
	}

	public AccountingQuestion(AccountingDataStorage solution,
							  AccountingDataStorage prefilled, float difficulty, String question, Image questionImage, String id) {
		this.difficulty = difficulty;
		this.solution = solution;
		this.id = id;
		setQuestionText(question);
		if (questionImage != null) setQuestionImage(questionImage);
		// Fill grid
		int iSoll = solution.getSoll().length, iHaben = solution.getHaben().length;
		soll = new AccountingRecordInputFields[iSoll];
		haben = new AccountingRecordInputFields[iHaben];
		for (int row = 0; row < iSoll; row++) {
			soll[row] = new AccountingRecordInputFields(
					prefilled.getSoll()[row]);
			addComponent(soll[row], Side.Left, row);
		}
		for (int row = 0; row < iHaben; row++) {
			haben[row] = new AccountingRecordInputFields(
					prefilled.getHaben()[row]);
			addComponent(haben[row], Side.Right, row);
		}

	}

	@Override
	public AccountingDataStorage getSolution() {
		return solution;
	}

	public AccountingQuestion clone() throws CloneNotSupportedException {
		AccountingQuestion objClone = (AccountingQuestion)super.clone();
		return objClone;
	}

	@SuppressWarnings("unchecked")
	public  AccountingQuestion cloneThroughSerialize(AccountingQuestion t) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		serializeToOutputStream(t, bos);
		byte[] bytes = bos.toByteArray();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
		return (AccountingQuestion)ois.readObject();
	}

	private static void serializeToOutputStream(Serializable ser, OutputStream os)
			throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(os);
			oos.writeObject(ser);
			oos.flush();
		} finally {
			oos.close();
		}
	}


	@Override
	public AccountingDataStorage getUserAnswer() {
		AccountingDataStorage dataStorage = new AccountingDataStorage();
		AccountRecordData[] accountRecordDatas;
		// Add Soll
		accountRecordDatas = new AccountRecordData[soll.length];
		for (int i = 0; i < soll.length; i++) {
			accountRecordDatas[i] = soll[i].getAccountRecordData();
		}
		dataStorage.setSoll(accountRecordDatas);
		// Add haben
		accountRecordDatas = new AccountRecordData[haben.length];
		for (int i = 0; i < haben.length; i++) {
			accountRecordDatas[i] = haben[i].getAccountRecordData();
		}
		dataStorage.setHaben(accountRecordDatas);
		return dataStorage;
	}

	@Override
	public double checkUserAnswer() {
		LogHelper.logInfo("Questionfile: " + id);
		AccountingDataStorage user = getUserAnswer(), solution = getSolution();
		AccountRecordData[] uSoll = user.getSoll(), uHaben = user.getHaben(), sSoll = solution
				.getSoll(), sHaben = solution.getHaben();
		// Return if the answer is right
		boolean correct = uSoll.length == sSoll.length && uHaben.length == sHaben.length
				&& check(sSoll, uSoll) && check(sHaben, uHaben);
		if (correct) {
			LogHelper.logInfo("Correct answer");
			return 1.0d;
		} else {
			LogHelper.logInfo("Incorrect answer");
			return 0.0d;
		}

	}

	private boolean check(AccountRecordData[] solution, AccountRecordData[] user) {
		List<AccountRecordData> answerRecords = new LinkedList<>();
		answerRecords.addAll(Arrays.asList(user));
		List<AccountRecordData> solutionRecords = new LinkedList<>();
		solutionRecords.addAll(Arrays.asList(solution));
		List<AccountRecordData> check = new LinkedList<>();
		check.addAll(Arrays.asList(solution));

		for (AccountRecordData solutionRecord : solutionRecords) {
			for (AccountRecordData answerRecord : answerRecords) {
				boolean match = true;
				if (solutionRecord.accountNumber != answerRecord.accountNumber) match = false;
				if (solutionRecord.accountName != null && answerRecord.accountName != null && !solutionRecord.accountName.equals(answerRecord.accountName)) match = false;
				if (solutionRecord.accountName == null && answerRecord != null) match = false;
				if (solutionRecord.accountName != null && answerRecord == null) match = false;
				if (Math.round(solutionRecord.value * 100) != Math.round(answerRecord.value * 100)) match = false;
				if (match == true) {
					LogHelper.logInfo("match found for "+solutionRecord.accountName);
					check.remove(solutionRecord);
				}
			}
		}

		if (check.isEmpty()) {
			LogHelper.logInfo("answerpart correct");
			return true;
		}
		LogHelper.logInfo("answerpart incorrect");
		return false;

/*		for (AccountRecordData data : solution) {
			if (aUser.indexOf(data) == -1) {
				LogHelper.logInfo("Answer wrong");
				return false;
			}
		}
		LogHelper.logInfo("Answer correct");
		return true;*/
	}

	@Override
	public float getDifficulty() {
		return difficulty;
	}

	@Override
	public XmlQuestionData<AccountingDataStorage> toXMLRepresentation() {
		return new XmlAccountingQuestion(getSolution(), getQuestionText(),
				getDifficulty());
	}

	@Override
	public double getMaxPoints() {
		return 1d;
	}

	@Override
	public String getQuestionText() {
		return super.getQuestionText();
	}

}
