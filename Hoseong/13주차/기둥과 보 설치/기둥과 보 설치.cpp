#include <string>
#include <vector>

using namespace std;

// 현재 구조물 전체가 규칙을 만족하는지 검사
bool valid(int n, const vector<vector<int>>& pillar, const vector<vector<int>>& beam) {
    for (int x = 0; x <= n; x++) {
        for (int y = 0; y <= n; y++) {
            // 기둥: 바닥 위 / 보의 한쪽 끝 위 / 다른 기둥 위
            if (pillar[x][y]) {
                bool ok = (y == 0)
                    || (y > 0 && pillar[x][y - 1])
                    || beam[x][y]
                    || (x > 0 && beam[x - 1][y]);
                if (!ok) return false;
            }
            // 보: 한쪽 끝이 기둥 위 / 양쪽 끝이 다른 보와 연결
            if (beam[x][y]) {
                bool ok = (y > 0 && pillar[x][y - 1])
                    || (y > 0 && pillar[x + 1][y - 1])
                    || (x > 0 && beam[x - 1][y] && beam[x + 1][y]);
                if (!ok) return false;
            }
        }
    }
    return true;
}

vector<vector<int>> solution(int n, vector<vector<int>> build_frame) {
    // 인덱스 0..n 까지 사용 (보의 오른쪽 끝 x+1 접근을 위해 n+2 크기)
    vector<vector<int>> pillar(n + 2, vector<int>(n + 2, 0));
    vector<vector<int>> beam(n + 2, vector<int>(n + 2, 0));

    for (const auto& f : build_frame) {
        int x = f[0], y = f[1], a = f[2], b = f[3];

        vector<vector<int>>& target = (a == 0) ? pillar : beam;
        int prev = target[x][y];
        target[x][y] = b;              // 일단 적용해 보고

        if (!valid(n, pillar, beam)) { // 조건 불만족 시 무시(되돌림)
            target[x][y] = prev;
        }
    }

    // x 오름차순, 같으면 y 오름차순, 같은 좌표면 기둥(0) 먼저
    vector<vector<int>> answer;
    for (int x = 0; x <= n; x++) {
        for (int y = 0; y <= n; y++) {
            if (pillar[x][y]) answer.push_back({x, y, 0});
            if (beam[x][y])   answer.push_back({x, y, 1});
        }
    }
    return answer;
}
