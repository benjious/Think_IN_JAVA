package my_chap21.p21_3.p21_2;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {
	@Override
	public void run() {
		while(true) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread()+" "+this );
			} catch (InterruptedException e) {
				System.out.println("sleep() interrupted");
			}// TODO Auto-generated method stub
			
			
		}
		}
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Thread daemonThread=new Thread(new SimpleDaemons());
			daemonThread.setDaemon(true);
			//�ǵ��߳�Ҫstart()�ŻῪ��
			daemonThread.start();
		}
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(175);
		
	}
	
}
