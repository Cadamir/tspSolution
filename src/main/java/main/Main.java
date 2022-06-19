package main;

import aoc.AntColonyOptimization;
import brute_force.BruteForce;
import configuration.Configuration;
import opti.Optimize;
import util.Route;

import java.io.File;

public class Main {
    public static void main(String[] args){
        String method = "aco";
        String tspFile = "tsp280";
        String bfLimit = "10000";
        String logPath = "./log/acoLog.log";
        String savePath = "./conf/config.csv";
        String loadPath = null;
        for (String para: args) {
            String[] paras = para.split("=");
            String key = paras[0].substring(1);
            String value = paras[1];
            switch (key) {
                case "method":
                    method = value.toLowerCase();
                    break;
                case "best":
                    loadPath = value;
                    break;
                case "save":
                    savePath = value;
                    break;
                case "log":
                    logPath = value;
                    break;
                case "tsp":
                    try {
                        int tspnumber = Integer.parseInt(value);
                        tspFile = "tsp"+tspnumber;
                    } catch (NumberFormatException e){
                        System.out.println("Using standard tsp File");
                        System.out.println("next time just give the number bsp.: -tsp=280");
                    }
                    break;
            }
        }

        File log = new File(logPath);
        File saveFile = new File(savePath);

        switch (method) {
            case "aco" -> {
                Configuration.INSTANCE.logOn = true;
                long startTime = System.currentTimeMillis();
                aco(tspFile, log, loadPath);
                long endTime = System.currentTimeMillis();
                System.out.println("The Search took " + ((endTime - startTime) / 1000) + " seconds");
            }
            case "bf" -> new BruteForce(tspFile).solve(Integer.parseInt(bfLimit));
            case "opt" -> new Optimize(tspFile, saveFile);
            default -> {
                System.out.println("Undefined Method: '" + method + "'");
                System.out.println("Please chose from the following method-parameters:");
                System.out.println("aco ; bf ; opt");
                System.out.println("example: -method=aco");
                System.exit(0);
            }
        }
    }

    public static void aco(String tsp, File log, String loadPath) {
        AntColonyOptimization aco = new AntColonyOptimization(tsp, log, loadPath);
        Route gBest = aco.solve();
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
}
