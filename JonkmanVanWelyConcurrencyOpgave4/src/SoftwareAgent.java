import java.util.ArrayList;

import akka.actor.UntypedActor;

public class SoftwareAgent extends	UntypedActor {

	private Groep groep;
	private ArrayList<Vak> lijstMetVakken = new ArrayList<Vak>();
	
	public SoftwareAgent(Groep groep,int[] vakNummers) {
		
		this.groep = groep;
		
		for (int i = 0; i < vakNummers.length; i++) {
			Vak v = new Vak(vakNummers[i]);
			lijstMetVakken.add(v);
		}
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
		
	}
	
	@Override	
	public	void	preStart(){	

	}	
	
	public	void	postStop(){	

	}	
	
	public Groep getGroep(){
		return groep;
	}
	
	public boolean plaatsBeschikbaar(int stoelNummer,int vakNummer,int rijNummer){
		
		for(Vak v : lijstMetVakken){
			if (v.getNummer() == vakNummer) {
				return v.plaatsBeschikbaar(rijNummer, stoelNummer);
			}
		}
		
		return false;
	}
	
	public void reserveren(int stoelNummer,int vakNummer,int rijNummer){
		
	}
	
	public void annuleren(int stoelNummer,int vakNummer,int rijNummer){
		
	}


}
