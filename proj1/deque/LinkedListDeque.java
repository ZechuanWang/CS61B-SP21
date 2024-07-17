package deque;


import java.util.Iterator;

/**Doubly linked list for all data types.
 * Firstly implemented based on supporting Integer, and then modify for all types
 * @author Zechuan Wang
 */

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private Node sentinel;

    /**
     * private node class
     */
    private  class Node {
        private T val;
        private Node next;
        private Node pre;

        Node(T v, Node p, Node n) {
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


    public int size() {
        return size;
    }

//    public boolean isEmpty(){
//        if (sentinel.pre == sentinel && sentinel.next == sentinel){
//            return true;
//        }else{
//            return false;
//        }
//    }

    public void addFirst(T item) {
        Node tmp = new Node(item, null, null);
        tmp.next = sentinel.next;
        tmp.next.pre = tmp;
        sentinel.next = tmp;
        tmp.pre = sentinel;
        size++;
    }

    public void addLast(T item) {
        Node tmp = new Node(item, null, null);
        tmp.pre = sentinel.pre;
        tmp.pre.next = tmp;
        sentinel.pre = tmp;
        tmp.next = sentinel;
        size++;
    }

    public void printDeque() {
        if (this.isEmpty()) {
            return;
        }
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.val);
            p = p.next;
            if (p != sentinel) {
                System.out.print(" ");
            } else {
                System.out.print('\n');
            }
        }

    }
    public T removeFirst() {
        if (this.isEmpty()) {
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

    public T removeLast() {
        if (this.isEmpty()) {
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

    public T get(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        }
        int idx = 0;
        Node p = sentinel.next;
        while (idx != index) {
            p = p.next;
            idx++;
        }
        return p.val;
    }
    /**Speak the secret language of gods*/
    private T getHelper(Node p, int index) {
        if (index == 0) {
            return p.val;
        }
        return getHelper(p.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        } else {
            return getHelper(sentinel.next, index);
        }
    }

    /**Returns an iterator*/
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;
        LinkedListDequeIterator() {
            pos = 0;
        }
        @Override
        public boolean hasNext() {
            return pos < size;
        }
        @Override
        public T next() {
            T returnItem = get(pos);
            pos++;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque) {
            LinkedListDeque oLLD  = (LinkedListDeque) o;
            if (oLLD.size != this.size) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (this.get(i) != oLLD.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static void main(String[] args) {
        LinkedListDeque<Integer> deque = new LinkedListDeque();
        System.out.println(deque.size());
        System.out.println(deque.isEmpty());
        deque.addFirst(10);
        deque.addFirst(5);
        deque.addLast(15);
        deque.addLast(20);
        LinkedListDeque<Integer> tmp = new LinkedListDeque<>();
        tmp.addFirst(10);
        tmp.addFirst(5);
        tmp.addLast(15);
        tmp.addLast(20);
        System.out.println(tmp.equals(deque));
    }
}

