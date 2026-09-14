#include <string>
#include <vector>
using namespace std;

long long solution(int cap, int n, vector<int> deliveries, vector<int> pickups) {
    long long answer = 0;
    int idx = n - 1;
    while (idx >= 0) {
        while (idx >= 0 && deliveries[idx] == 0 && pickups[idx] == 0) idx--;
        if (idx < 0) break;
        answer += (long long)(idx + 1) * 2;
        int d = cap, p = cap;
        int i = idx;
        while (i >= 0 && (d > 0 || p > 0)) {
            int use_d = min(d, deliveries[i]);
            deliveries[i] -= use_d;
            d -= use_d;
            int use_p = min(p, pickups[i]);
            pickups[i] -= use_p;
            p -= use_p;
            i--;
        }
        while (idx >= 0 && deliveries[idx] == 0 && pickups[idx] == 0) idx--;
    }
    return answer;
}