package aoc;

import configuration.Configuration;
import opti.ConfigSave;
import util.DistanceMatrix;
import util.Node;
import util.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ThreadLocalRandom;

import static aoc.AntColonyOptimization.*;

public class AntWorker implements Runnable {
    static ArrayList<Node> stations;
    static DistanceMatrix distMatrix;
    static Pheromone[][] pheromones;
    static Random randomGenerator;
    public AntWorker(){
        randomGenerator = Configuration.INSTANCE.randomGenerator;
    }
    
    public void run() {
        try {
            b.await();
            while(alive){;
                move();
                //b.await();
                best();
                b.await();
                //evaporation
                b.await();
                pheromon();
                b.await();
                //update best and reset for next round/terminate
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
            ants[antNr].clear();
            //Ant ant = new Ant(stations.size());
            for (int i = 0; i < stations.size(); i++) {
                ants[antNr].visitCity(null);
            }
            //ant.visitCity(ant.getRoute().getRoute().get(0)); //Needed if way back counts too
            //ants[antNr] = ant;

        }
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
            if (ants[antNr].getRoute().getLength() < bestSolutions.bests.get(9).getLength()) bestSolutions.add(ants[antNr].getRoute());
        }
    }

}
