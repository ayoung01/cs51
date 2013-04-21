/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/12/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Edge implements Comparable {
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

    public Vertex getFirstVertex() {
        return vertex1;
    }

    public Vertex getSecondVertex() {
        return vertex2;
    }

    public void setWeight(double x) {
        weight = x;
    }

    public void setVertex1(Vertex v) {
        vertex1 = v;
    }

    public void setVertex2(Vertex v) {
        vertex2 = v;
    }

    @Override
    public int compareTo(Object o) {
        Edge e = (Edge)(o);
        if (e.getWeight() > weight)
            return -1;
        else if (e.getWeight() < weight)
            return 1;
        return 0;
    }

    public String toString()
    {
        return getFirstVertex().toString() + " " + getSecondVertex().toString() + " " + weight;
    }
}


