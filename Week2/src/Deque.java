import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>{
    private class Node{
        Item item;
        Node next;
        Node prev;
    }

    private Node first = null;
    private Node last = null;
    private int length = 0;

    // construct an empty deque
    public Deque(){

    }

    // is the deque empty?
    public boolean isEmpty(){
       return (length == 0);
    }

    // return the number of items on the deque
    public int size(){
        return length;
    }

    // add the item to the front
    public void addFirst(Item item){
        if (item == null){
            throw new IllegalArgumentException("Null item is not accepted.");
        }
        Node new_node = new Node();
        new_node.item = item;
        new_node.next = first;
        new_node.prev = null;
        if (first == null){
            last = new_node;            
        }else{
            first.prev = new_node;
        }
        first = new_node;
        length++;
    }

    // add the item to the back
    public void addLast(Item item){
        if (item == null){
            throw new IllegalArgumentException("Null item is not accepted.");
        }
        Node new_node = new Node();
        new_node.item = item;
        new_node.next = null;
        new_node.prev = last;
        if (last == null){
            first = new_node;      
        }else{
            last.next = new_node;        
        }
        last = new_node;   
        length++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if (isEmpty()){
            throw new NoSuchElementException("Queue is empty.");
        }
        Item item = first.item;
        first = first.next;
        if(first != null){
            first.prev = null;
        }
        length--;

        if(first == null){
            last = null;
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if (isEmpty()){
            throw new NoSuchElementException("Queue is empty.");
        }

        Item item = last.item;
        if (length == 1){
            first = null;
            last = null;            
        }else{
            last = last.prev;
            last.next = null;
        }
        length--;
        
        return item;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new dequeIterator();
    }

    private class dequeIterator implements Iterator<Item>{
        private Node current = first;

        public boolean hasNext(){
            return current != null;
        }

        public void remove(){
            throw new UnsupportedOperationException("Remove operation is not supported.");
        }

        public Item next(){
            if (!hasNext()){
                throw new NoSuchElementException("No next item.");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<Integer> test_deque = new Deque<Integer>();

        System.out.println(test_deque.isEmpty());
        test_deque.addFirst(2);
        test_deque.addLast(5);
        System.out.println(test_deque.removeLast());
        System.out.println(test_deque.removeFirst());
        test_deque.addFirst(1);
        test_deque.addFirst(2);
        test_deque.addFirst(3);
        test_deque.addFirst(4);
        test_deque.removeLast();

        test_deque.addFirst(3);
        test_deque.addFirst(7);
        test_deque.addFirst(9);
        test_deque.addLast(13);
        test_deque.addLast(19);
        test_deque.addLast(1);
        System.out.println("Size: " + test_deque.size());
        Iterator<Integer> iter = test_deque.iterator();
        while(iter.hasNext()){
            System.out.print(iter.next());
            System.out.print(" ");
        }
        System.out.println("");
    }
}