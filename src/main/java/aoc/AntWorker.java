package aoc;

import configuration.Configuration;
import util.Node;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

import static aoc.AntColonyOptimization.*;

public class AntWorker {

    static ArrayList<Node> stations;
    static Pheromone[][] pheromones;
    public AntWorker(){
    }
    
    public void run() throws BrokenBarrierException, InterruptedException {
        b.await();
        while(alive){
            move();
            b.await();
            pheromon();
            b.await();
            best();
            b.await();
            b.await();
        }
        
        
    }

    public void move() {
        while(true) {
            int antNr = toMove.decrementAndGet(); //TODO check - returns the actual value or the decremented value
            if (antNr < 0) return;
            Ant ant = new Ant(stations.size());
            for (int i = 0; i < stations.size(); i++) {
                ant.visitCity(selectNextCity(ant));//distance jeweils übergeben ? für schnellere Berechnung
            }
            ants[antNr] = ant;
        }
    }

    private Node selectNextCity(Ant ant) {
        //random city
        if (Configuration.INSTANCE.randomGenerator.nextDouble() < Configuration.INSTANCE.randomFactor) {
            int t = Configuration.INSTANCE.randomGenerator.nextInt(stations.size());
            if (!ant.visited(t)) {
                return stations.get(t);
            }
        }

        double[] probabilities = calculateProbabilities(ant);

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

    public double[] calculateProbabilities(Ant ant) {
        double[] probabilities = new double[stations.size()];

        int routeSize = ant.getRoute().getRoute().size();
        if (routeSize==0) {
            double average = 1 / stations.size();
            Arrays.fill(probabilities, average);
            return probabilities;
        }

        Node currentStation = ant.getRoute().getRoute().get(routeSize-1);
        double pheromone = 0.01;

        for (Node station : stations) {
            if (!ant.visited(station.nr())) {
                pheromone += Math.pow(pheromones[currentStation.nr()][station.nr()].strength, Configuration.INSTANCE.alpha) * Math.pow(1.0 / distMatrix.get(currentStation.nr(),station.nr()), Configuration.INSTANCE.beta);
            }
        }


        for (Node station : stations) {
            double distance = Math.max(distMatrix.get(currentStation.nr(),station.nr()),0.0001);
            if (ant.visited(station.nr())) {
                probabilities[station.nr()] = 0.0;
            } else {
                double numerator = Math.pow(pheromones[currentStation.nr()][station.nr()].strength, Configuration.INSTANCE.alpha) * Math.pow(1.0 / distance, Configuration.INSTANCE.beta);
                probabilities[station.nr()] = numerator / pheromone;
            }
        }
        return probabilities;
    }



    private void pheromon() {
        //U
    }

    private void best() {
        //M
    }

}
