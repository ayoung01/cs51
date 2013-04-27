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

    long startTime;
    long endTime;
    double duration;
    TwoDimParser hello = new TwoDimParser("test.txt", 29);
    hello.printEverything();
    Graph g = new Graph(hello.allVertices());

    SimulatedAnnealing sim = new SimulatedAnnealing(1000000, 1, 0.995);
    startTime = System.nanoTime();
    Tour bestsim = sim.findShortestPath(g);
    endTime = System.nanoTime();
    duration = (endTime - startTime)/1000000000.0;
    System.out.printf("Simulated Annealing: %.2f in %.5f s\n ", bestsim.getLength(), duration);
    bestsim.printGraphToFile("simbest.txt");

    TwoOpt twoopt = new TwoOpt();
    startTime = System.nanoTime();
    Tour besttwoopt = twoopt.findShortestPath(g);
    endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000000.0;
    System.out.printf("Two Opt: %.2f in %.5f s\n", besttwoopt.getLength(), duration);
    besttwoopt.printGraphToFile("twooptbest.txt");

    Greedy greedisgood = new Greedy();
    startTime = System.nanoTime();
    Tour bestgreedy = greedisgood.findShortestPath(g);
    endTime = System.nanoTime();
    duration = (endTime - startTime)/1000000000.0;

    System.out.printf("Greedy: %.2f in %.5f s\n", bestgreedy.getLength(), duration);
    bestgreedy.printGraphToFile("Greedybest.txt");

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
