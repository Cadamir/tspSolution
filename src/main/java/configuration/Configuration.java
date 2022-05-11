package configuration;

import java.util.Random;

public enum Configuration {
    INSTANCE;

    // random generator
    public final Random randomGenerator = new Random(System.nanoTime());

    // data
    //public final int numberOfCities = 280;

    // algorithm
    public double initialPheromoneValue = 0.35;
    public double alpha = 10;              // pheromone importance
    public double beta = 10;               // distance priority
    public double evaporation = 0.65;
    public double q = 500;                // pheromone left on trail per ant
    public double antFactor = 0.8;        // no ants per node
    public double randomFactor = 0.005;    // introducing randomness
    public final int maximumIterations = 1000;
//    public final int numberOfAnts = (int) (numberOfCities * antFactor);

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

