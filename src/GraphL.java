import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ayoung01
 * Date: 4/28/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class GraphL implements Graph_I {

    private int numVertices;
    private HashMap<Integer, LinkedList<Edge>> adjList;
    private Random rand;

    GraphL(EuclideanVertex_2D[] vertices)
    {
        rand = new Random();
        numVertices = vertices.length;
        adjList = new HashMap<Integer, LinkedList<Edge>>();
        for (EuclideanVertex_2D v : vertices) {
            adjList.put(v.getId(), new LinkedList<Edge>());
            for (EuclideanVertex_2D v2 : vertices) {
                if (!(v2==v)) {
                    adjList.get(v).add(new Edge(v, v2, v.dist(v2)));
                }
            }
        }
    }

    // generates a GraphL given an adjacency matrix
    GraphL(double[][] matrix) {
        rand = new Random();
        numVertices = matrix.length;
        adjList = new HashMap<Integer, LinkedList<Edge>>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] > 0)
                    adjList.get(i).add(new Edge(i, j, matrix[i][j]));
            }
        }
    }

    GraphL(HashMap<Integer, LinkedList<Edge>> map) {
        rand = new Random();
        numVertices = map.size();
        adjList = map;
    }

    // generates a GraphL given a Graph
    GraphL(Graph g) {
        rand = new Random();
        double[][] matrix = g.getAdjMat();
        adjList = new HashMap<Integer, LinkedList<Edge>>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] > 0) {
                    if (adjList.get(i) == null) {
                        adjList.put(i, new LinkedList<Edge>());
                    }
                    adjList.get(i).add(new Edge(i, j, matrix[i][j]));
                }
            }
        }
        numVertices = adjList.size();
    }

    GraphL(LinkedList<Edge> edges) {
        rand = new Random();
        adjList = new HashMap<Integer, LinkedList<Edge>>();
        for (Edge e : edges) {
            HashSet<Integer> vertices = e.getVertices();
            Iterator<Integer> itr = vertices.iterator();
            while (itr.hasNext()) {
                int v = itr.next();
                if (adjList.get(v) == null) {
                    adjList.put(v, new LinkedList<Edge>());
                }
                LinkedList<Edge> edgeList = adjList.get(v);
                edgeList.add(e);
                adjList.put(v, edgeList);
            }
        }
        numVertices = adjList.size();
    }

    public Edge getEdge(Integer v1, Integer v2) {
        try {
            LinkedList<Edge> edges = adjList.get(v1);
            for (Edge e : edges) {
                if (e.getVertices().contains(v2))
                    return e;
            }
        }
        catch (Exception e) {
        }
        System.out.println("No edge found!");
        return null;
    }

    public HashMap<Integer, LinkedList<Edge>> getAdjList() {
        return adjList;
    }

    public int getDegree(Integer v) {
        return adjList.get(v).size();
    }

    public HashSet<Integer> getOddVertices() {
        HashSet<Integer> result = new HashSet<Integer>();
        for (Integer i : adjList.keySet()) {
            if (getDegree(i) % 2 != 0)
                result.add(i);
        }
        return result;
    }

    // checks if a Eulerian cycle exists if each vertex is of even degree
    public boolean isEulerian() {
        Set<Integer> vertices = adjList.keySet();
        for (Integer v: vertices) {
            if (getDegree(v) % 2 != 0) {
                return false;
            }
        }
        return true;
    }

    public LinkedList<Edge> edgesOf(Vertex v) {
        LinkedList<Edge> edgeList = adjList.get(v.getId());
        return edgeList;
    }

    public Edge edgeBetween(Vertex v1, Vertex v2) {
        LinkedList<Edge> edgeList = adjList.get(v1.getId());
        for (Edge e : edgeList) {
            if (e.getVertices().contains(v2.getId()))
                return e;
        }
        return null;
    }

    // returns true if all edges are contained in the graph, false otherwise
    public boolean containsAllVertices(LinkedList<Edge>edges) {
        Set<Integer> vertices = new HashSet<Integer>();

        // add all vertices contained by the cycle to a set
        for (Edge e : edges) {
            Iterator<Integer> itr = e.getVertices().iterator();
            while (itr.hasNext()) {
                int v = itr.next();
                vertices.add(v);
            }
        }

        // check to see if each cycle in the graph is contained by the vertex
        for (Integer key : adjList.keySet()) {
            if (!vertices.contains(key)) {
                return false;
            }
        }
        return true;
    }

    public Edge getRandomNeighborEdge(Vertex v) {
        LinkedList<Edge> edgeList = getNeighbors(v);
        int randomNum = rand.nextInt(numVertices);
        while(randomNum == v.getId()) {
            randomNum = rand.nextInt();
        }
        return edgeList.get(randomNum);
    }

    public LinkedList<Edge> getNeighbors(Vertex v) {
        return adjList.get(v.getId());
    }

    public boolean isNotMemberOf(Vertex v, Vertex[] all)
    {
        int x = v.getId();
        for(int i = 0; i < all.length; i++)
        {
            if(x == all[i].getId())
                return false;
        }
        return true;
    }

    public Edge getShortestNeighborEdge (Vertex v,Vertex[] already) {
        Edge minEdge = new Edge(0, 1, -1);
        LinkedList<Edge> edgeList = adjList.get(v.getId());
        for (Edge e : edgeList) {
            if (isNotMemberOf(e.getFirstVertex(), already)) {
                if (e.getWeight() < minEdge.getWeight()) {
                    minEdge = e;
                }
            }
        }
        return minEdge;
    }

    public void deleteEdges (LinkedList<Edge> edgeList) {
        // for each edge, iterate through each vertex and remove the edge from its edge list if the edge exists
        for (Edge e : edgeList) {
            Iterator<Integer> itr = e.getVertices().iterator();
            while (itr.hasNext()) {
                int v = itr.next();
                try{
                    // removes only the first occurrence, so supporting duplicates should be okay
                    adjList.get(v).remove(e);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        // remove any vertices that are no longer connected with the graph
        Iterator iter = adjList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry)iter.next();
            LinkedList<Edge> edges = (LinkedList<Edge>)(pair.getValue());
            Integer key = (Integer)(pair.getKey());
            // if its edge list is of size zero, it must be disconnected
            if (edges.size() == 0) {
                iter.remove();
            }
        }
    }

    public Vertex getRandomVertex() {
        List<Integer> keys = new ArrayList<Integer>(adjList.keySet());
        Integer randomKey = keys.get(rand.nextInt(keys.size()));
        return new Vertex(randomKey);
    }

    public int numVertices() {
        return adjList.size();
    }
}
