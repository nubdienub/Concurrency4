import actoren.Klant;
import actoren.TicketBox;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import bestelling.BestelStatus;
import bestelling.Bestelling;
import stoelen.GroepStatus;


public class Apl {

	public static void main(String[] args) {
		
		//Aanmaken van het systeem
		ActorSystem systeem = ActorSystem.create("TicketsForFans");
		
		//Ticketbox actor aanmaken
		ActorRef ticketBoxAgent = systeem.actorOf(Props.create(TicketBox.class),"ticketBoxActor");	
	
		//Klant 1 wil stoelen 1,2,3 en 4 van vak 3 bij de groep vloer bestellen
		int[] stoelNummers1 = {1,2,3,4};
		Bestelling b1 = new Bestelling(1, 3, stoelNummers1, GroepStatus.VLOER, BestelStatus.WILRESERVEREN);
	 	ActorRef k1 = systeem.actorOf(Props.create(Klant.class,1,b1,ticketBoxAgent),"klantActor1");	
	 	b1.setKlant(k1);

		//Klant 2 wil stoelnummers 50 en 100 van vak 3 bij de groep zuid bestellen
	 	//Deze stoelnummers bestaan niet, het vak bestaat wel
		int[] stoelNummers2 = {50,100};
		Bestelling b2 = new Bestelling(1, 3, stoelNummers2, GroepStatus.ZUID, BestelStatus.WILRESERVEREN);
	 	ActorRef k2 = systeem.actorOf(Props.create(Klant.class,2,b2,ticketBoxAgent),"klantActor2");	
	 	b2.setKlant(k2);
	 	
		//Klant 3 wil stoelen 1,2,3 en 4 van vak 3 bij de groep vloer bestellen
	 	//Klant 1 heeft deze stoelen al dus dit is niet mogelijk
		int[] stoelNummers3 = {1,2,3,4};
		Bestelling b3 = new Bestelling(1, 3, stoelNummers3, GroepStatus.VLOER, BestelStatus.WILRESERVEREN);
	 	ActorRef k3 = systeem.actorOf(Props.create(Klant.class,3,b3,ticketBoxAgent),"klantActor3");	
	 	b3.setKlant(k3);
	 	
		//Klant 4 wil stoelen 1,2,3,4 en 5 van vak 3 bij de groep vloer bestellen, dit is 1 stoel te veel
	 	//Klant 1 heeft deze stoelen al dus dit is niet mogelijk
		int[] stoelNummers4 = {1,2,3,4,5};
		Bestelling b4 = new Bestelling(1, 3, stoelNummers4, GroepStatus.NOORD, BestelStatus.WILRESERVEREN);
	 	ActorRef k4 = systeem.actorOf(Props.create(Klant.class,4,b4,ticketBoxAgent),"klantActor4");	
	 	b4.setKlant(k4);
	 	
		systeem.terminate();
	}

}
