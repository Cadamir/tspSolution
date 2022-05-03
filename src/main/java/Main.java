import brute_force.BruteForce;
import util.Route;

public class Main {
    public static void main(String[] args) {
        BruteForce bf = new BruteForce("tsp280");
        Route gBest = bf.solveRandom(16);
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
}
