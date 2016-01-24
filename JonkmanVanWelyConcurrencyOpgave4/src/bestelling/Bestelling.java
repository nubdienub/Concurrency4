package bestelling;

import akka.actor.ActorRef;
import stoelen.GroepStatus;

public class Bestelling {
	
	private ActorRef klant;
	private int vakNummer,rijNummer,klantNummer;
	private int[] stoelNummers;
	private BestelStatus status;
	private GroepStatus groep;
	private String redenWeigering = "";
	
	public Bestelling(int vakNummer, int rijNummer, int[] stoelNummers, GroepStatus groep,BestelStatus status){
		this.vakNummer = vakNummer;
		this.rijNummer = rijNummer;
		this.stoelNummers = stoelNummers;
		this.groep = groep;
		this.setStatus(status);
	}

	public int getRijNummer() {
		return rijNummer;
	}

	public int getVakNummer() {
		return vakNummer;
	}

	public int[] getStoelNummers() {
		return stoelNummers;
	}

	public GroepStatus getGroep() {
		return groep;
	}

	public BestelStatus getStatus() {
		return status;
	}

	public void setStatus(BestelStatus status) {
		this.status = status;
	}

	public int getKlantNummer() {
		return klantNummer;
	}

	public void setKlantNummer(int klantNummer) {
		this.klantNummer = klantNummer;
	}
	
	public String toString(){
		return "Vak nummer: " + vakNummer + " Rij nummer: " + rijNummer + " Stoel nummbers: "
				+ stoelNummers[0] + "," + stoelNummers[1] + "," + stoelNummers[2] + "," + stoelNummers[3] + " Groep: " + groep;
	}

	public String getRedenWeigering() {
		return redenWeigering;
	}

	public void setRedenWeigering(String redenWeigering) {
		this.redenWeigering = redenWeigering;
	}

	public ActorRef getKlant() {
		return klant;
	}

	public void setKlant(ActorRef klant) {
		this.klant = klant;
	}
}
