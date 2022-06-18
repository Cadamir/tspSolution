package aoc;

import configuration.Configuration;
import util.Node;
import util.Route;
import util.TspConverter;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static aoc.AntWorker.pheromones;
import static aoc.AntWorker.stations;

public class AntColonyOptimization {

    static Route bestRoute = new Route();

    protected static Ant[] ants;


    protected static CyclicBarrier b;


    protected static boolean alive; //flag - as long as true the algorithm should run
    protected static AtomicInteger toMove;
    protected static AtomicInteger toCheck;
    protected static AtomicInteger toSmell;

    protected static BestList bestSolutions;

    public static void main(String... args){
        AntColonyOptimization aco = new AntColonyOptimization("tsp280");
        Route best = aco.solve();
        System.out.println("Length: " + best.getLength() + " - Route: " + best.routeToString());
    }

    public AntColonyOptimization(String filename){
        alive = true;
        aoc.AntWorker.stations = new TspConverter().generateFromFile(filename);
        aoc.AntWorker.distMatrix = Node.generateDistanceMatrix(stations);
        aoc.AntWorker.pheromones = new Pheromone[stations.size()][stations.size()];
        ants = new Ant[(int) (stations.size() * Configuration.INSTANCE.antFactor)];
    }

    public Route solve() {
        init();
        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);
        toSmell = new AtomicInteger(bestSolutions.maxSize);
        int AntThreadsCount = Math.min(Runtime.getRuntime().availableProcessors(), ants.length);
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too

        ExecutorService executor = Executors.newFixedThreadPool(AntThreadsCount);
        for (int i = 0; i < AntThreadsCount; i++) {
            AntWorker aw = new AntWorker();
            executor.submit(aw::run);
        }
        executor.shutdown();

        try {
            for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){
                AntWorker aw = new AntWorker();
                b.await();
                //aw.move(); //Ants Move - in worker threads
                b.await();
                //aw.best(); //possibleBest - in worker threads
                b.await();
                evaporate();
                b.await();
                //aw.pheromon(); //Pheromone - in worker threads
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
            for (int i = pheromone.length-1; i >= 0; i--) {
                pheromone[i] = new Pheromone();
            }
        }
        for (int i = 0; i < ants.length; i++) {
            ants[i] = new Ant(stations.size());
        }
        bestSolutions = new BestList();
        bestRoute = new Route();
    }

    private void evaporate() {
        for (Pheromone[] pheromone : pheromones) {
            for (int i = pheromone.length - 1; i >= 0 ; i--) {
                pheromone[i].strength -= pheromone[i].strength * Configuration.INSTANCE.evaporation;
            }
        }

    }

    private void updateBest() {
        //U
        if (bestSolutions.bests.get(0).getLength() < bestRoute.getLength()) bestRoute = bestSolutions.bests.get(0);
    }

    private void clearList() {
        bestSolutions.clear();
        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);
        toSmell = new AtomicInteger(bestSolutions.maxSize);
    }
}
