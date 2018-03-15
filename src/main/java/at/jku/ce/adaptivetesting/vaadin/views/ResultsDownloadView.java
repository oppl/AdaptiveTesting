package at.jku.ce.adaptivetesting.vaadin.views;

import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.core.html.HtmlUtils;
import at.jku.ce.adaptivetesting.vaadin.views.def.DefaultView;
import at.jku.ce.adaptivetesting.vaadin.views.test.TestView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by oppl on 12/01/2017.
 */
public class ResultsDownloadView extends VerticalLayout implements View {

        private TestView manager;
        private Table table;

        public ResultsDownloadView(TestView manager) {
            this.setMargin(true);
            this.setSpacing(true);
            this.addComponent(new HtmlLabel(HtmlUtils.center("h1",
                    "Test Results")));

            Button zipDownload = new Button("Download ZIP-File");
            zipDownload.addClickListener( e -> {
                this.getUI().addWindow(new ZIPFileUI());
            });
            this.addComponent(zipDownload);
            this.manager = manager;
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            buildTable();
        }

        private void buildTable() {

            List<File> resultFiles = loadResults(new File(DefaultView.Servlet.getResultFolderName()));
            if (table != null) this.removeComponent(table);

            table = new Table("Available Results");
            table.addContainerProperty("ResultName", String.class, null);
            table.addContainerProperty("ResultDate", String.class, null);
            table.addContainerProperty("ButtonDownload",  Button.class, null);
            table.addContainerProperty("SortDate",Long.class,null);
            table.setSortContainerPropertyId("SortDate");
            table.setSortAscending(false);
            table.setWidth("100%");

            table.setColumnWidth("ResultDate",200);
            table.setColumnWidth("ButtonDownload",150);
            table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
            table.setColumnAlignment("ButtonDownload", Table.Align.CENTER);

            int itemID = 0;
            LogHelper.logInfo("ResultsDownloadView: " + resultFiles.size() + " results available.");

            for (File result: resultFiles) {

                FileDownloader fd = new FileDownloader(new FileResource(result));
                Button downloadButton = new Button ("download");

                fd.extend(downloadButton);

                BasicFileAttributes attr = null;
                try {
                    attr = Files.readAttributes(result.toPath(), BasicFileAttributes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (attr == null) continue;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                LocalDateTime creationTime = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.of("GMT+1"));
                table.addItem(new Object[]{
                        result.getName(),
                        dtf.format(creationTime),
                        downloadButton,
                        new Long(attr.lastModifiedTime().toMillis())
                }, itemID);
                LogHelper.logInfo("ResultsDownloadView: added "+result.getName());
                itemID++;
            }
            table.sort();
            table.setVisibleColumns(new String[] { "ResultName", "ResultDate", "ButtonDownload"});
            table.setPageLength(table.size());
            this.addComponent(table);
        }

    public List<File> loadResults(File containingFolder) {

        assert containingFolder.exists() && containingFolder.isDirectory();

        File[] files = containingFolder.listFiles(f -> f
                .isFile()
                && (f.canRead() || f.setReadable(true))
                && f.getName().endsWith(".csv"));

        // read all questions
        LogHelper.logInfo("Found " + files.length + " results");
        return Arrays.asList(files);
    }

    private class ZIPFileUI extends Window {

        GridLayout gLayout = new GridLayout(2,3);

        public ZIPFileUI() {
            super("Download ZIP-File");

            List<File> resultFiles = loadResults(new File(DefaultView.Servlet.getResultFolderName()));

            Date from = null;
            Date to = null;

            for (File result: resultFiles) {

                BasicFileAttributes attr = null;
                try {
                    attr = Files.readAttributes(result.toPath(), BasicFileAttributes.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (attr == null) continue;
                Date currentFileDate = new Date(attr.lastModifiedTime().toMillis());
                if (from == null) from = currentFileDate;
                if (to == null) to = currentFileDate;
                if (from.after(currentFileDate)) from = currentFileDate;
                if (!to.after(currentFileDate)) to = currentFileDate;
            }
            from.setTime(from.getTime()-1000);
            this.center();
            gLayout.setWidth("100%");
            this.setWidth("600px");
            Label titleLabel = new Label("<b>Please select date range:</b>", ContentMode.HTML);

            PopupDateField fromDate = new PopupDateField();
            fromDate.setResolution(Resolution.MINUTE);
            fromDate.setDateFormat("dd.MM.yyyy HH:mm");
            fromDate.setValue(from);
            PopupDateField toDate = new PopupDateField();
            toDate.setResolution(Resolution.MINUTE);
            toDate.setDateFormat("dd.MM.yyyy HH:mm");
            toDate.setValue(to);

            Button close = new Button("Close");

            close.addClickListener( e -> {
                this.close();
            });

            Button download = new Button("Download");

            StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    List<File> requestedFiles = new LinkedList<>();
                    for (File result: resultFiles) {

                        BasicFileAttributes attr = null;
                        try {
                            attr = Files.readAttributes(result.toPath(), BasicFileAttributes.class);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (attr == null) continue;
                        Date currentFileDate = new Date(attr.lastModifiedTime().toMillis());
                        if (currentFileDate.after(fromDate.getValue()) && !currentFileDate.after(toDate.getValue())) requestedFiles.add(result);
                    }
                    try {
                        BufferedInputStream origin = null;
                        final int BUFFER = 2048;
                        byte data[] = new byte[BUFFER];
                        File zipFile = new File(new File(DefaultView.Servlet.getResultFolderName()),"results.zip");
                        FileOutputStream dest = new FileOutputStream(zipFile);
                        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                        for (File f : requestedFiles) {
                            FileInputStream fi = new FileInputStream(f);
                            origin = new BufferedInputStream(fi, BUFFER);
                            ZipEntry entry = new ZipEntry(f.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                                out.flush();
                            }
                        }
                        origin.close();
                        out.flush();
                        out.close();
                        ByteArrayInputStream zipstream = new ByteArrayInputStream(FileUtils.readFileToByteArray(zipFile));
                        return zipstream;
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    return null;
                }
            }, "result.zip");
            FileDownloader fd = new FileDownloader(resource);
            fd.extend(download);

            gLayout.addComponent(titleLabel,0,0,1,0);

            gLayout.addComponent(fromDate,0,1);
            gLayout.addComponent(toDate,1,1);

            gLayout.addComponent(close,0,2);
            gLayout.addComponent(download,1,2);

            gLayout.setRowExpandRatio(1,1);

            gLayout.setMargin(true);
            gLayout.setSpacing(true);
            setContent(gLayout);
        }
    }
}
