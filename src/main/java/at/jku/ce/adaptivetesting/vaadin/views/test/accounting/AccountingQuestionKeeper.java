package at.jku.ce.adaptivetesting.vaadin.views.test.accounting;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.accounting.*;
import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingXmlHelper;
import at.jku.ce.adaptivetesting.vaadin.views.def.DefaultView;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountingQuestionKeeper {

    private List<AccountingQuestion> accountingList = new ArrayList<>();
    private List<MultiAccountingQuestion> multiAccountingList = new ArrayList<>();
    private List<ProfitQuestion> profitList = new ArrayList<>();
    private List<MultipleChoiceQuestion> multipleChoiceList = new ArrayList<>();
    private List<MultipleTaskTableQuestion> multipleTaskTableList = new ArrayList<>();
    private List<OpenAnswerKeywordQuestion> openAnswerKeywordList = new ArrayList<>();
    private int size = 0;

    public List<AccountingQuestion> getAccountingList() throws Exception {
        List<AccountingQuestion> clone = new ArrayList<>();
        for (AccountingQuestion item : accountingList){
            AccountingQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<MultiAccountingQuestion> getMultiAccountingList() throws Exception {
        List<MultiAccountingQuestion> clone = new ArrayList<>();
        for (MultiAccountingQuestion item : multiAccountingList){
            MultiAccountingQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<ProfitQuestion> getProfitList() throws Exception {
        List<ProfitQuestion> clone = new ArrayList<>();
        for (ProfitQuestion item : profitList){
            ProfitQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<MultipleChoiceQuestion> getMultipleChoiceList() throws Exception {
        List<MultipleChoiceQuestion> clone = new ArrayList<>();
        for (MultipleChoiceQuestion item : multipleChoiceList){
            MultipleChoiceQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<MultipleTaskTableQuestion> getMultipleTaskTableList() throws Exception {
        List<MultipleTaskTableQuestion> clone = new ArrayList<>();
        for (MultipleTaskTableQuestion item : multipleTaskTableList){
            MultipleTaskTableQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<OpenAnswerKeywordQuestion> getOpenAnswerKeywordList() throws Exception {
        List<OpenAnswerKeywordQuestion> clone = new ArrayList<>();
        for (OpenAnswerKeywordQuestion item : openAnswerKeywordList){
            OpenAnswerKeywordQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public int getSize() {
        return size;
    }

    public void initialize() {
        try {
            size = initialize(new File(DefaultView.Servlet.getQuestionFolderName() + "/accounting"));

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int initialize (File containingFolder) throws JAXBException, IOException {
        assert containingFolder.exists() && containingFolder.isDirectory();

        JAXBContext accountingJAXB = JAXBContext.newInstance(
                AccountingQuestionXml.class, AccountingDataStorage.class);
        JAXBContext multiAccountingJAXB = JAXBContext.newInstance(
                MultiAccountingQuestionXml.class, MultiAccountingDataStorage.class);
        JAXBContext profitJAXB = JAXBContext.newInstance(
                ProfitQuestionXml.class, ProfitDataStorage.class);
        JAXBContext multipleChoiceJAXB = JAXBContext.newInstance(
                MultipleChoiceQuestionXml.class, MultipleChoiceDataStorage.class);
        JAXBContext multipleTaskTableJAXB = JAXBContext.newInstance(
                MultipleTaskTableQuestionXml.class, MultipleTaskTableDataStorage.class);
        JAXBContext openAnswerKeywordJAXB = JAXBContext.newInstance(
                OpenAnswerKeywordQuestionXml.class, OpenAnswerKeywordDataStorage.class);


        Unmarshaller accountingUnmarshaller = accountingJAXB
                .createUnmarshaller();
        Unmarshaller multiAccountingUnmarshaller = multiAccountingJAXB
                .createUnmarshaller();
        Unmarshaller profitUnmarshaller = profitJAXB
                .createUnmarshaller();
        Unmarshaller multipleChoiceUnmarshaller = multipleChoiceJAXB
                .createUnmarshaller();
        Unmarshaller multipleTaskTableUnmarshaller = multipleTaskTableJAXB
                .createUnmarshaller();
        Unmarshaller openAnswerKeywordUnmarshaller = openAnswerKeywordJAXB
                .createUnmarshaller();

        accountingList = new ArrayList<>();
        multiAccountingList = new ArrayList<>();
        profitList = new ArrayList<>();
        multipleChoiceList = new ArrayList<>();
        multipleTaskTableList = new ArrayList<>();
        openAnswerKeywordList = new ArrayList<>();

        String accountingRootElement = AccountingQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String multiAccountingRootElement = MultiAccountingQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String profitRootElement = ProfitQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleChoiceRootElement = MultipleChoiceQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String multipleTaskTableRootElement = MultipleTaskTableQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String openAnswerKeywordRootElement = OpenAnswerKeywordQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();

        File[] questions = containingFolder.listFiles(f -> f
                .isFile()
                && (f.canRead() || f.setReadable(true))
                && f.getName().endsWith(".xml"));

        // read all questions
        LogHelper.logInfo("Found " + questions.length + " potential question(s)");
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
                questionInitializedInfo(f, successfullyLoaded, ProfitQuestion.class.getName());
                ProfitQuestionXml question = (ProfitQuestionXml) profitUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                ProfitQuestion pq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) pq.setQuestionImage(new Image("",new FileResource(image)));
                profitList.add(pq);
                successfullyLoaded++;
            } else if (fileAsString.contains(accountingRootElement)) {

                // Accounting Question
                questionInitializedInfo(f, successfullyLoaded, AccountingQuestion.class.getName());
                AccountingQuestionXml question = (AccountingQuestionXml) accountingUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                AccountingQuestion aq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image != null) aq.setQuestionImage(new Image("", new FileResource(image)));
                accountingList.add(aq);
                successfullyLoaded++;
            } else if (fileAsString.contains(multiAccountingRootElement)) {

                // Multi Accounting Question
                questionInitializedInfo(f, successfullyLoaded, MultiAccountingQuestion.class.getName());
                MultiAccountingQuestionXml question = (MultiAccountingQuestionXml) multiAccountingUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultiAccountingQuestion maq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image != null) maq.setQuestionImage(new Image("", new FileResource(image)));
                multiAccountingList.add(maq);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(multipleChoiceRootElement)) {

                // Multiple Choice Question
                questionInitializedInfo(f, successfullyLoaded, MultipleChoiceQuestion.class.getName());
                MultipleChoiceQuestionXml question = (MultipleChoiceQuestionXml) multipleChoiceUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultipleChoiceQuestion mq = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) mq.setQuestionImage(new Image("",new FileResource(image)));
                multipleChoiceList.add(mq);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(multipleTaskTableRootElement)) {

                // Multiple Task Table Question
                questionInitializedInfo(f, successfullyLoaded, MultipleTaskTableQuestion.class.getName());
                MultipleTaskTableQuestionXml question = (MultipleTaskTableQuestionXml) multipleTaskTableUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultipleTaskTableQuestion mtt = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) mtt.setQuestionImage(new Image("",new FileResource(image)));
                multipleTaskTableList.add(mtt);
                successfullyLoaded++;
            }
            else if (fileAsString.contains(openAnswerKeywordRootElement)) {

                // Open Answer Keyword Question
                questionInitializedInfo(f, successfullyLoaded, OpenAnswerKeywordQuestion.class.getName());
                OpenAnswerKeywordQuestionXml question = (OpenAnswerKeywordQuestionXml) openAnswerKeywordUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                OpenAnswerKeywordQuestion oak = AccountingXmlHelper.fromXml(question, f.getName());
                if (image!=null) oak.setQuestionImage(new Image("",new FileResource(image)));
                openAnswerKeywordList.add(oak);
                successfullyLoaded++;
            }
            else {
                LogHelper.logInfo("AccountingTestView: item type not supported for " + f.getName() + ", ignoring file.");
            }
        }
        LogHelper.logInfo("Successfully loaded " + successfullyLoaded + " question(s).");
        String notificationCaption, notificationDescription;
        switch (successfullyLoaded) {
            case 0:
                notificationCaption = "Ladevorgang fehlgeschlagen";
                notificationDescription = "Es wurden keine ladbaren Items gefunden";
                break;
            case 1:
                notificationCaption = "Ladevorgang abgeschlossen";
                notificationDescription = "Es wurde (" + successfullyLoaded + ") Frage erfolgreich geladen";
                break;
            default:
                notificationCaption = "Ladevorgang abgeschlossen";
                notificationDescription = "Es wurden (" + successfullyLoaded + ") Fragen erfolgreich geladen";
        }
        Notification.show(notificationCaption, notificationDescription, Notification.Type.TRAY_NOTIFICATION);

        return questions.length;
    }

    private File checkImageAvailable(File containingFolder, String fileName) {
        String imageName = fileName.replace(".xml",".png");
        File image = new File(containingFolder, imageName);
        if (image.exists()) {
            return image;
        }
        else {
            imageName = fileName.replace(".xml",".jpg");
            image = new File(containingFolder, imageName);
            if (image.exists()) {
                return image;
            }
            else {
                return null;
            }
        }
    }

    private static void questionInitializedInfo(File file, int counter, String questionType) {
        counter++;
        LogHelper.logInfo("(" + counter + ") Loading questionfile: " + file.getName());
        LogHelper.logInfo("Type: " + questionType);
    }
}
