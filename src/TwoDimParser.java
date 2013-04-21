/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/20/13
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class TwoDimParser {

    EuclideanVertex_2D[] vertices;

    public static void main(String[] args){

    TwoDimParser hello = new TwoDimParser("test.txt", 131);
    hello.printEverything();
    Greedy greedisgood = new Greedy();
    SimulatedAnnealing sim = new SimulatedAnnealing(3);
    Graph g = new Graph(hello.allVertices());
    g.printGraphToFile("lala.txt");
    Tour bestgreedy = greedisgood.findShortestPath(g);
    Tour bestsim = sim.findShortestPath(g);
    System.out.printf("%.2f\n", bestgreedy.getLength());
    bestgreedy.printGraphToFile("Greedybest.txt");
    System.out.printf("%.2f\n", bestsim.getLength());
    bestgreedy.printGraphToFile("simbest.txt");


    }
    public TwoDimParser(String f, int numEntries)
    {
      vertices = new EuclideanVertex_2D[numEntries];

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String sCurrentLine;
            String[] xy;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null && i < numEntries) {
                System.out.println(sCurrentLine);
                xy = sCurrentLine.trim().split("\\s+");
                vertices[i] = new EuclideanVertex_2D(i, Double.parseDouble(xy[1]), Double.parseDouble(xy[2]));
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    EuclideanVertex_2D[] allVertices()
    {
        return vertices;
    }

    void printEverything()
    {
        String mystring = "";
        for(int i = 0; i < vertices.length; i++)
        {
          mystring+= vertices[i].toString() + "\n";
        }

        System.out.print(mystring);
    }
}
