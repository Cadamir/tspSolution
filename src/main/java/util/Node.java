package util;

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
}
