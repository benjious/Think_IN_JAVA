package my_chap21.p21_3.p21_5;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownlatch : 一个线程(或者多个),等待另外N个线程完成某个事情之前才可以启动执行
 *
 * Created by Benjious on 2017/1/18.
 */
class WaitingTask implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch mDownLatch;

    public WaitingTask(CountDownLatch downLatch) {
        mDownLatch = downLatch;
    }

    @Override
    public void run() {
        try {
            mDownLatch.await();
            System.out.println("Latch barrier passed for " + this);

        } catch (InterruptedException e) {
            System.out.println(this + "interrupted");
        }
    }

    @Override
    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }
}

class TaskPortion implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch mDownLatch;
    private static Random sRandom = new Random(47);

    TaskPortion(CountDownLatch downLatch) {
        mDownLatch = downLatch;
    }

    @Override
    public void run() {
        try {
            doWork();
            mDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(sRandom.nextInt(2000));
        System.out.println(this + "completed");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d ", id);
    }
}

public class CountDownLatchDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch mDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new WaitingTask(mDownLatch));
        }
        for (int i = 0; i < 100; i++) {
            executorService.execute(new TaskPortion(mDownLatch));
        }
        System.out.println("Launched all tasks");
        executorService.shutdown();
    }
}
