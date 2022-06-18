import aoc.AntColonyOptimization;
import util.Route;

public class Main {
    public static void main(String[] args){
        long starttime = System.currentTimeMillis();
        aco();
        long endtime = System.currentTimeMillis();
        System.out.println(endtime-starttime);
    }

    public static void aco() {
        AntColonyOptimization aco = new AntColonyOptimization("tsp280");
        Route gBest = aco.solve();
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
}
