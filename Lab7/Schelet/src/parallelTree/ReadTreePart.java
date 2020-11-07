package parallelTree;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

/**
 * @author cristian.chilipirea
 *
 */
public class ReadTreePart implements Runnable {
	TreeNode tree;
	String fileName;
	BufferedReader br;
	Scanner sc;

	public ReadTreePart(TreeNode tree, String fileName) {
		this.tree = tree;
		this.fileName = fileName;

		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc = new Scanner(br);
	}

	@Override
	public void run() {
		while (sc.hasNext()) {
			int node = sc.nextInt();
			int parent = sc.nextInt();

			TreeNode t = tree.getNode(parent);
			while (t == null) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t = tree.getNode(parent);
			}

			t.addChild(new TreeNode(node));
		}

		try {
			Main.c.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}