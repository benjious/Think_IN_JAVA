package my_chap21.p21_3;

/**
 * Created by Benjious on 2016/12/5.
 */
public class EvenGenerator extends IntGenerator {
    private int currentEventValue = 0;

    @Override
    public int next() {
        ++currentEventValue;
        Thread.yield();
        //为什么这个放多个yield()方法就会快速地结束呢?
        // 这个因为我总是让步给其他线程执行,那么我加了一,对方在加一,等我在加一是,就是三了,就会出现单数
        ++currentEventValue;
        return currentEventValue;
    }

    public static void main(String[] args) {
        EventChecker.test(new EvenGenerator());
    }
}
