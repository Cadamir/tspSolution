package brute_force;

import util.Node;
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
        best = calculatePathLength(nodes);
        return best;
    }

    public double solveRandom(int limit) {
        best = 2000000;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        return best;
    }

    public double calculatePathLength(ArrayList<Node> nodes) {
        double distance = 0;
        int i = 0;
        while(i < nodes.size() - 1) {
            distance = distance + nodes.get(i).distance(nodes.get(i+1));
            i++;
        }
        distance = distance + nodes.get(i).distance(nodes.get(0)); // last node back to first node
        return distance;
    }

    public ArrayList<Node> shuffleNodes(ArrayList<Node> nodes) {
        ArrayList<Node> route = new ArrayList<>(nodes);
        Collections.shuffle(route);
        return route;
    }
}
