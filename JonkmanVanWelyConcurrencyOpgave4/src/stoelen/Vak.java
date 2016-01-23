package stoelen;
import java.util.ArrayList;
import java.util.Arrays;

public class Vak {
	
	private int nummer;

	private ArrayList<ArrayList<Stoel>> rijenEnStoelen = new ArrayList<ArrayList<Stoel>>();
	
	public Vak(int nummer,int rijGrootte, int stoelenPerRij){
		this.nummer = nummer;
		
		for (int i = 0; i < rijGrootte; i++) {
			ArrayList<Stoel> stoelen = new ArrayList<Stoel>();
			
			for (int j = 0; j < stoelenPerRij; j++) {
				Stoel s = new Stoel(i);
				stoelen.add(s);
			}
			
			rijenEnStoelen.add(stoelen);
		}	
	}

	public int getNummer() {
		return nummer;
	}
	
	public ArrayList<ArrayList<Stoel>> getRijenEnStoelen(){
		return rijenEnStoelen;
	}
	
	public boolean plaatsBeschikbaar(int rijNummer, int stoelNummer){
		try {
			ArrayList<Stoel> stoelen = rijenEnStoelen.get(rijNummer -1);
			if (stoelen.get(stoelNummer -1).getStatus() == StoelStatus.VRIJ) {	
				return true;
			}
			return false;
			
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean checkCorrecteGebruiker(int klantNummer,int rijNummer,int stoelNummer){
		try {		
			ArrayList<Stoel> stoelen = rijenEnStoelen.get(rijNummer -1);		
			if (stoelen.get(stoelNummer -1).getKlantNummer() == klantNummer) {	
				return true;
			}
			return false;
			
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public void reserveren(int klantNummer,int[] stoelNummers, int rijNummer){
		ArrayList<Stoel> stoelen = rijenEnStoelen.get(rijNummer -1);
		for (int i = 0; i < stoelNummers.length; i++) {
			stoelen.get(stoelNummers[i] - 1).setStatus(StoelStatus.GERESERVEERD);
			stoelen.get(stoelNummers[i] - 1).setKlantNummer(klantNummer);
		}
	}
	
	public void annuleren(int klantNummer,int[] stoelNummers, int rijNummer){
		ArrayList<Stoel> stoelen = rijenEnStoelen.get(rijNummer -1);
		for (int i = 0; i < stoelNummers.length; i++) {
			stoelen.get(stoelNummers[i] - 1).setStatus(StoelStatus.VRIJ);
			stoelen.get(stoelNummers[i] - 1).setKlantNummer(0);
		}
	}
	
	public void betalen(int klantNummer,int[] stoelNummers, int rijNummer){
		ArrayList<Stoel> stoelen = rijenEnStoelen.get(rijNummer -1);
		for (int i = 0; i < stoelNummers.length; i++) {
			stoelen.get(stoelNummers[i] - 1).setStatus(StoelStatus.BETAALD);
			stoelen.get(stoelNummers[i] - 1).setKlantNummer(klantNummer);
		}
	}
	



	
	
	
}
