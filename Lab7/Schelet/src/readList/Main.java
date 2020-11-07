import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
	static ArrayList<Integer> list = new ArrayList<Integer>();
	static Semaphore s;
    public static void main (String[] args) {
	    Thread threads[] = new Thread[4];
	    System.out.println("Reading - sorting problem");

	    s = new Semaphore(-2);

	    threads[0] = new Thread(new ReadList("elemente1.txt"));
	    threads[1] = new Thread(new ReadList("elemente2.txt"));
	    threads[2] = new Thread(new ReadList("elemente3.txt"));
	    threads[3] = new Thread(new SortedList());

	    for (int i = 0; i < 4; i++)
		    threads[i].start();
	    for (int i = 0; i < 4; i++) {
		    try {
			    threads[i].join();
		    } catch (InterruptedException e) {
			    e.printStackTrace();
		    }
	    }

	    for (int i = 0; i < list.size(); i++) {
		    System.out.println(list.get(i) + " ");
	    }

    }
}