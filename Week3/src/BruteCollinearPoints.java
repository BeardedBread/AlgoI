import java.util.Arrays;

public class BruteCollinearPoints {
    private int num_of_segments;
    private LineSegment[] line_segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points){
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

        line_segments = new LineSegment[1];
        num_of_segments = 0;
        int p, q, r, s;
        double s1;
        if(original_copy.length >=4){
            for(p=0;p<=original_copy.length-4;p++){
                for(q=p+1;q<=original_copy.length-3;q++){
                    s1 = original_copy[p].slopeTo(original_copy[q]);
    
                    for(r=q+1;r<=original_copy.length-2;r++){
                        if (original_copy[p].slopeTo(original_copy[r]) != s1)
                            continue;
                        
                        for(s=r+1;s<=original_copy.length-1;s++){
                            if (original_copy[p].slopeTo(original_copy[s]) != s1)
                                continue;
                            
                            if (num_of_segments == line_segments.length){
                                resize(2*num_of_segments);
                            }
                            line_segments[num_of_segments++] = new LineSegment(original_copy[p], original_copy[s]);
                            
                        
                        }
                    }
                }
            }
            if (num_of_segments > 0)
                resize(num_of_segments);

        }
        

    }
    
    // the number of line segments
    public int numberOfSegments(){
        return num_of_segments;
    }
    
    // the line segments
    public LineSegment[] segments(){
        LineSegment[] return_array= new LineSegment[num_of_segments];
        for (int i=0;i<num_of_segments;i++)
            return_array[i] = line_segments[i];
        return return_array; 
    }
    
    private void resize(int capacity){
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < num_of_segments; i++){
            copy[i] = line_segments[i]; 
        }   
        line_segments = copy;
    }
}