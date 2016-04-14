
public class WorkerTest {
	
	
	public static void main(String[] args){
		int number = 3;
		for (int i=0; i<number; i++){
			System.out.println("Creating new worker");
			new Worker().start();
		}
	}

}
