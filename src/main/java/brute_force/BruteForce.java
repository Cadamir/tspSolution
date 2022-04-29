package brute_force;

import util.Node;
import util.Route;
import util.TspConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BruteForce {
    private final ArrayList<Node> nodes;
    private double best = 2000000;

    public BruteForce(String file) {
        nodes = new TspConverter().generateFromFile(file);
    }

    public double solveLinear(){
        best = 2000000;
        Route route = new Route(nodes);
        best = route.length();
        return best;
    }

    public double solveRandom(int limit) {
        best = 2000000;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            /* Route route = new Route(nodes);
             * route.shuffle();
             * route.length();
             *
             */

        return best;
    }
}
