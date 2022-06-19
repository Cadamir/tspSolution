import aoc.AntColonyOptimization;
import aoc.AntWorker;
import configuration.Configuration;
import util.Route;

import java.net.URL;

public class Main {
    public static void main(String[] args){
        String method = "aco";
        URL log = AntWorker.class.getResource("../aocLog.log");
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
                    break;
            }
        }

        switch (method) {
            case "aco":
                Configuration.INSTANCE.logOn = true;
                long startTime = System.currentTimeMillis();
                aco(log);
                long endTime = System.currentTimeMillis();
                System.out.println("The Search took " + ((endTime-startTime)/1000) + " seconds");
                break;
            case "bf":
                break;
            case "opt":
                break;
            default:
                System.out.println("Undefined Method: '" + method + "'");
                System.out.println("Please chose from the following method-parameters:");
                System.out.println("aco ; bf ; opt");
                System.out.println("example: -method=aco");
                System.exit(0);
        }




    }

    public static void aco(URL logResource) {
        AntColonyOptimization aco = new AntColonyOptimization("tsp280", logResource);
        Route gBest = aco.solve();
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
}
