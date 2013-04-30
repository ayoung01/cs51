/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/28/13
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.geom.Ellipse2D;
import java.util.Random;


@SuppressWarnings("serial")

public class MainWindow extends JPanel
{
    Graph graph;
    Tour besttour;

    public static void main(String[] args) {

        long startTime;
        long endTime;
        double duration;
        TwoDimParser hello = new TwoDimParser("test.txt", 29);
        hello.printEverything();
        Graph g = new Graph(hello.allVertices());

        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        startTime = System.nanoTime();
        Tour bestsim = sim.findShortestPath(g);
        endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000000.0;
        System.out.printf("Simulated Annealing: %.2f in %.5f s\n ", bestsim.getLength(), duration);
        bestsim.printGraphToFile("simbest.txt");

        MainWindow panel = new MainWindow(g, bestsim);                            // window for drawing
        JFrame application = new JFrame("Graph");                            // the program itself

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // set frame to exit
        // when it is closed
        application.add(panel);
        application.setSize(500, 400);         // window is 500 pixels wide, 400 high
        application.setVisible(true);
    }


    public MainWindow(Graph g, Tour t)                       // set up graphics window
    {
        super();
        this.graph = g;
        this.besttour = t;
        setBackground(Color.WHITE);

    }


    public void paintComponent(Graphics g)  // draw graphics in the panel
    {
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels

        EuclideanVertex[] vertices = graph.getVertices();
        double[] mins = graph.getMins();
        double[] maxes = graph.getMaxes();


        super.paintComponent(g);            // call superclass to make panel display correctly

        Graphics2D g2d = (Graphics2D) g;
        g.drawString("Hello", 475 , 375 );
        g2d.setColor(Color.blue);

        drawGraph(graph);
        drawTour();

        for(int i=0;i<vertices.length;i++) {
            System.out.println("x1 = " + vertices[i].getCoord(0));
            System.out.println("x2 = " + vertices[i].getCoord(1));
            int pos1 = (int) (Math.abs((vertices[i].getCoord(0) - mins[0])/(maxes[0]-mins[0]))*(4*width/5));
            int pos2 = (int) (Math.abs((vertices[i].getCoord(1) - mins[1])/(maxes[1]-mins[1]))*(4*height/5));
            System.out.printf("Mins: %.2f,%.2f Maxes: %.2f, %.2f\n", mins[0],mins[1],maxes[0],maxes[1]);

            Ellipse2D.Double circle = new Ellipse2D.Double(pos1+(width/20), pos2+(height/20), 5, 5);
            g2d.fill(circle);
        }



        // Drawing code goes here
    }


}
