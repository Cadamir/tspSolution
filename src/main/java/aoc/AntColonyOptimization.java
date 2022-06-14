package aoc;

import configuration.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AntColonyOptimization {

    protected static AntWorker[] ant;
    protected static CyclicBarrier b;

    protected static boolean alive; //flag - as long as true the

    protected static AtomicInteger toMove;

    public AntColonyOptimization(){
        alive = true;
        //Threadpool
    }

    public void run() throws BrokenBarrierException, InterruptedException {
        //initialisieren
        //Threadpool erstellen
        int AntThreadsCount = Math.min(Runtime.getRuntime().availableProcessors(), ant.length);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AntThreadsCount);
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too

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
        alive = false;
    }

    private void updateBest() {
        //U
    }

    private void clearList() {
        toMove = new AtomicInteger(ant.length);

    }
}
