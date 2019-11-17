package at.jku.ce.adaptivetesting.views.test.math;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.engine.TestVariants;
import at.jku.ce.adaptivetesting.questions.math.*;
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
import java.util.LinkedList;
import java.util.List;

public class MathQuestionKeeper {
    private List<MathQuestion> mathList = new ArrayList<>();
    private List<SimpleMathQuestion> simpleMathList = new ArrayList<>();
    private List<MultipleChoiceMathQuestion> multiChoiceMathList = new ArrayList<>();
    private int size = 0;

    public List<MathQuestion> getMathList() throws Exception {
        List<MathQuestion> clone = new ArrayList<>();
        for (MathQuestion item : mathList){
            MathQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<SimpleMathQuestion> getSimpleMathList() throws Exception {
        List<SimpleMathQuestion> clone = new ArrayList<>();
        for (SimpleMathQuestion item : simpleMathList){
            SimpleMathQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public List<MultipleChoiceMathQuestion> getMultiChoiceMathList() throws Exception {
        List<MultipleChoiceMathQuestion> clone = new ArrayList<>();
        for (MultipleChoiceMathQuestion item : multiChoiceMathList){
            MultipleChoiceMathQuestion copy = item.cloneThroughSerialize(item);
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
        JAXBContext simpleMathJAXB = JAXBContext.newInstance(
                SimpleMathQuestionXml.class, SimpleMathDataStorage.class);
        JAXBContext multiMathJAXB = JAXBContext.newInstance(
                MultipleChoiceMathQuestionXml.class, MultipleChoiceMathDataStorage.class);

        Unmarshaller mathUnmarshaller = mathJAXB
                .createUnmarshaller();
        Unmarshaller simpleMathUnmarshaller = simpleMathJAXB
                .createUnmarshaller();
        Unmarshaller multiMathUnmarshaller = multiMathJAXB
                .createUnmarshaller();

        mathList = new ArrayList<>();
        simpleMathList = new ArrayList<>();
        multiChoiceMathList = new ArrayList<>();

        String mathRootElement = MathQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String simpleMathRootElement = SimpleMathQuestionXml.class.getAnnotation(
                XmlRootElement.class).name();
        String multiMathRootElement = MultipleChoiceMathQuestionXml.class.getAnnotation(
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
            if (fileAsString.contains(mathRootElement)) {
                File image = checkImageAvailable(containingFolder, f.getName());
                questionInitializedInfo(f, successfullyLoaded, MathQuestion.class.getName());
                MathQuestionXml question = (MathQuestionXml) mathUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MathQuestion mathq = MathXmlHelper.fromXml(question, f.getName());
                if (image!=null) mathq.setQuestionImage(new Image("",new FileResource(image)));
                mathList.add(mathq);
                successfullyLoaded++;
            } else if (fileAsString.contains(simpleMathRootElement)) {
                List<File> images = checkImagesAvailable(containingFolder, f.getName());
                questionInitializedInfo(f, successfullyLoaded, SimpleMathQuestion.class.getName());
                SimpleMathQuestionXml question = (SimpleMathQuestionXml) simpleMathUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                SimpleMathQuestion mathq = MathXmlHelper.fromXml(question, f.getName());
                if (images!=null){
                    List<Image> questionImages = new LinkedList<Image>();
                    for (File file : images){
                        questionImages.add(new Image("",new FileResource(file)));
                    }
                    mathq.setQuestionImages(questionImages);
                }
                simpleMathList.add(mathq);
                successfullyLoaded++;
            } else if (fileAsString.contains(multiMathRootElement)) {
                List<File> images = checkImagesAvailable(containingFolder, f.getName());
                List<File> answerOptionImages = checkAnswerOptionImagesAvailable(containingFolder, f.getName());
                questionInitializedInfo(f, successfullyLoaded, MultipleChoiceMathQuestion.class.getName());
                MultipleChoiceMathQuestionXml question = (MultipleChoiceMathQuestionXml) multiMathUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                MultipleChoiceMathQuestion mathq = MathXmlHelper.fromXml(question, f.getName());
                if (images!=null){
                    List<Image> questionImages = new LinkedList<Image>();
                    for (File file : images){
                        questionImages.add(new Image("",new FileResource(file)));
                    }
                    if (answerOptionImages!=null){
                        List<Image> answerImages = new LinkedList<Image>();
                        for (File file : answerOptionImages){
                            answerImages.add(new Image("",new FileResource(file)));
                        }
                        mathq.setQuestionImages(questionImages, answerImages);
                    } else {
                        mathq.setQuestionImages(questionImages, null);
                    }
                }
                multiChoiceMathList.add(mathq);
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

    private List<File> checkAnswerOptionImagesAvailable (File containingFolder, String fileName){
        List<File> foundImages = new LinkedList<File>();
        boolean foundFurtherImages = true;
        int i = 1;
        while (foundFurtherImages) {
            String imageName = fileName.replace(".xml", ".png");
            String[] parts = imageName.split("\\.");
            parts[0] = parts[0] + "-Option" + i;
            imageName = parts[0] + "." + parts[1];
            File image = new File(containingFolder, imageName);
            if (image.exists()) {
                foundImages.add(image);
            } else {
                imageName = imageName.replace(".png", ".jpg");
                image = new File(containingFolder, imageName);
                if (image.exists()) {
                    foundImages.add(image);
                } else {
                    foundFurtherImages = false;
                }
            }
            i++;
        }
        return foundImages;
    }

    private List<File> checkImagesAvailable(File containingFolder, String fileName) {
        List<File> foundImages = new LinkedList<File>();

        File imageFile = checkImageAvailable(containingFolder,fileName);
        if(imageFile != null) foundImages.add(imageFile);

        boolean foundFurtherImages = true;
        int i = 1;
        while (foundFurtherImages) {
            String imageName = fileName.replace(".xml", ".png");
            String[] parts = imageName.split("\\.");
            parts[0] = parts[0] + "-" + i;
            imageName = parts[0] + "." + parts[1];
            File image = new File(containingFolder, imageName);
            if (image.exists()) {
                foundImages.add(image);
            } else {
                imageName = imageName.replace(".png", ".jpg");
                image = new File(containingFolder, imageName);
                if (image.exists()) {
                    foundImages.add(image);
                } else {
                    foundFurtherImages = false;
                }
            }
            i++;
        }
        return foundImages;
    }

    private static void questionInitializedInfo(File file, int counter, String questionType) {
        counter++;
        LogHelper.logInfo("(" + counter + ") Loading questionfile: " + file.getName());
        LogHelper.logInfo("Type: " + questionType);
    }
}
