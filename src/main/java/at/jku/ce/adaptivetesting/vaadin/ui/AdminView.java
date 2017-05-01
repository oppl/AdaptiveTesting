package at.jku.ce.adaptivetesting.vaadin.ui;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.easyuploads.MultiFileUpload;

import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by oppl on 11/01/2017.
 */
public class AdminView extends VerticalLayout implements View {

    QuestionManager manager;
    Table table;

    public AdminView(QuestionManager manager) {
        if (manager.getEngine().getQuestions().isEmpty()) manager.loadQuestions();

        this.setMargin(true);
        this.setSpacing(true);
        this.addComponent(new HtmlLabel(HtmlUtils.center("h1",
                "Administration")));

        Button upload = new Button("Upload new Items ...");
        upload.addClickListener( e -> {
            this.getUI().addWindow(new UploadUI());
        });

        this.addComponent(upload);

        table = new Table("Available Items");
        table.addContainerProperty("ItemName", String.class, null);
        table.addContainerProperty("ButtonShowQuestionText",  Button.class, null);
        table.addContainerProperty("ButtonShowQuestionPreview",  Button.class, null);
        table.addContainerProperty("ButtonShowFullXML",  Button.class, null);
        table.addContainerProperty("ButtonDelete",  Button.class, null);
        table.setWidth("100%");

        table.setColumnWidth("ButtonShowQuestionText",155);
        table.setColumnWidth("ButtonShowQuestionPreview",210);
        table.setColumnWidth("ButtonShowFullXML",115);
        table.setColumnWidth("ButtonDelete",100);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.setColumnAlignment("ButtonShowQuestionText", Table.Align.CENTER);
        table.setColumnAlignment("ButtonShowQuestionPreview", Table.Align.CENTER);
        table.setColumnAlignment("ButtonShowFullXML", Table.Align.CENTER);
        table.setColumnAlignment("ButtonDelete", Table.Align.CENTER);

        this.addComponent(table);
        this.manager = manager;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        buildTable();
    }

