/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/12/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private double weight;
    public Edge(Vertex v1, Vertex v2, double w)
    {
      vertex1 = v1;
      vertex2 = v2;
      weight = w;
    }

    public double getWeight()
    {
      return weight;
    }

    public Vertex getFirstVertex()
    {
        return vertex1;
    }

    public Vertex getSecondVertex()
    {
        return vertex2;
    }
}
