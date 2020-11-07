import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class ReadList implements Runnable {
	String fileName;

	public ReadList(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(fileName));
			Scanner sc = new Scanner(br);

			while (sc.hasNextInt()) {
				int number = sc.nextInt();
				synchronized (Main.list) {
					Main.list.add(number);
				}
			}

			Main.s.release();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
