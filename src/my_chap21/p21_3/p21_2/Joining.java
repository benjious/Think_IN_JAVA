package my_chap21.p21_3.p21_2;

class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleepTime) {
		super(name);
		duration = sleepTime;
		start();
	}

	@Override
	public void run() {
		try {
			//sleep����1.5��
			sleep(duration);
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println(getName() + "was interrupted. "
					+ "isInterrupted(): " + isInterrupted());
			return;
		}
		System.out.println(getName()+" has awakened");
	}
}






class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//�����sleeper����join,�ø������߳�ִ��
			sleeper.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Interrupted");
		}
		System.out.println(getName() + " join completed");
	}
}

//Ҳ����˵��Ҫ�Ǳ��ж���(interrupted),�Ͳ�����
public class Joining {
	public static void main(String[] args) {
		//���췽��������ִ���߳���,run(),��������Sleeper�߳̾ͽ���������
		Sleeper sleeper=new Sleeper("Sleepy", 2000);
		Sleeper grumpy=new Sleeper("Grump",1500);
		//�����������߳�join,�Լ��ȴ�,��λ�������߳���ִ��,
		Joiner dopey=new Joiner("Dopey",sleeper);
		Joiner doc=new Joiner("Doc",grumpy);
		//Sleeper�߳��ж�
		grumpy.interrupt();
	}
}
