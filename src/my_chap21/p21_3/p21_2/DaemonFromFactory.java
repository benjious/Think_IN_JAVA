package my_chap21.p21_3.p21_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread()+" "+this);
		}
	}

	public static void main(String[] args) throws Exception {
		ExecutorService service = Executors
				.newCachedThreadPool(new DaemonThreadFactory());
		for (int i = 0; i < 10; i++) {
			service.execute(new DaemonFromFactory());
		}
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(500);

	}
}
