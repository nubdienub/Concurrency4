import java.util.ArrayList;

public class Vak {
	
	private int nummer;

	private ArrayList<Status[]> rijenEnStoelen = new ArrayList<Status[]>();
	
	public Vak(int nummer,int rijGrootte, int stoelenPerRij){
		this.nummer = nummer;
		
		for (int i = 0; i < rijGrootte; i++) {
			Status[] arr = new Status[stoelenPerRij];
			rijenEnStoelen.add(arr);
		}
		
	}

	public int getNummer() {
		return nummer;
	}
	
	public ArrayList<Status[]> getRijenEnStoelen(){
		return rijenEnStoelen;
	}
	
	public boolean plaatsBeschikbaar(int rijNummer, int stoelNummer){
		
		Status[] stoelen = rijenEnStoelen.get(rijNummer -1);
		
		if (stoelen[stoelNummer -1] == Status.VRIJ) {
			return true;
		}
		
		return false;
	}
	
	public void reserveren(int stoelNummer, int rijNummer){
		Status[] rij = rijenEnStoelen.get(rijNummer);
		rij[stoelNummer] = Status.GERESERVEERD;
	}
	
	public void annuleren(int stoelNummer, int rijNummer){
		Status[] rij = rijenEnStoelen.get(rijNummer);
		rij[stoelNummer] = Status.VRIJ;
	}
	
	public void betalen(int stoelNummer, int rijNummer){
		Status[] rij = rijenEnStoelen.get(rijNummer);
		rij[stoelNummer] = Status.BETAALD;
	}
	



	
	
	
}
