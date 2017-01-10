package my_chap21.p21_3.p21_5;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benjious on 2016/12/26.
 */
class Blocked {
    //等待,等给唤醒
    synchronized void waitingCall() {
        try {
            while (!Thread.interrupted()) {
                wait();
                System.out.println(Thread.currentThread() + " ");
            }
        } catch (InterruptedException e) {
        }
    }

    synchronized void prod() {
        notify();
    }

    synchronized void prodAll() {
        notifyAll();
    }

}

class Task implements Runnable {
    static Blocked sBlocked = new Blocked();

    @Override
    public void run() {
        sBlocked.waitingCall();
    }
}

class Task2 implements Runnable {
    static Blocked sBlocked = new Blocked();

    @Override
    public void run() {
        sBlocked.waitingCall();
    }
}


public class NotifyVsNotifyAll {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Task());
        }
        executorService.execute(new Task2());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            //线程封闭
            boolean prod = true;
            @Override
            public void run() {
                if (prod) {
                    System.out.println("\nnotify() ");
                    Task.sBlocked.prod();
                    prod = false;
                } else {
                    System.out.println("\nnotifyAll() ");
                    Task.sBlocked.prodAll();
                    prod = true;
                }

            }
        }, 400, 400);//Run every 4 second
        //给另外的线程一些时间,让他们有时间执行,不然主线程结束了那么由他开启的线程也死亡了
        TimeUnit.SECONDS.sleep(5);

        //定时器取消
        timer.cancel();
        System.out.println("\nTimer canceled ");

        //休眠一会
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Task2.blocker prodAll()");
        //唤醒Task2中的任务
        Task2.sBlocked.prodAll();

        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("\nShutting down ");

        executorService.shutdownNow();

    }
}
