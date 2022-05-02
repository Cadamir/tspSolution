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
    private Route gBest;
    private int counter;
    private final Object bestMutex = new Object();
    private boolean stop;

    public BruteForce(String file) {
        nodes = new TspConverter().generateFromFile(file);
        gBest = new Route();
    }

    public Route solveLinear(){
        gBest = new Route();
        stop = false;
        //TODO
        return gBest;
    }

    public Route solveRandom(int limit) {
        counter = limit;
        gBest = new Route();
        stop = false; // threat termination flag
        int threadsCount = Math.min(Runtime.getRuntime().availableProcessors(), limit);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0 ; i < threadsCount ; i++) {
            executorService.submit(this::solveRandomPermutations);
        }

        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(1, TimeUnit.MINUTES); //maximal runtime
            stop = true;
            if (!terminated) {
                terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
            }

            if (!terminated) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("BruteForce got canceled");
        }

        return gBest;
    }

    private void solveRandomPermutations() {
        Route pBest = new Route();
        while (counter > 0 && !stop) {
            Route route = new Route(nodes);
            route.shuffle();
            if (route.getLength() < pBest.getLength()) {
                pBest = route;
            }
            decreaseCounter();
        }
        synchronized (bestMutex) {
            if (pBest.getLength() < gBest.getLength()) {
                gBest = pBest;
            }
        }
    }
    synchronized void decreaseCounter() {
        counter--;
    }
}
