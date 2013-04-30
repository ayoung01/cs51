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

    public static void main(String[] args) {
        MainWindow panel = new MainWindow(new Graph(new TwoDimParser("test.txt", 29).allVertices()));                            // window for drawing
        JFrame application = new JFrame("Graph");                            // the program itself

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // set frame to exit
        // when it is closed
        application.add(panel);
        application.setSize(500, 400);         // window is 500 pixels wide, 400 high
        application.setVisible(true);
    }


    public MainWindow(Graph g)                       // set up graphics window
    {
        super();
        this.graph = g;
        setBackground(Color.WHITE);

    }


    public void paintComponent(Graphics g)  // draw graphics in the panel
    {
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels

        EuclideanVertex_2D[] vertices = graph.getVertices();
        double graphX1Min = graph.getX1Min();
        double graphX1Max = graph.getX1Max();
        double graphX2Min = graph.getX2Min();
        double graphX2Max = graph.getX2Max();


        super.paintComponent(g);            // call superclass to make panel display correctly

        Graphics2D g2d = (Graphics2D) g;
        g.drawString("Hello", 475 , 375 );
        g2d.setColor(Color.blue);
        for(int i=0;i<vertices.length;i++) {
            System.out.println("x1 = " + vertices[i].getX1());
            System.out.println("x2 = " + vertices[i].getX2());
            int pos1 = (int) (Math.abs((vertices[i].getX1() - graphX1Min)/(graphX1Max-graphX1Min))*(4*width/5));
            int pos2 = (int) (Math.abs((vertices[i].getX2() - graphX2Min)/(graphX2Max-graphX2Min))*(4*height/5));
            System.out.println("Min = " + graphX1Min + " Max = " + graphX1Max + " pos1 = " + pos1 +" pos2 = " + pos2);
            System.out.println("Min = " + graphX2Min + " Max = " + graphX2Max + " pos1 = " + pos1 +" pos2 = " + pos2);

            Ellipse2D.Double circle = new Ellipse2D.Double(pos1+(width/20), pos2+(height/20), 5, 5);
            g2d.fill(circle);
        }


        // Drawing code goes here
    }


}
