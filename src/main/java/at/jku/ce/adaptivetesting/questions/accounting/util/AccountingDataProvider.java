package at.jku.ce.adaptivetesting.questions.accounting.util;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Table;

public class AccountingDataProvider {

	private final List<String[]> data = new ArrayList<>();
	private static AccountingDataProvider dataProvider = null;

	public static AccountingDataProvider getInstance() {
		if (dataProvider == null) {
			dataProvider = new AccountingDataProvider();
		}
		return dataProvider;
	}

	private AccountingDataProvider() {
		data.add(new String[] { "0110", "Patente und Lizenzen" });
		data.add(new String[] { "0120", "Datenverarbeitungsprogramme" });
		data.add(new String[] { "0180", "Geleistete Anzahlungen für immaterielle Vermögensgegenstände" });
		data.add(new String[] { "0200", "unbebaute Grundstücke " });
		data.add(new String[] { "0210", "Bebaute Grundstücke" });
		data.add(new String[] { "0300", "Gebäude" });
		data.add(new String[] { "0400", "Maschinen" });
		data.add(new String[] { "0480", "Geringwertige Maschinen" });
		data.add(new String[] { "0500", "Werkzeuge" });
		data.add(new String[] { "0550", "Geringwertige Werkzeuge" });
		data.add(new String[] { "0620", "Büromaschinen, EDV-Anlagen" });
		data.add(new String[] { "0640", "LKW" });
		data.add(new String[] { "0660", "Betriebs- und Geschäftsausstattung" });
		data.add(new String[] { "0680", "GWG Büromaschinen" });
		data.add(new String[] { "0710", "Anlagen in Bau" });
		data.add(new String[] { "0800", "Anteile an verbundene Unternehmen" });
		data.add(new String[] { "0920", "Festverzinsliche Wertpapiere AV" });
		data.add(new String[] { "1000", "Bezugsverrechnung" });
		data.add(new String[] { "1100", "Rohstoffvorrat" });
		data.add(new String[] { "1200", "Vorrat bezogene Teile" });
		data.add(new String[] { "1210", "Weinvorrat" });
		data.add(new String[] { "1220", "Sekt- und Schaumweinvorrat" });
		data.add(new String[] { "1230", "Spirituosen- und Likörvorrat" });
		data.add(new String[] { "1240", "Sonstige alkohol. Getränke" });
		data.add(new String[] { "1250", "Speisevorrat" });
		data.add(new String[] { "1250", "Vorrat Ersatzteile" });
		data.add(new String[] { "1260", "Vorrat Kaffee, Tee" });
		data.add(new String[] { "1270", "Vorrat alkoholfreie Getränke" });
		data.add(new String[] { "1300", "Vorrat Hilfsstoffe" });
		data.add(new String[] { "1340", "Vorrat Verpackungsmaterial" });
		data.add(new String[] { "1350", "Vorrat Betriebsstoffe" });
		data.add(new String[] { "1360", "Vorrat Heizöle" });
		data.add(new String[] { "1364", "Vorrat feste Brennstoffe" });
		data.add(new String[] { "1365", "Vorrat Schmiermittel" });
		data.add(new String[] { "1370", "Vorrat Reinigungsmaterial" });
		data.add(new String[] { "1390", "Vorrat Büromaterial" });
		data.add(new String[] { "1400", "Unfertige Erzeugnisse" });
		data.add(new String[] { "1500", "Fertige Erzeugnisse" });
		data.add(new String[] { "1600", "Handelswarenvorrat" });
		data.add(new String[] { "2000", "Schnelzer GmbH" });
		data.add(new String[] { "2000", "Lieferforderungen" });
		data.add(new String[] { "2000", "Red Zac" });
		data.add(new String[] { "20001", "Sporthotel Pircher" });
		data.add(new String[] { "2001", "Point GmbH" });
		data.add(new String[] { "2002", "Aichinger Maria" });
		data.add(new String[] { "20025", "Ungerböck" });
		data.add(new String[] { "2003", "Gruber Gerlinde" });
		data.add(new String[] { "2004", "Gruber Franz" });
		data.add(new String[] { "2005", "Sturm Peter" });
		data.add(new String[] { "2006", "Hollaus Horst" });
		data.add(new String[] { "2007", "Steithofer Klaus" });
		data.add(new String[] { "2009", "Ungerböck Urs" });
		data.add(new String[] { "20145", "Neumeister AG" });
		data.add(new String[] { "2050", "Losungsverrechnung" });
		data.add(new String[] { "2080", "Einzelwertberichtigungen zu Forderungen" });
		data.add(new String[] { "2090", "Pauschalwertberichtigungen zu Forderungen" });
		data.add(new String[] { "2300", "Sonstige Forderungen" });
		data.add(new String[] { "2320", "Gegebene Darlehen" });
		data.add(new String[] { "2380", "Guthaben bei Lieferanten" });
		data.add(new String[] { "2400", "Lohn- und Gehaltsvorschüsse" });
		data.add(new String[] { "2410", "Darlehen an Dienstnehmer" });
		data.add(new String[] { "2500", "Vorsteuer" });
		data.add(new String[] { "2700", "Kassa" });
		data.add(new String[] { "2790", "Forderungen Kreditkartenunternehmen" });
		data.add(new String[] { "2794", "Forderungen Bankomatkarten" });
		data.add(new String[] { "2795", "Forderungen Quick" });
		data.add(new String[] { "2800", "Bank" });
		data.add(new String[] { "2810", "PSK" });
		data.add(new String[] { "2870", "Barverkehr mit Banken" });
		data.add(new String[] { "2880", "Schwebende Geldbewegungen" });
		data.add(new String[] { "2900", "Aktive Rechnungsabgrenzung" });
		data.add(new String[] { "2900", "Aktive Rechnungsabgrenzung (ARA)" });
		data.add(new String[] { "3000", "Rückstellungen Abfertigung" });
		data.add(new String[] { "3030", "Rückstellungen für Körperschaftsteuer" });
		data.add(new String[] { "3040", "Rückstellung Rechts- und Steuerberatungsaufwand" });
		data.add(new String[] { "3045", "Rückstellung Prozesskosten" });
		data.add(new String[] { "3090", "Sonstige Rückstellungen" });
		data.add(new String[] { "3110", "Bank (Bankschuld, Verbindlichkeiten gegenüber Kreditinstituten)" });
		data.add(new String[] { "3110", "Bankschuld" });
		data.add(new String[] { "3150", "Darlehen (von einer Bank)" });
		data.add(new String[] { "3160", "Verbindlichkeiten Darlehenszinsen" });
		data.add(new String[] { "3170", "Barverkehr mit Banken" });
		data.add(new String[] { "3180", "Verbindlichkeiten Kreditkartenunternehmen" });
		data.add(new String[] { "3190", "Verbindlichkeiten Bankomatkarten" });
		data.add(new String[] { "3300", "Lieferverbindlichkeiten" });
		data.add(new String[] { "33001", "Brau Union Österreich AG" });
		data.add(new String[] { "33002", "Barkley KG" });
		data.add(new String[] { "33004", "Elektrohändler Lett A.S. " });
		data.add(new String[] { "33005", "Domain Star GmbH" });
		data.add(new String[] { "3301", "AAtech GmbH" });
		data.add(new String[] { "33010", "Leuchti GmbH" });
		data.add(new String[] { "33012", "Bösch AG" });
		data.add(new String[] { "33033", "Plomeria KG" });
		data.add(new String[] { "33041", "JETech GmbH " });
		data.add(new String[] { "3305", "Obsthuber" });
		data.add(new String[] { "3306", "Wäschefein" });
		data.add(new String[] { "33067", "Medion Austria GmbH" });
		data.add(new String[] { "3307", "Bäckerei Kornspitz" });
		data.add(new String[] { "33091", "Lagerfelix " });
		data.add(new String[] { "33097", "Constructora GmbH" });
		data.add(new String[] { "33200", "1byone LLC" });
		data.add(new String[] { "33568", "Dr. Feind" });
		data.add(new String[] { "3370", "Interimskonto geleistete Anzahlung" });
		data.add(new String[] { "33999", "Polt" });
		data.add(new String[] { "3500", "Umsatzsteuer" });
		data.add(new String[] { "3520", "USt-Zahllast" });
		data.add(new String[] { "3520", "Umsatzsteuer-Zahllast" });
		data.add(new String[] { "3540", "Verbindlichkeiten FA" });
		data.add(new String[] { "3540", "Verbindlichkeiten Finanzamt" });
		data.add(new String[] { "3600", "Verbindlichkeiten KK" });
		data.add(new String[] { "3600", "Verbindlichkeiten Krankenkasse" });
		data.add(new String[] { "3610", "Verbindlichkeiten Gemeinde" });
		data.add(new String[] { "3700", "Verbindlichkeiten Darlehen" });
		data.add(new String[] { "3750", "Verbindlichkeiten gegenüber Kunden" });
		data.add(new String[] { "3800", "Sonstige Verbindlichkeiten" });
		data.add(new String[] { "3850", "Verbindlichkeit Mitarbeiter" });
		data.add(new String[] { "3850", "Verbindlichkeiten Mitarbeiter" });
		data.add(new String[] { "3900", "Passive Rechnungsabgrenzung" });
		data.add(new String[] { "4000", "Handelswarenerlöse" });
		data.add(new String[] { "4001", "Speisenerlöse" });
		data.add(new String[] { "4030", "Erlöse aus i.g. Lieferungen" });
		data.add(new String[] { "4100", "Biererlöse" });
		data.add(new String[] { "4110", "Weinerlöse" });
		data.add(new String[] { "4120", "Schaumweinerlöse" });
		data.add(new String[] { "4130", "Spirituosenerlöse" });
		data.add(new String[] { "4140", "Erlöse alkoholfreie Getränke" });
		data.add(new String[] { "4150", "Speiseeiserlöse" });
		data.add(new String[] { "4160", "Kaffee-, Tee-Erlöse" });
		data.add(new String[] { "4380", "sonstige Nebenerlöse" });
		data.add(new String[] { "4400", "Erlösberichtigung" });
		data.add(new String[] { "4410", "Kundenskonti" });
		data.add(new String[] { "4500", "Bestandsveränderung" });
		data.add(new String[] { "4580", "Aktivierte Eigenleistungen" });
		data.add(new String[] { "4600", "Erlöse aus dem Abgang von Anlagen " });
		data.add(new String[] { "4610", "Versicherungsentschädigung" });
		data.add(new String[] { "4660", "Erträge aus Zuschreibung AV" });
		data.add(new String[] { "4700", "Erträge aus Auflösung von Rückstellungen" });
		data.add(new String[] { "4700", "Erträge aus der Auflösung von Rückstellungen " });
		data.add(new String[] { "4801", "Peronsalverpflegungserlöse Speisen" });
		data.add(new String[] { "4802", "Personalverpflegungserlöse Getränke" });
		data.add(new String[] { "4810", "Mieterträge" });
		data.add(new String[] { "4812", "andere Personalverpflegungserlöse" });
		data.add(new String[] { "4820", "Provisionserträge" });
		data.add(new String[] { "4850", "Erträge aus Konventionalstrafen" });
		data.add(new String[] { "4870", "Erträge aus Auflösung von WB zu Forderungen" });
		data.add(new String[] { "4870", "Erträge aus der Auflösung von Wertberichtigungen zu Forderungen" });
		data.add(new String[] { "4880", "übrige betriebliche Erträge" });
		data.add(new String[] { "4890", "Mahnspesenvergütung" });
		data.add(new String[] { "4901", "Eigenverbrauch 10 %" });
		data.add(new String[] { "4902", "Eigenverbrauch 20 %" });
		data.add(new String[] { "5000", "Bezugsverrechnung" });
		data.add(new String[] { "5010", "Handelswareneinsatz" });
		data.add(new String[] { "5100", "Rohstoffverbrauch (-einsatz)" });
		data.add(new String[] { "5100", "Wareneinsatz Lebensmittel" });
		data.add(new String[] { "5200", "Verbrauch von bezogenen Fertig- und Einzelteilen" });
		data.add(new String[] { "5200", "Wareneinsatz Bier" });
		data.add(new String[] { "5210", "Wareneinsatz Wein" });
		data.add(new String[] { "5220", "Wareneinsatz Sekt- und Schaumweine" });
		data.add(new String[] { "5230", "Wareneinsatz Spirituosen und Liköre" });
		data.add(new String[] { "5240", "Wareneinsatz sonstige alkoholische Getränke" });
		data.add(new String[] { "5260", "Wareneinsatz Kaffe, Tee, Kakao" });
		data.add(new String[] { "5270", "Wareneinsatz alkoholfreie Getränke" });
		data.add(new String[] { "5300", "Hilfsstoffverbrauch" });
		data.add(new String[] { "5340", "Verpackungsmaterialverbrauch" });
		data.add(new String[] { "5400", "Betriebsstoffverbrauch" });
		data.add(new String[] { "5410", "Schmiermittelverbrauch" });
		data.add(new String[] { "5420", "Reparaturmaterialverbrauch" });
		data.add(new String[] { "5450", "Reinigungsmaterialverbrauch" });
		data.add(new String[] { "5500", "Verbrauch von Werkzeugen, Erzeugungshilfsmitteln" });
		data.add(new String[] { "5510", "Ersatzteileverbrauch" });
		data.add(new String[] { "5550", "Leerguteinsatz (Emballagen)" });
		data.add(new String[] { "5600", "Heizölverbrauch" });
		data.add(new String[] { "5610", "Treibstoffverbrauch" });
		data.add(new String[] { "5620", "Gasverbrauch" });
		data.add(new String[] { "5630", "Stromverbrauch" });
		data.add(new String[] { "5640", "Heizmaterialverbrauch (feste Brennstoffe)" });
		data.add(new String[] { "5880", "Lieferantenskonti Wareneinkauf" });
		data.add(new String[] { "5880", "Lieferantenskonti" });
		data.add(new String[] { "5881", "Lieferantenskonti Anlagenkäufe" });
		data.add(new String[] { "5890", "Umsatzbonus auf Wareneinkauf" });
		data.add(new String[] { "5910", "Wareneinsatz Personalverpflegung" });
		data.add(new String[] { "6000", "Löhne" });
		data.add(new String[] { "6010", "Hilfslöhne" });
		data.add(new String[] { "6100", "Lehrlingsentschädigungen Arbeiter" });
		data.add(new String[] { "6200", "Gehälter" });
		data.add(new String[] { "6300", "Lehrlingsentschädigungen Angestellter" });
		data.add(new String[] { "6500", "Gesetzlicher Sozialaufwand Arbeiter" });
		data.add(new String[] { "6560", "Gesetzlicher Sozialaufwand Angestellter" });
		data.add(new String[] { "6600", "Dienstgeberbeitrag Arbeiter" });
		data.add(new String[] { "6610", "Zuschlag DB Arbeiter" });
		data.add(new String[] { "6620", "Kommunalsteuer Arbeiter" });
		data.add(new String[] { "6630", "Wiener Dienstgeberabgabe Arbeiter" });
		data.add(new String[] { "6660", "Dienstgeberbeitrag Angestellte" });
		data.add(new String[] { "6670", "Zuschlag zum DB Angestellte" });
		data.add(new String[] { "6680", "Kommunalsteuer Angestellte" });
		data.add(new String[] { "6690", "Wiener Dienstgeberabgabe Angestellte" });
		data.add(new String[] { "6700", "Freiwilliger Sozialaufwand" });
		data.add(new String[] { "6710", "Sachbezug Personal" });
		data.add(new String[] { "6740", "Aufwand Dienstwohnung" });
		data.add(new String[] { "7010", "Abschreibung von Sachanlagen" });
		data.add(new String[] { "7030", "Abschreibung GWG" });
		data.add(new String[] { "7040", "Geringwertige Wirtschaftsgüter" });
		data.add(new String[] { "7050", "außerplanmäßige Abschreibung" });
		data.add(new String[] { "7100", "Grundsteuer" });
		data.add(new String[] { "7150", "Tourismusabgabe (Interessentenbeitrag)" });
		data.add(new String[] { "7180", "Gebühren" });
		data.add(new String[] { "7190", "Sonstige Abgaben" });
		data.add(new String[] { "7200", "Instandhaltung durch Dritte" });
		data.add(new String[] { "7201", "Instandhaltung Gebäude" });
		data.add(new String[] { "7210", "Reinigung durch Dritte" });
		data.add(new String[] { "7220", "Entsorgungsaufwand" });
		data.add(new String[] { "7300", "Ausgangsfrachten" });
		data.add(new String[] { "7310", "Paketgebühren" });
		data.add(new String[] { "7320", "PKW- Kombi Betriebsaufwand" });
		data.add(new String[] { "7320", "PKW-Betriebsaufwand" });
		data.add(new String[] { "7321", "Motorbezogene Versicherungssteuer Pkw und Kombis" });
		data.add(new String[] { "7325", "Versicherungsaufwand Pkw und Kombis" });
		data.add(new String[] { "7326", "Parkgebühren, Straßenmaut Pkw und Kombis" });
		data.add(new String[] { "7330", "Lkw Betriebsaufwand" });
		data.add(new String[] { "7331", "Motorbezogene Versicherungssteuer Lkw" });
		data.add(new String[] { "7332", "Kraftfahrzeugsteuer Lkw" });
		data.add(new String[] { "7335", "Versicherungsaufwand Lkw" });
		data.add(new String[] { "7336", "Parkgebühren, Straßenmaut Lkw" });
		data.add(new String[] { "7380", "Telefongebühren" });
		data.add(new String[] { "7381", "Internetgebühr" });
		data.add(new String[] { "7390", "Portogebühren" });
		data.add(new String[] { "7400", "Mietaufwand" });
		data.add(new String[] { "7540", "Provision an Dritte" });
		data.add(new String[] { "7600", "Büromaterialverbrauch" });
		data.add(new String[] { "7630", "Fachliteratur" });
		data.add(new String[] { "7650", "Werbeaufwand" });
		data.add(new String[] { "7690", "Spenden und Trinkgelder" });
		data.add(new String[] { "7700", "Versicherungsaufwand" });
		data.add(new String[] { "7740", "Versicherungsbeiträge an die Sozialversicherungsanstalt der gewerblichen Wirtschaft" });
		data.add(new String[] { "7750", "Rechts- und Beratungsaufwand" });
		data.add(new String[] { "7755", "Steuerberatungsaufwand" });
		data.add(new String[] { "7780", "Kammerumlage" });
		data.add(new String[] { "7780", "Sonstige Aufwände " });
		data.add(new String[] { "7790", "Spesen des Geldverkehrs" });
		data.add(new String[] { "7791", "Sonstige Bankspesen" });
		data.add(new String[] { "7792", "Provisionen, Gebühren, Kredit-, Bankomatkarten und Quick" });
		data.add(new String[] { "7800", "Abschreibung Vorräte" });
		data.add(new String[] { "7800", "Schwund" });
		data.add(new String[] { "7802", "Abschreibung Forderungen" });
		data.add(new String[] { "7802", "Abschreibung von Forderungen" });
		data.add(new String[] { "7805", "Zuweisung zu WB zu Forderung" });
		data.add(new String[] { "7805", "Zuweisung an Wertberichtigung von Forderungen" });
		data.add(new String[] { "7811", "Konventionalstrafen" });
		data.add(new String[] { "7815", "Fremdwährungskursverluste" });
		data.add(new String[] { "7819", "Sonstige Schadensfälle" });
		data.add(new String[] { "7819", "Schadensfälle" });
		data.add(new String[] { "7820", "Buchwert abgegangener Anlagen" });
		data.add(new String[] { "7850", "übrige betriebliche Aufwendungen" });
		data.add(new String[] { "7890", "Lieferantenskonti auf sonstige betriebliche Aufwendungen" });
		data.add(new String[] { "8100", "Zinserträge aus Bankguthaben" });
		data.add(new String[] { "8110", "Zinserträge aus gew. Darlehen" });
		data.add(new String[] { "8110", "Zinserträge aus gewährten Darlehen " });
		data.add(new String[] { "8130", "Verzugszinsenerträge" });
		data.add(new String[] { "8135", "sonstige Zinserträge" });
		data.add(new String[] { "8280", "Zinsaufwand für Bankkredite" });
		data.add(new String[] { "8285", "Sonstiger aufwand für Bankkredite (z.B. Bereitstellungsprovision, Überziehungsprovision)" });
		data.add(new String[] { "8290", "Zinsenaufwand für Darlehen" });
		data.add(new String[] { "8300", "Verzugsinsenaufwand" });
		data.add(new String[] { "8301", "Mahnspesen" });
		data.add(new String[] { "8310", "Zinsaufwand Lieferantenkredit" });
		data.add(new String[] { "8330", "sonstiger Aufwand für Fremdkapital" });
		data.add(new String[] { "8400", "Außerordentliche Erträge" });
		data.add(new String[] { "8450", "Außerordentliche Aufwendungen" });
		data.add(new String[] { "8500", "Körperschaftssteuer" });
		data.add(new String[] { "9000", "Eigenkapital" });
		data.add(new String[] { "9600", "Privat" });
		data.add(new String[] { "9610", "Privatsteuern" });
		data.add(new String[] { "9800", "Eröffnungsbilanzkonto (EBK)" });
		data.add(new String[] { "9850", "Schlussbilanzkonto (SBK)" });
		data.add(new String[] { "9890", "Gewinn- und Verlustkonto (GuV)" });
	}

	public Table toHtmlTable() {
		Table table = new Table();
		table.addContainerProperty("Kontonummer", String.class, null);
		table.addContainerProperty("Kontenname", String.class, null);
		for (int i = 0; i < data.size(); i++) {
			table.addItem(data.get(i), i + 1);
		}
		table.setPageLength(table.size());
		table.setSizeFull();
		return table;
	}

	public String[] getAllAccountNames() {
		return data.stream().map(array -> array[1])
				.collect(Collectors.toList()).toArray(new String[data.size()]);
	}

	public boolean containsString(String string) {
		return string == null
				|| string.length() == 0
				|| data.stream().map(e -> e[1]).filter(e -> e.equals(string))
				.count() > 0;
	}

	public boolean containsNumber(int number) {
		String intString = Integer.toString(number);
		if (intString.length() == 1) {
			intString = "0" + intString;
		}
		String finString = intString;
		return data.stream().map(e -> e[0].substring(0, 2))
				.filter(s -> finString.equals(s)).count() > 0;
	}
}
