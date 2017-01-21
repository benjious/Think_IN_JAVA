package my_chap21.p21_3.p21_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import my_chap21.p21_3.p21_2.LiftOff;

/**
 * 这个例子是使用了 BlockingQueue(接口) ,
 * 队列     : (queue)  LinkedBlockingQueue,ArrayBlockingQueue
 * 双段队列 : (deque)  LinkedBlockingDeque,ArrayBlockingDeque
 * <p>
 * BlockingQueue支持 生产-消费者 模式,当有时再唤醒,他更加方便,放进去直接执行(唤醒被封装在底层,
 * 简单的生产-消费者模式 则是需要唤醒操作),没有就阻塞,
 * <p>
 * SynchronousQueue( BQ的子类 ) :他不会为队列中元素维护储存空间,他维护一组线程,即:有任务马上交付处理
 * 用处: 当有足够多的消费者,并且总是有一个消费者准备好交付的工作时,才适合使用同步线程
 * <p>
 * Created by Benjious on 2017/1/9.
 */
class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> queue) {
        rockets = queue;
    }

    public void add(LiftOff liftOff) {
        try {
            rockets.put(liftOff);
        } catch (InterruptedException e) {
            System.out.println("Interrupted during put()");
        }

    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                LiftOff liftOff = rockets.take();
                liftOff.run();
            } catch (InterruptedException e) {
                System.out.println("Waking from take()");
            }
        }
        System.out.println("Exiting LiftOffRunner");

    }
}

public class TestBlockingQueues {
    static void getKey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void getKey(String message) {
        System.out.println(message);
        getKey();
    }

    static void test(String msg, BlockingQueue<LiftOff> queue) {
        System.out.println(msg);
        LiftOffRunner offRunner = new LiftOffRunner(queue);
        Thread thread = new Thread(offRunner);
        thread.start();
        for (int i = 0; i < 5; i++) {
            offRunner.add(new LiftOff(5));
        }

        //getKey()方法中涉及到IO(要我们输入东西到控制台),但我们输入完,线程中断继续执行
        getKey("Press 'Enter (" + msg + ")");
        thread.interrupt();
        System.out.println("Finished " + msg + " test");

    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue", new LinkedBlockingQueue<LiftOff>());
        test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));
        test("SynchronousQueue", new SynchronousQueue<LiftOff>());

    }
}
