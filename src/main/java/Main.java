import aoc.AntColonyOptimization;
import brute_force.BruteForce;
import configuration.Configuration;
import util.Route;

public class Main {
    public static int iteration = 10;
    public static int maxAlpha = 5;
    public static int maxBeta = 5;
    public static int maxPheromon = 1000;


/*    public static void main(String[] args) {

    }
*/
    public static void main(String[] args){
//        bf();
        aco();
//        iniPher();
//        alpha();
//        beta();
//        eva();
//        q();
//        ant();
//        rand();
    }

    public static void aco() {
        AntColonyOptimization aco = new AntColonyOptimization("tsp280");
        Route gBest = aco.solve();
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }

    public static void bf() {
        BruteForce bf = new BruteForce("tsp280");
        Route gBest = bf.solveRandom(Integer.MAX_VALUE);
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
    
    public static void iniPher(){
        double average = 0;
        for(double j = 0; j < 1; j += 0.05){
            Configuration.INSTANCE.setInitialPheromoneValue(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("init-pheromons: " + j + " - " + average);
        }
        Configuration.INSTANCE.setInitialPheromoneValue(1);
    }
    
    public static void alpha(){
        double average = 0;
        for(double j = 0; j < maxAlpha; j += 0.5){
            Configuration.INSTANCE.setAlpha(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("alpha: " + j + " - " + average);
        }
        Configuration.INSTANCE.setAlpha(2);
    }
    
    public static void beta(){
        double average = 0;
        for(double j = 0; j < maxBeta; j += 0.5){
            Configuration.INSTANCE.setBeta(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("beta: " + j + " - " + average);
        }
        Configuration.INSTANCE.setBeta(2);
    }
    
    public static void eva(){
        double average = 0;
        for(double j = 0; j < 1; j += 0.05){
            Configuration.INSTANCE.setEvaporation(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("eva: " + j + " - " + average);
        }
        Configuration.INSTANCE.setEvaporation(0.2);
    }
    
    public static void q(){
        double average = 0;
        for(double j = 0; j < maxPheromon; j += 100){
            Configuration.INSTANCE.setQ(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("q: " + j + " - " + average);
        }
        Configuration.INSTANCE.setQ(500);
    }
    
    public static void ant(){
        double average = 0;
        for(double j = 0; j < 3; j += 0.2){
            Configuration.INSTANCE.setAntFactor(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("ant: " + j + " - " + average);
        }
        Configuration.INSTANCE.setAntFactor(0.8);
    }
    
    public static void rand(){
        double average = 0;
        for(double j = 0; j < 1; j += 0.01){
            Configuration.INSTANCE.setRandomFactor(j);
            average = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization("tsp280");
                average += aco.solve().getLength();

            }
            average /= iteration;
            System.out.println("j: " + j + " - " + average);
        }
        Configuration.INSTANCE.setRandomFactor(0.02);
    }
}
