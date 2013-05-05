/*Greedy Algorithm: Starts at a random vertex and at each
 * step finds the closest non-traversed vertex, and adds it to the tour */
public class Greedy implements TSP_I {

    //finds the shortest path using the Greedy algorithm given a graph
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


