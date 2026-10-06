#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

vector<int> solution(int m, int n, int startX, int startY, vector<vector<int>> balls) {
    vector<int> answer;

    for (const auto& ball : balls) {
        int a = ball[0];
        int b = ball[1];
        int best = INT_MAX;

        // 왼쪽 벽: 대칭점 (-a, b)
        if (!(startY == b && a < startX)) {
            int dx = startX + a, dy = startY - b;
            best = min(best, dx * dx + dy * dy);
        }
        // 오른쪽 벽: 대칭점 (2m-a, b)
        if (!(startY == b && a > startX)) {
            int dx = startX - (2 * m - a), dy = startY - b;
            best = min(best, dx * dx + dy * dy);
        }
        // 아래쪽 벽: 대칭점 (a, -b)
        if (!(startX == a && b < startY)) {
            int dx = startX - a, dy = startY + b;
            best = min(best, dx * dx + dy * dy);
        }
        // 위쪽 벽: 대칭점 (a, 2n-b)
        if (!(startX == a && b > startY)) {
            int dx = startX - a, dy = startY - (2 * n - b);
            best = min(best, dx * dx + dy * dy);
        }

        answer.push_back(best);
    }
    return answer;
}
