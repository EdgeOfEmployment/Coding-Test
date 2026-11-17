#include <vector>
#include <algorithm>

using namespace std;

class Solution {
public:
    int m, n;

    void dfs(vector<vector<int>>& grid, int r, int c){
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] == 0) {
            return;
        }

        // 방문 처리
        grid[r][c] = 0;

        // 상하좌우 4방향 탐색
        dfs(grid, r - 1, c);
        dfs(grid, r + 1, c);
        dfs(grid, r, c - 1);
        dfs(grid, r, c + 1);
    }

    int numEnclaves(std::vector<std::vector<int>>& grid) {
        m = grid.size();
        n = grid[0].size();
        
        // 1. 좌우 경계(첫 번째 열, 마지막 열)에서 연결된 육지 지우기
        for (int i = 0; i < m; i++) {
            if (grid[i][0] == 1) dfs(grid, i, 0);
            if (grid[i][n - 1] == 1) dfs(grid, i, n - 1);
        }
        
        // 2. 상하 경계(첫 번째 행, 마지막 행)에서 연결된 육지 지우기
        for (int j = 0; j < n; j++) {
            if (grid[0][j] == 1) dfs(grid, 0, j);
            if (grid[m - 1][j] == 1) dfs(grid, m - 1, j);
        }
        
        // 3. 격자 내부에 남아 있는 고립된 육지(1) 개수 세기
        int enclaves = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    enclaves++;
                }
            }
        }
        
        return enclaves;
    }
};