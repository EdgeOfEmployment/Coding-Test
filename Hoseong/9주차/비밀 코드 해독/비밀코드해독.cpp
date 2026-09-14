#include <string>
#include <vector>
using namespace std;

int solution(int n, vector<vector<int>> q, vector<int> ans) {
    int m = q.size();
    
    // 각 시도(q[i])를 비트마스크로 변환
    vector<long long> qmask(m, 0);
    for (int i = 0; i < m; i++) {
        for (int v : q[i]) {
            qmask[i] |= (1LL << v);
        }
    }
    
    int answer = 0;
    
    // 1~n 중 서로 다른 5개(a<b<c<d<e)를 모두 시도
    for (int a = 1; a <= n - 4; a++)
    for (int b = a + 1; b <= n - 3; b++)
    for (int c = b + 1; c <= n - 2; c++)
    for (int d = c + 1; d <= n - 1; d++)
    for (int e = d + 1; e <= n; e++) {
        long long mask = (1LL << a) | (1LL << b) | (1LL << c) | (1LL << d) | (1LL << e);
        
        bool ok = true;
        for (int i = 0; i < m; i++) {
            int cnt = __builtin_popcountll(mask & qmask[i]);
            if (cnt != ans[i]) {
                ok = false;
                break;
            }
        }
        
        if (ok) answer++;
    }
    
    return answer;
}