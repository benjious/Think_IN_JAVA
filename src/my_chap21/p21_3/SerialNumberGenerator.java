package my_chap21.p21_3;

/**
 * Created by Benjious on 2016/12/6.
 */
public class SerialNumberGenerator  {
    private static volatile  int serialNumer=0;

    public static int nextSerialNumber() {
        return  serialNumer++;
    }
}
