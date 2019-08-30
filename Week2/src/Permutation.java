import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
   public static void main(String[] args){
    if (args.length < 1){
        throw new IllegalArgumentException("Only 1 argument is required");
    }
    int k = Integer.parseInt(args[0]);
    
    /*RandomizedQueue<String> queue = new RandomizedQueue<String>();

    while(!StdIn.isEmpty()){
        queue.enqueue(StdIn.readString());
    }
    Iterator<String> iter = queue.iterator();
    for (int i=0;i<k;i++){
        if (iter.hasNext()){
            System.out.println(iter.next());
        }
    }*/
    if (k >0){
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        String str;
        int n = 0;
        while(!StdIn.isEmpty()){
            str = StdIn.readString();
            n++;
            if (queue.size() < k){
                queue.enqueue(str);
            }else{
                int i = StdRandom.uniform(1, n+1);
                if (i <= k){
                    queue.dequeue();
                    queue.enqueue(str);
                }
            }
        }
        Iterator<String> iter = queue.iterator();
        while (iter.hasNext()){
            System.out.println(iter.next());
        }
    
    }
    

   }
}