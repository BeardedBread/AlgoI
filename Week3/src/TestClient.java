import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class TestClient{
    public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    /*int n = 8;
    int[] x_all = new int[]{10000, 0, 3000, 7000, 20000, 3000, 14000, 6000};
    int[] y_all = new int[]{0, 10000, 7000, 3000, 21000, 4000, 15000, 7000};*/
    
    /*int n = 6;
    int[] x_all = new int[]{19000, 18000, 32000, 21000, 1234, 14000};
    int[] y_all = new int[]{10000, 10000, 10000, 10000, 5678, 10000};*/

    
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
        //points[i] = new Point(x_all[i], y_all[i]);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    //32768
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}
}