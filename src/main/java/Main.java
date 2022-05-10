import aoc.AntColonyOptimization;
import brute_force.BruteForce;
import util.Route;

public class Main {
/*    public static void main(String[] args) {
        BruteForce bf = new BruteForce("tsp280");
        Route gBest = bf.solveRandom(Integer.MAX_VALUE);
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
*/
    public static void main(String[] args){
        AntColonyOptimization aco = new AntColonyOptimization();
        aco.run();
    }
}
