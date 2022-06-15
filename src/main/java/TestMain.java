import aoc.AntColonyOptimization;

import java.util.concurrent.BrokenBarrierException;

public class TestMain {
    public static void main(String... args) throws BrokenBarrierException, InterruptedException {
        AntColonyOptimization aco = new AntColonyOptimization("tsp280");
        aco.run("tsp280");
    }
}
