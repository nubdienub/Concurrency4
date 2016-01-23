package actoren;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import bestelling.BestelStatus;
import bestelling.Bestelling;
import stoelen.GroepStatus;
import stoelen.GroepenPunt;

public class SoftwareAgent extends	UntypedActor {
	private	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	GroepenPunt gpp;
	
	/**
	 * Constructor van software agent
	 * haalt de instantie van GroepenPunt (singleton) op
	 */
	public SoftwareAgent(){
		gpp = GroepenPunt.getInstance();		
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
		//Als het binnengekomen object een bestelling is
		if (obj instanceof Bestelling) {

			Bestelling b = (Bestelling) obj;
			
			//De bestelling is nieuw en moet verwerkt worden
			if (b.getStatus() == BestelStatus.RESERVEREN) {

				log.info("Ik handel nu een reservering af!");
				
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				boolean beschikbaar = true;
				
				//Kijk voor elke stoel of er een plaats beschikbaar is
				for (int i = 0; i < stoelNummers.length; i++) {
					beschikbaar = plaatsBeschikbaar(b.getGroep(), stoelNummers[i], b.getVakNummer(), b.getRijNummer());
					if (beschikbaar == false) break;
				}

				//Als de plekken beschikbaar zijn, reserveer deze dan
				if (beschikbaar) {
					reserveren(b.getKlantNummer(),b.getGroep(), stoelNummers, b.getVakNummer(), b.getRijNummer());
					b.setStatus(BestelStatus.BETALEN);
				}else{
					b.setStatus(BestelStatus.GEWEIGERD);
				}

				//Stuur het (aangepaste) bericht weer terug naar de oorsprong
				getSender().tell(b, getSelf());										
			}
			
			//De gebruiker wil zijn bestelling betalen
			else if (b.getStatus() == BestelStatus.BETALEN) {		
				
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				
				//Als de gebruiker de goede plekken kan betalen
				if (localCheckCorrecteGebruiker(stoelNummers,b)) {
					betalen(b.getKlantNummer(),b.getGroep(), stoelNummers, b.getVakNummer(), b.getRijNummer());
					b.setStatus(BestelStatus.BETAALD);
				}else{
					b.setStatus(BestelStatus.GEWEIGERD);
				}

				//Stuur het (aangepaste) bericht weer terug naar de oorsprong
				getSender().tell(b, getSelf());			
				
			}
			
			//De gebruiker wil zijn bestelling niet betalen
			else if(b.getStatus() == BestelStatus.ANNULEREN){
				
				
				//Haal de stoelnummers op
				int[] stoelNummers = b.getStoelNummers();
				
				//Als het de goede gebruiker betreft, kan hij zijn bestelling annuleren
				if (localCheckCorrecteGebruiker(stoelNummers,b)) {
					annuleren(b.getKlantNummer(),b.getGroep(), stoelNummers, b.getVakNummer(), b.getRijNummer());
					b.setStatus(BestelStatus.GEANNULEERD);
				}else{
					b.setStatus(BestelStatus.GEWEIGERD);
				}

				//Stuur het (aangepaste) bericht weer terug naar de oorsprong
				getSender().tell(b, getSelf());			
				
			}

		}
		
	}
	
	/**
	 * Methode die lokaal de methode checkCorrecteGebruiker uitvoert
	 * @param stoelNummers , de stoel nummers
	 * @param b , het object bestelling
	 * @return , true als de gebruiker correct is anders false
	 */
	public synchronized boolean localCheckCorrecteGebruiker(int[] stoelNummers, Bestelling b){
		
		boolean correcteGebruiker = true;
		
		//Kijk voor elke stoel in de bestelling of hij de goede stoel betaald
		for (int i = 0; i < stoelNummers.length; i++) {
			correcteGebruiker = checkCorrecteGebruiker(b.getKlantNummer(), b.getGroep(), stoelNummers[i], b.getVakNummer(), b.getRijNummer()); 
			if (correcteGebruiker == false) break;
		}
		return correcteGebruiker;
	}
	
	/**
	 * Kijkt of de plaatsen nog beschikbaar zijn
	 * @param groep de groep waarin we kijken of de plaats beschikbaar is
	 * @param stoelNummer het stoelnummer van die plaats
	 * @param vakNummer het vakNummer waar we de plaats gaan opzoeken
	 * @param rijNummer het rijnummer waarin de stoel zich bevind
	 * @return
	 */
	public synchronized boolean plaatsBeschikbaar(GroepStatus groep,int stoelNummer,int vakNummer,int rijNummer){
		return gpp.plaatsBeschikbaar(groep,stoelNummer, vakNummer, rijNummer);
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
	public boolean checkCorrecteGebruiker(int klantNummer,GroepStatus groep,int stoelNummer,int vakNummer,int rijNummer){
		return gpp.checkCorrecteGebruiker(klantNummer,groep,stoelNummer,vakNummer,rijNummer);
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
		gpp.reserveren(klantNummer,groep,stoelNummers, vakNummer, rijNummer);
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
		gpp.annuleren(klantNummer,groep,stoelNummers, vakNummer, rijNummer);
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
		gpp.betalen(klantNummer,groep,stoelNummers, vakNummer, rijNummer);
	}

	
	
}
