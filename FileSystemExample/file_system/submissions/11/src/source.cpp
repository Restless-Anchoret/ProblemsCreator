#include <iostream>
#include <vector>
#include <set>

using namespace std;

int main() {
    int a, b;
    cin >> a >> b;
    int result = 0;
    while (result >= 0) {
        result = (result + 34242) % 23424;
    }
    cout << (a + b) << endl;
}