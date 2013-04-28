import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ayoung01
 * Date: 4/28/13
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class GraphL implements Graph_I {

    private int numVertices;
    private HashMap<Vertex, LinkedList<Edge>> adjList;
    private Random rand;

    GraphL(EuclideanVertex_2D[] vertices)
    {
        rand = new Random();
        numVertices = vertices.length;
        adjList = new HashMap<Vertex, LinkedList<Edge>>();
        for (EuclideanVertex_2D v : vertices) {
            adjList.put(v, new LinkedList<Edge>());
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
        adjList = new HashMap<Vertex, LinkedList<Edge>>();
        for (int i = 0; i < matrix.length; i++) {
            Vertex v1 = new Vertex(i);
            for (int j = 0; j < matrix.length; j++) {
                Vertex v2 = new Vertex(j);
                if (matrix[i][j] < Double.MAX_VALUE)
                    adjList.get(v1).add(new Edge(v1, v2, matrix[i][j]));
            }
        }
    }

    // generates a GraphL given a Graph
    GraphL(Graph g) {
        rand = new Random();
        double[][] matrix = g.getAdjMat();
        numVertices = g.numVertices();
        adjList = new HashMap<Vertex, LinkedList<Edge>>();
        for (int i = 0; i < numVertices; i++) {
            Vertex v1 = new Vertex(i);
            for (int j = 0; j < numVertices; j++) {
                Vertex v2 = new Vertex(j);
                if (matrix[i][j] < Double.MAX_VALUE)
                    adjList.get(v1).add(new Edge(v1, v2, matrix[i][j]));
            }
        }
    }

    GraphL(Edge[] edges) {
        rand = new Random();
        adjList = new HashMap<Vertex, LinkedList<Edge>>();
        for (Edge e : edges) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            adjList.get(v1).add(e);
            adjList.get(v2).add(e);
        }
        numVertices = adjList.size();
    }

    public void addEdges(Edge[] edges) {
        for (Edge e : edges) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            adjList.get(v1).add(e);
            adjList.get(v2).add(e);
        }
    }

    public HashMap<Vertex, LinkedList<Edge>> getAdjList() {
        return adjList;
    }

    public int getDegree(Vertex v) {
        return adjList.get(v).size();
    }

    // checks if a Eulerian cycle exists if each vertex is of even degree
    public boolean isEulerian() {
        Set<Vertex> vertices = adjList.keySet();
        for (Vertex v: vertices) {
            if (getDegree(v) % 2 != 0) {
                return false;
            }
        }
        return true;
    }

    public Edge[] edgesOf(Vertex v) {
        LinkedList<Edge> edgeList = adjList.get(v);
        return (Edge[])(edgeList.toArray());
    }

    public Edge edgeBetween(Vertex v1, Vertex v2) {
        LinkedList<Edge> edgeList = adjList.get(v1);
        return edgeList.get(edgeList.indexOf(v2));
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
        return adjList.get(v);
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
        Edge minEdge = new Edge(new Vertex(0), new Vertex(1), Double.MAX_VALUE);
        LinkedList<Edge> edgeList = adjList.get(v);
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
        for (Edge e : edgeList) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            adjList.get(v1).remove(e);
            LinkedList<Edge> edges = adjList.get(v2);
            for (Edge e2 : edges) {
                if (e2.getSecondVertex() == v1)
                    edges.remove(e2);
            }
        }
    }

    public Vertex getRandomVertex() {
        int randomNum = rand.nextInt(numVertices);
        return new Vertex(randomNum);
    }

    public int numVertices() {
        return numVertices;
    }
}
