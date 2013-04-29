import java.util.*;

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
        LinkedList<Edge> christofides_solution = Christofides(g);
        return new Tour((Edge[])(christofides_solution.toArray()));
    }

    private LinkedList<Edge> Christofides(Graph g) {
        /*
        Create the minimum spanning tree MST T of G.
        Let O be the set of vertices with odd degree in T and find a perfect matching M with minimum weight in the complete graph over the vertices from .
        Combine the edges of and to form a multigraph O.
        Form an Eulerian circuit in  (H is Eulerian because it is connected, with only even-degree vertices).
        Make the circuit found in previous step Hamiltonian by skipping visited nodes (shortcutting).
         */
        Graph mst = PrimMST(g);
        Graph odd_mst = oddVerticesOnly(mst);
        Edge[] matches = greedyMatch(odd_mst);
        GraphL multigraph = combineGraphs(mst, matches);
        LinkedList<Edge> euler = Hierholzer(multigraph);
        return shortcutPaths(euler, new GraphL(g));
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
        HashMap<Vertex, LinkedList<Edge>> adjList = g.getAdjList();
        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        Vertex v = g.getRandomVertex();
        LinkedList<Edge> cycle = makeCycle(g, v);

        // while the cycle does not contain all edges of G
        while(!containsAllEdges(g, cycle)) {
            Edge neighbor = findCycleNeighborEdge(g, cycle);
            Vertex v1 = neighbor.getFirstVertex();

            // remove the edges of the cycle to avoid turning them up again
            g.deleteEdges(cycle);

            // continue building the circuit
            cycle = mergeTours(cycle, makeCycle(g, v1));
        }

        return edgeList;
    }

    private Edge findCycleNeighborEdge(GraphL g, LinkedList<Edge> cycle) {
        // Find a vertex v1 on the cycle that is incident with an unmarked edge
        for (Edge e : cycle) {
            LinkedList<Edge> neighbors = g.getNeighbors(e.getFirstVertex());
            for (Edge neighbor : neighbors) {
                if (!(cycle.contains(neighbor))) {
                    return neighbor;
                }
            }
        }
        System.out.println("Failed to find edge not included on cycle");
        return null;
    }

    // constructs a cycle in graph g starting from vertex v_source
    private LinkedList<Edge> makeCycle(GraphL g, Vertex v_source) {
        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        HashMap<Vertex, Boolean> discovered_v = new HashMap<Vertex, Boolean>();
        HashMap<Edge, Boolean> discovered_e = new HashMap<Edge, Boolean>();
        // need to do DFS until return to original vertex and keep track of order visited
        Stack<Vertex> s = new Stack<Vertex>();

        // label v as discovered
        discovered_v.put(v_source, Boolean.TRUE);

        // while the stack is not empty
        outer:
        while(!s.empty()) {
            Vertex v = s.pop();

            // if we are back at the source vertex, return our cycle
            if (v == v_source) {
                return edgeList;
            }

            // for all edges e adjacent to v
            for (Edge e : g.edgesOf(v)) {
                // if edge e is already labeled continue with next edge
                if (discovered_e.get(e))
                    continue;

                Vertex w = e.getSecondVertex();

                // if we are back at the source vertex, return our cycle
                if (w == v_source) {
                    edgeList.add(e);
                    return edgeList;
                }

                // if vertex w is not discovered and not the source vertex
                if (!discovered_v.get(w)) {
                    // label w and e as discovered
                    discovered_v.put(w, Boolean.TRUE);
                    discovered_e.put(e, Boolean.TRUE);

                    // push w onto the stack and and e to the list
                    s.push(w);
                    edgeList.add(e);
                    continue outer;
                }
            }
            // label v as explored
            discovered_v.put(v, Boolean.TRUE);
        }
        return edgeList;
    }

    // going off example here http://web.info.uvt.ro/~mmarin/lectures/GTC/c-09-new.pdf
    // merges tours by adding edges of list2 in order to list1
    private LinkedList<Edge> mergeTours(LinkedList<Edge> list1, LinkedList<Edge> list2) {

        LinkedList<Edge> result = list1;
        Vertex start2 = list2.getFirst().getFirstVertex();
        int index = -1;

        // figures out which index we want to start adding edges to
        ListIterator<Edge> iterator = list1.listIterator(0);
        while (iterator.hasNext()) {
            Edge e = iterator.next();
            Vertex v2 = e.getSecondVertex();
            if (v2 == start2) {
                index = list1.indexOf(e);
            }
        }

        result.addAll(index + 1, list2);
        return result;
    }

    // returns true if all edges in 'edges' are contained in graph 'g', false otherwise
    private boolean containsAllEdges(GraphL g, LinkedList<Edge>edges) {
        HashMap<Vertex, LinkedList<Edge>> adjList = g.getAdjList();
        for (Edge e : edges) {
            Vertex v = e.getFirstVertex();
            if (!(adjList.get(v).contains(e))) {
                return false;
            }
        }
        return true;
    }

    // turns a Eulerian circuit into a Hamiltonian path by skipping visited nodes
    private LinkedList<Edge> shortcutPaths(LinkedList<Edge>edges, GraphL g) {
        Vertex source = edges.getFirst().getFirstVertex();
        HashMap<Vertex, LinkedList<Edge>> adjList = g.getAdjList();

        LinkedList<Edge> hamiltonian_path = new LinkedList<Edge>();
        LinkedList<Vertex> vs = new LinkedList<Vertex>();
        LinkedList<Vertex> shortcut_vs = new LinkedList<Vertex>();

        // create a list of vertices in the order in which they are visited
        for (Edge e : edges) {
            vs.add(e.getFirstVertex());
        }

        // copy the list of vertices but shortcut any nodes that have already been visited
        for (Vertex v : vs) {
            if (!(shortcut_vs.contains(v))) {
                shortcut_vs.add(v);
            }
        }

        // create the list of edges in the resultant Hamiltonian path
        for (int i = 0; i < shortcut_vs.size()-1; i++) {
            Vertex v1 = shortcut_vs.get(i);
            Vertex v2 = shortcut_vs.get(i+1);
            hamiltonian_path.add(g.getEdge(v1, v2));
        }

        // add the last edge to close the cycle
        hamiltonian_path.add(g.getEdge(hamiltonian_path.getLast().getSecondVertex(), source));
        return hamiltonian_path;
    }
}
