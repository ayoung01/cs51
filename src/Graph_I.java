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

    Edge addEdge(Vertex v1, Vertex v2, int weight);

    boolean addVertex(Vertex v);

    EdgeSet edgesOf(Vertex v);

    double getEdgeWeight(Edge e);

    void generateRandomGraph();
    /* hello */

    /*
    Graph interface
     Methods
    Create graph from file: Parses some text file and creates a graph from it
    Generate random graph
    Choose random vertex
    Get adjacent edges to vertex
    Return shortest edge connected to certain vertex
    Return if edge exists between two vertices
    Save graph to file
    Variables: size of graph, number of edges

     */
}
