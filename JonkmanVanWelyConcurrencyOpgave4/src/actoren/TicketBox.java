package actoren;
import java.util.ArrayList;
import java.util.HashMap;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import bestelling.Bestelling;
import stoelen.GroepStatus;



public class TicketBox extends	UntypedActor {	
	
	private HashMap<GroepStatus, ActorRef> mapMetGroepAgents = new HashMap<>();;
	private Router ticketBoxRouter;

	public TicketBox(){	
		
		ArrayList<Routee> saRoutees = new ArrayList<>();
		
		for (int i = 0; i < GroepStatus.values().length; i++) {
			mapMetGroepAgents.put(GroepStatus.values()[i], getContext().actorOf(Props.create(GroepAgent.class),"groepAgentActor" + i));
		}

		for (int i = 0; i < 4; i++) {
			ActorRef ref = getContext().actorOf(Props.create(SoftwareAgent.class,mapMetGroepAgents),"softwareAgent" + i);
			getContext().watch(ref);
			saRoutees.add(new ActorRefRoutee(ref));
		}
		
		ticketBoxRouter = new Router(new RoundRobinRoutingLogic(),saRoutees);

	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
		//Als het obj een bestelling is
		if (obj instanceof Bestelling) {
			ticketBoxRouter.route(obj, getSender());
		}else{
			unhandled(obj);
		}
	}
	

}