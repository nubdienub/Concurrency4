import java.util.ArrayList;

public class Vak {
	
	private int nummer;

	private ArrayList<boolean[]> rijenEnStoelen = new ArrayList<boolean[]>();
	
	public Vak(int nummer){
		this.nummer = nummer;
		
		for (int i = 0; i < 15; i++) {
			boolean[] arr = new boolean[10];
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
