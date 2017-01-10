package my_chap21.p21_3.p21_4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReetrantLock锁的用法
 * Created by Benjious on 2016/12/23.
 */
class BlockedMutex {
    private Lock mLock = new ReentrantLock();

    public BlockedMutex() {
        //一开始就锁上
        mLock.lock();
    }

    public void f() {
        try {
            mLock.lockInterruptibly();
        } catch (InterruptedException e) {
            //在获得锁的过程中被中断
            System.out.println("Interrupted from lock acquisition in f()");
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex mBlockedMutex=new BlockedMutex();


    @Override
    public void run() {
        System.out.println("Waiting for f() in BlockedMutex");
        mBlockedMutex.f();
        System.out.println("Broken out of blocked call");
    }
}

public class Interrupting2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Blocked2());
        thread.start();
        //给时间给线程操作
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println("Issuing thread.interrupt()");
        thread.interrupt();

    }
}
