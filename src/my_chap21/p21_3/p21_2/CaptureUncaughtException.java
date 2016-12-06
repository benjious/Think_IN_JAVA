package my_chap21.p21_3.p21_2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExceptionThread2 implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thread = Thread.currentThread();
		System.out.println("run() by " + thread);
		//getUncaughtExceptionHandler()������ȡ�������г��ֵ�Exception
		System.out.println("eh =" + thread.getUncaughtExceptionHandler());
		throw new RuntimeException();

	}
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		System.out.println("caught " + arg1);
	}
}

//ThreadFactory
class HandlerThreadFactory implements ThreadFactory {
	@Override
	public Thread newThread(Runnable arg0) {
		System.out.println(this + "creating new Thread ");
		Thread thread = new Thread(arg0);
		System.out.println("created " + thread);
		//��������ӿ�ʵ����,����listener
		thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println("eh = " + thread.getUncaughtExceptionHandler());
		return thread;
	}
}

public class CaptureUncaughtException {
	public static void main(String[] args) {
		//ͨ���̳߳ش���һ���ض����߳�,
		ExecutorService service=Executors.newCachedThreadPool(new HandlerThreadFactory());
		//������ض����̷߳�������
		service.execute(new ExceptionThread2());
	}
}
