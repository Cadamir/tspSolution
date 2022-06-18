package aoc;

import configuration.Configuration;
import util.DistanceMatrix;
import util.Node;
import util.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

import static aoc.AntColonyOptimization.*;

public class AntWorker {

    static ArrayList<Node> stations;
    static DistanceMatrix distMatrix;
    static Pheromone[][] pheromones;
    public AntWorker(){
    }
    
    public void run() {
        AntWorker aw = new AntWorker();
        try {
            b.await();
            while(alive){
                aw.move();
                b.await();
                aw.best();
                b.await();
                //evaporation
                b.await();
                aw.pheromon();
                b.await();
                //uptade best and reset for next round/terminate
                b.await();
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }

    private void move() {
        while(true) {
            int antNr = toMove.decrementAndGet();
            if (antNr < 0) return;
            Ant ant = new Ant(stations.size());
            for (int i = 0; i < stations.size(); i++) {
                ant.visitCity(selectNextCity(ant));//distance jeweils übergeben ? für schnellere Berechnung
            }
            //ant.visitCity(ant.getRoute().getRoute().get(0)); //Needed if way back couts too
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

    private double[] calculateProbabilities(Ant ant) {
        double[] probabilities = new double[stations.size()];

        int routeSize = ant.getRoute().getRoute().size();
        if (routeSize==0) {
            Arrays.fill(probabilities, 1);
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
        while(true) {
            int antNr = toSmell.decrementAndGet();
            if (antNr < 0) return;

            Route route = bestSolutions.bests.get(antNr);
            double add = Configuration.INSTANCE.q / route.getLength();
            for(int i = 0; i < route.getRoute().size()-1; i++){
                pheromones[route.getRoute().get(i).nr()][route.getRoute().get(i+1).nr()].add(add);
                //pheromones[route.getRoute().get(i+1).nr()][route.getRoute().get(i).nr()].add(add);
            }
        }
    }

    public void best() {
        while(true) {
            int antNr = toCheck.decrementAndGet();
            if (antNr < 0) return;
            if (ants[antNr].getRoute().getLength() < bestSolutions.bests.get(bestSolutions.maxSize-1).getLength()) bestSolutions.add(ants[antNr].getRoute());
        }
    }

}
