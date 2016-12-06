package my_chap21.p21_3.p21_2;

import java.util.concurrent.Callable;

public class TaskWithResult  implements Callable<String>{
	
	private int id;
	public TaskWithResult(int id) {
		this.id=id;
		// TODO Auto-generated constructor stub
	}
	
	//call()������ʾ���ص�ֵ
	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return "result of TaskWithResult "+id;
	}

}
