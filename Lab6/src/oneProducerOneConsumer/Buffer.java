package oneProducerOneConsumer;
import java.util.*;
/**
 * @author cristian.chilipirea
 *
 */
public class Buffer {
	private ArrayList<Integer> buffer = new ArrayList<>();
	private int maxSize = 100, currentSize = 0;
	int a;

	synchronized void put(int value) {
		while (currentSize == maxSize) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		currentSize++;
		buffer.add(value);
		notifyAll();
	}

	synchronized int get() {
		while (currentSize == 0) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		currentSize--;
		a = buffer.get(0);
		buffer.remove(0);
		notifyAll();
		return a;
	}
}
