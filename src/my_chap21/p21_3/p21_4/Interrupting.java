package my_chap21.p21_3.p21_4;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 实例说明: 你可以中断线程sleep()方法,但是不能中断 IO操作 和 synchronized同步方法
 * Created by Benjious on 2016/12/18.
 */
class SleepBlocked implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("SleepBlocked InterruptedException");
        }
        System.out.println("Exiting SleepBlocked");
        System.out.println("-------------------------");
    }

    @Override
    public String toString() {
        return "SleepBlocked_Class";
    }
}

/**
 * 这个模仿IO操作
 */
class IOBlocked implements  Runnable{
    private InputStream mInputStream;

    public IOBlocked(InputStream inputStream) {
        mInputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for read()");
            mInputStream.read();
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrpted from blocked IO");
            }else {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Exiting IOBlocked.run()");
        System.out.println("-------------------------");

    }

    @Override
    public String toString() {
        return "IOBlocked_Class";
    }
}

/**
 * 同步任务
 */
class SynchronizedBlocked implements  Runnable{
    private synchronized void f(){
        //一直不释放锁
        while (true) {
            //让步给优先级更高的线程
            Thread.yield();

        }
    }

    public SynchronizedBlocked() {
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
        System.out.println("-------------------------");
    }

    @Override
    public String toString() {
        return "SynchronizedBlock_Class";
    }
}

public class Interrupting {
    private  static ExecutorService sService= Executors.newCachedThreadPool();

    /**
     * 使用submit()方法而不是executor()来启动,好处是可以中断某个单一任务,具体使用如下
     * 它会返回一个结果
     *
     * text(): 执行后停止一段时间,让这个线程中断,
     * @param runnable 传进来的线程
     */
    static void test(Runnable runnable) throws InterruptedException {
        Future<?> future=sService.submit(runnable);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Interrupteding "+runnable.getClass().getName());
        future.cancel(true);
        System.out.println("Interrupt sent to "+runnable.getClass().getName());

    }
    public static void main(String[] args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0);

    }
}
