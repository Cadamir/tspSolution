package aoc;

import configuration.Configuration;
import util.Route;

import java.util.ArrayList;

public class BestList {

    final int maxSize;
    ArrayList<Route> bests;

    public BestList() {
        this.maxSize = Configuration.INSTANCE.influencingAnts;
        bests = new ArrayList<>();
        zeroValues();
    }

    synchronized public void add(Route best) {
        int i = 0;
        while (i < maxSize - 1){
            if (best.getLength() < bests.get(i).getLength()) break;
            i++;
        }
        if (i == maxSize - 1) return;
        bests.add(i, best);
        bests.remove(maxSize);
    }

    public void clear() {
        bests.clear();
        zeroValues();
    }

    private void zeroValues() {
        for (int i = maxSize; i > 0; i--) {
            bests.add(new Route());
        }
    }

}
