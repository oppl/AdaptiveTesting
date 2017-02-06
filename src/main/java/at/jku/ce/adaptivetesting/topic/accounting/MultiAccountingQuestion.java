package at.jku.ce.adaptivetesting.topic.accounting;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputFields;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.AccountingRecordInputGrid;
import at.jku.ce.adaptivetesting.xml.XmlQuestionData;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlAccountingQuestion;
import at.jku.ce.adaptivetesting.xml.topic.accounting.XmlMultiAccountingQuestion;
import com.vaadin.ui.Image;

public class MultiAccountingQuestion extends AccountingRecordInputGrid implements
        IQuestion<MultiAccountingDataStorage> {

    private String id;
    private static final long serialVersionUID = 5932474069705038565L;
    private float difficulty = 0;
    private MultiAccountingDataStorage solution;
    private AccountingRecordInputFields[] soll, haben;

    public MultiAccountingQuestion() {
        this(MultiAccountingDataStorage.getEmptyDataStorage(), 0f, "", null, "");
    }

    public MultiAccountingQuestion(MultiAccountingDataStorage solution, float difficulty,
                              String question, Image image, String id) {
        this(solution, MultiAccountingDataStorage.getEmptyDataStorage(), difficulty,
                question, image, id);
    }

    @Override
    public String getQuestionID() {
        return id;
    }

    public MultiAccountingQuestion(MultiAccountingDataStorage solution,
                                   MultiAccountingDataStorage prefilled, float difficulty, String question, Image image, String id) {
        this.difficulty = difficulty;
        this.solution = solution;
        this.id = id;
        setQuestionText(question);
        if (image != null) setQuestionImage(image);
        // Fill grid
        int iSoll = solution.getSoll().get(0).length, iHaben = solution.getHaben().get(0).length;
        soll = new AccountingRecordInputFields[iSoll];
        haben = new AccountingRecordInputFields[iHaben];
        for (int row = 0; row < iSoll; row++) {
            soll[row] = new AccountingRecordInputFields(
                    prefilled.getSoll().get(0)[row]);
            addComponent(soll[row], Side.Left, row);
        }
        for (int row = 0; row < iHaben; row++) {
            haben[row] = new AccountingRecordInputFields(
                    prefilled.getHaben().get(0)[row]);
            addComponent(haben[row], Side.Right, row);
        }

    }

    @Override
    public MultiAccountingDataStorage getSolution() {
        return solution;
    }

    @Override
    public MultiAccountingDataStorage getUserAnswer() {
        MultiAccountingDataStorage dataStorage = new MultiAccountingDataStorage();
        AccountRecordData[] accountRecordDatas;
        // Add Soll
        accountRecordDatas = new AccountRecordData[soll.length];
        for (int i = 0; i < soll.length; i++) {
            accountRecordDatas[i] = soll[i].getAccountRecordData();
        }
        dataStorage.addSoll(accountRecordDatas);
        // Add haben
        accountRecordDatas = new AccountRecordData[haben.length];
        for (int i = 0; i < haben.length; i++) {
            accountRecordDatas[i] = haben[i].getAccountRecordData();
        }
        dataStorage.addHaben(accountRecordDatas);
        return dataStorage;
    }

    @Override
    public double checkUserAnswer() {
        MultiAccountingDataStorage user = getUserAnswer(), solution = getSolution();
        Vector<AccountRecordData[]> uSoll = user.getSoll(), uHaben = user.getHaben(), sSoll = solution
                .getSoll(), sHaben = solution.getHaben();
        // Return if the answer is right
        boolean correct = check(sSoll, uSoll) && check(sHaben, uHaben);
        LogHelper.logInfo("Correctness: "+correct);
        return correct ? 1d : 0d;

    }

    private boolean check(Vector<AccountRecordData[]> solution, Vector<AccountRecordData[]> user) {
        List<AccountRecordData> answerRecords = new LinkedList<>();
        answerRecords.addAll(Arrays.asList(user.get(0)));
        for (AccountRecordData[] potentialSolution: solution) {
            List<AccountRecordData> solutionRecords = new LinkedList<>();
            solutionRecords.addAll(Arrays.asList(potentialSolution));
            List<AccountRecordData> check = new LinkedList<>();
            check.addAll(Arrays.asList(potentialSolution));

            for (AccountRecordData solutionRecord : solutionRecords) {
                for (AccountRecordData answerRecord : answerRecords) {
                    boolean match = true;
                    if (solutionRecord.accountNumber != answerRecord.accountNumber) match = false;
                    if (solutionRecord.accountName != null && answerRecord.accountName != null && !solutionRecord.accountName.equals(answerRecord.accountName))
                        match = false;
                    if (solutionRecord.accountName == null && answerRecord != null) match = false;
                    if (solutionRecord.accountName != null && answerRecord == null) match = false;
                    if (Math.round(solutionRecord.value * 100) != Math.round(answerRecord.value * 100)) match = false;
                    if (match == true) {
                        LogHelper.logInfo("match found for " + solutionRecord.accountName);
                        check.remove(solutionRecord);
                    }
                }
            }

            if (check.isEmpty()) {
                LogHelper.logInfo("answerpart correct");
                return true;
            }
            LogHelper.logInfo("answerpart incorrect");
        }
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
    public XmlQuestionData<MultiAccountingDataStorage> toXMLRepresentation() {
        return new XmlMultiAccountingQuestion(getSolution(), getQuestionText(),
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
