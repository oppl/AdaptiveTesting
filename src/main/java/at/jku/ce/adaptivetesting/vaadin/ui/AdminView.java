package at.jku.ce.adaptivetesting.vaadin.ui;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.IQuestion;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.easyuploads.MultiFileUpload;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        table.addContainerProperty("ButtonShow",  Button.class, null);
        table.addContainerProperty("ButtonDelete",  Button.class, null);
        table.setWidth("100%");

        table.setColumnWidth("ButtonShow",150);
        table.setColumnWidth("ButtonDelete",150);
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.setColumnAlignment("ButtonShow", Table.Align.CENTER);
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
            Button showButton = new Button("show");
            showButton.addClickListener( e -> {
                this.getUI().addWindow(new DetailsUI(question));
            });

            Button deleteButton = new Button ("delete");
            deleteButton.addClickListener( e -> {
                this.getUI().addWindow(new DeleteUI(question));
            });

            table.addItem(new Object[]{
                    question.getQuestionID(),
                    showButton,
                    deleteButton
            }, itemID);
            itemID++;
        }
        table.setPageLength(table.size());

    }

    private class DetailsUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public DetailsUI(IQuestion<? extends AnswerStorage> question) {
            super("Question");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("400px");
            Label titleLabel = new Label("<b>"+question.getQuestionID()+"</b>", ContentMode.HTML);
            Label descrLabel = new Label(question.getQuestionText(), ContentMode.HTML);
            Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
            });

            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            gLayout.addComponent(close,0,2);

            gLayout.setRowExpandRatio(1,1);

            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }

    private class DeleteUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public DeleteUI(IQuestion<? extends AnswerStorage> question) {
            super("Delete");
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("800px");
            Label titleLabel = new Label(question.getQuestionID(), ContentMode.HTML);
            Label descrLabel = new Label("<b>The following command is destructive and cannot be undone!</b>", ContentMode.HTML);
            Button close = new Button("Cancel");
//            Button remove = new Button("Remove from this test");
            Button delete = new Button("Delete permanently from disk");

            close.addClickListener( e -> {
                this.close();
            });

/*            remove.addClickListener( e -> {
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
            gLayout.addComponent(close,0,2);
//            gLayout.addComponent(remove,1,2);
            gLayout.addComponent(delete,1,2);

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


            Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
                manager.getEngine().resetQuestions();
                manager.loadQuestions();
            });

            gLayout.addComponent(titleLabel,0,0,1,0);
            gLayout.addComponent(descrLabel,0,1,1,1);
            gLayout.addComponent(multiFileUpload,0,2,1,2);
            gLayout.addComponent(close,0,3);

            gLayout.setRowExpandRatio(1,1);

            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }


}
