#include <string>
#include <vector>

using namespace std;

int solution(int n, vector<vector<int>> results) {
    // win[a][b] = true 이면 a가 b를 이긴 것 (직접 또는 간접)
    vector<vector<bool>> win(n + 1, vector<bool>(n + 1, false));

    for (auto& r : results) {
        win[r[0]][r[1]] = true;
    }

    // 플로이드-워셜로 간접 승패 관계 채우기
    for (int k = 1; k <= n; k++) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (win[i][k] && win[k][j]) {
                    win[i][j] = true;
                }
            }
        }
    }

    int answer = 0;
    for (int i = 1; i <= n; i++) {
        int known = 0;
        for (int j = 1; j <= n; j++) {
            if (i != j && (win[i][j] || win[j][i])) {
                known++;
            }
        }
        // 나머지 n-1명과의 관계를 모두 알면 순위가 확정됨
        if (known == n - 1) {
            answer++;
        }
    }

    return answer;
}
