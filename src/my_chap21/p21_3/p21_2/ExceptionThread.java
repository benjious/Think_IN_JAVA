package my_chap21.p21_3.p21_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	public static void main(String[] args) {
		try {
			ExecutorService service = Executors.newCachedThreadPool();
			service.execute(new ExceptionThread());
		} catch (RuntimeException e) {

			// TODO: handle exception
		}

	}
}
