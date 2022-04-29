package util;

//A Node holds a position of the tsp Problem mapped to its number
public class Node {
    final private int nr;
    final private Position pos;

    public Node (int nr, Position pos){
        this.nr = nr;
        this.pos = pos;
    }

    public int getNr() {
        return nr;
    }

    public Position getPos() {
        return pos;
    }
}
