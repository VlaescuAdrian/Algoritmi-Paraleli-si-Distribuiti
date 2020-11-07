import java.util.*;
import java.util.concurrent.*;

public class ParallelSearch extends Thread {
    private int[] arr;
    private int element, start, end, tid, p;
    private CyclicBarrier b;

    public ParallelSearch(int[] arr, int element, int start, int end, int tid, int p, CyclicBarrier b) {
        this.arr = arr;
        this.element = element;
        this.start = start;
        this.end = end;
        this.tid = tid;
        this.b = b;
        this.p = p;
    }

    public static int search (int[] arr, int element, int start, int end) {
        while (start <= end) {
            int middle = start + (end - start) / 2;
            if (arr[middle] == element) {
                return middle;
            } else if (arr[middle] > element) {
                end = middle - 1;
            } else if (arr[middle] < element) {
                start = middle + 1;
            }
        }
        return -1;
    }

    @Override
    public void run() {
        int position = -1;
        int size = end - start;
        int left = start + (tid * size / p);
        int right = Math.min(end - 1, (start + ((tid + 1) * size / p)));

        try {
            b.await();
        } catch (InterruptedException| BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (start < end - 1 && position == -1) {
            try {
                b.await();
            } catch (InterruptedException| BrokenBarrierException e) {
                e.printStackTrace();
            }

            if (arr[left] < element && element < arr[right]) {
                start = left;
                end = right;
            }
            
            if (element ==  arr[left]) {
                position = left;
            }
            
            if (element == arr[right]) {
                position = right;
            }

            try {
                b.await();
            } catch (InterruptedException| BrokenBarrierException e) {
                e.printStackTrace();
            }

            left = start + (tid * size / p);
            right = Math.min(end - 1, (start + ((tid + 1) * size / p)));
        }
        System.out.println(position);
        
        try {
            b.await();
        } catch (InterruptedException| BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}