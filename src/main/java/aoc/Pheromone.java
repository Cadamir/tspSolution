package aoc;

import configuration.Configuration;

public class Pheromone {
    double strength;

    public Pheromone() {
        strength = Configuration.INSTANCE.initialPheromoneValue;
    }

    synchronized public void add(double summand) {
        strength += summand;
    }

    public double getStrength(){
        return strength;
    }

}
