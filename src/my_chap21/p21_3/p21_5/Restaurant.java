package my_chap21.p21_3.p21_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benjious on 2017/1/5.
 */
class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal" + orderNum;
    }
}

class WaitPerson implements Runnable {
    private Restaurant mRestaurant;

    public WaitPerson(Restaurant restaurant) {
        mRestaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (mRestaurant.mMeal == null) {
                        wait();
                    }
                    //服务员拿到饭了
                    System.out.println("WaitPreson got " + mRestaurant.mMeal);
                    //通知厨师再做
                    synchronized (mRestaurant.mChef) {
                        mRestaurant.mMeal= null;
                        mRestaurant.mChef.notifyAll();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}

class Chef implements Runnable {
    private Restaurant mRestaurant;
    private int count = 0;

    public Chef(Restaurant restaurant) {
        mRestaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (mRestaurant.mMeal != null) {
                        wait();
                    }
                }
                if (++count == 10) {
                    System.out.println("Out of food ,closing ");
                    mRestaurant.executorService.shutdownNow();
                }
                System.out.println("Order up !");
                //做好饭了
                synchronized (mRestaurant.mWaitPerson) {
                    mRestaurant.mMeal = new Meal(count);
                    mRestaurant.mWaitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrupted");
        }
    }
}

/**
 * Restaurant 中放着 Meal          饭
 * Chef           厨师
 * WaitPerson     服务员
 */
public class Restaurant {
    Meal mMeal;
    ExecutorService executorService = Executors.newCachedThreadPool();
    WaitPerson mWaitPerson = new WaitPerson(this);
    Chef mChef = new Chef(this);

    public Restaurant() {
        executorService.execute(mChef);
        executorService.execute(mWaitPerson);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
