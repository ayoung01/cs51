/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/12/13
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Edge {

    double getWeight();

    Vertex getFirstVertex();

    Vertex getSecondVertex();

    String toString();
}
