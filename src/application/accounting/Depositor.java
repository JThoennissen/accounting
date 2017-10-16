package application.accounting;
import java.util.List;

// Klasse fuer einen Sparer
public class Depositor {
	
	private String nummer;
	private String nachname;
	private String vorname;
	private long startguthaben;
	private List<AccountingEntry> einzahlungen;
	private static double zinssatz;

	// Konstruktor
	public Depositor(String nummer, String nachname, String vorname, long startguthaben, List<AccountingEntry> einzahlungen) {
		this.nummer = nummer;
		this.nachname = nachname;
		this.vorname = vorname;
		this.startguthaben = startguthaben;
		this.einzahlungen = einzahlungen;
	}

	// Einzahlung taetigen
	public void einzahlen(int tag, long betrag) {
		this.einzahlungen.add(new AccountingEntry(tag, betrag));
	}

	// Guthaben des Sparers am Ende des Jahres inkl. Verzinsung
	public double berechneGuthaben(double zinssatz) {
		double guthaben = (double) this.startguthaben;
		guthaben += guthaben*Depositor.zinssatz/100.;
		for(AccountingEntry eintrag : einzahlungen) {
			guthaben += eintrag.betrag*((Depositor.zinssatz/100.)*((360-eintrag.tag)/360.)+1);
		}
		return guthaben/100;
	}

	// Zinsen fuer alle Sparer setzen
	public static void setzeZinsen(double zinssatz) {
		Depositor.zinssatz = zinssatz;
	}

	// String-Darstellung eines Sparers
	public String toString() {
		double guthaben = this.berechneGuthaben(Depositor.zinssatz);
		String s = String.format("%s;%s;%s;%.2f", this.nummer, this.nachname, this.vorname, guthaben);
		return s;
	}
}

