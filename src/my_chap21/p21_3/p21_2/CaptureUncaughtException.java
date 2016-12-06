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
		//getUncaughtExceptionHandler()方法获取在运行中出现的Exception
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
		//设置这个接口实现类,就像listener
		thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		System.out.println("eh = " + thread.getUncaughtExceptionHandler());
		return thread;
	}
}

public class CaptureUncaughtException {
	public static void main(String[] args) {
		//通过线程池创建一个特定的线程,
		ExecutorService service=Executors.newCachedThreadPool(new HandlerThreadFactory());
		//给这个特定的线程分配任务
		service.execute(new ExceptionThread2());
	}
}
