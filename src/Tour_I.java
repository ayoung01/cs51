/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/12/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Tour_I {

    double getLength();
    boolean addEdge(Edge e);
    Vertex getCurrentVertex();
    Vertex[] verticesSoFar();


    String toString();
}
