package my_chap21.p21_3.p21_2;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory  implements ThreadFactory{
	

	@Override
	public Thread newThread(Runnable arg0) {
		Thread thread=new Thread(arg0);
		thread.setDaemon(true);
		System.out.println("--");
		return thread;
	}

}
