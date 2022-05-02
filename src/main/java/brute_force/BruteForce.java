package brute_force;

import util.Node;
import util.Route;
import util.TspConverter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BruteForce {
    private final ArrayList<Node> nodes;
    private double gBest;
    private Route gBestRoute;
    private int limitCounter;
    private final Object bestMutex = new Object();

    public BruteForce(String file) {
        nodes = new TspConverter().generateFromFile(file);
        gBest = Double.MAX_VALUE;
    }

    public double solveLinear(){
        gBest = Double.MAX_VALUE;
        Route route = new Route(nodes);
        gBest = route.length();
        return gBest;
    }

    public double solveRandom(int limit) {
        gBest = Double.MAX_VALUE;
        int threadsCount = Math.min(Runtime.getRuntime().availableProcessors(), limit);
        limitCounter = limit / threadsCount;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0 ; i < threadsCount ; i++) {
            executorService.submit(this::solveRandomPermutations);
        }

        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES); //maximal runtime
            if (!terminated) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("BruteForce got canceled");
        }


        return gBest;
    }

    private void solveRandomPermutations() {
        int counter = 0;
        double pBest = Double.MAX_VALUE;
        Route pBestRoute = null;
        while (counter < limitCounter) {
            Route route = new Route(nodes);
            route.shuffle();
            double length = route.length();
            if (length < pBest) {
                pBest = length;
                pBestRoute = route;
            }
            counter++;
        }
        synchronized (bestMutex) {
            if (pBest < gBest) {
                System.out.println(gBest + " g <- " + pBest);
                gBest = pBest;
                gBestRoute = pBestRoute;
            }
        }
    }
}
