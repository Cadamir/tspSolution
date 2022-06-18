package aoc;

import configuration.Configuration;
import util.Node;
import util.Route;
import util.TspConverter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static aoc.AntWorker.pheromones;
import static aoc.AntWorker.stations;

public class AntColonyOptimization {

    protected static final Logger LOGGER = Logger.getLogger("AocLog");
    FileHandler logHandler;
    static Route bestRoute = new Route();

    protected static Ant[] ants;


    protected static CyclicBarrier b;


    protected static boolean alive; //flag - as long as true the algorithm should run
    protected static AtomicInteger toMove;
    protected static AtomicInteger toCheck;
    protected static AtomicInteger toSmell;

    protected static BestList bestSolutions;

    public AntColonyOptimization(String filename){
        URL resource = getClass().getResource("../aocLog.log");
        File file1;
        try {
            if (resource != null) {
                file1 = new File(resource.toURI());
            } else {
                throw new RuntimeException("resource could not be found");
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            logHandler = new FileHandler(file1.getAbsolutePath());
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(logHandler);
        } catch (IOException e) {
            LOGGER.warning("Could not ");
        }
        alive = true;
        if (Configuration.INSTANCE.logOn) LOGGER.info("Generating Nodes from file");
        aoc.AntWorker.stations = new TspConverter().generateFromFile(filename);
        if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Generating Distance Matrix");
        aoc.AntWorker.distMatrix = Node.generateDistanceMatrix(stations);
        aoc.AntWorker.pheromones = new Pheromone[stations.size()][stations.size()];
        ants = new Ant[(int) (stations.size() * Configuration.INSTANCE.antFactor)];
    }

    public Route solve() {
        System.out.println("Starting ACO Algorithm");
        init();
        int AntThreadsCount = Math.min(Runtime.getRuntime().availableProcessors(), ants.length);
        if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Initialize ACO with " + AntThreadsCount + " threads");
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too

        ExecutorService executor = Executors.newFixedThreadPool(AntThreadsCount);
        for (int i = 0; i < AntThreadsCount; i++) {
            AntWorker aw = new AntWorker();
            executor.submit(aw::run);
        }
        executor.shutdown();

        try {
            if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Starting Algorithm with " + Configuration.INSTANCE.maximumIterations + " Iterations.");
            for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){
                LOGGER.log(Level.INFO, "Starting new Iteration.  " + i + " Iterations to go");
                //AntWorker aw = new AntWorker();
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

            if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Algorithm ended after " + Configuration.INSTANCE.maximumIterations + " Iterations with " + bestRoute.getLength() + " for the route :" + bestRoute.routeToString());
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
        if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Initiating Pheromone matrix, Ants and help variables.");
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
        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);
        toSmell = new AtomicInteger(bestSolutions.maxSize);
    }

    private void evaporate() {
        if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Evaporating by Factor: " + Configuration.INSTANCE.evaporation);
        for (Pheromone[] pheromone : pheromones) {
            for (int i = pheromone.length - 1; i >= 0 ; i--) {
                pheromone[i].strength -= pheromone[i].strength * Configuration.INSTANCE.evaporation;
            }
        }

    }

    private void updateBest() {
        //U
        if (bestSolutions.bests.get(0).getLength() < bestRoute.getLength()) {
            bestRoute = bestSolutions.bests.get(0);
            if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "New best Route is " + bestRoute.getLength() + " for the route :" + bestRoute.routeToString());
        }
    }

    private void clearList() {
        if (Configuration.INSTANCE.logOn) LOGGER.log(Level.INFO, "Clearing Iteration variables");
        bestSolutions.clear();
        toMove = new AtomicInteger(ants.length);
        toCheck = new AtomicInteger(ants.length);
        toSmell = new AtomicInteger(bestSolutions.maxSize);
    }
}
