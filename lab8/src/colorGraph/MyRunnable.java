package colorGraph;

import java.util.concurrent.ExecutorService;

public class MyRunnable implements Runnable {
    private ExecutorService tpe;
    private final int[][] graph;
    private final int[] colours;
    private final int crtStep;

    MyRunnable(ExecutorService tpe, int[][] graph, int[] colours, int crtStep) {
        this.tpe        = tpe;
        this.graph      = graph;
        this.colours    = colours;
        this.crtStep    = crtStep;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
       
            if (crtStep == Main.N) {
                printColors(colours);
                tpe.shutdownNow();
            }

            // for the node at position step try all possible colors
            for (int i = 0; i < Main.COLORS; i++) {
                int[] newColors = colours.clone();
                newColors[crtStep] = i;
                if (verifyColors(newColors)) {
                    tpe.submit(new MyRunnable(tpe, graph, newColors, crtStep + 1));
                }
            }
        }

    private boolean verifyColors(int[] newColors) {
        // TODO Auto-generated method stub
        for (int i = 0; i < crtStep; i++) {
            if (newColors[i] == newColors[crtStep] && isEdge(i, crtStep)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEdge(int a, int b) {
        // TODO Auto-generated method stub
        
        for (int[] edge : graph) {
            if (edge[0] == a && edge[1] == b) {
                return true;
            }
        }
        return false;
    }

    private void printColors(int[] colours2) {
        // TODO Auto-generated method stub
        StringBuilder buffer = new StringBuilder();

        for (int colour : colours) {
            buffer.append(colour).append(" ");
        }

        System.out.println(buffer);
        
    }
        
    }


