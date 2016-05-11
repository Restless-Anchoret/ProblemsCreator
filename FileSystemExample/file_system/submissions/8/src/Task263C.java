import java.util.*;
import java.math.*;
import static java.lang.Math.*;
import static java.lang.System.*;

public class Task263C {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int n = in.nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = in.nextInt();
		}
		Arrays.sort(a);
		
		long[] sum = new long[n];
		sum[n - 1] = a[n - 1];
		for (int i = n - 2; i >= 0; i--) {
			sum[i] = a[i] + sum[i + 1];
		}
		
		long result = sum[0];
		for (int i = 0; i < n - 1; i++) {
			result += sum[i];
		}
		
		out.println(result);
	}

}