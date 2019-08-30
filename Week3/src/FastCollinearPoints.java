import java.util.Arrays;

public class FastCollinearPoints {
    private int num_of_segments;
    private LineSegment[] line_segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points){
        if (points == null){
            throw new IllegalArgumentException("Points cannot be null.");
        }
        for(int i=0; i<points.length;i++){
            if (points[i] == null){
                throw new IllegalArgumentException("No null points allowed.");
            }
        }
        Point[] original_copy = (Point[]) points.clone();
        Arrays.sort(original_copy);
        for(int i=0; i<original_copy.length-1;i++){
            if (original_copy[i].compareTo(original_copy[i+1]) == 0){
                throw new IllegalArgumentException("No duplicate points allowed.");
            }
        }

        int est_segments = points.length / 2 + 1;
        line_segments = new LineSegment[est_segments];
        num_of_segments = 0;
        Point[] segments_start_points = new Point[est_segments];
        double[] segments_slopes = new double[est_segments];
        int n_connected = 1;
        double current_slope = 0;
        int p, q;
        double s1;

        if(original_copy.length >=4){
            for(p=0;p<=original_copy.length-4;p++){
                Point[] points_copy = new Point[original_copy.length-p];
                for(int i=p;i<original_copy.length;i++){
                    points_copy[i-p] = original_copy[i];            
                }
                Arrays.sort(points_copy, original_copy[p].slopeOrder());
                current_slope = Double.NEGATIVE_INFINITY;

                for(q=1;q<points_copy.length;q++){                
                    s1 = points_copy[0].slopeTo(points_copy[q]);
                    boolean check_needed = false;
                    int offset = 1;
                    if (s1 == current_slope){
                        n_connected++;
                        if (q==points_copy.length-1){
                            check_needed = true;
                            offset = 0;
                        }
                    }else{
                        check_needed = true;
                    }
                    
                    if (check_needed){
                        if(n_connected >= 4){
                            boolean dupe = false;
        
                            if (num_of_segments>0){
                                for (int i=0;i<num_of_segments;i++){
                                    if (current_slope == segments_slopes[i] &&
                                    points_copy[0].slopeTo(segments_start_points[i]) == segments_slopes[i]) {
                                        dupe = true;
                                        break;
                                    }
                                }
                            }
                            
                            if (!dupe){
                                if (num_of_segments == line_segments.length){
                                    resize(2*num_of_segments);
                                    Point[] copy2 = new Point[2*num_of_segments];
                                    double[] copy3 = new double[2*num_of_segments];
                                    for (int i = 0; i < num_of_segments; i++){
                                        copy2[i] = segments_start_points[i];
                                        copy3[i] = segments_slopes[i];
                                    }
                                    segments_start_points = copy2;
                                    segments_slopes = copy3;
        
                                }
                                line_segments[num_of_segments] = new LineSegment(points_copy[0], points_copy[q-offset]);
                                segments_start_points[num_of_segments] = points_copy[0];
                                segments_slopes[num_of_segments++] = current_slope;
                            }
                            
                        }
                        n_connected = 2;
                        current_slope = s1;
                    } 
                }
            }
            resize(num_of_segments);
        }

    }
    
    // the number of line segments
    public int numberOfSegments(){
        return num_of_segments;
    }
    
    // the line segments
    public LineSegment[] segments(){
        return (LineSegment[]) line_segments.clone();     
    }
    
    private void resize(int capacity){
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < num_of_segments; i++){
            copy[i] = line_segments[i];
        }   
        line_segments = copy;
    }
}