import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Apl {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("TicketsForFans");
		
		ActorRef SoftwareAgent = system.actorOf(Props.create(SoftwareAgent.class),"HiActor");	
		
		SoftwareAgent.tell("IK WIEL KAARTJE NU WOLLAH!", null);
		
		system.terminate();
	}

}
