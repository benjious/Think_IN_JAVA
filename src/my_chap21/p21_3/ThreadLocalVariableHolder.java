package my_chap21.p21_3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benjious on 2016/12/6.
 */

class Accessor implements Runnable {
    private int id;

    public Accessor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ThreadLocalVariableHolder.increment();
            System.out.println(this);
            Thread.yield();
        }

    }

    @Override
    public String toString() {
        return "#"+id+": "+ThreadLocalVariableHolder.get();
    }
}

public class ThreadLocalVariableHolder {
    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private Random mRandom = new Random(47);

        //ThreadLocal为所持有的线程初始化值
        protected synchronized Integer initialValue() {
            return mRandom.nextInt(10000);
        }

    };

    public static void increment() {
        value.set(value.get() + 1);
    }

    public static int get() {
        return value.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(3);
        executorService.shutdownNow();

    }
}
