import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ayoung
 * Date: 4/11/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Graph_I {

    boolean containsVertex( Vertex v);

    boolean containsEdge( Edge e);

    boolean containsEdge(Vertex v1, Vertex v2);

    Edge addEdge(Vertex v1, Vertex v2, double weight);

    Vertex addVertex(Vertex v);

    boolean removeEdge(Edge e);

    boolean removeVertex (Vertex v);

    EdgeSet edgesOf(Vertex v);

    VertexSet verticesOf(Edge e);

    double getEdgeWeight(Edge e);

    /*void generateRandomGraph( double maxWeight);

    void generateGraphFromFile(File file); */

    Vertex getRandomNeighborVertex( Vertex v);

    Edge getRandomNeighborEdge( Vertex v);

    Edge getShortestNeighborEdge (Vertex v);

    VertexSet allVertices();

    EdgeSet allEdges();

    int numEdges();

    int numVertices();

    void printGraphToFile(File file);

    String toString();

}
