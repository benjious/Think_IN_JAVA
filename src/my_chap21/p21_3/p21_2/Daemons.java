package my_chap21.p21_3.p21_2;

import java.util.concurrent.TimeUnit;
/**
 * 
 * @author benjious
 *
 */
class Daemon implements Runnable {
	private Thread[] threads = new Thread[10];

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//�����߳������е�ÿһ���߳�,����һ��DaemonSpawn����,
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new DaemonSpawn());
			threads[i].start();
			System.out.println("DaemonSpawn " + i + " started. ");
		}
		//��ӡ�����Ƿ��Ǻ�̨�߳�
		for (int i = 0; i < threads.length; i++) {
			System.out.println("thread[" + i + "].isDaemon() = "
					+ threads[i].isDaemon() + ". ");
		}
		while (true) {
			Thread.yield();
		}
	}
}

class DaemonSpawn implements Runnable {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			Thread.yield();
		}
	}

}

public class Daemons {
	public static void main(String[] args) throws Exception {
		Thread thread = new Thread(new Daemon());
		thread.setDaemon(true);
		thread.start();
		System.out.println("thread.isDaemon()="+thread.isDaemon()+", ");
		TimeUnit.SECONDS.sleep(1);
	}
}
