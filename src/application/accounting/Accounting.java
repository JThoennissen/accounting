package application.accounting;
import java.io.*;
import java.util.*;
import java.text.*;

// Klasse fuer die Buchhaltung
public class Accounting {

	// Liste der Sparer
	private static ArrayList<Depositor> mitglieder;	

	// Ausgabe von Listing 2
	public static void printDepositors() {
		for (Depositor mitglied : Accounting.mitglieder) {
			System.out.println(mitglied);
		}
	}

	public static void main(String args[]) throws FileNotFoundException{
	
		// .csv-Dateinamen und Zinssatz einlesen
		Scanner sc = new Scanner(new File(args[0]));
		String datei = sc.nextLine();
		double zinssatz = Double.parseDouble(sc.nextLine());
		sc.close();
		
		// Zinsen setzen und Liste erstellen
		Depositor.setzeZinsen(zinssatz);
		Accounting.mitglieder = new ArrayList<>();
		
		// NumberFormat wird gebraucht, damit , als Seperator eines Doubles erkannt wird
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		
		try(BufferedReader bf = new BufferedReader(new FileReader(datei))) {
			
			String s = "";
			while((s = bf.readLine()) != null) {
				// Ueberspringen von Kommentar- und Leerzeilen
				if(s.length() != 0 && Character.isDigit(s.charAt(0))) {
					// Informationen uber Sparer auslesen und in entsprechendem Objekt speichern
					String tmp[] = s.split(";");
					ArrayList<AccountingEntry> liste = new ArrayList<>();
					Depositor mitglied = null;					
					try {
						// Nummer, Nachname, Vorname, Startguthaben
						mitglied = new Depositor(tmp[0], tmp[1], tmp[2], (long) (format.parse(tmp[3]).doubleValue()*100), liste);
					} catch (ParseException e) {
						System.out.println(tmp[3] + "could not be parsed properly.");
					}
					// Einzahlungen
					for(int i = 4; i < tmp.length-1; i += 2) {
						double betrag = 0.0;
						try {
							betrag = format.parse(tmp[i+1]).doubleValue();
						} catch (ParseException e) {
							System.out.println(tmp[3] + "could not be parsed properly.");
						}
						mitglied.einzahlen(Integer.parseInt(tmp[i]), (long) (betrag*100));
					}
					// Sparer hinzufuegen
					Accounting.mitglieder.add(mitglied);
				} else {
					System.out.println(s);
				}
			}
		} catch(IOException e) {
			System.out.println(datei + " existiert nicht");
		}
		// Listing2 ausgeben
		Accounting.printDepositors();	
	}
}
