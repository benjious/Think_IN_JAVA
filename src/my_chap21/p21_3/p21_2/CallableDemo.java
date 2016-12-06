package my_chap21.p21_3.p21_2;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo {
	public static void main(String[] args) {
		ExecutorService service=Executors.newCachedThreadPool();
		ArrayList<Future<String>> results=new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			results.add(service.submit(new TaskWithResult(i)));
		}
		for (Future<String> future : results) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException e) {
				System.out.println(e);
				return;
				// TODO: handle exception
			}catch (ExecutionException e) {
				System.out.println(e);
				// TODO: handle exception
			}finally {
				service.shutdown();
			}
			
		}
	}
}
