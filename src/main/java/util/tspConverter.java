package util;

import java.io.*;
import java.util.ArrayList;

//reads csv example
public class tspConverter {
    public static Node[] generateFromFile(String file) {
        ArrayList<Node> nodes = new ArrayList<>(); //List to save all tsp nodes
        File file1 = new File("resources\\" + file);

        BufferedReader br = null;
        FileReader fr = null;

        try {
            String lineContent;

            fr = new FileReader(file1);
            br = new BufferedReader(fr);

            //check all lines for tsp nodes
            while ((lineContent = br.readLine()) != null) {
                Node node = generateNode(lineContent);
                if (node != null) nodes.add(node);
            }
        } catch (FileNotFoundException e) {
            //could not read file
            throw new RuntimeException(e);
        } catch (IOException e) {
            //could not read line
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return nodes.toArray(new Node[0]);
    }

    private static Node generateNode(String lineContent) {
        Node node;
        String[] contents = lineContent.trim().split(" ");

        try {
            int nr = Integer.parseInt(contents[0]);
            int x = Integer.parseInt(contents[1]);
            int y = Integer.parseInt(contents[2]);
            Position pos = new Position(x, y);
            node = new Node(nr, pos);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // line does not hold a tsp-node
            node = null;
        }

        return node;
    }
}
