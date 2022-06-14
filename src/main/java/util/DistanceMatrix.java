package util;

public class DistanceMatrix {
    private final double[][] matrix;

    public DistanceMatrix(double[][] dm){
        matrix = dm;
    }

    public double get(int i, int j) {
        return matrix[i][j];
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
