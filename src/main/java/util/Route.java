package util;

import java.util.ArrayList;
import java.util.Collections;

public class Route {
    final private ArrayList<Node> route;
    private double length;

    public Route(ArrayList<Node> nodes) {
        route = new ArrayList<>(nodes);
        length = calcLength();
    }

    public Route() {
        route = new ArrayList<>();
        length = Double.MAX_VALUE;
    }

    public void addNode(Node nextNode) {
        route.add(nextNode);
        if(route.size()==1) length = 0;
        else length += route.get(route.size()-2).distance(nextNode);
    }

    public void shuffle() {
        Collections.shuffle(route);
        length = calcLength();
    }

    public ArrayList<Node> getRoute() {
        return route;
    }

    public double getLength() {
        return length;
    }

    public String routeToString() {
        StringBuilder sb = new StringBuilder();
        for (Node node: route) {
            sb.append(" - ").append(node.nr() + 1);
        }
        return sb.toString();
    }

    private double calcLength() {
        double distance = 0;
        int i = 0;
        while(i < route.size() - 1) {
            distance = distance + route.get(i).distance(route.get(i+1));
            i++;
        }
        return distance; // last node back to first node
    }

    public static ArrayList<Node> order(ArrayList<Node> nodes, int[] order) {
        ArrayList<Node> orderedNodes = new ArrayList<>();
        for (int position: order) {
            orderedNodes.add(nodes.get(position));
        }
        return orderedNodes;
    }

    public void setRoute(ArrayList<Node> route){
        this.route.removeAll(this.route);
        this.route.addAll(route);
        calcLength();
    }
}
