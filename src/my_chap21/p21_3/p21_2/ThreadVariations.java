package my_chap21.p21_3.p21_2;

import java.util.concurrent.TimeUnit;

class InnerThread1 {
	private int countDown = 5;
	private Inner inner;

	private class Inner extends Thread {
		Inner(String name) {
			// TODO Auto-generated constructor stub
			super(name);
			start();
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(this);
					if (--countDown == 0) {
						return;
					}
					sleep(10);
				}
			} catch (InterruptedException e) {
				System.out.println("interrupted");

			}
		}

		@Override
		public String toString() {
			return getName() + ": " + countDown;
		}
	}

	public InnerThread1(String name) {
		inner = new Inner(name);

	}
}

class InnerThread2 {
	private int countDown = 5;
	private Thread thread;

	// ����һ��Thread����,���ڹ��췽���ж���
	public InnerThread2(String name) {
		thread = new Thread(name) {
			@Override
			public void run() {
				try {
					System.out.println(this);
					if (--countDown == 0) {
						return;
					}
					sleep(10);
				} catch (InterruptedException e) {
					System.out.println("sleep() interrupted");
				}
			}

			@Override
			public String toString() {
				return getName() + " " + countDown;
			}
		};
		thread.start();
	}

}

// ͬ��Ҳ�Ǳ���һ��Thread����,������һ�����з�����,ע�ⷽ������,�߳̾ͽ�����,ʧȥ���þͻᱻ����
class ThreadMethod {
	private int countDown = 5;
	private Thread t;
	private String nameString;

	public ThreadMethod(String name) {
		this.nameString = name;
		// TODO Auto-generated constructor stub
	}

	public void runTask() {
		if (t == null) {
			t = new Thread(nameString) {
				public void run() {
					try {
						while (true) {
							System.out.println(this);
							if (--countDown == 0) {
								return;
							}
							sleep(10);
						}
					} catch (InterruptedException e) {
						System.out.println("sleep() interrupted");
						// TODO: handle exception
					}
				}

				@Override
				public String toString() {
					return t.getName() + " " + countDown;
				}
			};
			t.start();
		}
	}
}

// ����һ���ڲ���(Runnable����),��������ڲ����б���һ��
// Thread����,���ڲ���Ĺ��췽���н��Լ���Thread,�����һ��Thread��start
class InnerRunnable1 {
	private int countDown = 5;
	private Inner inner;

	private class Inner implements Runnable {
		Thread thread;

		Inner(String name) {
			thread = new Thread(this, name);
			thread.start();
			// TODO Auto-generated constructor stub
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(this);
					if (--countDown == 0) {
						return;
					}
					TimeUnit.MILLISECONDS.sleep(10);
				}
			} catch (InterruptedException e) {
				System.out.println("sleep() interrupted");
				// TODO: handle exception
			}
		}

		@Override
		public String toString() {
			return thread.getName() + " " + countDown;
		}
	}
	public InnerRunnable1(String name) {
		inner=new Inner(name);
	}
}

public class ThreadVariations {
	public static void main(String[] args) {
		new InnerThread1("InnerThread1");
		System.out.println("-------------------------");
		new InnerThread2("InnerThread2");
		System.out.println("-------------------------");
		new InnerRunnable1("InnerRunnable1");
		System.out.println("-------------------------");
		new ThreadMethod("ThreadMethod").runTask();
	}
}
