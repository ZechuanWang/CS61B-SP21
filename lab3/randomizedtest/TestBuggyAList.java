package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer>  alistNoResize = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();
        alistNoResize.addLast(4);
        buggyAList.addLast(4);
        alistNoResize.addLast(5);
        buggyAList.addLast(5);
        alistNoResize.addLast(6);
        buggyAList.addLast(6);
        assertEquals(alistNoResize.size(), buggyAList.size());

        assertEquals(alistNoResize.removeLast(), buggyAList.removeLast());
        assertEquals(alistNoResize.removeLast(), buggyAList.removeLast());
        assertEquals(alistNoResize.removeLast(), buggyAList.removeLast());
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> Bug = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                Bug.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = Bug.size();
//                System.out.println("size1: " + size1);
//                System.out.println("size2: " + size2);
                assertEquals(size1, size2);
            }else if (operationNumber == 2){
                //getLast
                if (L.size() == 0) continue;
//                System.out.println("getLast1(" + L.getLast() + ")");
//                System.out.println("getLast2(" + Bug.getLast() + ")");
                assertEquals(L.getLast(), Bug.getLast());
            }else if (operationNumber == 3){
                //removeLast
                if(L.size() == 0) continue;
                int val1 = L.removeLast();
                int val2 = Bug.removeLast();
//                System.out.println("removeLast1(" + val1 + ")");
//                System.out.println("removeLast2(" + val2 + ")");
                assertEquals(val1, val2);
            }
        }

    }
}
