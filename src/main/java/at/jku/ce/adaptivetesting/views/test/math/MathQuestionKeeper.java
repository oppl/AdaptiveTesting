package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.engine.TestVariants;
import at.jku.ce.adaptivetesting.questions.math.MathDataStorage;
import at.jku.ce.adaptivetesting.questions.math.MathQuestion;
import at.jku.ce.adaptivetesting.questions.math.MathQuestionXml;
import at.jku.ce.adaptivetesting.questions.math.util.MathXmlHelper;
import at.jku.ce.adaptivetesting.views.def.DefaultView;
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

public class MathQuestionKeeper {
    private List<MathQuestion> mathList = new ArrayList<>();
    private int size = 0;

    public List<MathQuestion> getMathList() throws Exception {
        List<MathQuestion> clone = new ArrayList<>();
        for (MathQuestion item : mathList){
            MathQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public int getSize() {
        return size;
    }

    public void initialize() {
        try {
            size = initialize(new File(DefaultView.Servlet.getQuestionFolderName() + TestVariants.MATH.getFolderName()));

        } catch (JAXBException e) {
            //e.printStackTrace();
            e.getCause().printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int initialize (File containingFolder) throws JAXBException, IOException {
        assert containingFolder.exists() && containingFolder.isDirectory();

        JAXBContext mathJAXB = JAXBContext.newInstance(
                MathQuestionXml.class, MathDataStorage.class);

        Unmarshaller mathUnmarshaller = mathJAXB
                .createUnmarshaller();

        mathList = new ArrayList<>();

        String mathRootElement = MathQuestionXml.class.getAnnotation(
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
            if (fileAsString.contains(mathRootElement)) {
                questionInitializedInfo(f, successfullyLoaded, MathQuestion.class.getName());
                MathQuestionXml question = (MathQuestionXml) mathUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MathQuestion mathq = MathXmlHelper.fromXml(question, f.getName());
                if (image!=null) mathq.setQuestionImage(new Image("",new FileResource(image)));
                mathList.add(mathq);
                successfullyLoaded++;
            }
            else {
                LogHelper.logInfo("MathTestView: item type not supported for " + f.getName() + ", ignoring file.");
            }
        }
        LogHelper.logInfo("Successfully loaded " + successfullyLoaded + " question(s).");
        String notificationCaption, notificationDescription;
        switch (successfullyLoaded) {
            case 0:
                notificationCaption = "Ladevorgang fehlgeschlagen";
                notificationDescription = "Es wurden keine ladbaren Items gefunden";
                ConnectionProvider.closeConnection();
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
