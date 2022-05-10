package configuration;

import java.util.Random;

public enum Configuration {
    INSTANCE;

    // random generator
    public final Random randomGenerator = new Random(System.nanoTime());

    // data
    //public final int numberOfCities = 280;

    // algorithm
    public final double initialPheromoneValue = 1.0;
    public final double alpha = 2;              // pheromone importance
    public final double beta = 2;               // distance priority
    public final double evaporation = 0.05;
    public final double q = 500;                // pheromone left on trail per ant
    public final double antFactor = 0.8;        // no ants per node
    public final double randomFactor = 0.01;    // introducing randomness
    public final int maximumIterations = 10000;
//    public final int numberOfAnts = (int) (numberOfCities * antFactor);
}

