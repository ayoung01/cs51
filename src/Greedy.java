/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/20/13
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Greedy implements TSP_I {

    public Tour findShortestPath(Graph g) {
        Vertex start = g.getRandomVertex();
        int numVertices = g.numVertices();
        Tour t = new Tour(start, numVertices);
        for(int i = 0; i < numVertices - 1; i++)
        {
            Edge e = g.getShortestNeighborEdge(t.getCurrentVertex(),t.verticesSoFar());
            if(!t.addEdge(e))
            {
                System.out.print("Error");
            }
        }

        if(!t.addEdge(g.edgeBetween(t.getCurrentVertex(),start)))
        {
            System.out.print("Error");
        }

        return t;
    }
}


