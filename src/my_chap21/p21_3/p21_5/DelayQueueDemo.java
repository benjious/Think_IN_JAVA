package my_chap21.p21_3.p21_5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benjious on 2017/1/20.
 */
class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final int id = counter++;
    //间隙(执行-现在的时间)
    private final int delta;
    //触发执行的时间
    private final long trigger;
    //用这个List来表示Task放进去的顺序,可以和执行的顺序作比较
    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
        sequence.add(this);
    }

    public String summary() {
        return "(" + id + ": " + delta + ")";
    }

    @Override
    public void run() {
        System.out.print(this + " ");
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask task = (DelayedTask) o;
        if (trigger < task.trigger) {
            return -1;
        }
        if (trigger > task.trigger) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("[%1$-4d]", delta) + " Task " + id;
    }

    //静态内部类,且是继承了DT,
    public static class EndSentinel extends DelayedTask {
        private ExecutorService mExecutorService;

        public EndSentinel(int delayInMilliseconds, ExecutorService executorService) {
            super(delayInMilliseconds);
            mExecutorService = executorService;
        }

        //输出存放的顺序,就是List
        @Override
        public void run() {
            for (DelayedTask task : sequence
                    ) {
                System.out.println(task.summary() + "");
            }
            System.out.print(this + " Calling shutdownNow()");
            mExecutorService.shutdownNow();
        }
    }

}

class DelayTaskConsumer implements Runnable {
    private DelayQueue<DelayedTask> mDelayedTasks;

    public DelayTaskConsumer(DelayQueue<DelayedTask> delayedTasks) {
        mDelayedTasks = delayedTasks;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                mDelayedTasks.take().run();
                System.out.println("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Finished DelayedTaskConsumer");
        }
    }
}

public class DelayQueueDemo {
    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService executorService = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        //fill with tasks that have random delays;
        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(random.nextInt(5000)));
        }
        //这个是设置一个delta为5000的任务(在这里的任务延时最长),用它来停止任务,也就是最后一个任务也是DT,
        //只是它是DT的之类并且他的任务是输出存放的顺序
        queue.add(new DelayedTask.EndSentinel(5000, executorService));
        //这个是执行Queue的顺序
        executorService.execute(new DelayTaskConsumer(queue));
    }
}
