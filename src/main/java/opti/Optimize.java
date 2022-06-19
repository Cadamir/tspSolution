package opti;

import aoc.AntColonyOptimization;
import configuration.Configuration;
import org.json.JSONException;
import util.Route;

import java.io.IOException;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Optimize {
    public final static int MAXITERATIONS = 100;
    public Route best = new Route();
    public ConfigSave bestConfig = new ConfigSave();
    public long counter = 0;

    public static void main(String... args){
        //optimize();
        //linear(false);
        ConfigSave save =  new ConfigSave();
        save.save();
        try {
            save.saveToFile();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void linear(String tsp, File save) {
        Configuration.INSTANCE.setLogOn(false);

        AntColonyOptimization aco = new AntColonyOptimization(tsp, null, null);
        ConfigSave config = new ConfigSave();
        config.save();
        ConfigSave oldconfig = new ConfigSave();
        oldconfig.save();
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
                                Route newRoute = new AntColonyOptimization(tsp, null, null).solve();
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
    }

    public static void optimize(String tsp, File save){

        Configuration.INSTANCE.setAlpha(5);
        Configuration.INSTANCE.setBeta(5);
        Configuration.INSTANCE.setRandomFactor(0.0010);
        Configuration.INSTANCE.setEvaporation(0.5);
        Configuration.INSTANCE.setQ(100);

        for(int i = 0; i < 1; i++){
            optimizeAlphaBeta(tsp);
            optimizeRandom(tsp);
            optimizeEvaQ(tsp);
        }

        ConfigSave configSave = new ConfigSave();
        configSave.save();
        try{
            configSave.saveToFile();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static void optimizeAlphaBeta(String tsp){

        double alphaAk = Configuration.INSTANCE.alpha, alphaDiff = 0.5 * alphaAk;
        double betaAk = Configuration.INSTANCE.beta, betaDiff = 0.5 * betaAk;

        Route best = null;
        ConfigSave bestConfig = new ConfigSave();
        ConfigSave akConfig = new ConfigSave();
        Configuration.INSTANCE.setAlpha(alphaAk);
        Configuration.INSTANCE.setBeta(betaAk);
        best = new AntColonyOptimization(tsp, null, null).solve();

        for(int i = 0; i < 10; i++){
            Route newBest;
            double newA, newB;
            AntColonyOptimization aco = new AntColonyOptimization(tsp, null, null);

            //high alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk + betaDiff);
            Route newRoute = new AntColonyOptimization(tsp, null, null).solve();
            newBest = newRoute;
            newA = alphaAk + alphaDiff;
            newB = betaAk + betaDiff;

            //low alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk  + betaDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk + betaDiff;
            }

            //low alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk - betaDiff;
            }

            //high alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            if(betaAk -betaDiff > 0)
                Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
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
            System.out.println(best.getLength() + " - " + alphaAk + " - " + betaAk);
        }
    }

    private static void optimizeRandom(String tsp){
        double randomAk = 0.5, randomDiff  = 0.25;
        Configuration.INSTANCE.setRandomFactor(randomAk);
        Route best = new AntColonyOptimization(tsp, null, null).solve();
        ConfigSave bestConfig = new ConfigSave();
        bestConfig.save();
        ConfigSave oldConfig = new ConfigSave();
        oldConfig.save();

        for(int i = 0; i < 10; i++){
            oldConfig.save();
            double newRand = randomAk;
            //highRandom
            Configuration.INSTANCE.setRandomFactor(randomAk + randomDiff);
            Route newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newRand = randomAk + randomDiff;
            }

            //lowRandom
            Configuration.INSTANCE.setRandomFactor(randomAk - randomDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newRand = randomAk + randomDiff;
            }
            randomAk = newRand;
            System.out.println("bestRandom: " + randomAk);
        }
    }

    private static void optimizeEvaQ(String tsp){
        double qAk = 100, qDiff  = 50;
        double evaAk = 0.5, evaDiff = 0.25;

        Configuration.INSTANCE.setQ(qAk);
        Configuration.INSTANCE.setEvaporation(evaAk);

        Route best = new AntColonyOptimization(tsp, null, null).solve();
        System.out.println(1);
        ConfigSave bestConfig = new ConfigSave();
        bestConfig.save();
        ConfigSave oldConfig = new ConfigSave();
        oldConfig.save();
        System.out.println(2);

        for(int i = 0; i < 10; i++){

            oldConfig.save();

            double newQ = qAk;
            double newEva = evaAk;

            //high Eva high Q
            if(evaAk + evaDiff < 1)
                Configuration.INSTANCE.setEvaporation(evaAk + evaDiff);
            Configuration.INSTANCE.setQ(qAk + qDiff);
            Route newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk + evaDiff;
                newQ = qAk +qDiff;
            }
            System.out.println("11");

            //high Eva low Q
            if(evaAk + evaDiff < 1)
                Configuration.INSTANCE.setEvaporation(evaAk + evaDiff);
            if(qAk - qDiff > 0)
                Configuration.INSTANCE.setQ(qAk - qDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk + evaDiff;
                newQ = qAk - qDiff;
            }
            System.out.println("22");

            //low Eva low Q
            if(evaAk - evaDiff > 0)
                Configuration.INSTANCE.setEvaporation(evaAk - evaDiff);
            if(qAk - qDiff > 0)
                Configuration.INSTANCE.setQ(qAk - qDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk - evaDiff;
                newQ = qAk - qDiff;
            }
            System.out.println("33");

            //low Eva high Q
            if(evaAk - evaDiff > 0)
                Configuration.INSTANCE.setEvaporation(evaAk - evaDiff);
            Configuration.INSTANCE.setQ(qAk + qDiff);
            newRoute = new AntColonyOptimization(tsp, null, null).solve();
            if(newRoute.getLength() < best.getLength()){
                best = newRoute;
                bestConfig.save();
                newEva = evaAk - evaDiff;
                newQ = qAk + qDiff;
            }
            System.out.println("44");

            qAk = newQ;
            evaAk = newEva;
            evaDiff /= 2.;
            qDiff /= 2.;

            System.out.println("best Q: " + qAk + " - best Eva: " + evaAk);
        }
        //executor.shutdown();
        System.out.println(bestConfig.toString());
    }
}
