/**
 * Created with IntelliJ IDEA.
 * User: ayoung
 * Date: 4/14/13
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class EuclideanVertex_2D extends Vertex {
    private double x1;
    private double x2;

    public double getX1() {
        return x1;
    }
    public double getX2() {
        return x2;
    }

    // returns the distance between this vertex and vertex v
    public double dist(EuclideanVertex_2D v) {
        return Math.sqrt((Math.pow(v.getX1()-x1, 2) + Math.pow(v.getX2()-x2, 2)));
    }


    public EuclideanVertex_2D (int i, double a, double b) {
        x1 = a;
        x2 = b;
        this.setId(i);
    }

    public String toString() {
        return Integer.toString(getId()) + " " + Double.toString(x1) + " " + Double.toString(x2);
    }

}
