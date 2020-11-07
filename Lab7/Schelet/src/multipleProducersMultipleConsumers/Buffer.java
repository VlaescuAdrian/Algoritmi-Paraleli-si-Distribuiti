import java.util.*;
import java.util.concurrent.*;

public class Buffer {
	ArrayList<Integer> a = new ArrayList<>();
	volatile int current = 0;
	static Semaphore semPut = new Semaphore(5); 
	static Semaphore semGet = new Semaphore(0);

	static Semaphore semWork = new Semaphore(1);

	void put(int value) {
		try {
			semPut.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
				semWork.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		a.add(value);
		current ++;
		semWork.release();
		semGet.release();
	}

	int get() {
		int ret = 0;
		try {
			semGet.acquire();
		} catch (InterruptedException e) {
				e.printStackTrace();
		}

		try {
			semWork.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ret = a.remove(0);
		current --;
 		semWork.release();
 		semPut.release();

		return ret;

	}
}