import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;

import java.util.Iterator;

public class PointSET {
    private SET<Point2D> set_points;

    // construct an empty set of points
    public PointSET(){
        set_points = new SET<Point2D>();
    }

    // is the set empty?                             
    public boolean isEmpty(){
        return set_points.isEmpty();
    }  
    // number of points in the set                    
    public int size(){
        return set_points.size();
    }
    // add the point to the set (if it is not already in the set)                         
    public void insert(Point2D p){
        if (p == null){
            throw new IllegalArgumentException("Point is null.");
        }
        if (!set_points.contains(p))
            set_points.add(p);
    }
    // does the set contain point p?              
    public boolean contains(Point2D p){
        return set_points.contains(p);
    }
    // draw all points to standard draw              
    public void draw(){
        Iterator<Point2D> iter = set_points.iterator();

        while (iter.hasNext()){
            iter.next().draw();
        }
        //StdDraw.show();

    }
    // all points that are inside the rectangle (or on the boundary)                       
    public Iterable<Point2D> range(RectHV rect){
        if (rect == null){
            throw new IllegalArgumentException("Rect is null.");
        }
        Iterator<Point2D> iter = set_points.iterator();

        Point2D p;
        Point2D[] p_array = new Point2D[set_points.size()];
        int n = 0;
        while (iter.hasNext()){
            p = iter.next();
            if (rect.contains(p)){
                p_array[n++] = p;
            }
        }

        return new point_iterable(p_array, n);

    }
    // a nearest neighbor in the set to point p; null if the set is empty        
    public Point2D nearest(Point2D p){
        if (p == null){
            throw new IllegalArgumentException("Point is null.");
        }

        if (set_points.isEmpty()){
            return null;
        }

        Point2D current_p;
        double current_dist = 0;
        Point2D nearest_p = null;
        double shortest_dist = -1;
        
        Iterator<Point2D> iter = set_points.iterator();
        while (iter.hasNext()){
            current_p = iter.next();
            current_dist = p.distanceSquaredTo(current_p);
            if (shortest_dist < 0 || current_dist < shortest_dist){
                nearest_p = current_p;
                shortest_dist = p.distanceSquaredTo(current_p);
            }
        }

        return nearest_p;
    }          
    
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

    }                
 }