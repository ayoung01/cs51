//The highest level abstraction of a Vertex

public class Vertex implements Vertex_I{
    /* Do we want to add instance variables such that the vertex occupies some position in an n-dimensional space? */
    private int id;

    public Vertex()
    {
        id = -1;
    }
    public Vertex(int i)
    {
        id = i;
    }

    public int getId() {
        return id;
    }

    public void setId(int i)
    {
        id = i;
    }

    public double dist(Vertex v2)
    {
      return 0;
    }

    public String toString() {
        return Integer.toString(id);
    }


}
