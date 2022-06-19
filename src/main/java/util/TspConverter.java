package util;

import java.io.*;
import java.util.ArrayList;

//reads csv example
public class TspConverter {
    public ArrayList<Node> generateFromFile(String file) {
        ArrayList<Node> nodes = new ArrayList<>(); //List to save all tsp nodes
        InputStream resource = getClass().getClassLoader().getResourceAsStream(file + ".txt");
        if (resource == null) {
            System.out.println("The file " + file + " was not found.");
            System.out.println("Be sure the file was in the resource folder when the project was build");
            System.exit(0);
        }


        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
            String lineContent;

            //check all lines for tsp nodes
            while ((lineContent = br.readLine()) != null) {
                Node node = generateNode(lineContent);
                if (node != null) nodes.add(node);
            }
        } catch (IOException e) {
            //could not read line
            throw new RuntimeException(e);
        }

        return nodes;
    }

    private Node generateNode(String lineContent) {
        Node node;
        String[] contents = lineContent.trim().replaceAll(" +", " ").split(" ");

        try {
            int nr = Integer.parseInt(contents[0]);
            int x = Integer.parseInt(contents[1]);
            int y = Integer.parseInt(contents[2]);
            NodePosition pos = new NodePosition(x, y);
            node = new Node(nr-1, pos);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // line does not hold a tsp-node
            node = null;
        }

        return node;
    }
}
