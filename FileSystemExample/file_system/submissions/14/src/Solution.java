import java.util.Scanner;

public class Solution {

    public static long modul = 1_000_000_007;
    
    public static long getDegree(long number, long degree) {
        if (degree == 0) {
            return 1;
        } else if (degree % 2 == 1) {
            return (getDegree(number, degree - 1) * number) % modul;
        } else {
            long sqrt = getDegree(number, degree / 2);
            return (sqrt * sqrt) % modul;
        }
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long n = in.nextInt();
        long k = in.nextInt();
        System.out.println(getDegree(n, k));
    }
    
}