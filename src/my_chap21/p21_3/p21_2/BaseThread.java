package my_chap21.p21_3.p21_2;

public class BaseThread {
	public static void main(String[] args) {
		Thread thread = new Thread(new LiftOff());
		thread.start();
		System.out.println("waiting for LiftOff");
	}

	
}
