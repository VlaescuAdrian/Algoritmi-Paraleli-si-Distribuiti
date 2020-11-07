package queensProblem;

import java.util.concurrent.ExecutorService;

public class MyRunnable implements Runnable {
    
    private ExecutorService tpe;
    private final int[] positions;
    private final int crtStep;

    MyRunnable(ExecutorService tpe, int[] positions, int crtStep) {
        this.tpe        = tpe;
        this.positions  = positions;
        this.crtStep    = crtStep;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
        if (crtStep == Main.N) {
            printQueens();
            tpe.shutdownNow();
        }
        
        for (int i = 0; i < Main.N; ++i) {
            int[] newPositions = positions.clone();
            newPositions[crtStep] = i;

            if (correctPlacement(newPositions)) {
                tpe.submit(new MyRunnable(tpe, newPositions, crtStep + 1));
            }
        }
        
    }

    private boolean correctPlacement(int[] pos) {
        // TODO Auto-generated method stub
        for (int i = 0; i < crtStep; ++i) {
            for (int j = i + 1; j <= crtStep; ++j) {
                if (pos[i] == pos[j] || i - pos[i] == j - pos[j] || i + pos[i] == j + pos[j]) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private void printQueens() {
        // TODO Auto-generated method stub
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < Main.N; ++i) {
            for (int j = 0; j < Main.N; ++j) {
                if (j == positions[i]) {
                    buffer.append("Q");
                } else {
                    buffer.append("-");
                }
            }

            buffer.append("\n");
        }

        System.out.println(buffer);
        
    }

}
