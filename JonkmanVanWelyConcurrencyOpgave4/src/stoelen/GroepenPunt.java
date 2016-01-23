package stoelen;

import java.util.ArrayList;
import java.util.HashMap;

import akka.event.Logging;
import akka.event.LoggingAdapter;



public class GroepenPunt {
	
	private static GroepenPunt gpp = new GroepenPunt();
	
	/**
	 * Private constructor zodat geen andere class een instantie kan maken
	 */
	private GroepenPunt() {}
	
	/**
	 * Geeft deze static class terug
	 * @return deze class
	 */
	public static GroepenPunt getInstance(){
		return gpp;
	}
	
	private HashMap<GroepStatus, ArrayList<Vak>> mapGroepEnVakken = new HashMap<GroepStatus, ArrayList<Vak>>();
	private final int aantalRijen = 5;
	private final int stoelenPerRij = 5;
	
	/**
	 * Bouwt de vakken, stoelen en rijen op per groep
	 */
	public void init(){
		
		int teller = 1;
		
		for (int i = 1; i < 5; i++) {
			
			ArrayList<Vak> vakkenToevoegen = new ArrayList<Vak>();
			
			Vak v1 = new Vak(teller, aantalRijen, stoelenPerRij);
			vakkenToevoegen.add(v1);
			teller++;
			
			Vak v2 = new Vak(teller, aantalRijen, stoelenPerRij);
			vakkenToevoegen.add(v2);
			teller++;
			
			Vak v3 = new Vak(teller, aantalRijen, stoelenPerRij);
			vakkenToevoegen.add(v3);
			teller++;
			
			switch (i) {
			
			case 1:
					mapGroepEnVakken.put(GroepStatus.VLOER, vakkenToevoegen);
				break;
				
			case 2:
					mapGroepEnVakken.put(GroepStatus.ZUID, vakkenToevoegen);
				break;
				
			case 3:
					mapGroepEnVakken.put(GroepStatus.NOORD, vakkenToevoegen);
				break;
				
			case 4:
					mapGroepEnVakken.put(GroepStatus.WEST, vakkenToevoegen);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Kijkt of de plaatsen nog beschikbaar zijn
	 * @param groep de groep waarin we kijken of de plaats beschikbaar is
	 * @param stoelNummer het stoelnummer van die plaats
	 * @param vakNummer het vakNummer waar we de plaats gaan opzoeken
	 * @param rijNummer het rijnummer waarin de stoel zich bevind
	 * @return
	 */
	public synchronized  boolean plaatsBeschikbaar(GroepStatus groep,int stoelNummer,int vakNummer,int rijNummer){
		ArrayList<Vak> vakken = mapGroepEnVakken.get(groep);

		for(Vak v : vakken){
			if (v.getNummer() == vakNummer) {
				return v.plaatsBeschikbaar(rijNummer, stoelNummer);
			}
		}
		return false;
	}
	
	/**
	 * Kijkt of een gebruiker mag betalen of annuleren
	 * @param klantNummer het klant nummer
	 * @param groep de groep
	 * @param stoelNummer het stoelnummer
	 * @param vakNummer het vaknummer
	 * @param rijNummer het rijnummer
	 * @return
	 */
	public synchronized boolean checkCorrecteGebruiker(int klantNummer,GroepStatus groep,int stoelNummer,int vakNummer,int rijNummer){
		ArrayList<Vak> vakken = mapGroepEnVakken.get(groep);

		for(Vak v : vakken){
			if (v.getNummer() == vakNummer) {
				return v.checkCorrecteGebruiker(klantNummer,rijNummer,stoelNummer);
			}
		}
		return false;
	}
	
	/**
	 * Reserveren van plek(ken) 
	 * @param klantNummer
	 * @param groep
	 * @param stoelNummers
	 * @param vakNummer
	 * @param rijNummer
	 */
	public void reserveren(int klantNummer,GroepStatus groep,int[] stoelNummers,int vakNummer,int rijNummer){
		ArrayList<Vak> vakken = mapGroepEnVakken.get(groep);
		for(Vak v : vakken){
			if (v.getNummer() == vakNummer) {
				v.reserveren(klantNummer,stoelNummers,rijNummer);
			}
		}	
	}
	
	/**
	 * Plek(ken) annuleren
	 * @param klantNummer
	 * @param groep
	 * @param stoelNummers
	 * @param vakNummer
	 * @param rijNummer
	 */
	public void annuleren(int klantNummer,GroepStatus groep,int[] stoelNummers,int vakNummer,int rijNummer){
		ArrayList<Vak> vakken = mapGroepEnVakken.get(groep);
		for(Vak v : vakken){
			if (v.getNummer() == vakNummer) {
				v.annuleren(klantNummer,stoelNummers, rijNummer);
			}
		}
	}
	
	/**
	 * Plek(ken) betalen
	 * @param klantNummer
	 * @param groep
	 * @param stoelNummers
	 * @param vakNummer
	 * @param rijNummer
	 */
	public void betalen(int klantNummer,GroepStatus groep,int[] stoelNummers,int vakNummer,int rijNummer){
		ArrayList<Vak> vakken = mapGroepEnVakken.get(groep);
		for(Vak v : vakken){
			if (v.getNummer() == vakNummer) {
				v.betalen(klantNummer,stoelNummers, rijNummer);
			}
		}
	}
	
	
	
	
	
	
	
}
