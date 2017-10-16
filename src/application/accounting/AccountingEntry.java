package application.accounting;

// Klasse fuer eine Einzahlung
public class AccountingEntry {

	protected int tag;
	protected long betrag;
	
	// Konstruktor
	public AccountingEntry(int tag, long betrag) {
		this.tag = tag;
		this.betrag = betrag;
	}
}

