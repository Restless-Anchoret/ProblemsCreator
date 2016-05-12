import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Solution {
    
    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public static StringTokenizer tok;

    public static int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

    public static long nextLong() throws IOException {
        return Long.parseLong(nextToken());
    }

    public static double nextDouble() throws IOException {
        return Double.parseDouble(nextToken());
    }

    public static String nextToken() throws IOException {
        if (tok == null || !tok.hasMoreTokens()) {
            tok = new StringTokenizer(in.readLine());
        }
        return tok.nextToken();
    }
    
    public static void main(String[] args) throws IOException {
        int n = nextInt();
        int m = nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = nextInt();
        }
        
        Set<Integer> set = new HashSet<>();
        List<Integer> result = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            if (!set.contains(a[i])) {
                result.add(a[i]);
                set.add(a[i]);
            }
        }
        
        System.out.println(result.size());
        for (int number: result) {
            System.out.print(number + " ");
        }
    }
    
}