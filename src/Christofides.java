import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/21/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Christofides implements TSP_I {
    public Tour findShortestPath(Graph g)
    {
        int numVertices = g.numVertices();
        Tour t = new Tour(g.getRandomVertex(), numVertices);
        return t;
    }

    // source: http://cs.fit.edu/~ryan/java/programs/graph/Prim-java.html
    // returns an MST
    private Graph PrimMST(Graph g) {
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

    // helper function that identifies the next vertex to pop off priority queue
    private int getMinVertex(double[] dist, boolean[] visited) {
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

    // returns a graph with all vertices of even degree removed
    private Graph oddVerticesOnly(Graph g) {
        double[][] adjMat = g.getAdjMat();
        // set all weights of edges adjacent to even-degree vertices to zero
        for (int i = 0; i < adjMat.length; i++) {
            if (g.getDegree(i) % 2 == 0) {
                for (int j = 0; j < adjMat.length; j++) {
                    adjMat[i][j] = Double.MAX_VALUE;
                    adjMat[j][i] = Double.MAX_VALUE;
                }
            }
        }
        return new Graph(adjMat);
    }
    // finds a matching with approximate minimum weight in the complete graph using the
    // greedy approach
    private Edge[] greedyMatch(Graph g) {
        double[][] weights = g.getAdjMat();

        // number of edges will be 1/2 of the number of vertices
        Edge[] edges = new Edge[weights.length/2];
        int edges_index = 0;

        for (int iter = 0; iter < weights.length/2; iter++) {
            // find lowest weight edge in the graph by iterating through all edges
            // save the vertex ids of the lowest weight edge
            double min = Double.MAX_VALUE;
            int min_id1 = -1;
            int min_id2 = -1;
            for (int i = 0; i < weights.length; i++) {
                for (int j = i + 1; j < weights.length; j++) {
                    if (weights[i][j] < min) {
                        min_id1 = i;
                        min_id2 = j;
                    }
                }
            }
            // remove all edges incident on the two vertices by setting weight to infinity
            for (int i = 0; i < weights.length; i++) {
                weights[min_id1][i] = Double.MAX_VALUE;
                weights[i][min_id1] = Double.MAX_VALUE;
                weights[min_id2][i] = Double.MAX_VALUE;
                weights[i][min_id2] = Double.MAX_VALUE;
            }
            // add a new Edge to the result array
            try {
                edges[edges_index++] = new Edge(new Vertex(min_id1),
                        new Vertex(min_id2), min);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return edges;
    }

    // combines the edges of the MST and the matching into a new graph
    private GraphL combineGraphs(Graph mst, Edge[] matches) {
        GraphL g = new GraphL(mst);
        g.addEdges(matches);
        return g;
    }

    // uses Hierholzer's Algorithm to find a Eulerian circuit
    private LinkedList<Edge> Hierholzer (GraphL g) {
        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        Vertex v = g.getRandomVertex();

        return edgeList;
    }

    // constructs a cycle in graph g starting from vertex v
    private LinkedList<Edge> makeCycle(GraphL g, Vertex v) {
        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        // need to do DFS until return to original vertex and keep track of order visited
        return edgeList;
    }
}
