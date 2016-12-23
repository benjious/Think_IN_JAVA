package my_chap21.p21_3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Benjious on 2016/12/6.
 */
public class AttemptLocking {
    private ReentrantLock mReentrantLock = new ReentrantLock();

    private void untimed() {
        //尝试获取锁
        boolean captured = mReentrantLock.tryLock();
        try {
            System.out.println("tryLock() : " + captured);
        } finally {
            if (captured)
                mReentrantLock.unlock();
        }
    }

    private void timed() {
        boolean captured = false;
        try {

            captured = mReentrantLock.tryLock(2, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("tryLock(2, TimeUnit.SECONDS): " + captured);
        } finally {
            if (captured)
                mReentrantLock.unlock();
        }
    }


    public static void main(String[] args) {
        final AttemptLocking attemptLocking = new AttemptLocking();
        attemptLocking.untimed();
        attemptLocking.timed();
        new Thread(){
            {setDaemon(true);}

            @Override
            public void run() {
                attemptLocking.mReentrantLock.lock();
                System.out.println("acquired");
            }
        }.start();
        Thread.yield();
        attemptLocking.untimed();
        attemptLocking.timed();
    }
}
