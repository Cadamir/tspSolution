package aoc;

import configuration.Configuration;
import util.Node;
import util.Route;

import java.util.Arrays;

import static aoc.AntWorker.*;

public class Ant {
    private Route route;
    private final int routeMaxSize;
    private final boolean[] visited;

    public Ant(int tourSize) {
        route = new Route();
        routeMaxSize = tourSize;
        visited = new boolean[routeMaxSize];
    }

    public void visitCity(Node nextNode) {
        if(nextNode == null)
            nextNode = selectNextCity();
        route.addNode(nextNode);
        visited[nextNode.nr()] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength() {
        return route.getLength();
    }

    public void clear() {
        route = new Route();
        Arrays.fill(visited, false);
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getRouteMaxSize() {
        return routeMaxSize;
    }

    public boolean[] getVisited() {
        return visited;
    }

    public Node selectNextCity() {
        //random city

        if (randomGenerator.nextDouble() < Configuration.INSTANCE.randomFactor) {
            int t = randomGenerator.nextInt(stations.size());
            if (!visited(t)) {
                return stations.get(t);
            }
        }

        double[] probabilities = calculateProbabilities(); //11s
        //double[] probabilities = new double[stations.size()];
        //Arrays.fill(probabilities, 1./stations.size());

        double total = 0;
        for(double d : probabilities)
            total += d;

        double randomNumber = total * randomGenerator.nextDouble();

        total = 0;

        for (int i = 0; i < stations.size(); i++) {
            total += probabilities[i];
            if (total >= randomNumber) {
                return stations.get(i);
            }
        }

        throw new RuntimeException("runtime exception | other cities");
    }

    private double[] calculateProbabilities() {
        double[] probabilities = new double[stations.size()];

        int routeSize = route.getRoute().size();
        if (routeSize==0) {
            Arrays.fill(probabilities, 1);
            return probabilities;
        }

        Node currentStation = route.getRoute().get(routeSize-1);
        double pheromone = 0.01;

        for (Node station : stations) {
            if (!visited(station.nr())) {
                pheromone += Math.pow(pheromones[currentStation.nr()][station.nr()].strength, Configuration.INSTANCE.alpha) * Math.pow(1.0 / distMatrix.get(currentStation.nr(),station.nr()), Configuration.INSTANCE.beta);
            }
        }


        for (Node station : stations) {
            double distance = Math.max(distMatrix.get(currentStation.nr(),station.nr()),0.0001);
            if (visited(station.nr())) {
                probabilities[station.nr()] = 0.0;
            } else {
                double numerator = Math.pow(pheromones[currentStation.nr()][station.nr()].strength, Configuration.INSTANCE.alpha) * Math.pow(1.0 / distance, Configuration.INSTANCE.beta);
                probabilities[station.nr()] = numerator / pheromone;
            }
        }
        return probabilities;
    }
}

