import actoren.Klant;
import actoren.TicketBox;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import bestelling.BestelStatus;
import bestelling.Bestelling;
import stoelen.GroepStatus;
import stoelen.GroepenPunt;

public class Apl {

	public static void main(String[] args) {
		
		//Aanmaken van het systeem
		ActorSystem system = ActorSystem.create("TicketsForFans");
		
		//Ticketbox actor aanmaken
		ActorRef ticketBoxAgent = system.actorOf(Props.create(TicketBox.class),"ticketBoxActor");	
	
		//Klant 1 wil stoelen 1,2,3 en 4 van vak 3 bij de groep vloer bestellen
		int[] stoelNummers1 = {1,2,3,4};
		Bestelling b1 = new Bestelling(1, 3, stoelNummers1, GroepStatus.VLOER, BestelStatus.RESERVEREN);
	 	ActorRef k1 = system.actorOf(Props.create(Klant.class,1,b1,ticketBoxAgent),"klantActor1");	

		//Klant 2 wil stoelnummers 50 en 100 van vak 3 bij de groep zuid bestellen
	 	//Deze stoelnummers bestaan niet, het vak bestaat wel
		int[] stoelNummers2 = {50,100};
		Bestelling b2 = new Bestelling(1, 3, stoelNummers2, GroepStatus.ZUID, BestelStatus.RESERVEREN);
	 	ActorRef k2 = system.actorOf(Props.create(Klant.class,2,b2,ticketBoxAgent),"klantActor2");	
	 	
		//Klant 3 wil stoelen 1,2,3 en 4 van vak 3 bij de groep vloer bestellen
	 	//Klant 1 heeft deze stoelen al dus dit is niet mogelijk
		int[] stoelNummers3 = {1,2,3,4};
		Bestelling b3 = new Bestelling(1, 3, stoelNummers3, GroepStatus.VLOER, BestelStatus.RESERVEREN);
	 	ActorRef k3 = system.actorOf(Props.create(Klant.class,3,b3,ticketBoxAgent),"klantActor3");	
	 	
		system.terminate();
	}

}
