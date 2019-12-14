package at.jku.ce.adaptivetesting.views.test.accounting.misc;

import at.jku.ce.adaptivetesting.questions.accounting.util.AccountingDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

@Theme("vaadin")
public class AccountingCompanyDescription extends UI {

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Unternehmensbeschreibung");
        VerticalLayout vl = assembleCompanyDescription();
        vl.setMargin(true);
        vl.setSpacing(true);
        vl.setSizeUndefined();
        setContent(vl);
    }

    private VerticalLayout assembleCompanyDescription() {
        VerticalLayout layout = new VerticalLayout();

        Label initSit = new Label("<table>" +
                "<tbody>" +
                "<caption style=\"font-size:25px\"><strong>Ausgangssituation</strong></caption>" +
                "<tr>" +
                "<td colspan=\\\"4\\\"><strong><br>Sie sind als selbständiger " +
                "Steuerberater und Buchhalter tätig.<br>" +
                "Zu deinen Kunden gehören die unten angeführten Unternehmen.<br>" +
                "Für diese übernehmen Sie die Buchhaltung, d.h. Sie verbuchen die " +
                "angeführten Geschäftsfälle aus deren Sicht.</strong></td>" +
                "</tr>" +
                "</tbody>" +
                "</table>", ContentMode.HTML);
        layout.addComponent(initSit);

        Label contentTable = new Label("<table>" +
                "<tbody>" +
                "<tr>" +
                "<td colspan=\"4\"><strong><br>Unternehmensbeschreibungen</strong></td>" +
                "</tr>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<tr>" +
                "<td>Firmenname:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td><strong>World of Tabs GmbH</strong></td>" +
                "<td><strong>Restaurant Sommer</strong></td>" +
                "<td><strong>ATM GmbH</strong></td>" +
                "</tr>" +
                "<tr>" +
                "<td>Adresse:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>Unterfeld 15</td>" +
                "<td>Am Berg 5</td>" +
                "<td>Altenbergstr. 7</td>" +
                "</tr>" +
                "<tr>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>4541 Adlwang</td>" +
                "<td>4452 Ternberg</td>" +
                "<td>4040 Linz</td>" +
                "</tr>" +
                "<tr>" +
                "<td>E-Mail:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>office@worldtabs.at</td>" +
                "<td>office@summer.at</td>" +
                "<td>office@atm.at</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Internet:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>www.worldtabs.at</td>" +
                "<td>www.sommer.at</td>" +
                "<td>www.atm.at</td>" +
                "</tr>" +
                "<tr>" +
                "<td>UID-Nummer:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>ATU32589716</td>" +
                "<td>ATU89716325</td>" +
                "<td>ATU58932761</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Unternehmens-<br>gegenstand:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>Handel mit Tablets<br>und Zubehör</td>" +
                "<td>Restaurant mit<br>regionaler Küche</td>" +
                "<td>Metallbearbeitung: Bohren,<br>Fräsen, Stanzen, Lasern, …</td>" +
                "</tr>" +
                "<tr>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "</tr>" +
                "<tr>" +
                "<td colspan=\"4\"><strong><br>Hinweise zu den laufenden " +
                "und den Um- und Nachbuchungen</strong></td>" +
                "</tr>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<tr>" +
                "<td>Buchung:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">Sofern nichts Anderes angeführt ist, wird " +
                "erfolgsorientiert und nicht <br> bestandsorientiert gebucht. " +
                "D.h., sofern nichts anderes angeführt ist, wird<br>darauf " +
                "abgezielt, den Gewinn niedrig zu halten, um Steuerabgaben " +
                "gering<br>zu halten.</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Abschlussjahr:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">2016</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Abschreibung:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">Halbjahres- bzw. Ganzjahresabschreibung</td>" +
                "</tr><tr>" +
                "<td>Skonto:</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">Wird stets in Anspruch genommen.</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Zeitliche:<br> Abgrenzung</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">Die Abgrenzung erfolgt am Jahresende im Rahmen " +
                "der Um- und<br>Nachbuchungen.</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Geringwertig <br> Wirtschaftsgüter</td>" +
                "<td>&emsp;&emsp;&emsp;</td>" +
                "<td colspan=\"3\">Sofern nichts Anderes angeführt ist, wird für " +
                "geringwertige <br> Wirtschaftsgüter mit Anschaffungs- bzw. " +
                "Herstellungskosten bis 400,00<br>die Bewertungsfreiheit nach " +
                "§ 13 EStG (Sofortabschreibung) in Anspruch<br>genommen.</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>", ContentMode.HTML);
        layout.addComponent(contentTable);
        return layout;
    }
}