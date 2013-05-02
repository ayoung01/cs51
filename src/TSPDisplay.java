import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: mgentili
 * Date: 5/1/13
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TSPDisplay extends JFrame {
    private JButton greedyb, simb, geneticb, twooptb;
    private JPanel panel2;
    private MainWindow mwin;
    private Tour[] tours;
    private Graph gr;
    private int x,y;
    public static void main(String args[])
    {
        Tour[] results = new Tour[5];

        TwoDimParser hello = new TwoDimParser("test.txt", 29);

        Graph g = new Graph(hello.allVertices());

        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        TwoOpt twoopt = new TwoOpt();
        Greedy greed = new Greedy();
        Genetic genes = new Genetic(100,3000);

        results[0] = greed.findShortestPath(g);
        results[1] = twoopt.findShortestPath(g);
        results[2] = sim.findShortestPath(g);
        results[3] = genes.findShortestPath(g);

        TSPDisplay frame = new TSPDisplay(g, results);
        frame.setTitle("TSP Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        //frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }

    public TSPDisplay(Graph g, Tour[] results)
    {
        getContentPane().setLayout(new BorderLayout());

        tours = results;
        gr = g;
        x = 400;
        y = 400;

        mwin = new MainWindow(gr, tours[0],x,y);
        panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        simb = new JButton("Simulated Annealing");
        greedyb = new JButton("Greedy");
        geneticb = new JButton("Genetic");
        twooptb = new JButton("Two Opt");

        greedyb.addActionListener(new TourListener(tours[0]));
        twooptb.addActionListener(new TourListener(tours[1]));
        simb.addActionListener(new TourListener(tours[2]));
        geneticb.addActionListener(new TourListener(tours[3]));

        panel2.add(greedyb);
        panel2.add(twooptb);
        panel2.add(simb);
        panel2.add(geneticb);

        getContentPane().add(mwin, BorderLayout.WEST);
        getContentPane().add(panel2, BorderLayout.EAST);
    }


    public class TourListener implements ActionListener {

        private Tour here;
        public TourListener(Tour t)
        {
            here = t;
        }

        public void update(Tour t)
        {
            here = t;
        }

        public void actionPerformed(ActionEvent e) {
            remove(mwin);
            mwin = new MainWindow(gr, here,x,y);
            getContentPane().add(mwin,BorderLayout.WEST);
            revalidate();

        }

    }
}
