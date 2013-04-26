import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/21/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Christofides implements TSP_I {

    private Graph g;

    public Christofides(Graph inputGraph) {
        g = inputGraph;
    }

    public Tour findShortestPath(Graph g)
    {
        int numVertices = g.numVertices();
        Tour t = new Tour(g.getRandomVertex(), numVertices);
        return t;
    }

    // source: http://cs.fit.edu/~ryan/java/programs/graph/Prim-java.html
    Graph PrimMST() {
        // contains vertices with ids 0 to numVertices - 1
        double[][] weights = g.getAdjMat();

        int numVertices = g.numVertices();
        double[] dist = new double[numVertices];
        int[] prev = new int[numVertices];

        // all values initialized to false
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[0] = 0;

        for (int i = 0; i < dist.length; i++) {
            final int nextVertex = getMinVertex(dist, visited);
            visited[nextVertex] = true;

            for (int j = 0; j < numVertices; j++) {
                final double d = weights[i][j];
                if (d < dist[j]) {
                    dist[j] = d;
                    prev[j] = nextVertex;
                }
            }
        }

        // convert prev array to an Edge array
        Edge[] MST = new Edge[numVertices - 1];
        for (int i = 0; i < prev.length; i++) {
            Edge e = new Edge(new Vertex(i), new Vertex(prev[i]), weights[i][prev[i]]);
            MST[i] = e;
        }
        return new Graph(MST);
    }

    int getMinVertex(double[] dist, boolean[] visited) {
        double min_dist = Integer.MAX_VALUE;
        int vertex_id = -1;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] < min_dist && !visited[i]) {
                min_dist = dist[i];
                vertex_id = i;
            }
        }
        return vertex_id;
    }
}
