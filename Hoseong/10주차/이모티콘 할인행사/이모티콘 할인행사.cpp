#include <string>
#include <vector>
#include <functional>
using namespace std;

vector<int> solution(vector<vector<int>> users, vector<int> emoticons) {
    vector<int> discounts = {10, 20, 30, 40};
    int n = users.size();
    int m = emoticons.size();

    int bestSubscribers = -1;
    int bestSales = -1;

    vector<int> combo(m);

    function<void(int)> dfs = [&](int idx) {
        if (idx == m) {
            int subscribers = 0;
            int sales = 0;
            for (int i = 0; i < n; i++) {
                int ratio = users[i][0];
                int limit = users[i][1];
                int cost = 0;
                for (int j = 0; j < m; j++) {
                    if (combo[j] >= ratio) {
                        cost += emoticons[j] * (100 - combo[j]) / 100;
                    }
                }
                if (cost >= limit) subscribers++;
                else sales += cost;
            }
            if (subscribers > bestSubscribers ||
                (subscribers == bestSubscribers && sales > bestSales)) {
                bestSubscribers = subscribers;
                bestSales = sales;
            }
            return;
        }
        for (int d : discounts) {
            combo[idx] = d;
            dfs(idx + 1);
        }
    };

    dfs(0);

    return {bestSubscribers, bestSales};
}