package my_chap21.p21_3.p21_5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benjious on 2017/1/19.
 */
class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random sRandom = new Random(47);
    private static CyclicBarrier sBarrier;

    public Horse(CyclicBarrier cyclicBarrier){
        sBarrier=cyclicBarrier;
    }

    public synchronized int getStrides() {
        return strides;
    }


    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    strides += sRandom.nextInt(3);
                }
                //等待其他线程完成
                sBarrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Horse " + id + " ";
    }

    public String tracks() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            builder.append("*");
        }
        builder.append(id);
        return builder.toString();
    }
}


public class HorseRace {
    static final int FINISH_LINE = 75;
    private List<Horse> mHorses = new ArrayList<>();
    private CyclicBarrier mBarrier;
    private ExecutorService mService = Executors.newCachedThreadPool();

    public HorseRace(int nHorses, final int pause) {
        mBarrier = new CyclicBarrier(nHorses, new Runnable() {
            //定义在CyclicBarrier中的Runnable是当所有线程执行完后才会执行的
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < FINISH_LINE; i++) {
                    builder.append("=");
                }
                System.out.println(builder);
                for (Horse horse : mHorses
                        ) {
                    System.out.println(horse.tracks());
                }
                for (Horse horse : mHorses) {
                    if (horse.getStrides() >= FINISH_LINE) {
                        System.out.println(horse + "won ! ");
                        mService.shutdownNow();
                        return;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //传过来n只马,然后放入ArrayList中,一个个执行里面的 Runnable
        for (int i=0; i<nHorses; i++) {
            Horse horse = new Horse(mBarrier);
            mHorses.add(horse);
            mService.execute(horse);
        }
        
    }
    public static void main(String[] args) {
        int nHorses=7;
        int pause=200;
        if (args.length>0) {
            int n = new Integer(args[0]);
            nHorses=n>0?n:nHorses;
        }
        if (args.length>1) {
            int p = new Integer(args[1]);
            pause = p > -1 ? p : pause;
        }
        new HorseRace(nHorses, pause);
    }
}
