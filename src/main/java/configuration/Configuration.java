package configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public double influencingAnts = 0.02;

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

    public void setLogOn(boolean logOn) {
        this.logOn = logOn;
    }

    public void becomeBest(File config) throws IOException, JSONException {
        JSONObject jo = new JSONObject(config);
        this.alpha = jo.getDouble("alpha");
        this.beta = jo.getDouble("beta");
        this.evaporation = jo.getDouble("evaporation");
        this.q = jo.getDouble("q");
        this.antFactor = jo.getDouble("antFactor");
        this.randomFactor = jo.getDouble("randomFactor");
    }
}

