package opti;

import aoc.AntColonyOptimization;
import configuration.Configuration;
import org.json.JSONException;
import util.Route;

import java.io.IOException;
import java.util.Random;
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
        optimize();
    }

    public static void linear(boolean logOn){

        AntColonyOptimization aco = new AntColonyOptimization("tsp100");
        ConfigSave config = new ConfigSave();
        config.save();
        ConfigSave oldconfig = new ConfigSave();
        oldconfig.save();
        Route best = aco.solve(); //40s
        for(double a = 0; a < 10; a += 0.1){
            Configuration.INSTANCE.setAlpha(a);
            for(double b = 0; b < 10; b += 0.1){
                Configuration.INSTANCE.setBeta(b);

            }
        }


        // Optimieren nach 1. alpha, 2. beta, 3. evaporation, 4. randomfaktor
        for(int i = 0; i < MAXITERATIONS; i++){
            System.out.println("Die Aktuelle Konfiguration");
            System.out.println(config.toString());
            Route newR = new AntColonyOptimization("tsp100").solve(); //40s

            double diff = newR.getLength() - best.getLength();
            double rand = (int) (4. * new Random().nextDouble());
            System.out.println("Vergleich: " + rand);
            if(rand <= 1){
                if(diff <= 0){
                    System.out.println(newR.getLength());
                    best = newR;

                    if(oldconfig.alpha < config.alpha){
                        oldconfig.save();
                        Configuration.INSTANCE.alpha -= 0.02;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.alpha += 0.02;
                    }
                    config.save();
                }else{
                    if(oldconfig.alpha < config.alpha){
                        oldconfig.save();
                        Configuration.INSTANCE.alpha += 0.02;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.alpha -= 0.02;
                    }
                    config.save();
                }
            }else if(rand <=2){
                if(diff <= 0){
                    System.out.println(newR.getLength());
                    best = newR;

                    if(oldconfig.beta < config.beta){
                        oldconfig.save();
                        Configuration.INSTANCE.beta -= 0.02;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.beta += 0.02;
                    }
                    config.save();
                }else{
                    if(oldconfig.beta < config.beta){
                        oldconfig.save();
                        Configuration.INSTANCE.beta += 0.02;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.beta -= 0.02;
                    }
                    config.save();
                }
            }else if(rand <= 3){
                if(diff <= 0){
                    System.out.println(newR.getLength());
                    best = newR;

                    if(oldconfig.evaporation < config.evaporation){
                        oldconfig.save();
                        Configuration.INSTANCE.evaporation -= 0.001;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.evaporation += 0.001;
                    }
                    config.save();
                }else{
                    if(oldconfig.evaporation < config.evaporation){
                        oldconfig.save();
                        Configuration.INSTANCE.evaporation += 0.001;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.evaporation -= 0.001;
                    }
                    config.save();
                }
            } else {
                if(diff <= 0){
                    System.out.println(newR.getLength());
                    best = newR;
                    if(oldconfig.randomFactor < config.randomFactor){
                        oldconfig.save();
                        Configuration.INSTANCE.randomFactor -= 0.001;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.randomFactor += 0.001;
                    }
                    config.save();
                }else{
                    if(oldconfig.randomFactor < config.randomFactor){
                        oldconfig.save();
                        Configuration.INSTANCE.randomFactor += 0.001;
                    }else{
                        oldconfig.save();
                        Configuration.INSTANCE.randomFactor -= 0.001;
                    }
                    config.save();
                }
            }
        }
        System.out.println(bestConfig.toString());
        try {
            bestConfig.saveToFile();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void optimize(){
        String problem = "tsp280";
        double alphaMin = 0.0, alphaMax = 10, alphaDiff = 2.5, alphaAk = 5;
        double betaMin = 0.0, betaMax = 10, betaDiff = 2.5, betaAk = 5;

        Route best = null;
        ConfigSave bestConfig = new ConfigSave();
        ConfigSave akConfig = new ConfigSave();
        Configuration.INSTANCE.setAlpha(alphaAk);
        Configuration.INSTANCE.setBeta(betaAk);
        best = new AntColonyOptimization(problem).solve();

        for(int i = 0; i < 10; i++){
            Route newBest;
            double newA, newB;
            AntColonyOptimization aco = new AntColonyOptimization(problem);

            //high alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk + betaDiff);
            Route newRoute = new AntColonyOptimization(problem).solve();
            newBest = newRoute;
            newA = alphaAk + alphaDiff;
            newB = betaAk + betaDiff;

            //low alpha; high beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk  + betaDiff);
            newRoute = new AntColonyOptimization(problem).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk + betaDiff;
            }

            //low alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk - alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(problem).solve();
            if(newBest.getLength() > newRoute.getLength()){
                newBest = newRoute;
                newA = alphaAk - alphaDiff;
                newB = betaAk - betaDiff;
            }

            //high alpha; low beta
            Configuration.INSTANCE.setAlpha(alphaAk + alphaDiff);
            Configuration.INSTANCE.setBeta(betaAk - betaDiff);
            newRoute = new AntColonyOptimization(problem).solve();
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
}
