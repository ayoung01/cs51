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

    EuclideanVertex[] vertices;

    public static void main(String[] args){

    long startTime;
    long endTime;
    double duration;
    TwoDimParser hello = new TwoDimParser("test929.txt", 929);
    hello.printEverything();
    Graph g = new Graph(hello.allVertices());

    int numtrials = 10;
    SimulatedAnnealing sim = new SimulatedAnnealing(100000, 1, 0.95);
    double toursum = 0;
    double timesum = 0;
    for(int i = 0; i < numtrials; i++)
    {
        startTime = System.nanoTime();
        Tour bestsim = sim.findShortestPath(g);
        endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000000.0;
        timesum+=duration;
        toursum+=bestsim.getLength();
    }

    System.out.printf("Simulated Annealing Average over %d trials: %.2f in %.5f s\n ",
            numtrials, toursum/numtrials, timesum/numtrials);
    //bestsim.printGraphToFile("simbest.txt");


    /*TwoOpt twoopt = new TwoOpt();
    for(int i = 0; i < numtrials; i++)
    {
        startTime = System.nanoTime();
        Tour besttwoopt = twoopt.findShortestPath(g);
        endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000000.0;
        timesum+=duration;
        toursum+=besttwoopt.getLength();
    }

        System.out.printf("Two Opt Average over %d trials: %.2f in %.5f s\n ",
                numtrials, toursum/numtrials, timesum/numtrials);
    //besttwoopt.printGraphToFile("twooptbest.txt");
    */
    /*Greedy greedisgood = new Greedy();
    startTime = System.nanoTime();
    Tour bestgreedy = greedisgood.findShortestPath(g);
    endTime = System.nanoTime();
    duration = (endTime - startTime)/1000000000.0;
    System.out.printf("Greedy: %.2f in %.5f s\n", bestgreedy.getLength(), duration);
    bestgreedy.printGraphToFile("Greedybest.txt");

    Christofides christofides = new Christofides();
    startTime = System.nanoTime();
    Tour bestchrist = christofides.findShortestPath(g);
    endTime = System.nanoTime();
    duration = (endTime-startTime)/1000000000.0;
    System.out.printf("Christofides: %.2f in %.5f s\n", bestchrist.getLength(),duration);
    bestchrist.printGraphToFile("christbest.txt");

    Genetic genetic = new Genetic(100,30000);
    startTime = System.nanoTime();
    Tour bestgenetic = genetic.findShortestPath(g);
    endTime = System.nanoTime();
    duration = (endTime-startTime)/1000000000.0;
    System.out.printf("Genetic: %.2f in %.5f s\n", bestgenetic.getLength(),duration);
    bestgenetic.printGraphToFile("Geneticbest.txt");*/

    }

    public TwoDimParser(String f, int numEntries)
    {
      vertices = new EuclideanVertex[numEntries];

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String sCurrentLine;
            String[] xy;
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null && i < numEntries) {
                System.out.println(sCurrentLine);
                xy = sCurrentLine.trim().split("\\s+");
                double[] coords = {Double.parseDouble(xy[1]), Double.parseDouble(xy[2])};
                vertices[i] = new EuclideanVertex(i, coords);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    EuclideanVertex[] allVertices()
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
