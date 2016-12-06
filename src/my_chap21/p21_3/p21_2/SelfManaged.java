package my_chap21.p21_3.p21_2;

public class SelfManaged implements Runnable{
	private int countDown=5;
	private Thread thread=new Thread(this);
	public SelfManaged() {
		// TODO Auto-generated constructor stub
		thread.start();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Thread.currentThread().getName()+"("+countDown+"). ";
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (true) {
			System.out.println(this);
			if(--countDown==0)
				return;
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new SelfManaged();
		}
	}
	

}
