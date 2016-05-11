import java.util.*;

public class A {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int a = in.nextInt();
		int b = in.nextInt();
		int result = 0;
        while (result >= 0) {
            result = (result + 34242) % 23424;
        }
		System.out.println(a + b);
	}

}