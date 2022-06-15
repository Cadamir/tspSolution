package aoc;

import util.Route;

import java.util.ArrayList;

public class BestList {
    ArrayList<Route> bests;

    public BestList() {
        bests = new ArrayList<>();
    }

    synchronized public void add(Route best) {
        bests.add(best);
    }

}
