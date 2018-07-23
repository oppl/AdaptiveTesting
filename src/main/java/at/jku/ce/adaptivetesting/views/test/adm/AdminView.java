package at.jku.ce.adaptivetesting.views.test.adm;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.views.html.HtmlLabel;
import at.jku.ce.adaptivetesting.views.html.HtmlUtils;
import at.jku.ce.adaptivetesting.views.def.MenuWindow;
import at.jku.ce.adaptivetesting.views.def.DefaultView;
import at.jku.ce.adaptivetesting.views.test.TestView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.hene.expandingtextarea.ExpandingTextArea;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

    private TestView manager;
    private Table table;
    private String testTypeFolder;

    public AdminView(TestView manager, String testTypeFolder) {
        this.testTypeFolder = testTypeFolder;
        this.setMargin(true);
        this.setSpacing(true);
        this.addComponent(new HtmlLabel(HtmlUtils.center("h1", "Item Administration")));

        GridLayout gridLayout = new GridLayout(2, 1);
        gridLayout.setWidth("100%");

        Button upload = new Button("Neue Items hinzufügen");
        upload.addClickListener( e -> {
            this.getUI().addWindow(new UploadUI());
        });

        Button menu = new Button("Navigationsmenü");
        menu.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 32642854872179636L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                LogHelper.logInfo("Opened MenuWindow");
                MenuWindow menuWindow = new MenuWindow();

                menuWindow.addCloseListener(new Window.CloseListener() {
                    private static final long serialVersionUID = 7874342882437355680L;

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        event.getButton().setEnabled(true);
                    }
                });
                menuWindow.deactivateAdminMenuButton();
                getUI().addWindow(menuWindow);
                event.getButton().setEnabled(false);
            }
        });

        gridLayout.addComponent(upload, 0, 0);
        gridLayout.addComponent(menu, 1, 0);
        gridLayout.setComponentAlignment(menu, Alignment.BOTTOM_RIGHT);
        this.addComponent(gridLayout);

        table = new Table("Vorhandene Items");
        table.addContainerProperty("ItemName", String.class, null);
        table.addContainerProperty("ButtonShowQuestionText",  Button.class, null);
        table.addContainerProperty("ButtonShowQuestionPreview",  Button.class, null);
        table.addContainerProperty("ButtonShowFullXML",  Button.class, null);
        table.addContainerProperty("ButtonDelete",  Button.class, null);
        table.setWidth("100%");

        table.setColumnWidth("ButtonShowQuestionText",205);
        table.setColumnWidth("ButtonShowQuestionPreview",155);
        table.setColumnWidth("ButtonShowFullXML",155);
        table.setColumnWidth("ButtonDelete",125);
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

            Button showQuestionTextButton = new Button("Fragentext anzeigen");
            showQuestionTextButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new QuestionTextUI(question));
            });

            Button showQuestionPreviewButton = new Button("Item anzeigen");
            showQuestionPreviewButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new QuestionPreviewUI(question));
            });

            Button showFullXMLButton = new Button("XML anzeigen");
            showFullXMLButton.addClickListener( e -> {
                closeAllWindows ();
                this.getUI().addWindow(new FullXMLUI(question));
            });

            Button deleteButton = new Button ("Entfernen");
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
            super("Voransicht: Fragentext");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label("<b>" + question.getQuestionID() + "</b>", ContentMode.HTML);
            Label descrLabel = new Label(question.getQuestionText(), ContentMode.HTML);
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class QuestionPreviewUI extends Window {

        VerticalLayout vLayout = new VerticalLayout();

        public QuestionPreviewUI(IQuestion<? extends AnswerStorage> question) {
            super("Voransicht: komplettes Item");

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
                Label titleLabel = new Label("<b>" + question.getQuestionID() + "</b>", ContentMode.HTML);
                vLayout.addComponent(titleLabel);
                vLayout.addComponent(iQuestionSolution);
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
            super("Voransicht: XML");
            this.center();
            gLayout.setWidth("100%");
            gLayout.addStyleName("v-scrollable");
            this.setWidth("90%");
            this.setHeight("80%");
            Label titleLabel = new Label("<b>" + question.getQuestionID() + "</b>", ContentMode.HTML);
            ExpandingTextArea area = new ExpandingTextArea();
            area.setWordwrap(true);
            area.setWidth(100, Unit.PERCENTAGE);
            try {
                area.setValue(prettyPrint(toXmlDocument(question.toXML())));
            } catch (JAXBException | ParserConfigurationException |
                    SAXException | IOException | TransformerException e) {
                area.setValue(e.getMessage());
                e.printStackTrace();
            }
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(area,0,1,1,1);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class DeleteUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public DeleteUI(IQuestion<? extends AnswerStorage> question) {
            super("Frage entfernen");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label(question.getQuestionID(), ContentMode.HTML);
            Label descrLabel = new Label("<p style=\"color:red\"><b>Diese Aktion ist unumkehrbar!</b></p>", ContentMode.HTML);
            Button delete = new Button("Entfernen");
            delete.addClickListener( e -> {
                manager.getEngine().removeQuestion(question);
                File questionFolder = new File(getQuestionFolderName());
                assert questionFolder.isDirectory();
                File[] matches = questionFolder.listFiles(file ->
                        file.isFile()
                        && (file.canRead() || file.setReadable(true))
                        && file.getName().equals(question.getQuestionID()));
                File[] x = matches;
                for (File file: matches) {
                    try {
                        Files.deleteIfExists(file.toPath());
                        Notification.show("Information", "Das Item " + file.getName() +
                                " wurde erfolgreich entfernt.", Notification.Type.HUMANIZED_MESSAGE);
                        reloadQuestions();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                AdminView.this.buildTable();
                this.close();
            });
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
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
            super("Neue Items hinzufügen");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label("<b>Neue Items hinzufügen</b>", ContentMode.HTML);
            Label descrLabel = new Label("Bitte Item auswählen oder die Drag & Drop-Funktion benutzen.", ContentMode.HTML);

            MultiFileUpload multiFileUpload = new MultiFileUpload() {
                @Override
                protected void handleFile(File file, String filename, String mimeType, long length) {
                    File questionFolder = new File(getQuestionFolderName());
                    File target = new File(questionFolder,filename);
                    try {
                        Files.move(file.toPath(),target.toPath(),REPLACE_EXISTING);
                        reloadQuestions();
                        Notification.show("Information","Das Item " + filename + " wurde erfolgreich hinzugefügt.", Notification.Type.HUMANIZED_MESSAGE);
                        AdminView.this.buildTable();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            gLayout.addComponent(multiFileUpload,0,2,1,2);
            gLayout.setRowExpandRatio(1,1);
            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private static String prettyPrint(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(document);
        StringWriter strWriter = new StringWriter();
        StreamResult result = new StreamResult(strWriter);
        transformer.transform(source, result);
        return strWriter.getBuffer().toString();
    }

    private static Document toXmlDocument(String str) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(new InputSource(new StringReader(str)));
        return document;
    }

    private void reloadQuestions() {
        manager.resetQuestionNo();
        manager.getEngine().resetQuestions();
        manager.loadQuestions();
    }

    private String getQuestionFolderName() {
        String questionfolderName = DefaultView.Servlet.getQuestionFolderName();
        questionfolderName = questionfolderName + testTypeFolder;
        return questionfolderName;
    }
}

