package actoren;

import java.util.ArrayList;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import bestelling.BestelStatus;
import bestelling.Bestelling;
import stoelen.Vak;

public class GroepAgent extends	UntypedActor{
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ArrayList<Vak> lijstVakkenEnStoelen = new ArrayList<Vak>();
	private final int aantalRijen = 5;
	private final int stoelenPerRij = 5;
	
	public GroepAgent(){
		init();
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
	
		//Als het binnengekomen object een bestelling is
		if (obj instanceof Bestelling) {

			Bestelling b = (Bestelling) obj;
			
			if (b.getStoelNummers().length > 4) {
				log.info("Groep agent verwerkt: Klant " + b.getKlantNummer() + " wil graag reserveren!");
				
				b.setRedenWeigering("Maximaal 4 plekken");
				b.setStatus(BestelStatus.GEWEIGERD);
				
				//Stuur bericht terug naar software agent
				getSender().tell(b, getSelf());
			}
			
			
			if (b.getStatus() == BestelStatus.WILRESERVEREN) {
				log.info("Groep agent verwerkt: Klant " + b.getKlantNummer() + " wil graag reserveren!");
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				boolean beschikbaar = true;
				
				//Als de plekken beschikbaar zijn, reserveer deze dan
				beschikbaar = plaatsBeschikbaar(b.getKlantNummer(), stoelNummers, b.getVakNummer(), b.getRijNummer());
				
				//Weizigen van de status van de bestelling
				if (beschikbaar) {
					b.setStatus(BestelStatus.ISGERESERVEERD);
				}else{
					b.setRedenWeigering("Plaats(en) zijn al bezet"); 
					b.setStatus(BestelStatus.GEWEIGERD);
				}		
				
				//Stuur bericht terug naar software agent
				getSender().tell(b, getSelf());
			}
			
			if (b.getStatus() == BestelStatus.WILBETALEN) {
				log.info("Groep agent verwerkt: Klant " + b.getKlantNummer() + " wil graag betalen!");
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				
				//Als de gebruiker de goede plekken kan betalen
				if (checkCorrecteGebruiker(b.getKlantNummer(),b.getStoelNummers(),b.getVakNummer(),b.getRijNummer())) {
					
					betalen(b.getKlantNummer(),stoelNummers, b.getVakNummer(), b.getRijNummer());
					b.setStatus(BestelStatus.ISBETAALD);
					
				}else{
					b.setRedenWeigering("Gebruiker probeert verkeerde stoel te betalen");
					b.setStatus(BestelStatus.GEWEIGERD);
				}
				
				//Stuur bericht terug naar software agent
				getSender().tell(b, getSelf());
				
			}
			
			if (b.getStatus() == BestelStatus.WILANNULEREN) {
				log.info("Groep agent verwerkt: Klant " + b.getKlantNummer() + " wil graag annuleren!");
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				
				//Als de gebruiker de goede plekken kan betalen
				if (checkCorrecteGebruiker(b.getKlantNummer(),b.getStoelNummers(),b.getVakNummer(),b.getRijNummer())) {
					
					annuleren(b.getKlantNummer(),stoelNummers, b.getVakNummer(), b.getRijNummer());
					b.setStatus(BestelStatus.ISGEANNULEERD);
					
				}else{
					b.setRedenWeigering("Gebruiker probeert verkeerde stoel te annuleren");
					b.setStatus(BestelStatus.GEWEIGERD);
				}
				
				//Stuur bericht terug naar software agent
				getSender().tell(b, getSelf());
			}
			
		}else{
			unhandled(obj);
		}
			
	}
	
	/**
	 * Bouwt de vakken, stoelen en rijen op 
	 */
	public void init(){
		int teller = 1;
		
		for (int i = 0; i < 5; i++) {
			
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
			
			lijstVakkenEnStoelen.add(v1);
			lijstVakkenEnStoelen.add(v2);
			lijstVakkenEnStoelen.add(v3);
		}
	}
	
	/**
	 * Kijkt of de plaatsen nog beschikbaar zijn en reserveerd dan die plaatsen
	 * @param groep de groep waarin we kijken of de plaats beschikbaar is
	 * @param stoelNummer het stoelnummer van die plaats
	 * @param vakNummer het vakNummer waar we de plaats gaan opzoeken
	 * @param rijNummer het rijnummer waarin de stoel zich bevind
	 * @return
	 */
	public boolean plaatsBeschikbaar(int klantNummer,int[] stoelNummers,int vakNummer,int rijNummer){
		
		boolean beschikbaar = true;
		
		for(Vak v : lijstVakkenEnStoelen){
			if (v.getNummer() == vakNummer) {
				for (int i = 0; i < stoelNummers.length; i++) {
					beschikbaar = v.plaatsBeschikbaar(rijNummer, stoelNummers[i]);
					if (beschikbaar == false) break;
				}
			}
		}
		
		if (beschikbaar) {
			reserveren(klantNummer,stoelNummers, vakNummer, rijNummer);
		}
		
		return beschikbaar;
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
	

	
	public boolean checkCorrecteGebruiker(int klantNummer,int[] stoelNummers,int vakNummer,int rijNummer){

		boolean correcteGebruiker = true;
		
		//Kijk voor elke stoel in de bestelling of hij de goede stoel benaderd
			for(Vak v : lijstVakkenEnStoelen){
				if (v.getNummer() == vakNummer) {
					
					for (int i = 0; i < stoelNummers.length; i++) {
						correcteGebruiker = v.checkCorrecteGebruiker(klantNummer,rijNummer,stoelNummers[i]);
					}
						
				}
			}

		return correcteGebruiker;
			
	}
	
	/**
	 * Reserveren van plek(ken) 
	 * @param klantNummer
	 * @param groep
	 * @param stoelNummers
	 * @param vakNummer
	 * @param rijNummer
	 */
	public void reserveren(int klantNummer,int[] stoelNummers,int vakNummer,int rijNummer){

		for(Vak v : lijstVakkenEnStoelen){
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
	public void annuleren(int klantNummer,int[] stoelNummers,int vakNummer,int rijNummer){

		for(Vak v : lijstVakkenEnStoelen){
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
	public void betalen(int klantNummer,int[] stoelNummers,int vakNummer,int rijNummer){
		
		for(Vak v : lijstVakkenEnStoelen){
			if (v.getNummer() == vakNummer) {
				v.betalen(klantNummer,stoelNummers, rijNummer);
			}
		}
	}
	

}
