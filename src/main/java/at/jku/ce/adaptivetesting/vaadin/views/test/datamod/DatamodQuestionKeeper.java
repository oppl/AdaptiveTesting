package at.jku.ce.adaptivetesting.vaadin.views.test.datamod;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.questions.datamod.*;
import at.jku.ce.adaptivetesting.questions.datamod.util.DatamodXmlHelper;
import at.jku.ce.adaptivetesting.vaadin.views.def.DefaultView;
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

public class DatamodQuestionKeeper {

    private List<SqlQuestion> sqlList = new ArrayList<>();
    private int size = 0;

    public List<SqlQuestion> getSqlList() throws Exception {
        List<SqlQuestion> clone = new ArrayList<>();
        for (SqlQuestion item : sqlList){
            SqlQuestion copy = item.cloneThroughSerialize(item);
            clone.add(copy);
        }
        return clone;
    }

    public int getSize() {
        return size;
    }

    public void initialize() {
        try {
            size = initialize(new File(DefaultView.Servlet.getQuestionFolderName() + "/datamod"));

        } catch (JAXBException e) {
            //e.printStackTrace();
            e.getCause().printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int initialize (File containingFolder) throws JAXBException, IOException {
        assert containingFolder.exists() && containingFolder.isDirectory();

        JAXBContext sqlJAXB = JAXBContext.newInstance(
                SqlQuestionXml.class, SqlDataStorage.class);

        Unmarshaller sqlUnmarshaller = sqlJAXB
                .createUnmarshaller();

        sqlList = new ArrayList<>();

        String sqlRootElement = SqlQuestionXml.class.getAnnotation(
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
            if (fileAsString.contains(sqlRootElement)) {

                // SQL Question
                questionInitializedInfo(f, successfullyLoaded, SqlQuestion.class.getName());
                SqlQuestionXml question = (SqlQuestionXml) sqlUnmarshaller
                        .unmarshal(new StringReader(fileAsString));
                SqlQuestion sqlq = DatamodXmlHelper.fromXml(question, f.getName());
                if (image!=null) sqlq.setQuestionImage(new Image("",new FileResource(image)));
                sqlList.add(sqlq);
                successfullyLoaded++;
            }
            else {
                LogHelper.logInfo("DatamodTestView: item type not supported for " + f.getName() + ", ignoring file.");
            }
        }
        LogHelper.logInfo("Successfully loaded " + successfullyLoaded + " question(s).");

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
