package configuration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum Configuration {
    INSTANCE;

    // random generator
    public final Random randomGenerator = new Random(System.nanoTime());

    // data
    //public final int numberOfCities = 280;

    // algorithm
    public double initialPheromoneValue = 1.0;
    public double alpha = 1.71875;            // pheromone importance
    public double beta = 3.90625;               // distance priority
    public double evaporation = 0.005;
    public double q = 100;                // pheromone left on trail per ant
    public double antFactor = 0.8;        // no ants per node
    public double randomFactor = 0.005;    // introducing randomness
    public final int maximumIterations = 1500;
//    public final int numberOfAnts = (int) (numberOfCities * antFactor);

    public boolean logOn = true;
    public double influencingAnts = 0.02; //Factor how many Ants influence the pheromones

    public void setEvaporation(double eva){
        evaporation = eva;
    }

    public void setInitialPheromoneValue(double initialPheromoneValue) {
        this.initialPheromoneValue = initialPheromoneValue;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void setAntFactor(double antFactor) {
        this.antFactor = antFactor;
    }

    public void setRandomFactor(double randomFactor) {
        this.randomFactor = randomFactor;
    }
}

