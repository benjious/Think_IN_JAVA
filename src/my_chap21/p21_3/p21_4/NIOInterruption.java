package my_chap21.p21_3.p21_4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 表明可以被中断,NIO显示方式更加友好,可报出异常
 * Created by Benjious on 2016/12/23.
 */

class NIOBlocked implements Runnable{
    //NIO中的类
    private final SocketChannel mSocketChannel;

    public NIOBlocked(SocketChannel socketChannel) {
        mSocketChannel = socketChannel;
    }

    @Override
    public void run() {
        System.out.println("Waiting for read() in"+this);
        try {
            mSocketChannel.read(ByteBuffer.allocate(1));
        } catch (ClosedByInterruptException e) {
            System.out.println("ClosedByInterruptException");
        } catch (AsynchronousCloseException e ) {
            System.out.println("AsynchronousCloseException");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Exiting NIOBlocked.rum()"+this);
    }
}

public class NIOInterruption {
    public static void main(String[] args) throws Exception{
        ExecutorService executorService= Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8081);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8081);
        SocketChannel channel = SocketChannel.open(inetSocketAddress);
        SocketChannel channe2 = SocketChannel.open(inetSocketAddress);
        Future<?> future=executorService.submit(new NIOBlocked(channel));
        executorService.execute(new NIOBlocked(channe2));
        executorService.shutdown();
        TimeUnit.MILLISECONDS.sleep(1);
        //中断一个IO操作
        future.cancel(true);

        //释放阻塞
        channe2.close();
    }
}
