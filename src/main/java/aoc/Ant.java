package aoc;

import util.Node;
import util.Route;

public class Ant {
    private Route route;
    private final int routeMaxSize;
    private final boolean[] visited;

    public Ant(int tourSize) {
        route = new Route();
        routeMaxSize = tourSize;
        visited = new boolean[routeMaxSize];
    }

    public void visitCity(Node nextNode) {
        route.addNode(nextNode);
        visited[nextNode.nr()] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength() {
        return route.getLength();
    }

    public void clear() {
        route = new Route();
        for (int i = 0; i < routeMaxSize; i++) {
            visited[i] = false;
        }
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getRouteMaxSize() {
        return routeMaxSize;
    }

    public boolean[] getVisited() {
        return visited;
    }
}

