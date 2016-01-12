import java.util.ArrayList;

public class Vak {
	
	private int nummer;
	private ArrayList<boolean[]> rijenEnStoelen = new ArrayList<boolean[]>();
	
	public Vak(int nummer){
		
		this.nummer = nummer;
		
		for (int i = 0; i < 15; i++) {
			boolean[] arr = new boolean[10];
			rijenEnStoelen.add(arr);
		}
		
	}
	
	
	
}
