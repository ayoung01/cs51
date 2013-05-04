import java.util.HashSet;

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
    private HashSet<Integer> set;
    private double weight;

    public Edge(Integer a, Integer b, double w) {
        set = new HashSet<Integer>();
        set.add(a);
        set.add(b);
        weight = w;
    }

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

    public HashSet<Integer> getVertices() {
        return set;
    }

    public Vertex[] getVArray() {
        Object[] arr = set.toArray();
        Vertex res[] = new Vertex[2];
        for (int i=0; i<arr.length; i++) {
            res[i] = new Vertex((Integer)arr[i]);
        }
        return res;
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

    @Override
    public boolean equals(Object o) {
        // null instanceof Object will always return false
        if (!(o instanceof Edge))
            return false;
        if (o == this)
            return true;
        // return false if the edges contain different vertices
        for (Integer n : this.getVertices()) {
            if (!((Edge)(o)).getVertices().contains(n))
                return false;
        }
        return (this.weight == ((Edge)(o)).getWeight());
    }

    @Override
    public int hashCode() {
        return (int)(Math.round(weight * 351) % 2353419);
    }

    public String toString()
    {
        return getFirstVertex().toString() + " " + getSecondVertex().toString() + " " + weight;
    }
}


