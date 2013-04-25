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
        Greedy gree = new Greedy();
        Tour start = gree.findShortestPath(g);
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
        Vertex[] vertices = t.verticesSoFar();
        return new Tour(neighborVertexSet(vertices), g);
    }

    Vertex[] neighborVertexSet(Vertex[] vertices)
    {
        int len = vertices.length;
        Vertex[] newv = new Vertex[len];
        int random1 = rand.nextInt(len);
        int random2 = rand.nextInt(len);
        while(random1 == random2)
            random2 = rand.nextInt(len);

        for(int i = 0; i < len; i++)
        {
            if(i == random1)
            {
                newv[i] = vertices[random2];
            }
            else if(i == random2)
            {
                newv[i] = vertices[random1];
            }
            else
            {
                newv[i] = vertices[i];
            }
        }

        return newv;
    }

    double anneal(int iter, double a, double b)
    {
        double t = Math.pow(10,10)*Math.pow(0.8,(iter/300));
        return Math.exp(-(a-b)/t);
    }


}
