package aoc_old;

import java.util.ArrayList;
import java.util.List;

import configuration.*;
import util.DistanceMatrix;
import util.Node;
import util.Route;
import util.TspConverter;

public class AntColonyOptimization {
    private final ArrayList<Node> stations;
    private final DistanceMatrix distMatrix;

    private final double[][] trails; //pheromones
    private final List<Ant> ants = new ArrayList<>();
    private final double[] probabilities;
//    public StringBuilder stringBuilder = new StringBuilder();
    private int currentIndex;

    private Route bestRoute;

    public AntColonyOptimization(String filename) {
        stations = new TspConverter().generateFromFile(filename);
        distMatrix = Node.generateDistanceMatrix(stations);
        trails = new double[stations.size()][stations.size()];
        probabilities = new double[stations.size()];

        for (int i = 0; i < stations.size() * Configuration.INSTANCE.antFactor; i++) {
            ants.add(new Ant(stations.size()));
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

//        stringBuilder.append("\nbest tour length | ").append((bestTourLength - stations.size()));
//        stringBuilder.append("\nbest tour order  | ").append(Arrays.toString(bestTourOrder));
//        stringBuilder.append("\nruntime          | ").append(System.currentTimeMillis() - runtimeStart).append(" ms");

        //System.out.println(stringBuilder);
        return new Route(bestRoute.getRoute());
    }

    private void setupAnts() {
        for (Ant ant : ants) {
            ant.clear();
            ant.visitCity(stations.get((Configuration.INSTANCE.randomGenerator.nextInt(stations.size()))));
        }
        currentIndex = 0;
    }

    private void moveAnts() {
        for (int i = currentIndex; i < stations.size() - 1; i++) {
            for (Ant ant : ants) {
                ant.visitCity(selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    private Node selectNextCity(Ant ant) {
        int t = Configuration.INSTANCE.randomGenerator.nextInt(stations.size() - currentIndex);
        if (Configuration.INSTANCE.randomGenerator.nextDouble() < Configuration.INSTANCE.randomFactor) {
            int cityIndex = -999;

            for (int i = 0; i < stations.size(); i++) {
                if (i == t && !ant.visited(i)) {
                    cityIndex = i;
                    break;
                }
            }

            if (cityIndex != -999) {
                return stations.get(cityIndex);
            }
        }

        calculateProbabilities(ant);

        double total2 = 0;
        for (int i = 0; i < stations.size(); i++) {
            total2 += probabilities[i];
        }

        double randomNumber = total2 * Configuration.INSTANCE.randomGenerator.nextDouble();

        double total = 0;

        for (int i = 0; i < stations.size(); i++) {
            total += probabilities[i];
            if (total >= randomNumber) {
                return stations.get(i);
            }
        }

        throw new RuntimeException("runtime exception | other cities");
    }

    public void calculateProbabilities(Ant ant) {
        Node currentStation = ant.getRoute().getRoute().get(currentIndex);
        double pheromone = 0.01;

        for (Node station : stations) {
            if (!ant.visited(station.nr())) {
                if(trails[currentStation.nr()][station.nr()] == 0)
                    trails[currentStation.nr()][station.nr()] = Configuration.INSTANCE.initialPheromoneValue;
                pheromone += Math.pow(trails[currentStation.nr()][station.nr()], Configuration.INSTANCE.alpha) * Math.pow(1.0 / distMatrix.get(currentStation.nr(),station.nr()), Configuration.INSTANCE.beta);
            }
        }

        for (Node station : stations) {
            if (ant.visited(station.nr())) {
                probabilities[station.nr()] = 0.0;
            } else {
                double numerator = Math.pow(trails[currentStation.nr()][station.nr()], Configuration.INSTANCE.alpha) * Math.pow(1.0 / distMatrix.get(currentStation.nr(),station.nr()), Configuration.INSTANCE.beta);
                probabilities[station.nr()] = numerator / pheromone;
            }
        }
    }

    private void updateTrails() {
        for (int i = 0; i < stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
                trails[i][j] *= Configuration.INSTANCE.evaporation;
            }
        }

        for (Ant ant : ants) {
            double contribution = Configuration.INSTANCE.q / ant.trailLength();
            for (int i = 0; i < stations.size() - 1; i++) {
                trails[ant.getRoute().getRoute().get(i).nr()][ant.getRoute().getRoute().get(i+1).nr()] += contribution;
            }
            trails[ant.getRoute().getRoute().get(stations.size() - 1).nr()][ant.getRoute().getRoute().get(0).nr()] += contribution;
        }
    }

    private void updateBest() {
        if (bestRoute == null) {
            bestRoute = ants.get(0).getRoute();
        }

        for (Ant ant : ants) {
            if (ant.trailLength() < bestRoute.getLength()) {
                bestRoute = ant.getRoute();
            }
        }
    }

    private void clearTrails() {
        for (int i = 0; i < stations.size(); i++) {
            for (int j = 0; j < stations.size(); j++) {
                trails[i][j] = Configuration.INSTANCE.initialPheromoneValue;
            }
        }
    }
}
