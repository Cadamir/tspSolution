package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Route {
    final private ArrayList<Node> route;

    public Route(ArrayList<Node> nodes) {
        route = new ArrayList<>(nodes);
    }

    public void shuffle() {
        Collections.shuffle(route);
    }

    public double length() {
        double distance = 0;
        int i = 0;
        while(i < route.size() - 1) {
            distance = distance + route.get(i).distance(route.get(i+1));
            i++;
        }
        distance = distance + route.get(i).distance(route.get(0)); // last node back to first node
        return distance;
    }

    public ArrayList<Node> getRoute() {
        return route;
    }
}
