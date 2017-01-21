package my_chap21.p21_3.p21_5;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * BlockingQueue的另外一个例子,模拟了生产-消费者的过程,一生产就消费掉
 *
 * Created by Benjious on 2017/1/11.
 */
class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toast {
    public enum Statue {DRY, BUTTERD, JAMMER}

    private Statue mStatue = Statue.DRY;
    private final int id;

    public Toast(int id) {
        this.id = id;
    }

    public void butter() {
        mStatue = Statue.BUTTERD;
    }

    public void jammer() {
        mStatue = Statue.JAMMER;
    }

    public int getId() {
        return id;
    }

    public Statue getStatue() {
        return mStatue;
    }

    public void setStatue(Statue statue) {
        mStatue = statue;
    }

    @Override
    public String toString() {
        return "Toast" + id + ": " + mStatue;
    }
}

class Toaster implements Runnable {
    private ToastQueue mToastQueue;
    private int count = 0;
    private Random mRandom = new Random(47);
    public Toaster(ToastQueue toastQueue) {
        mToastQueue = toastQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + mRandom.nextInt(500));
                //make toast
                Toast toast = new Toast(count++);
                System.out.println(toast);
                //insert in queue
                mToastQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted ");
        }
        System.out.println("Toaster off ");
    }
}
class  Butter implements Runnable{
    private ToastQueue dryQueue,butterQueue;

    public Butter(ToastQueue dryQueue, ToastQueue butterQueue) {
        this.dryQueue = dryQueue;
        this.butterQueue = butterQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            //Blocks until next piece of toast is available ;
            try {
                Toast toast = dryQueue.take();
                toast.butter();
                System.out.println(toast);
                butterQueue.put(toast);
            } catch (InterruptedException e) {
                System.out.println("Butterer interrupted ");
            }
        }
        System.out.println("Butterer off ");
    }
}

class Jammer implements Runnable {
    private ToastQueue butteredQueue,finishedQueue;

    public Jammer(ToastQueue butteredQueue, ToastQueue finishedQueue) {
        this.butteredQueue = butteredQueue;
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Toast toast = butteredQueue.take();
                toast.jammer();
                System.out.println(toast);
                finishedQueue.put(toast);
            } catch (InterruptedException e) {
                System.out.println("Jammer interrupted");
            }
        }
        System.out.println("Jammer off");
    }
}
class  Eater implements  Runnable{
    private ToastQueue finishedQueue;
    private int counter=0;
    public Eater(ToastQueue finishedQueue) {
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {

                Toast toast = finishedQueue.take();
                if (toast.getId() != counter++ || toast.getStatue() != Toast.Statue.JAMMER) {
                    System.out.println(">>>> Error : " + toast);
                    System.exit(0);
                } else {
                    System.out.println("Chomp ! "+toast);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Eater off");
        }
    }
}

public class ToastMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Toaster(dryQueue));
        executorService.execute(new Butter(dryQueue,butteredQueue));
        executorService.execute(new Jammer(butteredQueue,finishedQueue));
        executorService.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(5);
        executorService.shutdownNow();
    }
}
