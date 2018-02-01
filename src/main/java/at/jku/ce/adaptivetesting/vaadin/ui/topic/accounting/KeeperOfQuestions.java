package at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.topic.accounting.*;
import at.jku.ce.adaptivetesting.vaadin.ui.QuestionManager;
import at.jku.ce.adaptivetesting.vaadin.ui.VaadinUI;
import at.jku.ce.adaptivetesting.xml.topic.accounting.*;
import com.vaadin.server.FileResource;
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


//import static at.jku.ce.adaptivetesting.topic.accounting.ProfitQuestion.cloneThroughSerialize;

//import static at.jku.ce.adaptivetesting.vaadin.ui.topic.accounting.QuestionLoaderSingleton.addQuestion;

public class KeeperOfQuestions extends QuestionManager {
    List<AccountingQuestion> accountingList = new ArrayList<>();
    List<MultiAccountingQuestion> multiAccountingList = new ArrayList<>();
    List<ProfitQuestion> profitList = new ArrayList<>();
    List<MultipleChoiceQuestion> multipleChoiceList = new ArrayList<>();
    List<MultipleTaskTableQuestion> multipleTaskTableList = new ArrayList<>();
    List<OpenAnswerKeywordQuestion> openAnswerKeywordList = new ArrayList<>();
    int size = 0;

    public List<AccountingQuestion> getAccountingList() throws Exception {
        List<AccountingQuestion> clone = new ArrayList<AccountingQuestion>();
        for (AccountingQuestion item : accountingList){
           // AccountingQuestion copy = item.clone();
            AccountingQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public void setAccountingList(List<AccountingQuestion> accountingList) {
        this.accountingList = accountingList;
    }

    public List<MultiAccountingQuestion> getMultiAccountingList() throws Exception {
        List<MultiAccountingQuestion> clone = new ArrayList<MultiAccountingQuestion>();
        for (MultiAccountingQuestion item : multiAccountingList){
           //MultiAccountingQuestion copy = item.clone();
            MultiAccountingQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public void setMultiAccountingList(List<MultiAccountingQuestion> multiAccountingList) {
        this.multiAccountingList = multiAccountingList;
    }

    public List<ProfitQuestion> getProfitList() throws Exception {
        List<ProfitQuestion> clone = new ArrayList<ProfitQuestion>();
        for (ProfitQuestion item : profitList){
            //ProfitQuestion copy = item.clone();
            ProfitQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
            LogHelper.logInfo("---" + copy.getComponent(1).getDescription()+ "---");
        }
        return clone;
    }

    public void setProfitList(List<ProfitQuestion> profitList) {
        this.profitList = profitList;
    }

    public List<MultipleChoiceQuestion> getMultipleChoiceList() throws Exception {
        List<MultipleChoiceQuestion> clone = new ArrayList<MultipleChoiceQuestion>();
        for (MultipleChoiceQuestion item : multipleChoiceList){
            //MultipleChoiceQuestion copy = item.clone();
            MultipleChoiceQuestion copy = item.cloneThroughSerialize(item);
                    clone.add(copy);
        }
        return clone;
    }

    public void setMultipleChoiceList(List<MultipleChoiceQuestion> multipleChoiceList) {
        this.multipleChoiceList = multipleChoiceList;
    }

    public List<MultipleTaskTableQuestion> getMultipleTaskTableList() throws Exception {
        List<MultipleTaskTableQuestion> clone = new ArrayList<MultipleTaskTableQuestion>();
        for (MultipleTaskTableQuestion item : multipleTaskTableList){
            //MultipleTaskTableQuestion copy = item.clone();
            MultipleTaskTableQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public void setMultipleTaskTableList(List<MultipleTaskTableQuestion> multipleTaskTableList) {
        this.multipleTaskTableList = multipleTaskTableList;
    }

    public List<OpenAnswerKeywordQuestion> getOpenAnswerKeywordList() throws Exception {
        List<OpenAnswerKeywordQuestion> clone = new ArrayList<OpenAnswerKeywordQuestion>();
        for (OpenAnswerKeywordQuestion item : openAnswerKeywordList){
            //OpenAnswerKeywordQuestion copy = item.clone();
            OpenAnswerKeywordQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public void setOpenAnswerKeywordList(List<OpenAnswerKeywordQuestion> openAnswerKeywordList) {
        this.openAnswerKeywordList = openAnswerKeywordList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public KeeperOfQuestions(String name){
        super(name);
        try {
            size = loadQuestions(new File(VaadinUI.Servlet.getQuestionFolderName()));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadQuestions(){

    }

    public int loadQuestions(File containingFolder) throws JAXBException,
            IOException {
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


        Unmarshaller accountingUnmarshaller = accountingJAXB
                .createUnmarshaller();
        Unmarshaller multiAccountingUnmarshaller = multiAccountingJAXB
                .createUnmarshaller();
        Unmarshaller profitUnmarshaller = profitJAXB.createUnmarshaller();
        Unmarshaller multipleChoiceUnmarshaller = multipleChoiceJAXB.createUnmarshaller();
        Unmarshaller multipleTaskTableUnmarshaller = multipleTaskTableJAXB.createUnmarshaller();
        Unmarshaller openAnswerKeywordUnmarshaller = openAnswerKeywordJAXB.createUnmarshaller();

        accountingList = new ArrayList<>();
        multiAccountingList = new ArrayList<>();
        profitList = new ArrayList<>();
        multipleChoiceList = new ArrayList<>();
        multipleTaskTableList = new ArrayList<>();
        openAnswerKeywordList = new ArrayList<>();

        String accountingRootElement = XmlAccountingQuestion.class
                .getAnnotation(XmlRootElement.class).name();
        String multiAccountingRootElement = XmlMultiAccountingQuestion.class
                .getAnnotation(XmlRootElement.class).name();
        String profitRootElement = XmlProfitQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleChoiceRootElement = XmlMultipleChoiceQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleTaskTableRootElement = XmlMultipleTaskTableQuestion.class.getAnnotation(
                XmlRootElement.class).name();
        String openAnswerKeywordRootElement = XmlOpenAnswerKeywordQuestion.class.getAnnotation(
                XmlRootElement.class).name();

        File[] questions = containingFolder.listFiles(f -> f
                .isFile()
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
                LogHelper.logInfo("QuestionManager: item type not supported for "+f.getName()+", ignoring file.");
                continue;
            }
        }
        // Add question to the question manager
        /*
        accountingList.forEach(q -> addQuestion(q));
        multiAccountingList.forEach(q -> addQuestion(q));
        profitList.forEach(q -> addQuestion(q));
        multipleChoiceList.forEach(q -> addQuestion(q));
        multipleTaskTableList.forEach(q -> addQuestion(q));
        openAnswerKeywordList.forEach(q -> addQuestion(q));
        */
        LogHelper.logInfo("Successfully loaded "+successfullyLoaded+" question(s).");

        return questions.length;
    }

    private File checkImageAvailable(File containingFolder, String fileName) {
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
    private static void questionLoadedInfo(File file, int counter, String questionType) {
        counter++;
        LogHelper.logInfo("(" + counter + ") Loading questionfile: " + file.getName());
        LogHelper.logInfo("Type: " + questionType);
    }
}
