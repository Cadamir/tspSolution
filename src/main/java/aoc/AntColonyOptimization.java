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

        int iterations = 100;
        for(int i = 0; i <= iterations; i++){
            //Ants Move
            b.await();

            //Pheromone
            b.await();

            //possibleBest
            b.await();
            updateBest();
            clearList();
        }
    }

    private void updateBest() {
    }

    private void clearList() {
    }
}
