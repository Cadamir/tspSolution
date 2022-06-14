package aoc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Ant {
    CyclicBarrier b;
    
    public Ant(CyclicBarrier b){
        this.b = b;
    }
    
    public void run() throws BrokenBarrierException, InterruptedException {
        b.await();
        while(true){
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
        //M
    }
}
