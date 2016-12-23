package my_chap21.p21_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 为什么会出现线程退出呢?
 * 答:几个线程同时修改同一个数据,当他们同时修改到一个
 * Created by Benjious on 2016/12/6.
 */

/**
 * 里卖存放着一个数int组
 */
class CircularSet{
    private int[] array;
    private int len;
    private  int index=0;

    public CircularSet(int size) {
        this.len = size;
        array = new int[len];
        //初始化数组,所有的值都是-1
        for (int i=0; i<size; i++) {
            array[i]=-1;
        }
    }

    public synchronized  boolean contains(int val) {
        for (int i=0; i<len; i++) {
            if (array[i]==val) {
                return true;
            }
        }
        return false;
    }

    public synchronized void add(int i) {
        array[index]=i;
        index = ++index % len;


    }
}

public class SerialNumberChecker {
    private static final int SIZE =10 ;
    //这个是共享的,
    private static CircularSet sSet = new CircularSet(1000);
    private static ExecutorService sExecutorService = Executors.newCachedThreadPool();
    static class SerialChecker implements Runnable{
        @Override
        public void run() {
            while (true) {
                int serial = SerialNumberGenerator.nextSerialNumber();
                //有就推出没就加入到Circular中
                if (sSet.contains(serial)) {
                    System.out.println("Duplocate: "+serial);
                    System.exit(0);
                }
                sSet.add(serial);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        //开10个线程去执行,
        for (int i=0; i<SIZE; i++) {
            sExecutorService.execute(new SerialChecker());
            //在n秒后,有一个argument
        }
        if (args.length>0) {
            TimeUnit.SECONDS.sleep(new Integer(args[0]));
            System.out.println("No duplicates detected");
            System.exit(0);

        }
    }

}
