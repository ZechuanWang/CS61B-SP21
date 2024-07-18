package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    /**Tests student's ArrayDeque */
    @Test
    public void studentArrayDequeTest(){
        StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> tea = new ArrayDequeSolution<>();
        int N = 10000;
        String failureSequence = "";
        for (int i = 0; i < N; i++) {
            int opNum = StdRandom.uniform(0, 4);
            if (opNum == 0) {
                int randVal = StdRandom.uniform(0, 100);
                failureSequence += String.format("addFirst(%d)\n", randVal);
                stu.addFirst(randVal);
                tea.addFirst(randVal);
            } else if (opNum == 1) {
                int randVal = StdRandom.uniform(0, 100);
                failureSequence += String.format("addLast(%d)\n", randVal);
                stu.addLast(randVal);
                tea.addLast(randVal);
            } else if (opNum == 2) {
                if (stu.isEmpty()) {
                    continue;
                }
                if (tea.isEmpty()) {
                    continue;
                }
                failureSequence += "removeFirst()\n";
                Integer stuVal = stu.removeFirst();
                Integer teaVal = tea.removeFirst();
                assertEquals(failureSequence, teaVal, stuVal);
            } else {
                if (stu.isEmpty()) {
                    continue;
                }
                if (tea.isEmpty()) {
                    continue;
                }
                failureSequence += "removeLast()\n";
                Integer stuVal = stu.removeLast();
                Integer teaVal = tea.removeLast();
                assertEquals(failureSequence, teaVal, stuVal);
            }


        }
    }

}
