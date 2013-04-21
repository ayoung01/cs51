/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 4/14/13
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.Random;
public class SimulatedAnnealing implements TSP_I {

    private int maxIter;
    private Random rand;
    public SimulatedAnnealing(int numTimes)
    {
       maxIter = numTimes;
       rand = new Random();
    }

    public Tour findShortestPath(Graph g)
    {
      Tour start = g.getRandomTour();
      Tour best = start;
      for(int i = 0; i < maxIter; i++)
      {
        Tour comparison = getNeighborTour(start, g);
        double a = comparison.getLength();
        double b = start.getLength();
        if(a < b)
        {
            start = comparison;
        }
        else if (rand.nextDouble() < anneal(i,a,b))
        {
            start = comparison;
        }
        if(start.getLength() < best.getLength())
        {
            best = start;
        }
      }

        return best;
    }


    Tour getNeighborTour(Tour t, Graph g)
    {
        Random rand = new Random();
        Vertex[] vertices = t.verticesSoFar();
        int len = g.numVertices();
        Vertex[] newv = new Vertex[len];

        int random1 = rand.nextInt(len);
        int random2 = rand.nextInt(len);
        while(random1 == random2)
            random2 = rand.nextInt(len);
        if(random2 < random1)
        {
            int temp = random1;
            random1 = random2;
            random2 = temp;
        }

        for(int i = 0; i < len; i++)
        {
          if(random1 == i)
        }




        return t;
    }

    double anneal(int iter, double a, double b)
    {
        double t = Math.pow(10,10)*Math.pow(0.8,(iter/300));
        return Math.exp(-(a-b)/t);
    }


}
