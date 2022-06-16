package aoc;

import configuration.Configuration;
import util.Node;
import util.Route;
import util.TspConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc.AntWorker.pheromones;
import static aoc.AntWorker.stations;

public class AntColonyOptimization {

    static Route bestRoute;

    protected static Ant[] ants;


    protected static CyclicBarrier b;


    protected static boolean alive; //flag - as long as true the algorithm should run
    protected static AtomicInteger toMove;
    protected static AtomicInteger toCheck;

    protected static BestList betterSolutions;

    public AntColonyOptimization(String filename){
        alive = true;
        aoc.AntWorker.stations = new TspConverter().generateFromFile(filename);
        aoc.AntWorker.distMatrix = Node.generateDistanceMatrix(stations);
        aoc.AntWorker.pheromones = new Pheromone[stations.size()][stations.size()];
        ants = new Ant[stations.size()];
        //Threadpool
    }

    public Route solve() {
        init();
        //initialisieren
        //Threadpool erstellen
        int AntThreadsCount = Math.min(Runtime.getRuntime().availableProcessors(), ants.length);
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too
        AntWorker aw = new AntWorker();

        ExecutorService executor = Executors.newFixedThreadPool(AntThreadsCount);
        for (int i = 0; i < AntThreadsCount; i++) {
            executor.submit(aw::run);
        }

        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);
        try {
            for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){
                b.await();
                //aw.move(); //Ants Move - in worker threads
                b.await();

                //aw.pheromon(); //Pheromone - in worker threads
                b.await();

                //aw.best(); //possibleBest - in worker threads
                b.await();
                updateBest();
                clearList();
            }
            alive = false;

            b.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        executor.shutdown();
        try {
            boolean terminated = executor.isTerminated();
            if (!terminated) {
                terminated = executor.awaitTermination(10, TimeUnit.SECONDS);
            }

            if (!terminated) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Could not shutdown");
        }

        return bestRoute;
    }

    private void init() {
        for (Pheromone[] pheromone : pheromones) {
            Arrays.fill(pheromone, new Pheromone());
        }
        for (int i = 0; i < ants.length; i++) {
            ants[i] = new Ant(stations.size());
        }
        betterSolutions = new BestList();
        bestRoute = new Route();
    }

    private void updateBest() {
        for (Route best : betterSolutions.bests) {
            if (best.getLength() < bestRoute.getLength()) bestRoute = best;
        }
        betterSolutions.bests.clear();
    }

    private void clearList() {
        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);

    }
}
