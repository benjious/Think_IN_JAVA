package my_chap21.p21_3.p21_4;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 上个例子知道IO不能被中断,这有可能发生多个线程同时对同一资源操作
 * 解决办法就是关闭底层资源,同时shutDownNow()这个方法
 * Created by Benjious on 2016/12/23.
 */
public class CloseResource {
    public static void main(String[] args) throws Exception{
        ExecutorService executorService= Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        InputStream inputStream = new Socket("localhost", 8080).getInputStream();
        executorService.execute(new IOBlocked(inputStream));
        executorService.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Shutting down all threads");
        executorService.shutdownNow();
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println("Closing "+inputStream.getClass().getName());
        inputStream.close();
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println("Closing "+System.in.getClass().getName());
        System.in.close();
    }
}
