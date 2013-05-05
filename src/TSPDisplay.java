import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//contains the main method to create a GUI for our algorithms
public class TSPDisplay extends JFrame {
    private JButton greedyb, simb, geneticb, twooptb, christofidesb;
    private JPanel panel2;
    private MainWindow mwin;
    private Tour[] tours;
    private Graph gr;
    private int x,y;

    /*
        Usage: java TSPDisplay input_file.txt
     */
    public static void main(String args[])
    {
        String input_file = args[0];
        Tour[] results = new Tour[5];

        TwoDimParser hello = new TwoDimParser(input_file);
        //TwoDimParser hello = new TwoDimParser(args[1],Integer.parseInt(args[2]));

        Graph g = new Graph(hello.allVertices());

        SimulatedAnnealing sim = new SimulatedAnnealing(2000000, 1, 0.995);
        TwoOpt twoopt = new TwoOpt();
        Greedy greed = new Greedy();
        Genetic genes = new Genetic(100,10000);
        Christofides christofides = new Christofides();

        results[0] = greed.findShortestPath(g);
        results[1] = twoopt.findShortestPath(g);
        results[2] = sim.findShortestPath(g);
        results[3] = genes.findShortestPath(g);
        results[4] = christofides.findShortestPath(g);
        System.out.printf("%s", results[4].toString());
        TSPDisplay frame = new TSPDisplay(g, results);
        frame.setTitle("TSP Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        //frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }

    //takes in a graph and an array of tours resulting from the different algorithms
    //and displays them graphically
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
        christofidesb = new JButton("Christofides");

        greedyb.addActionListener(new TourListener(tours[0]));
        twooptb.addActionListener(new TourListener(tours[1]));
        simb.addActionListener(new TourListener(tours[2]));
        geneticb.addActionListener(new TourListener(tours[3]));
        christofidesb.addActionListener(new TourListener(tours[4]));

        panel2.add(greedyb);
        panel2.add(twooptb);
        panel2.add(simb);
        panel2.add(geneticb);
        panel2.add(christofidesb);

        getContentPane().add(mwin, BorderLayout.WEST);
        getContentPane().add(panel2, BorderLayout.EAST);
    }

    //creates a listener that updates which graph is displayed when
    //the corresponding button is clicked
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
