package my_chap21.p21_3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventChecker implements Runnable {
    private IntGenerator generator;
    private final int id;

    public EventChecker(IntGenerator generator, int ident) {
        this.generator = generator;
        id = ident;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!generator.isCanceled()) {
            int val = generator.next();
            //当出现单数的时候
            if (val % 2 != 0) {
                System.out.println(val + "not even!");
                generator.cancel();
            }
        }
    }

    public static void test(IntGenerator generator, int count) {
        System.out.println("press control-c to exit ");
        ExecutorService executorService = Executors.newCachedThreadPool();
        //这里用的同一个generator,
        for (int i = 0; i < count; i++) {
            executorService.execute(new EventChecker(generator, i));
        }
        executorService.shutdown();
    }

    public static void test(IntGenerator generator) {
        test(generator, 10);

    }

}
