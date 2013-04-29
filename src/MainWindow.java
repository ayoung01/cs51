/**
 * Created with IntelliJ IDEA.
 * User: Brian
 * Date: 4/28/13
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;

public class MainWindow extends JPanel
{
    /*
    public MainWindow()                       // set up graphics window
    {
        super();
        setBackground(Color.WHITE);
    }
    */

    public void paintComponent(Graphics g)  // draw graphics in the panel
    {
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels

        super.paintComponent(g);            // call superclass to make panel display correctly


        // Drawing code goes here
    }

    public static void main(String[] args)
    {
        MainWindow panel = new MainWindow();                            // window for drawing
        JFrame application = new JFrame();                            // the program itself

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // set frame to exit
        // when it is closed
        application.add(panel);


        application.setSize(500, 400);         // window is 500 pixels wide, 400 high
        application.setVisible(true);
    }

}
