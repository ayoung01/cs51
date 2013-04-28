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

// 29 cities: mutation 0.05 tournamentSize 7 population 100 generations 30000

public class Genetic implements TSP_I{

    private final double mutationRate = 0.03;
    private final int tournamentSize = 7;


    private Random rand;
    private int popSize;
    private int generations;

    public Genetic (int popSize, int generations) {
        rand = new Random();
        this.popSize = popSize;
        this.generations = generations;
    }

    public Tour findShortestPath(Graph g) {
        //System.out.println("Beginning genetic algorithm...");
        //System.out.println("Graph contains " + g.numVertices()+ " cities.");
        Tour[] population = generatePopulation(g);
        Tour[] newPopulation = population;


        // tournament selection: select 7 random tours, pick the best of 7 random tours
        for (int j=0;j<generations;j++) {
            //System.out.println("Beginning generation " + (j+1) + "...");
            Tour elite = fittest(population);
            int elitePlace = indexFittest(population);
            Tour temp = population[0];
            population[0] = elite;
            population[elitePlace] = temp;

            for (int i=1;i<population.length;i++) {
                //System.out.println("Obtaining first parent in " +(i+1)+ "th place of the " + (j+1) + "th generation...");
                Tour a = tournament(population);
                //System.out.println("Obtaining second parent in " +(i+1)+ "th place of the "+ (j+1) + "th generation...");
                Tour b = tournament(population);
                while (b == a) {
                    b = tournament(population);
                }
                //System.out.println("Beginning crossover of parents with tours of vertex length "
                 //   +a.verticesSoFar().length+ " and " + b.verticesSoFar().length);
                newPopulation[i] = greedyCrossover(a,b,g);
                //System.out.println("Successful crossover. Created child of vertex length " +
                 //   newPopulation[i].verticesSoFar().length);
                newPopulation[i] = mutate(newPopulation[i],g);
                //System.out.println("Successful child created.");
            }
            population = newPopulation;
            //System.out.println("New population created. " + (j+1)+ " generations have passed.");
        }
        //System.out.println("Finished genetic algorithm!");
        return getFittest(population);

        // select higher fitness tours from population, crossbreed them with higher fitness ones being more likely
        // to be crossbred, with some chance of mutation, and generate a next generation population.
        // continue for a specified number of times, then return the current most fit tour in the population, which
        // will hopefully be a very close approximation to the real solution.

        // at each generation, pick two parents and create two new child tours (perform two crossovers)
        // and replace the two parents in the population

    }


    public double timeAlgorithm(Graph g) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Tour[] generatePopulation (Graph g) {
        int initialPopulationSize = popSize;
        Tour[] population = new Tour[initialPopulationSize];
        for (int i=0;i<initialPopulationSize;i++) {
            population[i] = g.getRandomTour();
            //System.out.print(i+ " ");
        }
        //System.out.println();
        return population;
    }

    private Tour[] rankFitness(Tour[] population) {

        return null;
    }
    private Tour fittest(Tour[] population) {
        Tour a = population[0];
        for(int i=1;i<population.length;i++) {
            if (a.getLength() > population[i].getLength()) {
                a = population[i];
            }
        }
        return a;
    }
    private int indexFittest(Tour[] population) {
        Tour a = population[0];
        int index = 0;
        for(int i=1;i<population.length;i++) {
            if (a.getLength() > population[i].getLength()) {
                a = population[i];
                index = i;
            }
        }
        return index;
    }

