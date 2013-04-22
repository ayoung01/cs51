import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/14/13
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */


public class Genetic implements TSP_I{

    private Random rand;

    public Tour findShortestPath(Graph g) {
        Tour[] population = generatePopulation(g);
        // select higher fitness tours from population, crossbreed them with higher fitness ones being more likely
        // to be crossbred, with some chance of mutation, and generate a next generation population.
        // continue for a specified number of times, then return the current most fit tour in the population, which
        // will hopefully be a very close approximation to the real solution.




        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public double timeAlgorithm(Graph g) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Tour[] generatePopulation (Graph g) {
        int initialPopulationSize = g.numVertices();
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
    public Tour greedyCrossover (Tour a, Tour b) {
        Tour child;
        Vertex[] vertices1,vertices2;
        Edge[] edges1, edges2;
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
        Vertex start = vertices1[0];
        child = new Tour(start, vertices1.length);

        // while the child tour is not complete
        while (child.verticesSoFar().length < vertices1.length) {
            if (edges1[0].getWeight() <= edges2[indexOfVertex(vertices2,start)].getWeight()) {
                child.addEdge(edges1[0]);
            }
            else {
                child.addEdge(edges2[0]);
            }
        }
        return null;
    }

    public Tour mutate (Tour a) {
        return null;
    }

    private int indexOfVertex(Vertex[] vertices, Vertex a) {
        int i = 0;
        while (vertices[i].getId() != a.getId()) {
            i++;
        }
        return i;
    }
}