    private void buildTable() {
        List<IQuestion<? extends AnswerStorage>> questions = manager.getEngine().getQuestions();
        table.removeAllItems();

        int itemID = 0;
        LogHelper.logInfo("Admin: "+questions.size()+" questions available.");

        for (IQuestion<? extends AnswerStorage> question: questions) {

            Button showQuestionTextButton = new Button("question text");
            showQuestionTextButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new QuestionTextUI(question));
            });

            Button showQuestionPreviewButton = new Button("question full preview");
            showQuestionPreviewButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new QuestionPreviewUI(question));
            });

            Button showFullXMLButton = new Button("full XML");
            showFullXMLButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new FullXMLUI(question));
            });

            Button deleteButton = new Button ("delete");
            deleteButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new DeleteUI(question));
            });

            table.addItem(new Object[]{
                    question.getQuestionID(),
                    showQuestionTextButton,
                    showQuestionPreviewButton,
                    showFullXMLButton,
                    deleteButton
            }, itemID);
            itemID++;
        }
        table.setPageLength(table.size());

    }

    private void closeAllWindows () {
        Collection<Window> windows = this.getUI().getWindows();
        if (windows.size() != 0) {
            for (Window w : windows) {
                if (w != null) w.close();
            }
        }
    }

    private class QuestionTextUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public QuestionTextUI(IQuestion<? extends AnswerStorage> question) {
            super("Question Text");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label("<b>"+question.getQuestionID()+"</b>", ContentMode.HTML);
            Label descrLabel = new Label(question.getQuestionText(), ContentMode.HTML);
            /*
            Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
            });
            */
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            //gLayout.addComponent(close,0,2);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class QuestionPreviewUI extends Window {

        VerticalLayout vLayout = new VerticalLayout();

        public QuestionPreviewUI(IQuestion<? extends AnswerStorage> question) {
            super("Full Question Preview");

            try {
                Class<? extends AnswerStorage> dataStorageClass = question.getSolution().getClass();
                Constructor<? extends IQuestion> constructor = question.getClass()
                        .getConstructor(dataStorageClass,
                                dataStorageClass,
                                float.class,
                                String.class,
                                Image.class,
                                String.class);

                Component iQuestionSolution = (Component) constructor
                        .newInstance(question.getSolution(),
                                question.getSolution(),
                                question.getDifficulty(),
                                question.getQuestionText(),
                                null,"");

                this.center();
                vLayout.setWidth("100%");
                this.setWidth("90%");
                this.setHeight("80%");
                Label titleLabel = new Label("<b>"+question.getQuestionID()+"</b>", ContentMode.HTML);
                /*
                Button close = new Button("Close");

                close.addClickListener( e -> {
                    this.close();
                });
                */
                vLayout.addComponent(titleLabel);
                vLayout.addComponent(iQuestionSolution);
                //vLayout.addComponent(close);
                vLayout.setMargin(true);
                vLayout.setSpacing(true);
                setContent(vLayout);
                if (iQuestionSolution instanceof Sizeable) {
                    Sizeable sizeable = iQuestionSolution;
                    sizeable.setSizeFull();
                }
            } catch (Exception e) {
                LogHelper.logError(e.toString());
            }
        }
    }

    private class FullXMLUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public FullXMLUI(IQuestion<? extends AnswerStorage> question) {
            super("Full Question XML");
            this.center();
            gLayout.setWidth("100%");
            gLayout.addStyleName("v-scrollable");
            this.setWidth("1100px");
            this.setHeight("80%");
            Label titleLabel = new Label("<b>"+question.getQuestionID()+"</b>", ContentMode.HTML);
            Label descrLabel = null;
            try {
                descrLabel = new Label(prettyFormat(question.toXML()), ContentMode.PREFORMATTED);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            /*
            Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
            });
            */
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            //gLayout.addComponent(close,0,2);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class DeleteUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public DeleteUI(IQuestion<? extends AnswerStorage> question) {
            super("Delete Question");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label(question.getQuestionID(), ContentMode.HTML);
            Label descrLabel = new Label("<font color=\"red\"><b>The following command is destructive and cannot be undone!</b></font>", ContentMode.HTML);
//          Button remove = new Button("Remove from this test");
            Button delete = new Button("Delete permanently from disk");
            /*
            Button close = new Button("Cancel");

            close.addClickListener( e -> {
                this.close();
            });
            remove.addClickListener( e -> {
                manager.getEngine().removeQuestion(question);
                AdminView.this.buildTable();
                this.close();
            });
            */
            delete.addClickListener( e -> {
                manager.getEngine().removeQuestion(question);
                File questionFolder = new File(VaadinUI.Servlet.getQuestionFolderName());
                assert questionFolder.isDirectory();
                File[] matches = questionFolder.listFiles(f ->
                        f.isFile()
                        && (f.canRead() || f.setReadable(true))
                        && f.getName().equals(question.getQuestionID()));
                for (File f: matches) {
                    try {
                        Files.deleteIfExists(f.toPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                AdminView.this.buildTable();
                this.close();
            });

            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            //gLayout.addComponent(close,0,2);
            //gLayout.addComponent(remove,1,2);
            gLayout.addComponent(delete,0,2);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class UploadUI extends Window {

        GridLayout gLayout = new GridLayout(2,4);

        public UploadUI() {
            super("Upload new Items");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label("<b>Upload new Items</b>", ContentMode.HTML);
            Label descrLabel = new Label("Please select files or drop them below.", ContentMode.HTML);

            MultiFileUpload multiFileUpload = new MultiFileUpload() {
                @Override
                protected void handleFile(File file, String filename, String mimeType, long length) {
                    File questionFolder = new File(VaadinUI.Servlet.getQuestionFolderName());
                    File target = new File(questionFolder,filename);
                    try {
                        Files.move(file.toPath(),target.toPath(),REPLACE_EXISTING);
                        Notification.show("Successfully uploaded file "+filename+".", Notification.Type.TRAY_NOTIFICATION);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            /*Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
                manager.getEngine().resetQuestions();
                manager.loadQuestions();
            });
            */
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            gLayout.addComponent(multiFileUpload,0,2,1,2);
            //gLayout.addComponent(close,0,3);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyFormat(String input) {
        String xmlOutput = prettyFormat(input, 5);
        String delimiter = "question\u003E";
        String[] xml = xmlOutput.split(delimiter);

        // from start of xml until <question>
        String xmlStart = xml[0].substring(0, xml[0].length()-1);

        // Everything between <question> and </question>
        String xmlMiddle = "\u003C" + delimiter + xml[1].substring(0, xml[1].length()-2), temp = "";
        for (int x = 0; x < xmlMiddle.length(); x++) {
            temp = temp + xmlMiddle.charAt(x);
            if (x != 0 && x % 100 == 0) temp = temp + "\n\u0020\u0020\u0020\u0020\u0020";
        }
        xmlMiddle = temp;

        // from </question> until end of file
        String xmlEnd = "\n\u0020\u0020\u0020\u0020\u0020\u003C\u002F" + delimiter + xml[2];
        return xmlStart + xmlMiddle + xmlEnd;
    }
}
