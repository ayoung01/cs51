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
        TwoDimParser hello = new TwoDimParser("test.txt", 29);
        //hello.printEverything();
        Graph g = new Graph(hello.allVertices());


        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
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

        /*Christofides christofides = new Christofides();
        startTime = System.nanoTime();
        Tour bestchrist = christofides.findShortestPath(g);
        endTime = System.nanoTime();
        duration = (endTime-startTime)/1000000000.0;
        System.out.printf("Christofides: %.2f in %.5f s\n", bestchrist.getLength(),duration);
        bestchrist.printGraphToFile("christbest.txt");  */

        System.out.println();
        // good population size seems to be around 3 times number of cities
        Genetic genetic = new Genetic(100, 10000);
        double genLengthSum = 0;
        double genTimeSum = 0;
        int numTimes = 10;
        for(int i=0;i<numTimes;i++) {
            startTime = System.nanoTime();
            Tour bestgenetic = genetic.findShortestPath(g);
            endTime = System.nanoTime();
            duration = (endTime-startTime)/1000000000.0;
            genTimeSum += (endTime-startTime)/1000000000.0;
            genLengthSum += bestgenetic.getLength();
            System.out.printf("Genetic: %.2f in %.5f s\n", bestgenetic.getLength(),duration);
            bestgenetic.printGraphToFile("Geneticbest.txt");
        }
        System.out.printf("Average Genetic: %.2f in %.5f s\n", genLengthSum/numTimes, genTimeSum/numTimes);


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
                //System.out.println(sCurrentLine);
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
