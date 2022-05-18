package util;

import java.util.ArrayList;

//A Node holds a position of the tsp Problem mapped to its number
public record Node(int nr, NodePosition pos) {
    public double distance(Node node) {
        return Math.round(Math.abs(Math.sqrt(
                (
                        this.pos().x() - node.pos().x()) * (this.pos().x() - node.pos().x()
                ) + (
                        this.pos().y() - node.pos().y()) * (this.pos().y() - node.pos().y()
                )
        )));
    }

    public static DistanceMatrix generateDistanceMatrix(ArrayList<Node> nodes) {
        double[][] distanceMatrix = new double[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    distanceMatrix[i][j] = Math.abs(nodes.get(i).distance(nodes.get(j)));
                    if(distanceMatrix[i][j] == 0){
                        distanceMatrix[i][j] = 0.000001;
                    }
                }
            }
        }

        return new DistanceMatrix(distanceMatrix);
    }
}
