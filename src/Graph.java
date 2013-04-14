import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ayoung
 * Date: 4/11/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Graph {

    private int numVertices;
    private double[][] adjMat;

    public Graph(Vertex[] vertices)
    {
        numVertices = vertices.length;
        adjMat = new double [numVertices][numVertices] ;
        for(int i = 0; i < numVertices; i++)
        {
            for(int j = 0; j <= i; j++ )
            {
                double val = vertices[i].dist(vertices[j]);
                adjMat[i][j] = val;
                adjMat[j][i] = val;
            }
        }
    }

    Edge[] edgesOf(Vertex v)
    {
      int x = v.getId();
      Edge[] edgeList = new Edge[numVertices-1];
      int counter = 0;
      for(int i = 0; i < numVertices; i++)
      {
        if(i != x)
        {
            Vertex v2 = new Vertex(i);
            Edge e = new Edge(v,v2,v.dist(v2));
            edgeList[counter] = e;
            counter++;
        }
      }

        return edgeList;
    }

    Edge getRandomNeighborEdge( Vertex v);

    Edge getShortestNeighborEdge (Vertex v)
    {
        int x = v.getId();
        int minweight = 0;
        for(int i = 0; i < )
    }

    Vertex[] allVertices();

    Edge[] allEdges();

    int numVertices();

    void printGraphToFile(File file);

    String toString();
}
