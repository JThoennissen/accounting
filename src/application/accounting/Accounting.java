package application.accounting;
import java.io.*;
import java.util.*;
import java.text.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

// Klasse fuer die Buchhaltung
public class Accounting {

    private static final Logger logger = Logger.getLogger(Accounting.class.getName());

	// Liste der Sparer
	private static ArrayList<Depositor> mitglieder;	

	public static void main(String args[]) throws FileNotFoundException{
	
        // Logger initialisieren
        try {
            boolean append = true;
            FileHandler fh = new FileHandler("./src/data/log.txt", append);
            fh.setFormatter(new Formatter() {
                public String format(LogRecord rec) {
                    StringBuffer buf = new StringBuffer(1000);
                    buf.append(new java.util.Date()).append(' ');
                    buf.append(rec.getLevel()).append(' ');
                    buf.append(formatMessage(rec)).append('\n');
                    return buf.toString();
                }
            });
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.severe("Datei kann nicht geschrieben werden.");
        }
        // Logger in Datei oder Konsole
        logger.setLevel(Level.ALL);
	
        String datei;
        double zinssatz;
        
        // .csv-Dateinamen und Zinssatz einlesen
        try {
            BufferedWriter bw;
            if(args.length != 0) {
                ArgParser parser = new ArgParser(args);
                datei = parser.getInputFilename();
                zinssatz = Double.parseDouble(parser.getNonOptions().replace(",","."));
                bw = new BufferedWriter(new FileWriter(parser.getOutputFilename()));
            } else {
                bw = new BufferedWriter(new OutputStreamWriter(System.out));
                Scanner sc = new Scanner(System.in);
                datei = sc.nextLine();
                zinssatz = Double.parseDouble(sc.nextLine());
                sc.close();
            }
            
            String baseName = "Accounting";
            File file = new File("./dist/data/lang/");
            ResourceBundle rb = null;
            try {
                URL[] urls = {file.toURI().toURL()};
                ClassLoader loader = new URLClassLoader(urls);
                rb = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            
            String readinput_msg = rb.getString("readinput_msg");
            logger.info(readinput_msg + ": " + datei);
            
            // Zinsen setzen und Liste erstellen
            Depositor.setzeZinsen(zinssatz);
            Accounting.mitglieder = new ArrayList<>();
            
            // NumberFormat wird gebraucht, damit , als Seperator eines Doubles erkannt wird
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            
            try(BufferedReader bf = new BufferedReader(new FileReader(datei))) {
                
                String s = "";
                while((s = bf.readLine()) != null) {
                    logger.info("gelesene Zeile: " + s);
                    // Ueberspringen von Kommentar- und Leerzeilen
                    if(s.length() != 0 && Character.isDigit(s.charAt(0))) {
                        // Informationen uber Sparer auslesen und in entsprechendem Objekt speichern
                        String tmp[] = s.split(";");
                        ArrayList<AccountingEntry> liste = new ArrayList<>();
                        Depositor mitglied = null;					
                        try {
                            // Nummer, Nachname, Vorname, Startguthaben
                            long start = (long) (format.parse(tmp[3]).doubleValue()*100);
                            if (start < 0) {
                                logger.warning("Der Kontostand ist negativ!");
                            }
                            mitglied = new Depositor(tmp[0], tmp[1], tmp[2], start, liste);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            logger.warning("Fehlerhafte Eingabe!");
                            System.exit(1);
                        } catch (ParseException e) {
                            logger.warning(tmp[3] + " konnte nicht geparsed werden.");
                        }
                        // Einzahlungen
                        for(int i = 4; i < tmp.length-1; i += 2) {
                            double betrag = 0.0;
                            try {
                                betrag = format.parse(tmp[i+1]).doubleValue();
                                if (betrag < 0) {
                                    logger.warning("Ein Betrag ist negativ!");
                                }
                            } catch (ParseException e) {
                                logger.warning(tmp[3] + " konnte nicht geparsed werden.");
                            }
                            mitglied.einzahlen(Integer.parseInt(tmp[i]), (long) (betrag*100));
                        }
                        // Sparer hinzufuegen
                        Accounting.mitglieder.add(mitglied);
                    } else {
                        bw.write(s+'\n');
                    }
                }
            } catch(IOException e) {
                logger.severe(datei + " existiert nicht");
            }
            // Listing2 ausgeben
            for (Depositor mitglied : Accounting.mitglieder) {
                bw.write(mitglied.toString()+'\n');
            }
            bw.flush();
        } catch (IOException e) {
            logger.severe("Ausgabe fehlgeschlagen");
        }
	}
}
