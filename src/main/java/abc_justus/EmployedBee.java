package abc_justus;

import util.Node;
import util.Route;

import java.util.ArrayList;

public class EmployedBee {
    private boolean scouting;
    private Route route;

    public EmployedBee(ArrayList<Node> nodes) {
        route = new Route(nodes);
        scouting = true;
    }

    public void scout() {
        route.shuffle();
        scouting = false;
    }

    public boolean isScouting() {
        return scouting;
    }
}
