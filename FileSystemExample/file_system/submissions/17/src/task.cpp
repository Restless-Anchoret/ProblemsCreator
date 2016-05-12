#include <iostream>
#include <algorithm>

using namespace std;

const int MODUL = 20007;

int main() {
	int n, k;
	cin >> n >> k;

	int ** dp = new int * [n * (k - 1) + 1];
	int ** sums = new int * [n * (k - 1) + 1];
	for (int i = 0; i <= n * (k - 1); i++) {
		dp[i] = new int[n];
		sums[i] = new int[n];
	}

	dp[0][0] = sums[0][0] = 1;
	for (int i = 1; i <= k - 1; i++) {
		dp[i][0] = 1;
		sums[i][0] = sums[i - 1][0] + dp[i][0];
	}

	for (int j = 1; j < n; j++) {
		dp[0][j] = sums[0][j] = 1;
		for (int i = 1; i <= (k - 1) * (j + 1); i++) {
			int upIndex = max(-1, i - k);
			int downIndex = min(i, (k - 1) * j);
			dp[i][j] = sums[downIndex][j - 1];
			if (upIndex >= 0) dp[i][j] -= sums[upIndex][j - 1];
			if (dp[i][j] < 0) dp[i][j] += MODUL;
			sums[i][j] = (sums[i - 1][j] + dp[i][j]) % MODUL;
		}
	}

	int result = 0;
	for (int i = 0; i <= n * (k - 1); i++) {
		result = (result + dp[i][n - 1] * dp[i][n - 1]) % MODUL;
	}
	cout << result << endl;

	return 0;
}