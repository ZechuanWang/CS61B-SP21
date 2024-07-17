package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c){
        cmp = c;
    }

    public T max(){
        if(isEmpty()){
            return null;
        }
        int maxIdx = 0;
        for(int i = 0; i < size(); i++){
            if(cmp.compare(get(i), get(maxIdx)) > 0){
                maxIdx = i;
            }
        }
        return get(maxIdx);
    }


    public T max(Comparator<T>  c){
        if(isEmpty()){
            return null;
        }
        int maxIdx = 0;
        for(int i = 0; i < size(); i++){
            if(c.compare(get(i), get(maxIdx)) > 0){
                maxIdx = i;
            }
        }
        return get(maxIdx);
    }


}
