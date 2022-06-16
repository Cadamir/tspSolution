package opti;

import configuration.Configuration;

public class ConfigSave {
    public double alpha, beta, evaporation, q, antFactor, randomFactor;

    public void save(){
        alpha = Configuration.INSTANCE.alpha;
        beta = Configuration.INSTANCE.beta;
        evaporation = Configuration.INSTANCE.evaporation;
        q = Configuration.INSTANCE.q;
        antFactor = Configuration.INSTANCE.antFactor;
        randomFactor = Configuration.INSTANCE.randomFactor;
    }

    @Override
    public String toString() {
        return "Alpha: " + alpha +
                "\nBeta: " + beta +
                "\nEvaporation: " + evaporation +
                "\nQ: " + q +
                "\nAntFactor: " + antFactor +
                "\nrandomFactor: " + randomFactor;
    }
}
