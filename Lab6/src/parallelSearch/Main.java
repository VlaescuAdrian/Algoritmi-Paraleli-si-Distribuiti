import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 3, 5, 6, 7, 9};
        int p = Runtime.getRuntime().availableProcessors();
        CyclicBarrier barrier = new CyclicBarrier(p);
		Thread[] threads = new Thread[p];

		for (int i = 0; i < p; ++i) {
			threads[i] = new ParallelSearch(arr, 8, 0, arr.length, i, p, barrier);
		}

		for (int i = 0; i < p; ++i) {
			threads[i].start();
		}

		for (int i = 0; i < p; ++i) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}