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

    private final double mutationRate = 0.04;
    private final double mutationSwapProbability = 0.90;
    private final int tournamentSize = 7;

    private int popSize;
    private int generations;

    private double startTime;
    private double endTime;

    private Random rand;

    // new Genetic (population size, number of generations to go through)
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

            // Use to keep track of where the algorithm is
            // System.out.println("Beginning generation " + (j+1) + "...");

            /* approximates how long the algorithm will take based on first 10 generations
               used for alogorithms running large graphs/huge numbers of generations
            if (j==0) {
                startTime = System.nanoTime();
            }
            else if (j==9) {
                endTime = System.nanoTime();
                double duration = (endTime-startTime)*generations/(10*1000000000.0);
                System.out.println("Expected time to complete: " + ((int) (duration/60)) + " minutes " + (duration%60) + " seconds");
            }
            */

            // elitism: always keep the best tour in the population after every generation
            Tour elite = getFittest(population);
            int elitePlace = indexFittest(population);
            Tour temp = population[0];
            population[0] = elite;
            population[elitePlace] = temp;
            newPopulation[0] = population[0];

            // next child in the population will be formed from two parent tours crossed over with a chance of mutation
            for (int i=1;i<population.length;i++) {
                // create two parents
                Tour[] parents;

                //System.out.println("Obtaining first parent in " +(i+1)+ "th place of the " + (j+1) + "th generation...");

                /***** TRY ME *****/
                // two different implementation of selecting two parents from a population to mate, roulette is
                // generally better

                //parents = tournamentSelection(population);
                parents = rouletteSelection(population);

                //System.out.println("Beginning crossover of parents with tours of vertex length "
                //   +a.verticesSoFar().length+ " and " + b.verticesSoFar().length);

                // crossover parents to make child
                newPopulation[i] = greedyCrossover(parents[rand.nextInt(2)],parents[rand.nextInt(2)],g);
                //System.out.println("Successful crossover. Created child of vertex length " +
                 //   newPopulation[i].verticesSoFar().length);

                // mutate child tour
                newPopulation[i] = mutate(newPopulation[i],g);
                //System.out.println("Successful child created.");
            }
            population = newPopulation;
            //System.out.println("New population created. " + (j+1)+ " generations have passed.");
        }
        //System.out.println("Finished genetic algorithm!");

        // done!
        return getFittest(population);

    }

    // generates a random population of tours from the graph
    private Tour[] generatePopulation (Graph g) {
        Tour[] population = new Tour[popSize];
        for (int i=0;i<popSize;i++) {
            population[i] = g.getRandomTour();
        }
        return population;
    }

    /* returns two parent tours selected by roulette selection, where each tour gets a proportionate probability
       of being selected according to its fitness */
    private Tour[] rouletteSelection(Tour[] population) {
        // two parents to be returned
        Tour[] parents = new Tour[2];
        int length = population.length;
        // sort the population best to worst
        Tour[] sorted = mergeSort(population);
        // sum of all the fitnesses of the sorted population
        double totalFitness = 0;
        for(int i=0;i<length;i++) {
            totalFitness += sorted[length-1].getLength()/sorted[i].getLength();
        }
        // accumulatedFitness is each tour's accumulatedfitness value, adding up all the fitnesses preceding and itself
        double accumulatedFitness[] = new double[length];
        double accumulatingSum = 0;
        for(int i=0;i<length;i++) {
            double fitness = sorted[length-1].getLength()/sorted[i].getLength();
            accumulatedFitness[i] = accumulatingSum + fitness/totalFitness;
            accumulatingSum += fitness/totalFitness;
        }

        double place;
        for(int j=0;j<2;j++) {
            place = rand.nextDouble();
            int i=0;
            while (place>=accumulatedFitness[i] && i<length) {
                i++;
            }
            parents[j] = sorted[i];
        }
        return parents;
    }

    // pick two parents using tournament selection
    private Tour[] tournamentSelection(Tour[] population) {
        Tour[] a = new Tour[2];
        a[0] = tournament(population);
        a[1] = tournament(population);
        while (a[1] == a[0]) {
            a[1] = tournament(population);
        }
        return a;
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

            // 1. if both next vertices are already in the child tour, then select a random vertex as next vertex
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
            // 2. if the child tour contains the next vertex of the second tour and NOT the vertex of first tour
            else if (child.containsVertex(vertices2[nextPos2])) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices1[nextPos1]));
                //System.out.println("Added edge from first tour because second tour's next city was already visited.");
            }
            // 3. if the child tour contains the next vertex of the first tour and NOT of the second tour
            else if (child.containsVertex(vertices1[nextPos1])) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(),vertices2[nextPos2]));
                //System.out.println("Added edge from second tour because first tour's next city was already visited.");
            }
            // 4. neither next vertex is in the child tour, so take the closer one
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

            /* System.out.println("The num of vertices of the tours passed in were: Tour a = " + vertices1.length +
            //        " Tour b = " + vertices2.length
            //        + " The num of vertices of the child tour is " + child.verticesSoFar().length);

            //System.out.println("Added one edge to child. Child tour now contains " + child.verticesSoFar().length +
            " vertices");  */

            if (count == length-1) {
                child.addEdge(g.edgeBetween(child.getCurrentVertex(), child.verticesSoFar()[0]));
                count++;
            }
        }
        return child;
    }

    /* mutation mutates tour with some probability mutationRate, and swaps two cities with probability mutationSwapProbability
       if it results in a shorter tour  */
    private Tour mutate (Tour a, Graph g) {
        Tour mutated;
        Vertex[] v = a.verticesSoFar();
        double chance;

        // mutate each city in tour with some probability
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

        // determine whether to return the shorter or longer tour of the two
        mutated = new Tour(v,g);
        double mutatedLength = mutated.getLength();
        double originalLength = a.getLength();
        if (rand.nextDouble() < mutationSwapProbability) {
            if (mutatedLength < originalLength) {
                return mutated;
            }
            else return a;
        }
        else {
            if (mutatedLength < originalLength) {
                return a;
            }
            else return mutated;
        }
    }

    /******* Helper methods ********/

    // performs deterministic tournament selection from a population and returns a tour: used in tournamentSelection
    private Tour tournament(Tour[] population) {
        Tour[] a = new Tour[tournamentSize];
        for(int i=0;i<tournamentSize;i++) {
            a[i] = population[rand.nextInt(population.length)];
        }
        return getFittest(a);
    }
    // helper method returns the index that a particular vertex is at: used in crossover
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

    // returns shortest tour in a tour array: used everywhere
    private Tour getFittest(Tour[] population) {
        Tour a = population[0];
        for (int i=1;i<population.length;i++) {
            if (population[i].getLength() < a.getLength()) {
                a = population[i];
            }
        }
        return a;
    }

    // index of shortest tour in population: used for elitism
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

    // mergeSort has been tested in a separate file with integer arrays: used for rouletteSelection
    private Tour[] mergeSort(Tour[] a) {
        if (a.length == 1) {
            return a;
        }
        Tour[] left = new Tour[a.length/2];
        Tour[] right = new Tour[a.length-left.length];
        for(int i=0;i<left.length;i++) {
            left[i] = a[i];
        }
        for(int i=0;i<right.length;i++) {
            right[i] = a[i+a.length/2];
        }
        return merge(mergeSort(left), mergeSort(right));

    }

    // helper to mergeSort
    private Tour[] merge(Tour[] a, Tour[] b) {
        Tour[] c = new Tour[a.length+b.length];
        int l=0,r=0;
        for(int i=0;i<c.length;i++) {
            if (l >=a.length) {
                c[i] = b[r];
                r++;
            }
            else if (r >= b.length) {
                c[i] = a[l];
                l++;
            }
            else if (a[l].getLength() <= b[r].getLength()) {
                c[i] = a[l];
                l++;
            }
            else {
                c[i] = b[r];
                r++;
            }
        }
        return c;
    }





}
