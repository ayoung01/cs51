
public class EuclideanVertex extends Vertex {

    private double[] coords;
    private int dim;

    //initializes a EuclideanVertex with a unique id and a double array of coordinates
    public EuclideanVertex (int i, double[] coordinates) {
        coords = coordinates;
        dim = coords.length;
        this.setId(i);
    }

    //returns the ith coordinate of the vertex
    public double getCoord(int i) {
            return coords[i];
    }

    //returns the number of coordinates of the vertex
    public int getDim()
    {
        return dim;
    }

    // returns the distance between this vertex and vertex v
    public double dist(EuclideanVertex v) {
        double total = 0;
        for(int i = 0; i < dim; i++)
        {
            total+=Math.pow(coords[i] - v.getCoord(i),2);
        }
        return Math.sqrt(total);
    }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(getId() + " ");
            for(int i = 0; i < dim; i++)
            {
                builder.append(coords[i] + " ");
            }
            return builder.toString();
        }

    }

