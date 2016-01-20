import java.util.ArrayList;

import akka.actor.UntypedActor;

public class TicketBox extends	UntypedActor {

	private ArrayList<SoftwareAgent> lijstMetSoftwareAgents = new ArrayList<SoftwareAgent>();
	private final int aantalRijen = 5;
	private final int stoelenPerRij = 5;
	
	public TicketBox(){
		int[] array1 = {1,2,3};
		int[] array2 = {4,5,6};
		int[] array3 = {7,8,9};
		
		lijstMetSoftwareAgents.add(new SoftwareAgent(Groep.VLOER,array1 , aantalRijen, stoelenPerRij));
		lijstMetSoftwareAgents.add(new SoftwareAgent(Groep.NOORD1,array2 , aantalRijen, stoelenPerRij));
		lijstMetSoftwareAgents.add(new SoftwareAgent(Groep.ZUID2,array3 , aantalRijen, stoelenPerRij));
	}
	
	@Override
	public void onReceive(Object obj) throws Exception {

	}
	
	@Override	
	public	void	preStart(){	

	}	
	
	public	void	postStop(){	

	}	

}