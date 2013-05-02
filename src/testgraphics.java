/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 5/2/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;

public class testgraphics {

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        long startTime;
        long endTime;
        double duration;
        TwoDimParser hello = new TwoDimParser("test.txt", 29);
        hello.printEverything();
        final Graph g = new Graph(hello.allVertices());

        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        startTime = System.nanoTime();
        Tour bestsim = sim.findShortestPath(g);
        endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000000.0;
        System.out.printf("Simulated Annealing: %.2f in %.5f s\n ", bestsim.getLength(), duration);
        bestsim.printGraphToFile("simbest.txt");


        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
