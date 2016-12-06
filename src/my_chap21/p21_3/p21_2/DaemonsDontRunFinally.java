package my_chap21.p21_3.p21_2;

import java.util.concurrent.TimeUnit;

class ADaemon implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Starting ADaemon");
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("Exiting via InterruptedException");
		}finally {
			System.out.println("This is should always run?");
			
		}

	}
}

public class DaemonsDontRunFinally {
	public static void main(String[] args) {
		Thread thread=new Thread(new ADaemon());
		thread.setDaemon(true);
		thread.start();
	}
}
