package actoren;
import java.util.ArrayList;
import java.util.HashMap;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import stoelen.GroepStatus;
import stoelen.GroepenPunt;

public class TicketBox extends	UntypedActor {	

	private ArrayList<ActorRef> lijstMetSoftwareAgents = new ArrayList<ActorRef>();
	GroepenPunt gpp;
	
	private final Router ticketBoxRouter;
	private ArrayList<Routee> saRoutees = new ArrayList<>();
	
	public TicketBox(){	
		gpp = GroepenPunt.getInstance();	
		gpp.init();
		
	 	ActorRef sa1 = getContext().actorOf(Props.create(SoftwareAgent.class),"softwareAgent1");	
	 	ActorRef sa2 = getContext().actorOf(Props.create(SoftwareAgent.class),"softwareAgent2");	
	 	ActorRef sa3 = getContext().actorOf(Props.create(SoftwareAgent.class),"softwareAgent3");	
	 	ActorRef sa4 = getContext().actorOf(Props.create(SoftwareAgent.class),"softwareAgent4");	
		
	 	lijstMetSoftwareAgents.add(sa1);
	 	lijstMetSoftwareAgents.add(sa2);
	 	lijstMetSoftwareAgents.add(sa3);
	 	lijstMetSoftwareAgents.add(sa4);
		
		for (int i = 0; i < lijstMetSoftwareAgents.size(); i++) {
			saRoutees.add(new ActorRefRoutee(lijstMetSoftwareAgents.get(i)));
		}
		
		ticketBoxRouter = new Router(new RoundRobinRoutingLogic(),saRoutees);
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {
		ticketBoxRouter.route(obj, getSender());
	}
	

}