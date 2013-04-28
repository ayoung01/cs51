import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/14/13
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */

// resources: http://www.codeproject.com/Articles/1403/Genetic-Algorithms-and-the-Traveling-Salesman-Prob
//  http://www.ai-junkie.com/ga/intro/gat2.html
//  http://jgap.sourceforge.net/javadoc/3.01/org/jgap/impl/GreedyCrossover.html
// http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3

public class Genetic implements TSP_I{

    private Random rand;

    private final double mutationRate = 0.03;
    private int popSize;

    public Genetic (int popSize) {
        popSize = popSize;
    }

    public Tour findShortestPath(Graph g) {
        Tour[] population = generatePopulation(g);
        // select higher fitness tours from population, crossbreed them with higher fitness ones being more likely
        // to be crossbred, with some chance of mutation, and generate a next generation population.
        // continue for a specified number of times, then return the current most fit tour in the population, which
        // will hopefully be a very close approximation to the real solution.

        // at each generation, pick two parents and create two new child tours (perform two crossovers)
        // and replace the two parents in the population





        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public double timeAlgorithm(Graph g) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Tour[] generatePopulation (Graph g) {
        int initialPopulationSize = popSize;
        Tour[] population = new Tour[initialPopulationSize];
        for (int i=0;i<initialPopulationSize;i++) {
            population[i] = g.getRandomTour();
        }
        return population;
    }

    private Tour[] rankFitness(Tour[] population) {

        return null;
    }

    // http://jgap.sourceforge.net/javadoc/3.01/org/jgap/impl/GreedyCrossover.html
    // select randomly a starting city from one parent. Then compare
    public Tour greedyCrossover (Tour a, Tour b, Graph g) {
        Tour child;
        Vertex[] vertices1,vertices2;
        Edge[] edges1, edges2;
        int length;

        if (rand.nextInt(2) == 0) {
            vertices1 = a.verticesSoFar();
            edges1 = a.allEdges();
            vertices2 = b.verticesSoFar();
            edges2 = b.allEdges();
        }
        else {
            vertices1 = b.verticesSoFar();
            edges1 = b.allEdges();
            vertices2 = a.verticesSoFar();
            edges2 = b.allEdges();
        }

        // vertices1 and vertices2 should always be the same length - invariant of population of tours
        length = vertices1.length;
        child = new Tour(vertices1[0], length);

        int pos1, pos2;
        int nextPos1, nextPos2;

        // while the child tour is not complete
        while (child.verticesSoFar().length < length) {
            // position of the vertex at each respective vertex array
            pos1 = indexOfVertex(vertices1, child.getCurrentVertex());
            pos2 = indexOfVertex(vertices2, child.getCurrentVertex());

            // if both are valid vertices, look at respective next vertices and compare
            if (pos1 == length-1) {
                nextPos1 = 0;
            }
            else {
                nextPos1 = pos1+1;
            }
            if (pos2 == length-1) {
                nextPos2 = 0;
            }
            else {
                nextPos2= pos2+1;
            }

            // Four cases: both next vertices are already in child, one of next vertices is in child, neither next
            // vertices are in child

            // if both next vertices are already in the child tour, then select a random vertex as next vertex
            if (child.containsVertex(vertices1[nextPos1]) && child.containsVertex(vertices2[nextPos2])) {
                // pick a random vertex array to pull vertex from
                int pickOne = rand.nextInt(2);
                Vertex[] randTour;
                if (pickOne == 0) {
                    randTour = vertices1;
                }
                else randTour = vertices2;

                // pick random vertex to connect to
                int next = rand.nextInt(length);
                while (child.containsVertex(randTour[next])) {
                    next = rand.nextInt(length);
                }
                // add this random vertex to the tour
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),randTour[next]));
            }
            // if the child tour contains the next vertex of the first tour and NOT the vertex of second tour
            else if (child.containsVertex(vertices1[nextPos1])) {
                child.addEdge(edges2[pos2]);
            }
            // if the child tour contains the next vertex of the second tour and NOT of the first tour
            else if (child.containsVertex(vertices2[nextPos2])) {
                child.addEdge(edges1[pos1]);
            }
            // neither next vertex is in the child tour, so take the closer one
            else {
                if (edges1[pos1].getWeight() <= edges2[pos2].getWeight()) {
                    child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices1[nextPos1]));
                }
                else child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices2[nextPos2]));
            }
            System.out.println("The num of vertices of the tours passed in were: Tour a = " + vertices1.length +
                    " Tour b = " + vertices2.length
                    + " The num of vertices of the child tour is " + child.verticesSoFar().length);

        }
        return child;
    }

    public Tour mutate (Tour a, Graph g) {
        Tour mutated;
        int index1 = rand.nextInt(a.verticesSoFar().length);
        int index2 = rand.nextInt(a.verticesSoFar().length);
        Vertex[] swappedVertices = a.verticesSoFar()
        Vertex temp = swappedVertices[index1];
        swappedVertices[index1] = a.verticesSoFar()[index2];
        swappedVertices[index2] = temp;
        return new Tour(swappedVertices,g);
    }

    private int indexOfVertex(Vertex[] vertices, Vertex a) {
        int i = 0;
        while (vertices[i].getId() != a.getId() && i<vertices.length) {
            i++;
        }
        if (i == vertices.length) {
            return -1;
        }
        else return i;
    }
}
