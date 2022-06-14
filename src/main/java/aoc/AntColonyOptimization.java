package aoc;

import java.util.concurrent.*;

public class AntColonyOptimization {

    public AntColonyOptimization(){
        //Threadpool
    }

    public void run() throws BrokenBarrierException, InterruptedException {
        //initialisieren
        //Threadpool erstellen
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int azAnts = 10;
        CyclicBarrier b = new CyclicBarrier(1+azAnts);

        toMove = new AtomicInteger(ant.length);
        for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){

            //Ants Move - in ant threads
            b.await();

            //Pheromone - in ant threads
            b.await();

            //possibleBest - in ant threads
            b.await();
            updateBest();
            clearList();
        }
    }

    private void updateBest() {
        //U
    }

    private void clearList() {
    }
}
