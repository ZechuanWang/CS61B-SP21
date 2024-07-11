package deque;
/**Doubly linked list for all data types.
 * Firstly implemented based on supporting Integer, and then modify for all types
 * @author Zechuan Wang
 */

public class LinkedListDeque<T> {
    private int size;
    private Node sentinel;

    /**
     * private node class
     */
    private  class Node {
        public T val;
        public Node next;
        public Node pre;

        public Node(T v, Node p, Node n) {
            this.val = v;
            this.next = n;
            this.pre = p;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        size = 0;
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
    }


    public int size(){
        return size;
    }

    public boolean isEmpty(){
        if (sentinel.pre == sentinel && sentinel.next == sentinel){
            return true;
        }else{
            return false;
        }
    }

    public void addFirst(T item){
        Node tmp = new Node(item, null, null);
        tmp.next = sentinel.next;
        tmp.next.pre= tmp;
        sentinel.next = tmp;
        tmp.pre = sentinel;
        size++;
    }

    public void addLast(T item){
        Node tmp = new Node(item, null, null);
        tmp.pre = sentinel.pre;
        tmp.pre.next = tmp;
        sentinel.pre = tmp;
        tmp.next = sentinel;
        size++;
    }

    public void printDeque(){
        if(this.isEmpty()){
            return;
        }
        Node p = sentinel.next;
        while (p != sentinel){
            System.out.print(p.val);
            p = p.next;
            if(p != sentinel){
                System.out.print(" ");
            }else{
                System.out.print('\n');
            }
        }

    }
    public T removeFirst(){
        if(this.isEmpty()){
            return null;
        }
        Node tmp = sentinel.next;
        sentinel.next = tmp.next;
        tmp.next.pre = sentinel;
        tmp.next = null;
        tmp.pre = null;
        size--;
        return tmp.val;
    }

    public T removeLast(){
        if(this.isEmpty()){
            return null;
        }
        Node tmp = sentinel.pre;
        sentinel.pre = tmp.pre;
        tmp.pre.next = sentinel;
        tmp.next = null;
        tmp.pre = null;
        size--;
        return tmp.val;
    }

    public T get(int index){
        if(index > size - 1){
            return null;
        }
        int idx = 0;
        Node p = sentinel.next;
        while (idx != index){
            p = p.next;
            idx++;
        }
        return p.val;
    }
    /**Speak the secret language of gods*/
    private T getHelper(Node p, int index){
        if(index == 0){
            return p.val;
        }
        return getHelper(p.next, index - 1);
    }
    public T getRecursive(int index){
        if (index > size - 1){
            return null;
        }else{
            return getHelper(sentinel.next, index);
        }
    }

    public static void main(String[] args){
        LinkedListDeque<Integer> deque = new LinkedListDeque();
        System.out.println(deque.size());
        System.out.println(deque.isEmpty());
        deque.addFirst(10);
        deque.addFirst(5);
        deque.addLast(15);
        deque.addLast(20);
        System.out.println(deque.getRecursive(4));
        System.out.println(deque.getRecursive(3));
        System.out.println(deque.getRecursive(2));
        System.out.println(deque.getRecursive(1));
        System.out.println(deque.getRecursive(0));
        deque.printDeque();
        deque.removeFirst();
        deque.removeLast();
        deque.printDeque();
    }
}