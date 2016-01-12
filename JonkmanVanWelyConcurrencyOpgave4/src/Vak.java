import java.util.ArrayList;

public class Vak {
	
	private int nummer;

	private ArrayList<boolean[]> rijenEnStoelen = new ArrayList<boolean[]>();
	
	public Vak(int nummer,int rijGrootte, int stoelenPerRij){
		this.nummer = nummer;
		
		for (int i = 0; i < rijGrootte; i++) {
			boolean[] arr = new boolean[stoelenPerRij];
			rijenEnStoelen.add(arr);
		}
		
	}

	public int getNummer() {
		return nummer;
	}
	
	public ArrayList<boolean[]> getRijenEnStoelen(){
		return rijenEnStoelen;
	}
	
	public boolean plaatsBeschikbaar(int rijNummer, int stoelNummer){
		
		boolean[] stoelen = rijenEnStoelen.get(rijNummer -1);
		
		if (stoelen[stoelNummer -1] == false) {
			return true;
		}
		
		return false;
	}
	
	public void reserveren(int stoelNummer){
		
	}
	
	public void annuleren(int stoelNummer){
		
	}
	



	
	
	
}
