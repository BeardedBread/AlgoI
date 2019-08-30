import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int length = 0;

    // construct an empty randomized queue
    public RandomizedQueue(){
        queue = (Item[]) new Object[1];
    }
   
    // is the randomized queue empty?
    public boolean isEmpty(){
        return (length == 0);
    }

    // return the number of items on the randomized queue
    public int size(){
        return length;
    }

    // add the item
    public void enqueue(Item item){
        if (item == null){
            throw new IllegalArgumentException("Null item is not accepted.");
        }
        if (length == queue.length){
            resize(2 * queue.length);
        }
        queue[length++] = item;
    }

    // remove and return a random item
    public Item dequeue(){
        if (isEmpty()){
            throw new NoSuchElementException("Queue is empty.");
        }
                
        int index = StdRandom.uniform(0, length);
        Item item = queue[index];
        queue[index] = queue[length-1];
        queue[--length] = null;

        if (length > 0 && length == queue.length/4){
            resize(queue.length/2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if (isEmpty()){
            throw new NoSuchElementException("Queue is empty.");
        }
        return queue[StdRandom.uniform(0, length)];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < length; i++){
            copy[i] = queue[i]; 
        }   
        queue = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new queueIterator();
    }

    private class queueIterator implements Iterator<Item>{
        private int current = 0;
        private Item[] iter_list;

        private queueIterator(){
            iter_list = (Item[]) new Object[length];
            
            for (int i = 0; i < length; i++){
                iter_list[i] = queue[i]; 
            }   
            StdRandom.shuffle(iter_list);
        }

        public boolean hasNext(){
            return current != iter_list.length;
        }

        public void remove(){
            throw new UnsupportedOperationException("Remove operation is not supported.");
        }

        public Item next(){
            if (!hasNext()){
                throw new NoSuchElementException("No next item.");
            }
            return iter_list[current++];
        }

    }

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<Integer> test_deque = new RandomizedQueue<Integer>();
        System.out.println(test_deque.isEmpty());
        test_deque.enqueue(2);
        test_deque.enqueue(3);
        System.out.println("Size: "+test_deque.size());
        System.out.println(test_deque.dequeue());
        System.out.println(test_deque.dequeue());

        for(int i=0;i<10;i++){
            test_deque.enqueue(i);
        }
        System.out.println("Size: "+test_deque.size());
        System.out.println(test_deque.sample());
        System.out.println(test_deque.sample());

        Iterator<Integer> iter = test_deque.iterator();
        Iterator<Integer> iter2 = test_deque.iterator();

        while(iter.hasNext()){
            System.out.print(iter.next());
            System.out.print(" ");
        }
        System.out.println("");
        while(iter2.hasNext()){
            System.out.print(iter2.next());
            System.out.print(" ");
        }
        System.out.println("");
    }

}