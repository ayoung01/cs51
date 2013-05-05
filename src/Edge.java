import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/12/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Edge implements Comparable {

    private HashSet<Integer> set;
    private Vertex[] vertices;
    private double weight;

    public Edge(Integer a, Integer b, double w) {
        set = new HashSet<Integer>();
        set.add(a);
        set.add(b);
        weight = w;
        Object[] arr = set.toArray();
        Vertex res[] = new Vertex[2];
        for (int i=0; i<arr.length; i++) {
            res[i] = new Vertex((Integer)arr[i]);
        }
        vertices = res;
    }

    public Edge(Vertex v1, Vertex v2, double w) {
        vertices = new Vertex[2];
        vertices[0] = v1;
        vertices[1] = v2;
        weight = w;
        /*set = new HashSet<Integer>();
        set.add(v1.getId());
        set.add(v2.getId());*/
    }

    public double getWeight()
    {
        return weight;
    }

    public HashSet<Integer> getVertices() {
        return set;
    }

    public Vertex[] getVArray() {
        return vertices;
    }

    public Vertex getFirstVertex() {
        return vertices[0];
    }

    public Vertex getSecondVertex() {
        return vertices[1];
    }

    public void setWeight(double x) {
        weight = x;
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

    public String toString() {
        Vertex[] arr = getVArray();
        int id1 = arr[0].getId();
        int id2 = arr[1].getId();
        return "(" + id1 + ", " + id2 + "): " + weight;
    }
}


