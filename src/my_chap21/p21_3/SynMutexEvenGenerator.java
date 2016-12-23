package my_chap21.p21_3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Benjious on 2016/12/6.
 */
public class SynMutexEvenGenerator extends  IntGenerator {
    private int currentEventValue = 0;
    private Lock mLock = new ReentrantLock();

    @Override
    public synchronized int next() {
        mLock.lock();
        try {
            ++currentEventValue;
            Thread.yield();
            return currentEventValue;
        } finally {
            mLock.unlock();
        }

    }
    public static void main(String[] args) {
        EventChecker.test(new SynMutexEvenGenerator());
    }
}
