import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ayoung
 * Date: 4/11/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */

public interface Graph_I {

//    Edge[] edgesOf(Vertex v);

    Edge edgeBetween(Vertex v1, Vertex v2);

    Edge getRandomNeighborEdge(Vertex v);

    Edge getShortestNeighborEdge (Vertex v,Vertex[] already);

    Vertex getRandomVertex();

    //Tour getRandomTour();

    int numVertices();

    //double[] getMins();

    //double[] getMaxes();

    //void printGraphToFile(String file);

    //String display();

}
