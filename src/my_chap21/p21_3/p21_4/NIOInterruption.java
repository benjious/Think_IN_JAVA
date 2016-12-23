package my_chap21.p21_3.p21_4;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
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

    }
}

public class NIOInterruption {
    public static void main(String[] args) throws Exception{
        ExecutorService executorService= Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        SocketChannel channel = SocketChannel.open(inetSocketAddress);
        SocketChannel channe2 = SocketChannel.open(inetSocketAddress);
        executorService.submit(new NIOBlocked(channel));
        executorService.execute(new NIOBlocked(channe2));
        executorService.shutdown();

    }
}
