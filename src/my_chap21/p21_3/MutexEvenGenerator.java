package my_chap21.p21_3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用显式的Lock对象
 * Created by Benjious on 2016/12/6.
 */
public class MutexEvenGenerator extends IntGenerator {
    private int currentEventValue = 0;
    private Lock mLock = new ReentrantLock();

    @Override
    public int next() {
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
        EventChecker.test(new MutexEvenGenerator());
    }
}
