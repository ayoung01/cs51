/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/28/13
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;


@SuppressWarnings("serial")

public class MainWindow extends JPanel
{
    Graph graph;
    Tour besttour;


    public MainWindow(Graph g, Tour t, int height, int width)                       // set up graphics window
    {
        super();
        this.graph = g;
        this.besttour = t;
        setBackground(Color.WHITE);
        Dimension d1 = new Dimension(height,width);
        this.setMaximumSize(d1);
        this.setPreferredSize(d1);

    }


    public void paintComponent(Graphics g)  // draw graphics in the panel
    {
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels
        //System.out.printf("Width: %d, Height: %d", width, height);
        int radius = 2;

        EuclideanVertex[] vertices = graph.getVertices();
        int length = vertices.length;
        double[] mins = graph.getMins();
        double[] maxes = graph.getMaxes();


        super.paintComponent(g);            // call superclass to make panel display correctly

        Graphics2D g2d = (Graphics2D) g;
        //g.drawString("Hello", 475 , 375 );
        g2d.setColor(Color.blue);

        Point[] points = new Point[length];

        for(int i=0;i<vertices.length;i++) {
            int currentVertexId = besttour.verticesSoFar()[i].getId();
            int pos1 = (int) (((vertices[currentVertexId].getCoord(0) - mins[0])/(maxes[0]-mins[0]))*(4*width/5) + (width/20));
            int pos2 = (int) (((vertices[currentVertexId].getCoord(1) - mins[1])/(maxes[1]-mins[1]))*(4*height/5) + (height/20));
            points[i] = new Point(pos1,pos2);
        }

        for(int i=0;i<length;i++) {
            //System.out.println("x1 = " + points[i].getX());
            //System.out.println("x2 = " + points[i].getY());
            //System.out.printf("Mins: %.2f,%.2f Maxes: %.2f, %.2f\n", mins[0],mins[1],maxes[0],maxes[1]);

            Ellipse2D.Double circle = new Ellipse2D.Double(points[i].getX(), points[i].getY(), 2*radius, 2*radius);
            g2d.fill(circle);
        }

        for(int i=0;i<length;i++) {
            g2d.drawLine(points[i].getX()+radius,points[i].getY()+radius,points[(i+1)%length].getX()+radius,points[(i+1)%length].getY()+radius);
        }

        g2d.setColor(Color.red);
        String mystring = "Tour length: " + besttour.getLength();
        g2d.drawString(mystring, 12,12);// Drawing code goes here
    }

}

