package opti;

import aoc.AntColonyOptimization;
import configuration.Configuration;
import util.Route;

import java.io.File;

public class Optimize {
    public final static int MAXITERATIONS = 100;
    public static void linear(String problem, File save) {
        Configuration.INSTANCE.setLogOn(false);

        AntColonyOptimization aco = new AntColonyOptimization(problem, null, null);
        Route best = aco.solve(); //40s
        ConfigSave bestConfig = new ConfigSave();
        bestConfig.save();

        for (double a = 0; a < 10.; a += 0.1) {
            Configuration.INSTANCE.setAlpha(a);
            for (double b = 0; b < 10; b += 0.1) {
                Configuration.INSTANCE.setBeta(b);
                for (double c = 0; c < 1; c += 0.001) {
                    Configuration.INSTANCE.setEvaporation(c);
                    for (double d = 10; d < 500; d += 10) {
                        Configuration.INSTANCE.setQ(d);
                        for (double e = 0.2; e < 2; e += 0.1) {
                            Configuration.INSTANCE.setAlpha(e);
                            for (double f = 0; f < 1; f += 0.001) {
                                Configuration.INSTANCE.setRandomFactor(f);
                                Route newRoute = new AntColonyOptimization(problem, null, null).solve();
                                if (newRoute.getLength() < best.getLength()) {
                                    best = newRoute;
                                    bestConfig.save();
                                }
                            }
                        }
                    }
                }
            }
        }

        bestConfig.saveToFile(save);
    }

    public static void optimize(String problem, File save){
        Configuration.INSTANCE.setAlpha(5);
        Configuration.INSTANCE.setBeta(5);
        Configuration.INSTANCE.setRandomFactor(0.0010);
        Configuration.INSTANCE.setEvaporation(0.5);
        Configuration.INSTANCE.setQ(100);

        for(int i = 0; i < MAXITERATIONS; i++){
            optimizeAlphaBeta(problem);
            optimizeRandom(problem);
            optimizeEvaQ(problem);
        }

        ConfigSave configSave = new ConfigSave();
        configSave.save();
        configSave.saveToFile(save);
    }

    private static void optimizeAlphaBeta(String problem){

        double alphaAk = Configuration.INSTANCE.alpha, alphaDiff = 0.5 * alphaAk;
        double betaAk = Configuration.INSTANCE.beta, betaDiff = 0.5 * betaAk;

        Configuration.INSTANCE.setAlpha(alphaAk);
        Configuration.INSTANCE.setBeta(betaAk);
        Route best = new AntColonyOptimization(problem, null, null).solve();

        for(int i = 0; i < 10; i++){
            Route newBest;
            double newA, newB;

             //high alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk + betaDiff);
            Route newRoute = new AntColonyOptimization(problem, null, null).solve();
            newBest = newRoute;
            newA = alphaAk + alphaDiff;
            newB = betaAk + betaDiff;

            //low alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk  + betaDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk + betaDiff;
            }

            //low alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk - betaDiff;
            }

            //high alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            if(betaAk -betaDiff > 0)
                Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk + alphaDiff;
                newB = betaAk - betaDiff;
            }

            if(best.getLength() > newBest.getLength()){
                best = newBest;
                alphaAk = newA;
                betaAk = newB;
            }
            alphaDiff /= 2.;
            betaDiff /= 2.;
            System.out.println("Best Alpha: " + " - " + alphaAk + " - Best Beta: " + betaAk);
        }
    }

    private static void optimizeRandom(String problem){
        double randomAk = Configuration.INSTANCE.randomFactor, randomDiff  = randomAk * 0.5;

        Route best = new AntColonyOptimization(problem, null, null).solve();
        ConfigSave bestConfig = new ConfigSave();
        bestConfig.save();

        for(int i = 0; i < 10; i++){
            double newRand = randomAk;
            //highRandom
            Configuration.INSTANCE.setRandomFactor(randomAk + randomDiff);
            Route newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newRand = randomAk + randomDiff;
            }

            //lowRandom
            Configuration.INSTANCE.setRandomFactor(randomAk - randomDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newRand = randomAk + randomDiff;
            }
            randomAk = newRand;
            System.out.println("bestRandom: " + randomAk);
        }
    }

    private static void optimizeEvaQ(String problem){
        double qAk = Configuration.INSTANCE.q, qDiff  = 0.5 * qAk;
        double evaAk = Configuration.INSTANCE.evaporation, evaDiff = 0.5 * evaAk;

        Route best = new AntColonyOptimization(problem, null, null).solve();
        ConfigSave bestConfig = new ConfigSave();
        bestConfig.save();

        for(int i = 0; i < 10; i++){
            double newQ = qAk;
            double newEva = evaAk;

            //high Eva high Q
            if(evaAk + evaDiff < 1)
                Configuration.INSTANCE.setEvaporation(evaAk + evaDiff);
            Configuration.INSTANCE.setQ(qAk + qDiff);
            Route newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk + evaDiff;
                newQ = qAk +qDiff;
            }

            //high Eva low Q
            if(evaAk + evaDiff < 1)
                Configuration.INSTANCE.setEvaporation(evaAk + evaDiff);
            if(qAk - qDiff > 0)
                Configuration.INSTANCE.setQ(qAk - qDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk + evaDiff;
                newQ = qAk - qDiff;
            }

            //low Eva low Q
            if(evaAk - evaDiff > 0)
                Configuration.INSTANCE.setEvaporation(evaAk - evaDiff);
            if(qAk - qDiff > 0)
                Configuration.INSTANCE.setQ(qAk - qDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk - evaDiff;
                newQ = qAk - qDiff;
            }

            //low Eva high Q
            if(evaAk - evaDiff > 0)
                Configuration.INSTANCE.setEvaporation(evaAk - evaDiff);
            Configuration.INSTANCE.setQ(qAk + qDiff);
            newRoute = new AntColonyOptimization(problem, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk - evaDiff;
                newQ = qAk + qDiff;
            }
            qAk = newQ;
            evaAk = newEva;
            evaDiff /= 2.;
            qDiff /= 2.;

            System.out.println("best Q: " + qAk + " - best Eva: " + evaAk);
        }
        //executor.shutdown();
        System.out.println(bestConfig);
    }
}
