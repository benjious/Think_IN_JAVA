package my_chap21.p21_3.p21_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepingTask extends LiftOff {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (countDown-- > 0) {
				System.out.println(status());
				//Thread.sleep(100);
				TimeUnit.MILLISECONDS.sleep(100);
			}
		} catch (InterruptedException e) {
			System.err.println("Interrupted");
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			service.execute(new SleepingTask());

		}
		service.shutdown();
	}

}
