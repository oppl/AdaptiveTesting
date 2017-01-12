package at.jku.ce.adaptivetesting.vaadin.ui;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.html.HtmlLabel;
import at.jku.ce.adaptivetesting.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.ui.core.VaadinUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by oppl on 12/01/2017.
 */
public class ResultView extends VerticalLayout implements View {

        QuestionManager manager;
        Table table;

        public ResultView(QuestionManager manager) {
            this.setMargin(true);
            this.setSpacing(true);
            this.addComponent(new HtmlLabel(HtmlUtils.center("h1",
                    "Test Results")));

            table = new Table("Available Results");
            table.addContainerProperty("ResultName", String.class, null);
            table.addContainerProperty("ResultDate", String.class, null);
            table.addContainerProperty("ButtonDownload",  Button.class, null);
            table.setWidth("100%");

            table.setColumnWidth("ResultDate",200);
            table.setColumnWidth("ButtonDownload",150);
            table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
            table.setColumnAlignment("ButtonDownload", Table.Align.CENTER);

            this.addComponent(table);
            this.manager = manager;
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            buildTable();
        }

        private void buildTable() {

            List<File> resultFiles = loadResults(new File(VaadinUI.Servlet.getResultFolderName()));

            table.removeAllItems();

            int itemID = 0;
            LogHelper.logInfo("Admin: "+resultFiles.size()+" questions available.");

            for (File result: resultFiles) {

                Button downloadButton = new Button ("download");
                downloadButton.addClickListener( e -> {
                });
                BasicFileAttributes attr = null;
                try {
                    attr = Files.readAttributes(result.toPath(), BasicFileAttributes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (attr == null) continue;
                table.addItem(new Object[]{
                        result.getName(),
                        attr.creationTime().toString(),
                        downloadButton
                }, itemID);
                itemID++;
            }
            table.setPageLength(table.size());

        }

    public List<File> loadResults(File containingFolder) {

        assert containingFolder.exists() && containingFolder.isDirectory();

        File[] files = containingFolder.listFiles(f -> f
                .isFile()
                && (f.canRead() || f.setReadable(true))
                && f.getName().endsWith(".csv"));

        // read all questions
        LogHelper.logInfo("Found "+files.length+" results");
        return Arrays.asList(files);
    }

}
