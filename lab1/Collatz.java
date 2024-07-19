/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** Buggy implementation of nextNumber! */
    /**Return the required number depends on the given n
     if n is odd return 3 * n + 1
     if n is even, return n / 2 */
    public static int nextNumber(int n) {
        if(n % 2 == 1){
            return 3 * n + 1; // n is odd
        }else{ // n is even
            return n / 2;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

