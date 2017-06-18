package at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.engine.IEngine;
import at.jku.ce.adaptivetesting.topic.accounting.*;
import at.jku.ce.adaptivetesting.xml.topic.accounting.*;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Baumann
 */
public class QuestionLoaderSingleton {

    private static QuestionLoaderSingleton instance = new QuestionLoaderSingleton();
    private static boolean status = false;
    private static final List<AccountingQuestion> accountingList = new ArrayList<>();
    private static final List<MultiAccountingQuestion> multiAccountingList = new ArrayList<>();
    private static final List<ProfitQuestion> profitList = new ArrayList<>();
    private static final List<MultipleChoiceQuestion> multipleChoiceList = new ArrayList<>();
    private static final List<MultipleTaskTableQuestion> multipleTaskTableList = new ArrayList<>();
    private static final List<OpenAnswerKeywordQuestion> openAnswerKeywordList = new ArrayList<>();
    private static List<IQuestion<? extends AnswerStorage>>[] bags;
    private static int questionNumber;

    public static QuestionLoaderSingleton getInstance() {
        return instance;
    }

    private QuestionLoaderSingleton() {

    }

    protected void initialize(IEngine iEngine, File containingFolder) throws JAXBException, IOException {
        if (status == false) {
            loadQuestions(iEngine, containingFolder);
        } else {
            LogHelper.logInfo("XML-questions already loaded");
            //iEngine.skipLoad();
        }
    }

    public void resetQuestions() {
        LogHelper.logInfo("Number of XML-questions changed. Forcing reload");
        status = false;
        accountingList.clear();
        multiAccountingList.clear();
        profitList.clear();
        multipleChoiceList.clear();
        multipleTaskTableList.clear();
        openAnswerKeywordList.clear();
    }

