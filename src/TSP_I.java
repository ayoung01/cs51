/**
 * Created with IntelliJ IDEA.
 * User: ayoung
 * Date: 4/11/13
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */

/*
Takes in a graph as input
Methods:
FindShortestPath: effectively implements the algorithm
Timing
Variables:
Optimal Tour (list of edges in best approximation done by algorithm)
Length of optimal tour

 */
public interface TSP_I {

    public Tour findShortestPath(Graph g);

    public double timeAlgorithm(Graph g);

}
