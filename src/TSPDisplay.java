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
    private JLabel nameOfTour;
    private Tour[] tours;
    private Graph gr;
    private int x,y;
    public static void main(String args[])
    {
        Tour[] results = new Tour[5];

        TwoDimParser hello = new TwoDimParser("test38.txt", 38);

        Graph g = new Graph(hello.allVertices());

        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        TwoOpt twoopt = new TwoOpt();
        Greedy greed = new Greedy();
        Genetic genes = new Genetic(100,10000);

        results[0] = greed.findShortestPath(g);
        results[1] = twoopt.findShortestPath(g);
        results[2] = sim.findShortestPath(g);
        results[3] = genes.findShortestPath(g);

        TSPDisplay frame = new TSPDisplay(g, results);
        frame.setTitle("TSP Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setMinimumSize(new Dimension(400,400));
        //frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }

    public TSPDisplay(Graph g, Tour[] results)
    {
        getContentPane().setLayout(new BorderLayout());

        tours = results;
        gr = g;
        x = 500;
        y = 500;

        mwin = new MainWindow(gr, tours[0],x,y);
        panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
        simb = new JButton("Simulated Annealing");
        greedyb = new JButton("Greedy");
        geneticb = new JButton("Genetic");
        twooptb = new JButton("Two Opt");

        greedyb.addActionListener(new TourListener(tours[0], "Greedy"));
        twooptb.addActionListener(new TourListener(tours[1], "Two Opt"));
        simb.addActionListener(new TourListener(tours[2], "Simulated Annealing"));
        geneticb.addActionListener(new TourListener(tours[3], "Genetic"));

        nameOfTour = new JLabel("Tour: ");

        panel2.add(greedyb);
        panel2.add(twooptb);
        panel2.add(simb);
        panel2.add(geneticb);

        getContentPane().add(mwin, BorderLayout.WEST);
        getContentPane().add(panel2, BorderLayout.EAST);
    }


    public class TourListener implements ActionListener {

        private Tour here;
        private String name;
        public TourListener(Tour t, String name)
        {
            here = t;
            this.name = name;
        }

        public void update(Tour t)
        {
            here = t;
        }



        public void actionPerformed(ActionEvent e) {
            remove(mwin);
            mwin = new MainWindow(gr, here,x,y);
            getContentPane().add(mwin,BorderLayout.WEST);

            panel2.remove(nameOfTour);
            nameOfTour = new JLabel(name);
            panel2.add(nameOfTour);
            revalidate();

        }

    }
}
