import java.util.*;

public class A {

	public static void test() {
		long start = System.currentTimeMillis();
        int quantity = 100_000_000;
        long modul = 100_000_000_000_000_000L;
        int m = 10_000;
        long[] num = new long[quantity];
        num[0] = 0;
        num[m] = 1;
        for (int i = 2; i * m < quantity; i++) {
            num[i * m] = (num[(i - 2) * m] + num[(i - 1) * m]) % modul;
        }
        System.out.println(num[quantity - m]);
        System.out.println(System.currentTimeMillis() - start);
	}

	public static void main(String[] args) {
		test();
		Scanner in = new Scanner(System.in);
		System.out.println(in.nextInt() + in.nextInt());
	}

}