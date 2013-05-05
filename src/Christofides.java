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
        Edge[] tsp_tour = new Edge[christofides_solution.size()];
        for (int i = 0; i < tsp_tour.length; i++) {
            tsp_tour[i] = christofides_solution.get(i);
        }
        return new Tour(tsp_tour);
    }

    private LinkedList<Edge> Christofides(Graph g) {
        /*
        Create the minimum spanning tree MST T of G.
        Let O be the set of vertices with odd degree in T and find a perfect matching M with minimum weight in the complete graph over the vertices from .
        Combine the edges of and to form a multigraph O.
        Form an Eulerian circuit in  (H is Eulerian because it is connected, with only even-degree vertices).
        Make the circuit found in previous step Hamiltonian by skipping visited nodes (shortcutting).
         */
        GraphL gL = new GraphL(g);
        GraphL gL_orig = new GraphL(g);
        Graph mst = PrimMST(g);
        GraphL mstL = new GraphL(mst);
        HashSet<Integer> oddVertices = mstL.getOddVertices();
        GraphL matches = greedyMatch(oddVertices, gL);
        GraphL multigraph = combineGraphs(mstL, matches);
        LinkedList<Edge> euler = Hierholzer(multigraph);
        return shortcutPaths(euler, gL_orig);
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

            // assuming complete graph
            for (int j = 0; j < numVertices; j++) {
                if (j != nextVertex && !visited[j]) {
                    final double d = weights[i][j];
                    if (d < dist[j]) {
                        dist[j] = d;
                        prev[j] = nextVertex;
                    }
                }
            }
        }

        // convert prev array to an Edge array
        Edge[] MST = new Edge[numVertices - 1];
        for (int i = 1; i < prev.length; i++) {
            Edge e = new Edge(new Vertex(i), new Vertex(prev[i]), weights[i][prev[i]]);
            MST[i-1] = e;
        }
        Graph graph = new Graph(MST);
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

    // finds a matching with approximate minimum weight in the COMPLETE GRAPH using the greedy approach
    private GraphL greedyMatch(HashSet<Integer> vertices, GraphL g) {
        LinkedList<Edge> matches = new LinkedList<Edge>();
        HashMap<Integer, LinkedList<Edge>> adjList = g.getAdjList();
        int num_matches = vertices.size() / 2;

        // delete all edges/vertices that are not incident on the given vertices
        HashMap<Integer, LinkedList<Edge>> trash = new HashMap<Integer, LinkedList<Edge>>();

        Iterator e_iter = adjList.entrySet().iterator();
        while (e_iter.hasNext()) {
            Map.Entry pair = (Map.Entry)e_iter.next();
            LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
            Integer key = (Integer)(pair.getKey());
            outer:
            for (Edge e : edges) {
                Iterator<Integer> v_iter = e.getVertices().iterator();
                while (v_iter.hasNext()) {
                    int v = v_iter.next();
                    if (!vertices.contains(v)) {
                        // save for later removal
                        if (trash.get(key) == null) {
                            trash.put(key, new LinkedList<Edge>());
                        }
                        trash.get(key).add(e);
                        continue outer;
                    }
                }
            }
        }

        // delete all the edges marked
        Iterator iterator0 = trash.entrySet().iterator();

        while (iterator0.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator0.next();
            Integer key = (Integer)(pair.getKey());
            LinkedList<Edge> edge_list = (LinkedList<Edge>)(pair.getValue());
            if (!vertices.contains(key)) {
                adjList.remove(key);
            }
            else {
                for (Edge e : edge_list) {
                    adjList.get(key).remove(e);
                }
            }
        }

        // while we can still match two vertices
        while (matches.size() < num_matches) {
            // find the lowest weight edge
            Iterator it = adjList.entrySet().iterator();
            Edge emin = new Edge(0, 1, Double.MAX_VALUE);
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
                outer:
                for (Edge e : edges) {
                    // ignore edge if it is not incident on any of the given vertices
                    Iterator<Integer> itr = e.getVertices().iterator();
                    while (itr.hasNext()) {
                        int v = itr.next();
                        if (!vertices.contains(v)) {
                            continue outer;
                        }
                    }

                    if (e.getWeight() < emin.getWeight()) {
                        emin = e;
                    }
                }
            }

            // add the edge to the result graph
             matches.add(emin);

            HashMap<Integer, LinkedList<Edge>> deathRow = new HashMap<Integer, LinkedList<Edge>>();

            // remove all edges incident on the vertices
            Iterator edge_iter = adjList.entrySet().iterator();
            while (edge_iter.hasNext()) {
                Map.Entry pair = (Map.Entry)edge_iter.next();
                LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
                Integer currkey = (Integer)(pair.getKey());
                for (Edge e : edges) {
                    Iterator<Integer> vertex_iter = emin.getVertices().iterator();
                    while (vertex_iter.hasNext()) {
                        int v = vertex_iter.next();
                        if (e.getVertices().contains(v)) {
                            // save for later removal
                            if (deathRow.get(currkey) == null) {
                                deathRow.put(currkey, new LinkedList<Edge>());
                            }
                            deathRow.get(currkey).add(e);
                            Vertex[] arr = e.getVArray();
                            int id1 = arr[0].getId();
                            int id2 = arr[1].getId();
//                            System.out.println("Sentenced (" + id1 + ", " + id2 + ") to execution");
                        }
                    }
                }
            }

            // delete all the edges marked on death row
            Iterator iterator = deathRow.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                Integer key1 = (Integer)(pair.getKey());
                LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
                for (Edge e : edges) {
                    adjList.get(key1).remove(e);
                }
            }
        }
        return new GraphL(matches);
    }

    // combines two GraphL's. Supports multiple edges between two vertices.
    private GraphL combineGraphs(GraphL a, GraphL b) {
        HashMap<Integer, LinkedList<Edge>> map_combined = new HashMap<Integer, LinkedList<Edge>>();
        HashMap<Integer, LinkedList<Edge>> a_list = a.getAdjList();
        map_combined.putAll(a_list);
        Iterator it = b.getAdjList().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Integer key = (Integer)pair.getKey();
            LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
            if (map_combined.get(key) == null) {
                map_combined.put(key, edges);
            }
            else {
                for (Edge e : edges) {
                    LinkedList ll = map_combined.get(key);
                    ll.add(e);
                }
            }
        }
        return new GraphL(map_combined);
    }

    // uses Hierholzer's Algorithm to find a Eulerian circuit
    private LinkedList<Edge> Hierholzer (GraphL g) {
        if (!g.isEulerian()) {
            System.out.println("Invalid non-Eulerian input");
            return null;
        }
        Vertex v = g.getRandomVertex();

        // initialize the complete cycle and the next cycle to be merged
        LinkedList<Edge> cycle = makeCycle(g, v);
        LinkedList<Edge> next_cycle = cycle;

        // while the complete cycle does not contain all vertices of G
        while(!g.containsAllVertices(cycle)) {
            // find an edge neighboring the cycle
            Edge neighbor = findCycleNeighborEdge(g, cycle);
            // start on a vertex on the cycle
            Vertex v1 = neighbor.getFirstVertex();

            // remove the edges and vertices of the cycle to avoid turning them up again
            if (!(cycle == null))
                g.deleteEdges(next_cycle);

            // continue building the circuit
            next_cycle = makeCycle(g, v1);
            cycle = mergeTours(cycle, next_cycle);
        }

        return cycle;
    }

    private Edge findCycleNeighborEdge(GraphL g, LinkedList<Edge> cycle) {
        // Find a vertex v1 on the cycle that is incident with an unmarked edge
        for (Edge e : cycle) {
            Vertex[] arr = e.getVArray();
            LinkedList<Edge> neighbors = g.getNeighbors(arr[0]);
            if (neighbors == null)
                neighbors = g.getNeighbors(arr[1]);
            if (neighbors == null)
                continue;
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
        if (g.numVertices() < 2) {
            System.out.println("Invalid input graph: cannot find cycle on single node");
            return null;
        }

        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        Set<Integer> vs = new HashSet<Integer>();
        Set<Edge> es = new HashSet<Edge>();

        // need to do DFS until we return to original vertex and keep track of order visited
        Stack<Vertex> s = new Stack<Vertex>();
        s.push(v_source);

        // label v as discovered
        vs.add(v_source.getId());

        // while the stack is not empty
        outer:
        while(!s.empty()) {
            Vertex v = s.pop();
            // label v as explored
            vs.add(v.getId());

            // if we are back at the source vertex, return our cycle
            if (v == v_source && edgeList.size() > 1) {
                return edgeList;
            }

            // for all edges e adjacent to v
            for (Edge e : g.edgesOf(v)) {
                // if edge e is already labeled continue with next edge
                if (es.contains(e))
                    continue;

                Vertex[] v_array = e.getVArray();
                Vertex w;

                // set w to the vertex of the edge that is not v
                if (v_array[0].getId() == v.getId()) {
                    w = v_array[1];
                }
                else {
                    w = v_array[0];
                }

                // if we are back at the source vertex, return our cycle
                if (edgeList.size() > 1 && e.getVertices().contains(v_source.getId())) {
                    edgeList.add(e);
                    return edgeList;
                }

                // if vertex w is not discovered and not the source vertex
                if (!vs.contains(w)) {
                    // label w and e as discovered
                    vs.add(w.getId());
                    es.add(e);

                    // push w onto the stack and and e to the list
                    s.push(w);
                    edgeList.add(e);
                    continue outer;
                }
            }
        }
        // if we have a single edge, then let's just duplicate it
        if (edgeList.size() < 2) {
            edgeList.add(edgeList.getFirst());
        }
        return edgeList;
    }

    // going off example here http://web.info.uvt.ro/~mmarin/lectures/GTC/c-09-new.pdf
    // merges tours by adding edges of list2 in order to list1
    private LinkedList<Edge> mergeTours(LinkedList<Edge> list1, LinkedList<Edge> list2) {

        LinkedList<Edge> result = list1;

        // take a vertex in the cycle that we're adding that is also in the first cycle
        Vertex[] arr = list2.getFirst().getVArray();
        int insert = arr[0].getId();
        int index = -100;

        // figures out which index we want to start adding edges to
        ListIterator<Edge> iterator = list1.listIterator(0);

        // iterate through edges of first cycle
        while (iterator.hasNext()) {
            Edge e = iterator.next();

            // if we have a first match
            if (e.getVertices().contains(insert)) {
                if (iterator.hasNext()) {
                    e = iterator.next();
                    // if we have as second match, take note of index
                    if (e.getVertices().contains(insert)) {
                        index = list1.indexOf(e) - 1;
                    }
                }
                else {
                    // we must be at the last vertex in the cycle--which wraps around to the first
                    index = list1.indexOf(e);
                }
            }
        }
        if (index < 0) {
            // we'll try again with the other vertex!~
            insert = arr[1].getId();

            // figures out which index we want to start adding edges to
            ListIterator<Edge> iterator2 = list1.listIterator(0);

            // iterate through edges of first cycle
            while (iterator2.hasNext()) {
                Edge e = iterator2.next();

                // if we have a first match
                if (e.getVertices().contains(insert)) {
                    if (iterator2.hasNext()) {
                        e = iterator2.next();
                        // if we have as second match, take note of index
                        if (e.getVertices().contains(insert)) {
                            index = list1.indexOf(e) - 1;
                        }
                    }
                    else {
                        // we must be at the last vertex in the cycle--which wraps around to the first
                        index = list1.indexOf(e);
                    }
                }
            }
        }
        if (index < 0) {
            System.out.println("Couldn't figure out where to insert");
            return null;
        }
        result.addAll(index + 1, list2);
        return result;
    }



    // turns a Eulerian circuit into a Hamiltonian path by skipping visited nodes
    private LinkedList<Edge> shortcutPaths(LinkedList<Edge>edges, GraphL g) {
        // figure out which is the source vertex
        Edge first_edge = edges.get(0);
        Edge second_edge = edges.get(1);
        Vertex[] arr = first_edge.getVArray();
        int source;

        int v1 = arr[0].getId();
        int v2 = arr[1].getId();

        if (second_edge.getVertices().contains(v1)) {
            source = v1;
        }
        else {
            source = v2;
        }

        LinkedList<Edge> hamiltonian_path = new LinkedList<Edge>();
        LinkedList<Integer> vs = new LinkedList<Integer>();
        LinkedList<Integer> shortcut_vs = new LinkedList<Integer>();

        vs.add(source);
        int curr_vertex = source;

        // create a list of vertices in the order in which they are visited
        for (Edge e : edges) {
            // get the next vertex (of the edge) that is not the current vertex
            Vertex[] v_arr = e.getVArray();
            int v_1 = v_arr[0].getId();
            int v_2 = v_arr[1].getId();
            if (curr_vertex == v_1) {
                vs.add(v_2);
                curr_vertex = v_2;
            }
            else {
                vs.add(v_1);
                curr_vertex = v_1;
            }
        }

        // copy the list of vertices but shortcut any nodes that have already been visited
        for (Integer v : vs) {
            if (!(shortcut_vs.contains(v))) {
                shortcut_vs.add(v);
            }
        }

        // create the list of edges in the resultant Hamiltonian path
        for (int i = 0; i < shortcut_vs.size() - 1; i++) {
            hamiltonian_path.add(g.getEdge(shortcut_vs.get(i), shortcut_vs.get(i+1)));
        }

        // add the last edge to close the cycle
        hamiltonian_path.add(g.getEdge(shortcut_vs.getLast(), source));
        return hamiltonian_path;
    }
}