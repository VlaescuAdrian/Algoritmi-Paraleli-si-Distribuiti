import java.util.Collections;

public class SortedList implements Runnable {

    @Override
    public void run() {
        try {
            Main.s.acquire();
            Collections.sort(Main.list);
            Main.s.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
