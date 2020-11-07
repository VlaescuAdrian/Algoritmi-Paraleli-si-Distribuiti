package getPathParallel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class MyRunnable implements Runnable{
    private ExecutorService tpe;
    private ArrayList<Integer> partialPath;
    private final int[][] graph;
    private final int target;

    MyRunnable(ExecutorService tpe, ArrayList<Integer> partialPath, int[][] graph, int target) {
        this.tpe        = tpe;
        this.partialPath    = partialPath;
        this.graph      = graph;
        this.target     = target;
    }

    @Override
    public void run() {
        
        if (partialPath.get(partialPath.size() - 1) == target) {
            System.out.println(partialPath);
            tpe.shutdown();
            try {
                if(!tpe.awaitTermination(1, TimeUnit.SECONDS)) {
                    tpe.shutdownNow();
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        int lastNodeInPath = partialPath.get(partialPath.size() - 1);
        
        for (int i = 0; i < graph.length; i++) {
            if (graph[i][0] == lastNodeInPath) {
                if (partialPath.contains(graph[i][1]))
                    continue;
                ArrayList<Integer> newPartialPath = (ArrayList<Integer>) partialPath.clone();
                newPartialPath.add(graph[i][1]);
                tpe.submit(new MyRunnable(tpe, newPartialPath, graph, target));
            }
        }
        
        
        
        
    }
    
    

}
