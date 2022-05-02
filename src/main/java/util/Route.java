package util;

import java.util.ArrayList;
import java.util.Collections;

public class Route {
    final private ArrayList<Node> route;
    final private double length;

    public Route(ArrayList<Node> nodes) {
        route = new ArrayList<>(nodes);
        double distance = 0;
        int i = 0;
        while(i < route.size() - 1) {
            distance = distance + route.get(i).distance(route.get(i+1));
            i++;
        }
        length = distance + route.get(i).distance(route.get(0)); // last node back to first node
    }

    public Route() {
        route = null;
        length = Double.MAX_VALUE;
    }

    public void shuffle() {
        Collections.shuffle(route);
    }

    public ArrayList<Node> getRoute() {
        return route;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node: route) {
            sb.append(" - ").append(node.nr());
        }
        return sb.toString();
    }
}
