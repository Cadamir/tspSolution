package aoc;

import java.util.concurrent.BrokenBarrierException;

import static aoc.AntColonyOptimization.*;

public class AntWorker {

    public AntWorker(){
    }
    
    public void run() throws BrokenBarrierException, InterruptedException {
        b.await();
        while(alive){
            move();
            b.await();
            pheromon();
            b.await();
            best();
            b.await();
            b.await();
        }
        
        
    }

    private void best() {
        //M
    }

    private void pheromon() {
        //U
    }

    private void move() {
        while(true) {
            int antNr = toMove.decrementAndGet(); //TODO check - returns the actual value or the decremented value
            if (antNr < 0) return;
            AntWorker antWorker = antWorkers[antNr];
            for (int i = 0; i < graph.length - 1; i++) {
                antWorker.visitCity(currentIndex, selectNextCity(antWorker));
            }

            //M
        }
    }
}
