package actoren;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import bestelling.BestelStatus;
import bestelling.Bestelling;
import stoelen.GroepStatus;

public class Klant extends	UntypedActor {
	
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private ActorRef TicketBox;
	private int klantNummer;
	private Bestelling bestelling;
	
	/**
	 * Constructor van de klant
	 * @param b het object van de bestelling
	 * @param tb de actor van ticketbox
	 */
	public Klant(int klantNummer,Bestelling b,ActorRef tb){
		this.klantNummer = klantNummer;
		this.bestelling = b;
		this.TicketBox = tb;
		this.bestelling.setKlantNummer(this.klantNummer);
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
		//Als het binnengekomen object een bestelling is
		if (obj instanceof Bestelling) {
			
			Bestelling b = (Bestelling) obj;
			
			//De bestelling is afgewezen
			if (b.getStatus().equals(BestelStatus.GEWEIGERD)) {
				log.info("Bericht aan de klant: Klant " + b.getKlantNummer() + " bestelling is geweigert, reden: " + b.getRedenWeigering());
			}
			
			//De gereserveerde plekken zijn betaald
			if (b.getStatus().equals(BestelStatus.ISGERESERVEERD)) {
				
				log.info("Bericht aan de klant: Klant " + b.getKlantNummer() + " bestelling is gereserveerd");
				
				int g = 1 + (int)(Math.random() * ((2 - 1) + 1));
				// Gebruiker wil betalen
				if (g == 1) { 
					b.setStatus(BestelStatus.WILBETALEN);
					TicketBox.tell(b, getSelf());
				}
				// Gebruiker wil niet meer betalen
				else{ 		 
					b.setStatus(BestelStatus.WILANNULEREN);
					TicketBox.tell(b, getSelf());
				}
			}
			
			//De gereserveerde plekken zijn betaald
			if (b.getStatus().equals(BestelStatus.ISBETAALD)) {
				log.info("Bericht aan de klant: Klant " + b.getKlantNummer() + " bestelling is succesvol betaald!" + " Info over bestelling: " + b.toString());
				
			}
			
			//De gereserveerde plekken zijn betaald
			if (b.getStatus().equals(BestelStatus.ISGEANNULEERD)) {
				log.info("Bericht aan de klant: Klant " + b.getKlantNummer() + " bestelling is succesvol geannuleerd!");
			}
				
		}else{
			unhandled(obj);
		}
	
	}

	@Override
	public void preStart() throws Exception {
		//Zeg tegen de ticket box van het ziggo dome dat ik mijn bestelling wil plaatsen
		//Stuur mij zelf mee
		TicketBox.tell(bestelling, getSelf());
	}
	
}