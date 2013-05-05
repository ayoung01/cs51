
public interface Tour_I {

    double getLength();
    boolean addEdge(Edge e);
    Vertex getCurrentVertex();
    Vertex[] verticesSoFar();
    Edge[] allEdges();
    String toString();
    void printGraphToFile(String file);
}
