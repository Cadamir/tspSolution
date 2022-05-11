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
        BruteForce bf = new BruteForce("tsp280");
        Route gBest = bf.solveRandom(Integer.MAX_VALUE);
        System.out.println("Best result is " + gBest.getLength() + " for the route :" + gBest.routeToString());
    }
*/
    public static void main(String[] args){
//        iniPher();
        alpha();
//        beta();
//        eva();
//        q();
//        ant();
//        rand();
    }
    
    public static void iniPher(){
        double avergage = 0;
        for(double j = 0; j < 1; j += 0.05){
            Configuration.INSTANCE.setInitialPheromoneValue(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setInitialPheromoneValue(1);
    }
    
    public static void alpha(){
        double avergage = 0;
        for(double j = 0; j < maxAlpha; j += 0.5){
            Configuration.INSTANCE.setAlpha(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setAlpha(2);
    }
    
    public static void beta(){
        double avergage = 0;
        for(double j = 0; j < maxBeta; j += 0.5){
            Configuration.INSTANCE.setBeta(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setBeta(2);
    }
    
    public static void eva(){
        double avergage = 0;
        for(double j = 0; j < 1; j += 0.05){
            Configuration.INSTANCE.setEvaporation(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setEvaporation(0.2);
    }
    
    public static void q(){
        double avergage = 0;
        for(double j = 0; j < maxPheromon; j += 100){
            Configuration.INSTANCE.setQ(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("q: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setQ(500);
    }
    
    public static void ant(){
        double avergage = 0;
        for(double j = 0; j < 3; j += 0.2){
            Configuration.INSTANCE.setAntFactor(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setAntFactor(0.8);
    }
    
    public static void rand(){
        double avergage = 0;
        for(double j = 0; j < 1; j += 0.01){
            Configuration.INSTANCE.setRandomFactor(j);
            avergage = 0;
            for(int i = 0; i < iteration; i++){
                AntColonyOptimization aco = new AntColonyOptimization();
                avergage += aco.run();

            }
            avergage /= iteration;
            System.out.println("j: " + j + " - " + avergage);
        }
        Configuration.INSTANCE.setRandomFactor(0.02);
    }
}
