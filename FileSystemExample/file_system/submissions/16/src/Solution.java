import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeSet;

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
    
    public static class Pair {
        public int to, length;
        public Pair(int to, int length) {
            this.to = to;
            this.length = length;
        }
    }
    
    public static void main(String[] args) throws IOException {
        int n = nextInt();
        int m = nextInt();
        int s = nextInt() - 1;
        int t = nextInt() - 1;
        ArrayList<ArrayList<Pair>> g = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int a, b, c;
            a = nextInt() - 1;
            b = nextInt() - 1;
            c = nextInt();
            g.get(a).add(new Pair(b, c));
            g.get(b).add(new Pair(a, c));
        }
        int d[] = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = Integer.MAX_VALUE;
        }
        d[s] = 0;

        TreeSet<Integer> set = new TreeSet<>((first, second) -> {
            if (d[first] == d[second]) {
                return Integer.compare(first, second);
            } else {
                return Integer.compare(d[first], d[second]);
            }
        });
        set.add(s);

        while (!set.isEmpty()) {
            int v = set.first();
            set.remove(v);
            for (Pair pair: g.get(v)) {
                if (d[v] + pair.length < d[pair.to]) {
                    set.remove(pair.to);
                    d[pair.to] = d[v] + pair.length;
                    set.add(pair.to);
                }
            }
        }
        System.out.println(d[t]);
    }
    
}