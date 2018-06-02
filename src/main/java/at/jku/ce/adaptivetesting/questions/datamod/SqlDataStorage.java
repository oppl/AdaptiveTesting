package at.jku.ce.adaptivetesting.questions.datamod;

import at.jku.ce.adaptivetesting.core.AnswerStorage;
import at.jku.ce.adaptivetesting.core.LogHelper;
import at.jku.ce.adaptivetesting.core.db.ConnectionProvider;
import at.jku.ce.adaptivetesting.core.html.HtmlLabel;
import at.jku.ce.adaptivetesting.vaadin.views.test.datamod.TableWindow;
import com.vaadin.ui.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Vector;

/**
 * Created by Peter
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class SqlDataStorage extends AnswerStorage {

    private static final long serialVersionUID = -8179746363246548567L;
    private String answer;

    @XmlElement(name = "answerQuery")
    private String answerQuery;

    @XmlElement(name = "table")
    private Vector<String> tables = new Vector<>();

    @XmlElement(name = "infoKeys")
    private String infoKeys;

    @XmlElement(name = "infoTop")
    private String infoTop;

    @XmlElement(name = "infoBottom")
    private String infoBottom;

    @XmlElement(name = "tries")
    private int tries;

    public static SqlDataStorage getEmptyDataStorage() {
        return new SqlDataStorage();
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public void setAnswer(String sql) {
        this.answer = ConnectionProvider.executeQuery(sql);
    }

    public void setAnswerQuery(String answerQuery) {
        this.answerQuery = answerQuery;
    }

    public String getAnswerQuery() {
        return answerQuery;
    }

    public String getInfoTop() {
        return infoTop + "<br><br>" + infoKeys;
    }

    public String getInfoBottom() {
        return infoBottom;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Query: <p>" + answerQuery + "</p>");
        buffer.append("Output: <p>" + answer + "</p>");
        return buffer.toString();
    }

    public void createTableWindows(Layout layout) {
        GridLayout southLayout = new GridLayout(tables.size()*2, 1);
        int counter = 0;
        HtmlLabel text = new HtmlLabel();
        text.setValue("<b><i>Tabelleninformationen:</i><b>");
        layout.addComponent(text);
        for (String table : tables) {
            final Button tableButton = new Button(table);
            tableButton.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 32642854872179636L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    LogHelper.logInfo("Opened " + table + " table window");
                    TableWindow tableWindow = new TableWindow(table);
                    tableWindow.setSizeUndefined();
                    tableWindow.addCloseListener(new Window.CloseListener() {
                        private static final long serialVersionUID = 7874342882437355680L;

                        @Override
                        public void windowClose(Window.CloseEvent e) {
                            event.getButton().setEnabled(true);
                        }
                    });
                    layout.getUI().addWindow(tableWindow);
                    event.getButton().setEnabled(false);
                }
            });
            southLayout.addComponent(tableButton, counter, 0);
            HtmlLabel spacer = new HtmlLabel();
            spacer.setValue("&ensp;");
            southLayout.addComponent(spacer, counter + 1, 0);
            counter = counter + 2;
        }
        layout.addComponent(southLayout);
    }
}