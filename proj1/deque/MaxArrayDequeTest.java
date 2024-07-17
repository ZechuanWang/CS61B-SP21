package deque;
import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;
public class MaxArrayDequeTest {

    private static class IntComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer a, Integer b){
            return a - b;
        }
    }
    private static class StringComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2){
            int l1 = s1.length();
            int l2 = s2.length();
            for (int i = 0; i < l1 && i < l2; i++){
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);

                if(c1 != c2){
                    return c1 - c2;
                }

            }
            return l1 - l2;
        }
    }
    private static class StringLengthComparator implements Comparator<String>{
        @Override
        public int compare(String s1, String s2){
            return s1.length() - s2.length();
        }
    }
    @Test
    public void maxWithoutComparatorTest(){
        MaxArrayDeque<Integer> ma = new MaxArrayDeque<>(new IntComparator());
        for(int i = 0; i < 5; i++){
            ma.addLast(i);
        }
        assertEquals((Integer)4, ma.max());
    }
    @Test
    public void maxWithComparatorTest(){
        MaxArrayDeque<String> ma = new MaxArrayDeque<>(new StringComparator());
        ma.addLast("The world is my oyster.Voila");
        ma.addLast("The world is yr oyster.");
        assertEquals("The world is yr oyster.", ma.max() );
        assertEquals("The world is my oyster.Voila", ma.max(new StringLengthComparator()));
    }

}
