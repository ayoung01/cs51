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

      for(int i = 0; i < numVertices; i++)
      {
        if(i != x)
        {

        }

      }
    }

    Edge getRandomNeighborEdge( Vertex v);

    Edge getShortestNeighborEdge (Vertex v);

    Vertex[] allVertices();

    Edge[] allEdges();

    int numVertices();

    void printGraphToFile(File file);

    String toString();
}
