/*Two Opt Algorithm: Takes a given tour, and at each step,
  swaps the order the tour goes through two vertices, until
  the tour cannot be improved any more*/

public class TwoOpt {
    private int numVertices;

    //returns a tour containing the shortest path TwoOpt finds
    public Tour findShortestPath(Graph g)
    {
        numVertices = g.numVertices();

        Tour start = g.getRandomTour();
        Tour comparison;

        int c = 1;
        while(c > 0)
        {
            c = 0;
            for(int i = 0; i < numVertices; i++)
            {
                for(int j = 0; j < i; j++)
                {
                    comparison = new Tour(neighborVertexSet(start.verticesSoFar(),j,i),g);
                    //System.out.printf("%s", comparison.toString());
                    if(comparison.getLength() < start.getLength())
                    {
                        start = comparison;
                        c++;
                    }
                }
            }
        }

        return start;
    }

    //returns a rearrangement of the vertices in a tour, with the vertices
    //at a and b swapped
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
