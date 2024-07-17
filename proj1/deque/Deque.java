package deque;

public interface Deque<T> {
    /**Adds item to the front of the deque. */
    void addFirst(T item);

    /**Adds item to the back of the deque. */
    void addLast(T item);

    /**Returns ture if the deque is empty, otherwise false. */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**Returns the size of the deque. */
    int size();

    /**Prints out the whole deque. */
    void printDeque();

    /**removes the first item of the deque and returns it. */
    T removeFirst();

    /**removes the first item of the deque and returns it. */
    T removeLast();

    /**Returns the index-th item in the deque. */
    T get(int index);
}
