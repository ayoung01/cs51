import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/14/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tour {
    private double length;
    private Edge[] touredges;
    private Vertex[] vertices;
    private int currpos;
    private int num;

    public Tour (Edge[] edges) {
        length = 0;
        touredges = edges;
        num = edges.length;
        vertices = new Vertex[num];
        for(int i = 0; i < num; i++)
        {
            vertices[i] = edges[i].getFirstVertex();
            length+= edges[i].getWeight();
        }
        currpos = num;
    }

    public Tour(Vertex[] allVertices, Graph g)
    {
        num = g.numVertices();
        length = 0;
        touredges = new Edge[num];
        vertices = allVertices;

        for(int i = 0; i < num; i++)
        {
          touredges[i] = g.edgeBetween(vertices[(i%num)],vertices[(i+1)%num]);
          length+=touredges[i].getWeight();
        }

        currpos = num;
    }

    public Tour (Vertex v, int numVertices)
    {
        this.length = 0;
        num = numVertices;
        touredges = new Edge[num];
        vertices = new Vertex[num];
        vertices[0] = v;
        currpos = 0;
    }

    boolean containsVertex(Vertex v)
    {
        int x = v.getId();
        for(int i = 0; i <= currpos; i++)
        {
            if (x == vertices[i].getId())
            {
                return true;
            }
        }

        return false;
    }

    boolean addEdge(Edge e)
    {
        if( (e.getFirstVertex().getId() != vertices[currpos].getId()
                || containsVertex(e.getSecondVertex()))
                && currpos != num - 1)
        {
            return false;
        }
        else
        {
            touredges[currpos] = e;
            if(currpos < num - 1)
            {
                vertices[currpos+1] = e.getSecondVertex();
            }
            currpos++;
            length+=e.getWeight();
            return true;
        }
    }

    Vertex getCurrentVertex()
    {
        return vertices[currpos];
    }

    Edge[] allEdges()
    {
        return touredges;
    }

    Vertex[] verticesSoFar()
    {
        Vertex[] soFar = new Vertex[currpos];
        for(int i = 0; i < currpos; i++)
        {
            soFar[i] = vertices[i];
        }
        return soFar;
    }

    double getLength() {
        return length;
    }

    public String toString() {
        String mystring = "";
        for(int i = 0; i < currpos; i++)
        {
            mystring+=touredges[i].toString() + "\n";
        }

        return mystring;
    }

    void printGraphToFile(String file)
    {
        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.print(toString());
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