    private static void loadQuestions (IEngine iEngine, File containingFolder) throws JAXBException, IOException {
        assert containingFolder.exists() && containingFolder.isDirectory();
        JAXBContext accountingJAXB = JAXBContext.newInstance(
                XmlAccountingQuestion.class, AccountingDataStorage.class);
        JAXBContext multiAccountingJAXB = JAXBContext.newInstance(
                XmlMultiAccountingQuestion.class, MultiAccountingDataStorage.class);
        JAXBContext profitJAXB = JAXBContext.newInstance(
                XmlProfitQuestion.class, ProfitDataStorage.class);
        JAXBContext multipleChoiceJAXB = JAXBContext.newInstance(
                XmlMultipleChoiceQuestion.class, MultipleChoiceDataStorage.class);
        JAXBContext multipleTaskTableJAXB = JAXBContext.newInstance(
                XmlMultipleTaskTableQuestion.class, MultipleTaskTableDataStorage.class);
        JAXBContext openAnswerKeywordJAXB = JAXBContext.newInstance(
                XmlOpenAnswerKeywordQuestion.class, OpenAnswerKeywordDataStorage.class);


        Unmarshaller accountingUnmarshaller = accountingJAXB.createUnmarshaller();
        Unmarshaller multiAccountingUnmarshaller = multiAccountingJAXB.createUnmarshaller();
        Unmarshaller profitUnmarshaller = profitJAXB.createUnmarshaller();
        Unmarshaller multipleChoiceUnmarshaller = multipleChoiceJAXB.createUnmarshaller();
        Unmarshaller multipleTaskTableUnmarshaller = multipleTaskTableJAXB.createUnmarshaller();
        Unmarshaller openAnswerKeywordUnmarshaller = openAnswerKeywordJAXB.createUnmarshaller();

        String accountingRootElement = XmlAccountingQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String multiAccountingRootElement = XmlMultiAccountingQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String profitRootElement = XmlProfitQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleChoiceRootElement = XmlMultipleChoiceQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleTaskTableRootElement = XmlMultipleTaskTableQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String openAnswerKeywordRootElement = XmlOpenAnswerKeywordQuestion.class.getAnnotation(
                XmlRootElement.class).name();

        File[] questions = containingFolder.listFiles(f -> f.isFile()
                && (f.canRead() || f.setReadable(true))
                && f.getName().endsWith(".xml"));

        // read all questions
        LogHelper.logInfo("Found "+questions.length+" potential question(s)");
        int successfullyLoaded = 0;
        for (File f : questions) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(
                        new BOMInputStream(new FileInputStream(f),
                                ByteOrderMark.UTF_8), "UTF8"));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            String fileAsString = sb.toString().replaceAll("& ", "&amp; ");
            File image = checkImageAvailable(containingFolder, f.getName());
            if (fileAsString.contains(profitRootElement)) {
                // Profit Question
                questionLoadedInfo(f, successfullyLoaded, ProfitQuestion.class.getName());
                XmlProfitQuestion question = (XmlProfitQuestion) profitUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                ProfitQuestion pq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) pq.setQuestionImage(new Image("",new FileResource(image)));
                profitList.add(pq);
                successfullyLoaded++;
            } else if (fileAsString.contains(accountingRootElement)) {
                // Accounting Question
                questionLoadedInfo(f, successfullyLoaded, AccountingQuestion.class.getName());
                XmlAccountingQuestion question = (XmlAccountingQuestion) accountingUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                AccountingQuestion aq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image != null) aq.setQuestionImage(new Image("", new FileResource(image)));
                accountingList.add(aq);
                successfullyLoaded++;
            } else if (fileAsString.contains(multiAccountingRootElement)) {
                // Multi Accounting Question
                questionLoadedInfo(f, successfullyLoaded, MultiAccountingQuestion.class.getName());
                XmlMultiAccountingQuestion question = (XmlMultiAccountingQuestion) multiAccountingUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultiAccountingQuestion maq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image != null) maq.setQuestionImage(new Image("", new FileResource(image)));
                multiAccountingList.add(maq);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(multipleChoiceRootElement)) {
                // Multiple Choice Question
                questionLoadedInfo(f, successfullyLoaded, MultipleChoiceQuestion.class.getName());
                XmlMultipleChoiceQuestion question = (XmlMultipleChoiceQuestion) multipleChoiceUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultipleChoiceQuestion mq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) mq.setQuestionImage(new Image("",new FileResource(image)));
                multipleChoiceList.add(mq);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(multipleTaskTableRootElement)) {
                // Multiple Task Table Question
                questionLoadedInfo(f, successfullyLoaded, MultipleTaskTableQuestion.class.getName());
                XmlMultipleTaskTableQuestion question = (XmlMultipleTaskTableQuestion) multipleTaskTableUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultipleTaskTableQuestion mtt = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) mtt.setQuestionImage(new Image("",new FileResource(image)));
                multipleTaskTableList.add(mtt);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(openAnswerKeywordRootElement)) {
                // Open Answer Keyword Question
                questionLoadedInfo(f, successfullyLoaded, OpenAnswerKeywordQuestion.class.getName());
                XmlOpenAnswerKeywordQuestion question = (XmlOpenAnswerKeywordQuestion) openAnswerKeywordUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                OpenAnswerKeywordQuestion oak = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) oak.setQuestionImage(new Image("",new FileResource(image)));
                openAnswerKeywordList.add(oak);
                successfullyLoaded++;
            }
            else {
                LogHelper.logError("Item type not supported for "+f.getName()+", ignoring file.");
                continue;
            }
        }
        // Add question to the question manager
        accountingList.forEach(q -> addQuestion(iEngine, q));
        multiAccountingList.forEach(q -> addQuestion(iEngine, q));
        profitList.forEach(q -> addQuestion(iEngine, q));
        multipleChoiceList.forEach(q -> addQuestion(iEngine, q));
        multipleTaskTableList.forEach(q -> addQuestion(iEngine, q));
        openAnswerKeywordList.forEach(q -> addQuestion(iEngine, q));
        LogHelper.logInfo("Successfully loaded "+successfullyLoaded+" questions.");
        //bags = iEngine.getBags();
        //questionNumber = iEngine.getQuestionNumber();
        status = true;
    }

    private static File checkImageAvailable(File containingFolder, String fileName) {
        String imageName = fileName.replace(".xml",".png");
        File image = new File(containingFolder,imageName);
        if (image.exists()) {
            return image;
        }
        else {
            imageName = fileName.replace(".xml",".jpg");
            image = new File(containingFolder,imageName);
            if (image.exists()) {
                return image;
            }
            else {
                return null;
            }
        }
    }

    private static <QuestionComponent extends IQuestion<? extends AnswerStorage> & Component & Sizeable> void addQuestion(
            IEngine iEngine, QuestionComponent question) {
        iEngine.addQuestionToPool(question);
    }

    private static void questionLoadedInfo(File file, int counter, String questionType) {
        counter++;
        LogHelper.logInfo("(" + counter + ") Loading questionfile: " + file.getName());
        LogHelper.logInfo("Type: " + questionType);
    }

    public static List<IQuestion<? extends AnswerStorage>>[] getBags() {
        return bags;
    }

    public static int getQuestionNumber () {
        return questionNumber;
    }
}
