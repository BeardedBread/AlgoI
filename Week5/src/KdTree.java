import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class KdTree {
    private final int VERTICAL = 0;
    private final int HORIZONTAL = 1;

    private class pointNode{
        public Point2D p;
        public pointNode left = null;
        public pointNode right = null;
        public int line_type;
    }

    private pointNode root;
    private int n_points;

    // construct an empty set of points
    public KdTree(){
        root = null;
        n_points = 0;
    }

    // is the set empty?                             
    public boolean isEmpty(){
        return (root == null);
    }  
    // number of points in the set                    
    public int size(){
        return n_points;
    }
    // add the point to the set (if it is not already in the set)                         
    public void insert(Point2D p){
        if (p == null){
            throw new IllegalArgumentException("Point is null.");
        }

        if (isEmpty()){
            root = new pointNode();
            root.p = p;
            root.line_type = VERTICAL;
            n_points++;
        }else{
            add(p, root);
        }
    }

    private void add(Point2D p, pointNode node){
        // Check if current node is p
        if (node.p.equals(p)){
            return;
        }

        if (node.line_type == VERTICAL){
            //compare x to decide traversal direction
            if (p.x() < node.p.x()){
                // check if the direction is null
                if (node.left == null){
                    // insert node if null
                    node.left = new pointNode();
                    node.left.p = p;
                    node.left.line_type = HORIZONTAL;
                    n_points++;
                }else{
                    // recurse add if not 
                    add(p, node.left);
                }
            }else{
                if (node.right == null){
                    node.right = new pointNode();
                    node.right.p = p;
                    node.right.line_type = HORIZONTAL;
                    n_points++;
                }else{
                    // recurse add if not 
                    add(p, node.right);
                }
            }
        }else{
            if (p.y() < node.p.y()){
                // check if the direction is null
                if (node.left == null){
                    // insert node if null
                    node.left = new pointNode();
                    node.left.p = p;
                    node.left.line_type = VERTICAL;
                    n_points++;
                }else{
                    // recurse add if not 
                    add(p, node.left);
                }
            }else{
                if (node.right == null){
                    node.right = new pointNode();
                    node.right.p = p;
                    node.right.line_type = VERTICAL;
                    n_points++;
                }else{
                    // recurse add if not 
                    add(p, node.right);
                }
            }
        }
    }

    // does the set contain point p?              
    public boolean contains(Point2D p){
        if (p == null){
            throw new IllegalArgumentException("Point is null.");
        }

        if (isEmpty()){
            return false;
        }else{
            return search(p, root);
        }
    }

    private boolean search(Point2D p, pointNode node){
        // Check if current node is p
        if (node.p.equals(p)){
            return true;
        }

        if (node.line_type == VERTICAL){
            //compare x to decide traversal direction
            if (p.x() < node.p.x()){
                if (node.left != null)
                    return search(p, node.left);
                return false;
            }else{
                if (node.right != null)
                    return search(p, node.right);
                return false;
            }
        }else{
            if (p.y() < node.p.y()){
                if (node.left != null)
                    return search(p, node.left);
                return false;
            }else{
                if (node.right != null)
                    return search(p, node.right);
                return false;
            }
        }
    }

    // draw all points to standard draw              
    public void draw(){
        if (!isEmpty()){
            inorder_draw(root, 0, 0, 1, 1);
        }
        //StdDraw.show();

    }

    private void inorder_draw(pointNode node, double xmin, double ymin, double xmax, double ymax){
        if (node.left != null){
            if(node.line_type == VERTICAL){
                inorder_draw(node.left, xmin, ymin, node.p.x(), ymax);
            }else{
                inorder_draw(node.left, xmin, ymin, xmax, node.p.y());
            }
        }

        //Insert draw routine
        StdDraw.setPenRadius(0.005);
        if(node.line_type == VERTICAL){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), ymin, node.p.x(), ymax);
        }else{
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, node.p.y(), xmax, node.p.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);   
        StdDraw.point(node.p.x(), node.p.y());

        if (node.right != null){
            if(node.line_type == VERTICAL){
                inorder_draw(node.right, node.p.x(), ymin, xmax, ymax);
            }else{
                inorder_draw(node.right, xmin, node.p.y(), xmax, ymax);
            }

        }

    }

    // all points that are inside the rectangle (or on the boundary)                       
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null){
            throw new IllegalArgumentException("Rect is null.");
        }
        
        if (!isEmpty()){
            Point2D[] p_array = new Point2D[n_points];
            int[] n = new int[1];
            n[0] = 0;
            range_search(root, rect, p_array, n, 0, 0, 1, 1);
    
            return new point_iterable(p_array, n[0]);

        }  
        return null;
        

    }

    private void range_search(pointNode node, RectHV rect, Point2D[] p_array, int[] n, double xmin, double ymin, double xmax, double ymax){
        // Check for point
        if (rect.contains(node.p)){
            p_array[n[0]++] = node.p;
        }
        if (node.left != null){
            // Check for rect intersection
            // Then traverse
            if(node.line_type == VERTICAL){
                if (rect.intersects(new RectHV(xmin, ymin, node.p.x(), ymax)))
                    range_search(node.left, rect, p_array, n, xmin, ymin, node.p.x(), ymax);
            }else{
                if (rect.intersects(new RectHV(xmin, ymin, xmax, node.p.y())))
                    range_search(node.left, rect, p_array, n, xmin, ymin, xmax, node.p.y());
            }
        }

        if (node.right != null){
            if(node.line_type == VERTICAL){
                if (rect.intersects(new RectHV(node.p.x(), ymin, xmax, ymax)))
                    range_search(node.right, rect, p_array, n, node.p.x(), ymin, xmax, ymax);
            }else{
                if (rect.intersects(new RectHV(xmin, node.p.y(), xmax, ymax)))
                    range_search(node.right, rect, p_array, n, xmin, node.p.y(), xmax, ymax);
            }

        }
            
    }

    // a nearest neighbor in the set to point p; null if the set is empty        
    public Point2D nearest(Point2D p){
        if (p == null){
            throw new IllegalArgumentException("Point is null.");
        }

        if (isEmpty()){
            return null;
        }


        Point2D[] nearest_p = new Point2D[1];
        nearest_p[0] = root.p;
        double[] shortest_dist = new double[1];
        shortest_dist[0] = p.distanceSquaredTo(root.p);

        //System.out.println(root.p.toString());

        // Update the nearest point so far if there is none or one is found
        
        double straight_dist = Math.pow(p.x() - root.p.x(), 2);
        boolean to_left = p.x() < root.p.x();

        //can probably generalise this
        if (to_left){
            if (root.left != null)
                nearest_neighbour(root.left , p, nearest_p, shortest_dist, 0, 0, root.p.x(), 1);

            if (root.right != null && straight_dist < shortest_dist[0]){
                // Need a separate check for other side
                // Get the two intersecting points which the short distance circle 
                // makes with the root vertical line
                double x_dist = Math.abs(root.p.x() - p.x());
                double y_dist = Math.sqrt(shortest_dist[0] - x_dist * x_dist);
                double y_min = Math.max(p.y() - y_dist, 0);
                double y_max = Math.min(p.y() + y_dist, 1);
                
                nearest_neighbour(root.right, p, nearest_p, shortest_dist, root.p.x(), 0, 1, 1);
            }
        }else{
            if (root.right != null)
                nearest_neighbour(root.right , p, nearest_p, shortest_dist, root.p.x(), 0, 1, 1);

            if (root.left != null && straight_dist < shortest_dist[0]){
                // Need a separate check for other side
                // Get the two intersecting points which the short distance circle 
                // makes with the root vertical line
                
                nearest_neighbour(root.left, p, nearest_p, shortest_dist, 0, 0, root.p.x(), 1);
            }

        }
        return nearest_p[0];
    }
    
    private void nearest_neighbour(pointNode node, Point2D p, Point2D[] nearest_p, double[] shortest_dist, double xmin, double ymin, double xmax, double ymax){
        //System.out.println(node.p.toString());
        
        // This is a check for the points opposite of the query point
        double dist = p.distanceSquaredTo(node.p);
        // Update the nearest point so far if there is none or one is found        
        if (dist < shortest_dist[0]){
            nearest_p[0] = node.p;
            shortest_dist[0] = dist;
        }

        RectHV rect;
        double point_dist;
        // This is the rigorous version of the checking
        // Using on the distance of the query point to the rectangle enclosed by the spillter
        // Comparing to the shortest distance to see if there is an overlap

        if (node.line_type == HORIZONTAL){

            // Have a check which direction to go
            boolean to_left = p.y() < node.p.y();

            if (to_left){
                rect = new RectHV(xmin, ymin , xmax, node.p.y());
                point_dist = rect.distanceSquaredTo(p);
                if (node.left != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.left, p, nearest_p, shortest_dist, xmin, ymin , xmax, node.p.y());
                }
    
                rect = new RectHV(xmin, node.p.y() , xmax, ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.right != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.right, p, nearest_p, shortest_dist, xmin, node.p.y() , xmax, ymax);                
                }

            }else{
                rect = new RectHV(xmin, node.p.y() , xmax, ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.right != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.right, p, nearest_p, shortest_dist, xmin, node.p.y() , xmax, ymax);                
                }
                rect = new RectHV(xmin, ymin , xmax, node.p.y());
                point_dist = rect.distanceSquaredTo(p);
                if (node.left != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.left, p, nearest_p, shortest_dist, xmin, ymin , xmax, node.p.y());
                }
            }
            

        }else{
            // For vertical line
            boolean to_left = p.x() < node.p.x();
            
            if (to_left){

                rect = new RectHV(xmin, ymin , node.p.x(), ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.left != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.left, p, nearest_p, shortest_dist, xmin, ymin , node.p.x(), ymax);
                }
                rect = new RectHV(node.p.x(), ymin , xmax, ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.right != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.right, p, nearest_p, shortest_dist, node.p.x(), ymin , xmax, ymax);
                }
            }else{
                rect = new RectHV(node.p.x(), ymin , xmax, ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.right != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.right, p, nearest_p, shortest_dist, node.p.x(), ymin , xmax, ymax);
                }
                rect = new RectHV(xmin, ymin , node.p.x(), ymax);
                point_dist = rect.distanceSquaredTo(p);
                if (node.left != null && point_dist < shortest_dist[0]){
                    nearest_neighbour(node.left, p, nearest_p, shortest_dist, xmin, ymin , node.p.x(), ymax);
                }
            }

        }
    }
    
    /*private void nearest_neighbour(pointNode node, Point2D p, Point2D[] nearest_p, double[] shortest_dist){
        System.out.println(node.p.toString());

        double dist = p.distanceSquaredTo(node.p);
        // Update the nearest point so far if one is found        
        if (dist < shortest_dist[0]){
            nearest_p[0] = node.p;
            shortest_dist[0] = dist;
        }

        // This is the simplified version of the checking
        // Using on the straight line dist from the splitter
        // Make use of the fact that the query point is on the same side
        // Check which direction to go on the splitter
        double straight_dist;
        boolean to_left;
        if(node.line_type == VERTICAL){
            straight_dist = Math.pow(p.x() - node.p.x(), 2);
            to_left = p.x() < node.p.x();
        }else{
            straight_dist = Math.pow(p.y() - node.p.y(), 2);
            to_left = p.y() < node.p.y();
        }

        // Go on the side with the point
        // After checking the side of the splitter with the point,
        // Only go to the other side if the straight distance is less than the shortest distance
        // This is because the circle by the shortest distance overlaps both side of the spiltter if so
        if (to_left){
            if (node.left != null){
                nearest_neighbour(node.left, p, nearest_p, shortest_dist);
            }
            if (node.right != null && straight_dist < shortest_dist[0]){
                nearest_neighbour(node.right, p, nearest_p, shortest_dist);
            }
        }else{
            if (node.right != null){
                nearest_neighbour(node.right, p, nearest_p, shortest_dist);
            }
            if (node.left != null && straight_dist < shortest_dist[0]){
                nearest_neighbour(node.left, p, nearest_p, shortest_dist);
            }
        }
    }
    */
    private class point_iterable implements Iterable<Point2D> {
        Point2D[] points_array;
        
        public point_iterable(Point2D[] points, int n){
            points_array = new Point2D[n];
            for(int i=0;i<n;i++){
                points_array[i] = points[i];
            }
        }

        public Iterator<Point2D> iterator(){
            return new point_iterator();
        }

        private class point_iterator implements Iterator<Point2D>{
            private int current = 0;

            public boolean hasNext(){
                return current < points_array.length;
            }

            public Point2D next(){
                return points_array[current++];
            }
        }

    }
 
    // unit testing of the methods (optional)     
    public static void main(String[] args)  {

        KdTree kdtree = new KdTree();
        In in = new In("test_traversal.txt");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);


            System.out.println("inserting");
            kdtree.insert(p);
            System.out.println(kdtree.size());
        }
        System.out.println(kdtree.contains(new Point2D(0.48, 0.04)));
        System.out.println(kdtree.contains(new Point2D(-2, 0)));
        System.out.println("Nearest: " + kdtree.nearest(new Point2D(0.46875, 0.125)));
    }                
 }