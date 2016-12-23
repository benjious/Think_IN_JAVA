package my_chap21.p21_3.p21_4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 需求: 有多个大门,现在需要统计一下每天进来的总人数
 * 每一个大门就是一个线程,
 * Created by Benjious on 2016/12/6.
 */
class Count {
    private int count = 0;
    private Random mRandom = new Random(47);

    public synchronized int increment() {
        int temp = count;
        if (mRandom.nextBoolean()) {
            Thread.yield();
        }
        return (count = ++temp);
    }

    public synchronized int value() {
        return count;
    }
}

class Entrance implements Runnable {

    //这个count是静态变量来的
    private static Count sCount = new Count();
    private static List<Entrance> sEntrances = new ArrayList<>();

    //某个门进了几个人计数
    private int number = 0;
    //标识id一般用final
    private final int id;
    private static volatile boolean canceled = false;

    public static void cancel() {
        canceled = true;
    }

    public static int getTotalCount() {
        return sCount.value();
    }

    public Entrance(int i) {
        this.id = i;
        sEntrances.add(this);
    }

    public synchronized int getValue() {
        return number;
    }

    public static int sumEntrances() {
        int sum = 0;
        for (Entrance entrance : sEntrances
                ) {
            sum += entrance.getValue();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Entrance " + id + ":  " + getValue();
    }

    /**
     * 要是没有被取消,就增加一个人,然后输入是哪个门进去的人,并sleep一段时间,
     * sleep:(1)方法是Thread的
     *       (2)sleep暂停指定的时间内,继续执行
     *       (3)不会释放对象锁
     */
    @Override
    public void run() {
        while (!canceled) {
            synchronized (this) {
                ++number;
            }
            System.out.println(this + " Total: " + sCount.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }
        }
        System.out.println("Stopping " + this);
    }
}


public class OrnamentalGarden {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new Entrance(i));
        }
        //运行一会,停止并收集数据
        TimeUnit.SECONDS.sleep(4);
        //这个每个门都关了
        Entrance.cancel();
        executorService.shutdown();
        //awaitTermination()这个方法是等待所有任务在规定时间完成任务
        if (!executorService.awaitTermination(250, TimeUnit.MILLISECONDS)) {
            System.out.println("Some tasks were not terminated!");
        }
        System.out.println("Total : " + Entrance.getTotalCount());
        System.out.println("Sum of Entrance : " +Entrance.sumEntrances());
    }
}
