#include <vector>
#include <algorithm>

using namespace std;

long long solution(int n, int m, int x, int y, vector<vector<int>> queries) {
    // 0-indexed 위치 및 범위를 long long으로 관리
    long long r1 = x, r2 = x;
    long long c1 = y, c2 = y;
    
    // 쿼리를 역순으로 처리
    for (int i = queries.size() - 1; i >= 0; --i) {
        int dir = queries[i][0];
        long long dx = queries[i][1];
        
        if (dir == 0) { // 원본: 좌측 이동 -> 역동작: 우측 이동
            if (c1 != 0) c1 += dx;
            c2 = min((long long)m - 1, c2 + dx);
            if (c1 >= m) return 0; // 격자 범위를 완전히 벗어남
        } 
        else if (dir == 1) { // 원본: 우측 이동 -> 역동작: 좌측 이동
            if (c2 != m - 1) c2 -= dx;
            c1 = max(0LL, c1 - dx);
            if (c2 < 0) return 0;
        } 
        else if (dir == 2) { // 원본: 상향 이동 -> 역동작: 하향 이동
            if (r1 != 0) r1 += dx;
            r2 = min((long long)n - 1, r2 + dx);
            if (r1 >= n) return 0;
        } 
        else if (dir == 3) { // 원본: 하향 이동 -> 역동작: 상향 이동
            if (r2 != n - 1) r2 -= dx;
            r1 = max(0LL, r1 - dx);
            if (r2 < 0) return 0;
        }
    }
    
    // 최종 가능한 영역의 크기 반환
    return (r2 - r1 + 1) * (c2 - c1 + 1);
}