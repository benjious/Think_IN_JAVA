package my_chap21.p21_3.p21_2;

public class MoreBasicThreads {
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread(new LiftOff()).start();

		}
		System.out.println("waiting for LiftOff");
	}
}
