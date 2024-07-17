package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;


    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
    }

//    public boolean isEmpty(){
//        return size == 0;
//    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int p = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            a[i] = items[p];
            p = (p + 1) % items.length;
        }
        nextFirst = capacity - 1;
        nextLast = size;
        items = a;
    }


    public void addFirst(T item) {
        if (size == items.length) { // array is full, need to resize
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst + items.length - 1) % items.length;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }
    public void printDeque() {
        if (this.isEmpty()) {
            return;
        }
        int cnt  = 0; //count for the printed items.
        int p = (nextFirst + 1) % items.length;
        while (p != nextLast || cnt < size) {
            System.out.print(items[p]);
            cnt++;
            p = (p + 1) % items.length;
            if (p != nextLast) {
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
        if ((items.length >= 16) && ((size - 1) < items.length / 4)) {
            resize(items.length / 2);
        }
        int p = (nextFirst + 1) % items.length;
        T val = items[p];
        items[p] = null;
        nextFirst = p;
        size--;
        return val;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        if ((items.length >= 16) && ((size - 1) < items.length / 4)) {
            resize(items.length / 2);
        }
        int p = (nextLast + items.length - 1) % items.length;
        T val = items[p];
        items[p] = null;
        nextLast = p;
        size--;
        return val;
    }

    public T get(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        }
        int p = (nextFirst + 1 + index) % items.length;

        return items[p];
    }


    /**Returns an iterator*/
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;

        ArrayDequeIterator() {
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
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Deque) {
            Deque<T> other  = (Deque<T>) o;
            if (other.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (!this.get(i).equals(other.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static void main(String[] args) {
        ArrayDeque<Integer> deque = new ArrayDeque();
        deque.addFirst(5);
        deque.addFirst(10);
        deque.addLast(15);
        deque.addLast(20);
        for (Object x : deque) {
            System.out.println(x);
        }

    }
}
