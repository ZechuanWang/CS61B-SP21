package deque;

public interface Deque<T> {
    /**Adds item to the front of the deque. */
    public void addFirst(T item);

    /**Adds item to the back of the deque. */
    public void addLast(T item);

    /**Returns ture if the deque is empty, otherwise false. */
    default public boolean isEmpty(){
        return size() == 0;
    }

    /**Returns the size of the deque. */
    public int size();

    /**Prints out the whole deque. */
    public void printDeque();

    /**removes the first item of the deque and returns it. */
    public T removeFirst();

    /**removes the first item of the deque and returns it. */
    public T removeLast();

    /**Returns the index-th item in the deque. */
    public T get(int index);
}
