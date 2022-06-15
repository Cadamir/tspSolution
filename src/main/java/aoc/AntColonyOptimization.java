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

    Route bestRoute;

    protected static Ant[] ants;


    protected static CyclicBarrier b;


    protected static boolean alive; //flag - as long as true the algorithm should run
    protected static AtomicInteger toMove;

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
        try {
            for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){
                b.await();
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

            b.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
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

    }

    private void updateBest() {
        //U
        Route route = new Route(); //Todo Remove
        route.addNode(stations.get(1));//TODO remove
        bestRoute = route;//Todo Remove
    }

    private void clearList() {
        toMove = new AtomicInteger(ants.length);

    }
}
