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

    TwoDimParser hello = new TwoDimParser("test.txt", 436);
    hello.printEverything();
    Graph g = new Graph(hello.allVertices());

    Greedy greedisgood = new Greedy();
    Tour bestgreedy = greedisgood.findShortestPath(g);
    System.out.printf("Greedy: %.2f\n", bestgreedy.getLength());
    bestgreedy.printGraphToFile("Greedybest.txt");


    SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 5, 0.95);
    Tour bestsim = sim.findShortestPath(g);
    System.out.printf("Simulated Annealing: %.2f\n", bestsim.getLength());
    bestsim.printGraphToFile("simbest.txt");

    TwoOpt twoopt = new TwoOpt(15);
    Tour besttwoopt = twoopt.findShortestPath(g);
    System.out.printf("Two Opt: %.2f\n", besttwoopt.getLength());
    besttwoopt.printGraphToFile("twooptbest.txt");
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
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < vertices.length; i++)
        {
          builder.append(vertices[i].toString() + "\n");
        }

        System.out.print(builder.toString());
    }
}
