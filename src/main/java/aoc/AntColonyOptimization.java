package aoc;

import java.util.ArrayList;
import java.util.List;

import configuration.*;
import util.Node;
import util.Route;
import util.TspConverter;

public class AntColonyOptimization {
    private final ArrayList<Node> stations;
    private final double[][] graph;
    private final double[][] trails;
    private final List<Ant> ants = new ArrayList<>();
    private final double[] probabilities;
//    public StringBuilder stringBuilder = new StringBuilder();
    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    public AntColonyOptimization(String filename) {
        stations = new TspConverter().generateFromFile(filename);
        graph = Node.generateRandomDistanceMatrix(stations); //eign. von Cmd-Line
        trails = new double[graph.length][graph.length];
        probabilities = new double[graph.length];

        for (int i = 0; i < graph.length * Configuration.INSTANCE.antFactor; i++) {
            ants.add(new Ant(graph.length));
        }
    }

    public Route solve() {
//        long runtimeStart = System.currentTimeMillis();

        setupAnts();
        clearTrails();

        for (int i = 0; i < Configuration.INSTANCE.maximumIterations; i++) {
            moveAnts();
            updateTrails();
            updateBest();
        }

//        stringBuilder.append("\nbest tour length | ").append((bestTourLength - graph.length));
//        stringBuilder.append("\nbest tour order  | ").append(Arrays.toString(bestTourOrder));
//        stringBuilder.append("\nruntime          | ").append(System.currentTimeMillis() - runtimeStart).append(" ms");

        //System.out.println(stringBuilder);
        return new Route(Route.order(stations, bestTourOrder));
    }

    private void setupAnts() {
        for (Ant ant : ants) {
            ant.clear();
            ant.visitCity(-1, Configuration.INSTANCE.randomGenerator.nextInt(graph.length));
        }
        currentIndex = 0;
    }

    private void moveAnts() {
        for (int i = currentIndex; i < graph.length - 1; i++) {
            for (Ant ant : ants) {
                ant.visitCity(currentIndex, selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    private int selectNextCity(Ant ant) {
        int t = Configuration.INSTANCE.randomGenerator.nextInt(graph.length - currentIndex);
        if (Configuration.INSTANCE.randomGenerator.nextDouble() < Configuration.INSTANCE.randomFactor) {
            int cityIndex = -999;

            for (int i = 0; i < graph.length; i++) {
                if (i == t && !ant.visited(i)) {
                    cityIndex = i;
                    break;
                }
            }

            if (cityIndex != -999) {
                return cityIndex;
            }
        }

        calculateProbabilities(ant);

        double total2 = 0;
        for (int i = 0; i < graph.length; i++) {
            total2 += probabilities[i];
        }

        double randomNumber = total2 * Configuration.INSTANCE.randomGenerator.nextDouble();

        double total = 0;

        for (int i = 0; i < graph.length; i++) {
            total += probabilities[i];
            if (total >= randomNumber) {
                return i;
            }
        }

        throw new RuntimeException("runtime exception | other cities");
    }

    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.01;

        for (int l = 0; l < graph.length; l++) {
            if (!ant.visited(l)) {
                if(trails[i][l] == 0)
                    trails[i][l] = Configuration.INSTANCE.initialPheromoneValue;
                pheromone += Math.pow(trails[i][l], Configuration.INSTANCE.alpha) * Math.pow(1.0 / graph[i][l], Configuration.INSTANCE.beta);
            }
        }

        for (int j = 0; j < graph.length; j++) {
            if (ant.visited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(trails[i][j], Configuration.INSTANCE.alpha) * Math.pow(1.0 / graph[i][j], Configuration.INSTANCE.beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    private void updateTrails() {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                trails[i][j] *= Configuration.INSTANCE.evaporation;
            }
        }

        for (Ant ant : ants) {
            double contribution = Configuration.INSTANCE.q / ant.trailLength(graph);
            for (int i = 0; i < graph.length - 1; i++) {
                trails[ant.trail[i]][ant.trail[i + 1]] += contribution;
            }
            trails[ant.trail[graph.length - 1]][ant.trail[0]] += contribution;
        }
    }

    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(graph);
        }

        for (Ant ant : ants) {
            if (ant.trailLength(graph) < bestTourLength) {
                bestTourLength = ant.trailLength(graph);
                bestTourOrder = ant.trail.clone();
            }
        }
    }

    private void clearTrails() {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                trails[i][j] = Configuration.INSTANCE.initialPheromoneValue;
            }
        }
    }
}