    // http://jgap.sourceforge.net/javadoc/3.01/org/jgap/impl/GreedyCrossover.html
    // select randomly a starting city from one parent. Then compare
    private Tour greedyCrossover (Tour a, Tour b, Graph g) {
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
        //System.out.println("Tour A contains " + vertices1.length + " vertices and Tour B contains " + vertices2.length
        //+ " vertices.");

        // vertices1 and vertices2 should always be the same length - invariant of population of tours

        // length = number of vertices in the tour, and consequently the number of edges that should be in the tour
        length = vertices1.length;

        child = new Tour(vertices1[0], length);


        int pos1, pos2;
        int nextPos1, nextPos2;

        // while the child tour is not complete

        // number of edges in child tour so far
        int count = 0;
        //System.out.println("Created new child. Contains "+ count + " edges.");
        while (count < length) {
            //System.out.println("Number of edges in child is " + count);

            // position of the vertex at each respective vertex array
            pos1 = indexOfVertex(vertices1, child.getCurrentVertex());
            //System.out.println("Position of child's current vertex in first tour is " + pos1);
            pos2 = indexOfVertex(vertices2, child.getCurrentVertex());
            //System.out.println("Position of child's current vertex in second tour is " + pos2);

            // if both are valid vertices, look at respective next vertices and compare
            if (pos1 == length-1) {
                nextPos1 = 0;
                //System.out.println("First tour's next vertex is at 0");
            }
            else {
                nextPos1 = pos1+1;
                //System.out.println("First tour's next vertex is at " + nextPos1);
            }
            if (pos2 == length-1) {
                nextPos2 = 0;
               // System.out.println("Second tour's next vertex is at 0");
            }
            else {
                nextPos2= pos2+1;
               // System.out.println("Second tour's next vertex is at " + nextPos2);
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
                   // System.out.println("randTour is now Tour 1");
                }
                else {
                    randTour = vertices2;
                    //System.out.println("randTour is now Tour 2");
                }

                // pick random vertex of the ones not connected to yet to connect to
                Vertex[] availableVertices = new Vertex[length-count-1];
                //System.out.println("Length of tour should be " + length + ". Length of list of available vertices is "
                        //+availableVertices.length);
                for(int i=0,j=0;i<availableVertices.length;j++) {
                    //System.out.print("j = " + j+" ");
                    if (!child.containsVertex(randTour[j])) {
                        availableVertices[i] = randTour[j];
                        //System.out.println("Added one to availableVertices at j = " + j);
                        i++;
                    }
                }
                int next = rand.nextInt(availableVertices.length);
                //System.out.println("Random index that we will choose is " + next);
                // add this random vertex to the tour
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),availableVertices[next]));
                //System.out.println("Added a random edge to child because both tours' next vertex had been used.");

            }
            // if the child tour contains the next vertex of the second tour and NOT the vertex of first tour
            else if (child.containsVertex(vertices2[nextPos2])) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices1[nextPos1]));
                //System.out.println("Added edge from first tour because second tour's next city was already visited.");
            }
            // if the child tour contains the next vertex of the first tour and NOT of the second tour
            else if (child.containsVertex(vertices1[nextPos1])) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices2[nextPos2]));
                //System.out.println("Added edge from second tour because first tour's next city was already visited.");
            }
            // neither next vertex is in the child tour, so take the closer one
            else {
                if (edges1[pos1].getWeight() <= edges2[pos2].getWeight()) {
                    child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices1[nextPos1]));
                    //System.out.println("Added edge from first tour because it's closer.");
                }
                else {
                    child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices2[nextPos2]));
                    //System.out.println("Added edge from second tour because it's closer.");
                }
            }
            count++;
            //System.out.println("The num of vertices of the tours passed in were: Tour a = " + vertices1.length +
            //        " Tour b = " + vertices2.length
            //        + " The num of vertices of the child tour is " + child.verticesSoFar().length);

            //System.out.println("Added one edge to child. Child tour now contains " + child.verticesSoFar().length +
            //" vertices");

            if (count == length-1) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(), child.verticesSoFar()[0]));
                count++;
            }
        }
        return child;
    }

    // greedy mutation returns a tour with two swapped cities if it is more fit than original tour
    private Tour mutate (Tour a, Graph g) {
        Tour mutated;
        Vertex[] v = a.verticesSoFar();
        double chance;
        for(int i=0;i<v.length;i++) {
            chance = rand.nextDouble();
            if (chance<mutationRate) {
                int indexOther = rand.nextInt(v.length);
                while (indexOther == i) {
                    indexOther = rand.nextInt(v.length);
                }
                Vertex temp = v[indexOther];
                v[indexOther] = v[i];
                v[i] = temp;
            }
        }
        mutated = new Tour(v,g);
        if (mutated.getLength()<a.getLength()) {
            return mutated;
        }
        else return a;
        /*
        double chance = rand.nextDouble();
        if (chance < mutationRate) {
            int index1 = rand.nextInt(a.verticesSoFar().length);
            int index2 = rand.nextInt(a.verticesSoFar().length);
            Vertex[] swappedVertices = a.verticesSoFar();
            Vertex temp = swappedVertices[index1];
            swappedVertices[index1] = a.verticesSoFar()[index2];
            swappedVertices[index2] = temp;
            mutated = new Tour(swappedVertices,g);
            if (mutated.getLength() < a.getLength()) {
                return mutated;
            }
        }
        */

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

    // performs tournament selection from a population and returns a tour
    private Tour tournament(Tour[] population) {
        Tour a = population[rand.nextInt(population.length)];
        //System.out.println("Obtained random tour from initial population");
        Tour next = population[rand.nextInt(population.length)];
        for (int i=0;i<tournamentSize;i++) {
            while (next==a) {
                next = population[rand.nextInt(population.length)];
            }
            if (next.getLength() < a.getLength()) {
                a = next;
            }
        }
        return a;

    }
    private Tour getFittest(Tour[] population) {
        Tour a = population[0];
        for (int i=1;i<population.length;i++) {
            if (population[i].getLength() < a.getLength()) {
                a = population[i];
            }
        }
        return a;
    }
}
