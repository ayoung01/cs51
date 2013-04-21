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

    Vertex[] vertices;

    public static void main(String[] args){

    TwoDimParser hello = new TwoDimParser("test.txt", 20);
    hello.printEverything();
    Greedy greedisgood = new Greedy();
    Graph g = new Graph(hello.allVertices());
    g.printGraphToFile("lala.txt");
    //System.out.printf("%.2f", greedisgood.findShortestPath(g).getLength());


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
                vertices[i] = new EuclideanVertex_2D(i, Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Vertex[] allVertices()
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
