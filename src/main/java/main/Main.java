package main;

import aoc.AntColonyOptimization;
import aoc.AntWorker;
import brute_force.BruteForce;
import configuration.Configuration;
import opti.Optimize;
import util.Route;

import java.io.File;
import java.net.URL;

public class Main {
    public static void main(String[] args){
        String method = "aco";
        String tspFile = "tsp280.csv";
        String bfLimit = "10000";
        String logPath = "acoLog.log";
        for (String para: args) {
            String[] paras = para.split("=");
            String key = paras[0].substring(1);
            String value = paras[1];
            switch (key) {
                case "method":
                    method = value.toLowerCase();
                    break;
                case "best":
                    break;
                case "save":
                    break;
                case "log":
                    logPath = value;
                    break;
                case "tsp":
                    try {
                        int tspnumber = Integer.parseInt(value);
                        tspFile = "tsp"+tspnumber+".csv";
                    } catch (NumberFormatException e){
                        System.out.println("Using standard tsp File");
                        System.out.println("next time just give the number bsp.: -tsp=280");
                    }
                    break;
            }
        }
        File log = new File(logPath);
        switch (method) {
            case "aco":
                Configuration.INSTANCE.logOn = true;
                long startTime = System.currentTimeMillis();
                aco(log);
                long endTime = System.currentTimeMillis();
                System.out.println("The Search took " + ((endTime-startTime)/1000) + " seconds");
                break;
            case "bf":
                new BruteForce(tspFile).solve(Integer.parseInt(bfLimit));
                break;
            case "opt":
                new Optimize();
                break;
            default:
                System.out.println("Undefined Method: '" + method + "'");
                System.out.println("Please chose from the following method-parameters:");
                System.out.println("aco ; bf ; opt");
                System.out.println("example: -method=aco");
                System.exit(0);
        }




    }

    public static void aco(File log) {
        AntColonyOptimization aco = new AntColonyOptimization("tsp280", log);
        Route gBest = aco.solve();
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
}
