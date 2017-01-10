package my_chap21.p21_3.p21_4;

import java.util.concurrent.TimeUnit;

/**
 * 要知道利用interupted标志来判断中断
 * Created by Benjious on 2016/12/23.
 */
class NeedsCleanup {
    private final int id;

    public NeedsCleanup(int id) {
        this.id = id;
    }

    public void cleanup() {
        System.out.println("Cleaning up " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //pointer1,定义完马上进入finally()方法,执行clearup()方法
                NeedsCleanup needsCleanup = new NeedsCleanup(1);
                try {
                    System.out.println("Sleeping");
                    TimeUnit.MILLISECONDS.sleep(1);
                    //创建pointer2
                    NeedsCleanup needsCleanup2 = new NeedsCleanup(2);
                    try {
                        //这里制造一个时间消耗,不会阻塞的操作
                        System.out.println("Calculating");
                        for (int i = 1; i < 2500000; i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("Finished time_consuming operation");
                    } finally {
                        needsCleanup2.cleanup();
                    }
                } finally {
                    needsCleanup.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
    }
}


public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Blocked3());
        thread.start();
        TimeUnit.MILLISECONDS.sleep(50);
        thread.interrupt();
    }
}
