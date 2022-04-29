package brute_force;

import util.Node;
import util.TspConverter;

public class BruteForce {
    private Node[] nodes= new Node[280];
    private int best = 2000000;

    public BruteForce(String file) {
        nodes = new TspConverter().generateFromFile(file);

    }

    /*public BruteForce() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\Programmierer\\Documents\\unfinished\\bee\\src\\main\\java\\tsp280")));
    }

    public ArrayList<Integer> addKoordinate(HashMap<Integer, Integer> rest, ArrayList<Integer> al){
        if(rest.size() == 0){
            return al;
        }
        Math.random()
    }*/
}
