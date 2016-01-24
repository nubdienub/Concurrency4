package actoren;
import java.util.HashMap;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import bestelling.Bestelling;
import stoelen.GroepStatus;



public class SoftwareAgent extends	UntypedActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private HashMap<GroepStatus, ActorRef> mapOfGroupAgents;

	
	/**
	 * Constructor van software agent
	 * haalt de instantie van GroepenPunt (singleton) op
	 */
	public SoftwareAgent(HashMap<GroepStatus, ActorRef> mapOfGroupAgents){
		this.mapOfGroupAgents = mapOfGroupAgents;	
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {	
		//Als het binnengekomen object een bestelling is
		if (obj instanceof Bestelling) {

			Bestelling b = (Bestelling) obj;
			
			switch (b.getStatus()){
			
			case WILRESERVEREN:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " wil graag reserveren!");
				//Kies de groep agent die bij de correcte groep hoort
					mapOfGroupAgents.get(b.getGroep()).tell(b, getSelf());		
				break;
			
			case ISGERESERVEERD:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " heeft gereserveerd!");
				//Stuur het (aangepaste) bericht weer terug naar de oorsprong	
					b.getKlant().tell(b, getSelf());
				break;
				
			case WILBETALEN:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " wil graag betalen!");
				//Kies de groep agent die bij de correcte groep hoort
					mapOfGroupAgents.get(b.getGroep()).tell(b, getSelf());		
				break;
				
			case ISBETAALD:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " heeft betaald!");
				//Stuur het (aangepaste) bericht weer terug naar de oorsprong
					b.getKlant().tell(b, getSelf());
				break;
				
			case WILANNULEREN:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " wil graag annuleren!");
				//Kies de groep agent die bij de correcte groep hoort
					mapOfGroupAgents.get(b.getGroep()).tell(b, getSelf());	
				break;
				
			case ISGEANNULEERD:
				log.info("Software agent onvangt: Klant " + b.getKlantNummer() + " heeft geannuleerd");
				//Stuur het (aangepaste) bericht weer terug naar de oorsprong
					b.getKlant().tell(b, getSelf());
				break;
				
			case GEWEIGERD:
				log.info("Software agent onvangt: De reservering van klant " + b.getKlantNummer() + " is geweigerd!");
				//Stuur het (aangepaste) bericht weer terug naar de oorsprong	
					b.getKlant().tell(b, getSelf());
				break;
				
			default:
				unhandled(b);
				break;	
			}				
		}else{
			unhandled(obj);
		}
	}
}
