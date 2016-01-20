import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Apl {

	public static void main(String[] args) {
		TicketBox ticketbox = new TicketBox();

		ActorSystem system = ActorSystem.create("TicketsForFans");

		ActorRef KlantAgent = system.actorOf(Props.create(Klant.class),"HiActor");	
		
		KlantAgent.tell("Ik wil graag een kaart bestellen voor ...", null);
		
		system.terminate();
	}

}
