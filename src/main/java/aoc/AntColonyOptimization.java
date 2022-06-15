package aoc;

import configuration.Configuration;
import util.DistanceMatrix;
import util.Node;
import util.Route;
import util.TspConverter;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AntColonyOptimization {

    private ArrayList<Route> best;

    protected static Ant[] ants;
    protected static DistanceMatrix distMatrix;
    protected static double[][] trails;

    protected static CyclicBarrier b;


    protected static boolean alive; //flag - as long as true the algorithm should run
    protected static AtomicInteger toMove;

    public AntColonyOptimization(String filename){
        alive = true;
        ArrayList<Node> stations = new TspConverter().generateFromFile(filename);
        aoc.AntWorker.stations = stations;
        distMatrix = Node.generateDistanceMatrix(stations);
        trails = new double[stations.size()][stations.size()];
    }

    public void run(String filename) throws BrokenBarrierException, InterruptedException {
        init();
        //initialisieren
        //Threadpool erstellen
        int AntThreadsCount = Math.min(Runtime.getRuntime().availableProcessors(), ants.length);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AntThreadsCount);
        b = new CyclicBarrier(AntThreadsCount + 1); //This Thread has to await too

        toMove = new AtomicInteger(ants.length);
        for(int i = Configuration.INSTANCE.maximumIterations; i >= 0; i--){

            //Ants Move - in ant threads
            b.await();

            //Pheromone - in ant threads
            b.await();

            //possibleBest - in ant threads
            b.await();
            updateBest();
            clearList();
            b.await();

        }
        alive = false;
    }

    private void init() { }

    private void updateBest() {
        //U
        for(Ant ant:ants){
            for(int i = 0; i < best.size(); i++){
                if(ant.trailLength() < best.get(i).getLength()){
                    best.add(i,ant.getRoute());
                    best.remove(best.size()-1);
                    break;
                }
            }
        }
    }

    private void clearList() {
        toMove = new AtomicInteger(ants.length);

    }
}
