package deque;

public class ArrayDeque<T> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;

    public ArrayDeque(){
        size = 0;
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void addFirst(T item){
        items[nextFirst] = item;
        nextFirst = (nextFirst + 7) % 8;
        size++;
    }

    public void addLast(T item){
        items[nextLast] = item;
        nextLast = (nextLast + 1) % 8;
        size++;
    }
    public void printDeque(){
        if (this.isEmpty()){
            return;
        }
        int cnt  = 0; //count for the printed items.
        int p = (nextFirst + 1) % 8;
        while(p != nextLast || cnt < size){
            System.out.print(items[p]);
            cnt++;
            p = (p + 1) % 8;
            if(p != nextLast){
                System.out.print(" ");
            }else {
                System.out.print('\n');
            }
        }

    }
    public T removeFirst(){
        if(this.isEmpty()){
            return null;
        }
        int p = (nextFirst + 1) % 8;
        T val = items[p];
        items[p] = null;
        nextFirst = p;
        size--;
        return val;
    }

    public T removeLast(){
        if(this.isEmpty()){
            return null;
        }
        int p = (nextLast + 7) % 8;
        T val = items[p];
        items[p] = null;
        nextLast = p;
        size--;
        return val;
    }

    public T get(int index){
        if(index > size - 1){
            return null;
        }
        int idx = 0;
        int p = (nextFirst + 1) % 8;
        while (idx != index){
            p = (p + 1) % 8;
            idx++;
        }
        return items[p];
    }
    public static void main(String[] args){
        ArrayDeque<Character> deque = new ArrayDeque();
        System.out.println(deque.isEmpty());
        System.out.println(deque.size());
        deque.addLast('a');
        deque.addLast('b');
        deque.printDeque();
        deque.addFirst('c');
        deque.printDeque();
        deque.addLast('d');
        deque.addLast('e');
        deque.addFirst('f');
        deque.printDeque();
        deque.addLast('g');
        deque.printDeque();
        deque.addLast('h');
        deque.printDeque();
        Character x = deque.removeFirst();
        System.out.println(x);
        x = deque.removeLast();
        System.out.println(x);
        deque.printDeque();
        System.out.println(deque.get(2));
    }
}
