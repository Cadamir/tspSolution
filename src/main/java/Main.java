import brute_force.BruteForce;

public class Main {
    public static void main(String[] args) {
        BruteForce bf = new BruteForce("tsp280");
        System.out.println(bf.solveLinear());
    }
}
