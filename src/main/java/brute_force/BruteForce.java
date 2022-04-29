package brute_force;

import util.Node;
import util.TspConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BruteForce {
    private Node[] nodes= new Node[280];
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

    public double calculatePathLength(Node[] nodes) {
        double distance = 0;
        int i = 0;
        while(i < nodes.length- 1) {
            distance = distance + nodeDistance(nodes[i], nodes[i+1]);
            i++;
        }
        distance = distance + nodeDistance(nodes[i], nodes[0]); // last node back to first node
        return distance;
    }

    public double nodeDistance(Node node1, Node node2) {
        return Math.round(Math.abs(Math.sqrt(
                (node1.pos().x() - node2.pos().x()) * (node1.pos().x() - node2.pos().x())
                        + ((node1.pos().y() - node2.pos().y()) * (node1.pos().y() - node2.pos().y())))));
    }
}
