package opti;

import aoc.AntColonyOptimization;
import configuration.Configuration;
import util.Route;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Optimize {
    //public double alpha, beta, evaporation, q, antFactor, randomFactor;
    public final double maxAlpha = 2, maxBeta = 2, maxEvaporation = 1, maxQ = 1000, maxAntFactor = 5, maxRandomFactor = 1;

    public final static Lock lockBest = new ReentrantLock();

    public Route best = new Route();
    public ConfigSave bestConfig = new ConfigSave();
    public long counter = 0;


    public Optimize(){
        Configuration.INSTANCE.logOn = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Route route = new AntColonyOptimization("tsp280", null).solve();
                if(route.getLength() < best.getLength()){
                    lockBest.lock();
                    best = route;
                    bestConfig.save();
                    lockBest.unlock();
                }
            }
        };
        //ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for(double a = 1; a < maxAlpha; a += 0.1){
            Configuration.INSTANCE.setAlpha(a);
            for(double b = 1; b < maxBeta; b += 0.1){
                Configuration.INSTANCE.setBeta(b);
                //for(double c = 0.001; c < maxEvaporation; c += 0.001){
                    //Configuration.INSTANCE.setEvaporation(c);
                    //for(double d = 50; d < maxQ; d += 50){
                        //Configuration.INSTANCE.setQ(d);
                        //for(double e = 0.3; e < maxAntFactor; e += 0.1){
                            //Configuration.INSTANCE.setAntFactor(e);
                            //for(double f = 0; f < maxRandomFactor; f += 0.001){
                                //Configuration.INSTANCE.setRandomFactor(f);
                                runnable.run();
                                counter++;
                                System.out.println(best.getLength() + " : " + counter);
                            //}
                        //}
                    //}
                //}
            }
        }
        //executor.shutdown();
        System.out.println(bestConfig.toString());
        System.out.println("Es hat " + counter + " Versuche benötigt");
    }
}
