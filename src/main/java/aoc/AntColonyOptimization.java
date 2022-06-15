package aoc;

import configuration.Configuration;
import util.Node;
import util.Route;
import util.TspConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AntThreadsCount);
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too

        toMove = new AtomicInteger(ants.length);
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
