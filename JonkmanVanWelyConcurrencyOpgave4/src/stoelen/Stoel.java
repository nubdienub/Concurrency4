package stoelen;

public class Stoel {
	private int stoelNummer;
	private int klantNummer;
	private StoelStatus status;
	
	public Stoel(int stoelNummer){
		this.status = StoelStatus.VRIJ;
		setStoelNummer(stoelNummer);
	}
	
	public int getStoelNummer() {
		return stoelNummer;
	}
	public void setStoelNummer(int stoelNummer) {
		this.stoelNummer = stoelNummer;
	}
	public int getKlantNummer() {
		return klantNummer;
	}
	public void setKlantNummer(int klantNummer) {
		this.klantNummer = klantNummer;
	}
	public StoelStatus getStatus() {
		return status;
	}
	public void setStatus(StoelStatus status) {
		this.status = status;
	}
	
	
}
