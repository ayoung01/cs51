/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/26/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class TwoOpt {
    private int numVertices;
    private int numloops;

    public TwoOpt(int loops)
    {
        numloops = loops;
    }
    public Tour findShortestPath(Graph g)
    {
        Tour start = g.getRandomTour();
        Tour comparison;
        numVertices = g.numVertices();
        for(int k = 0; k < numloops; k++)
        {
            for(int i = 1; i < numVertices; i++)
            {
                for(int j = 1; j < i; j++)
                {
                    comparison = new Tour(neighborVertexSet(start.verticesSoFar(),j,i),g);
                    //System.out.printf("%s", comparison.toString());
                    if(comparison.getLength() < start.getLength())
                    {
                        start = comparison;
                    }
                }
            }
        }

        return start;
    }

    Vertex[] neighborVertexSet(Vertex[] vertices, int a, int b)
    {
        if(a == b)
        {
            return vertices;
        }

        else
        {
            Vertex[] newv = new Vertex[vertices.length];

            for(int i = 0; i < a; i++)
            {
                newv[i] = vertices[i];
            }

            newv[a] = vertices[b];

            for(int i = a + 1; i < b; i++)
            {
                newv[i] = vertices[b + a - i];
            }
            newv[b] = vertices[a];

            for(int i = b + 1; i < vertices.length; i++)
            {
                newv[i] = vertices[i];
            }
            return newv;
        }

    }

}
