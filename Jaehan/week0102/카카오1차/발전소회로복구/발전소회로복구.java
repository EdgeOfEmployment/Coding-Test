import java.util.*;

class Solution {
    // 1. 모든 층이 완전히 같은 구조(같은 격자, 같은 엘리베이터 위치) -> 패널 간 이동 거리를
    //    "같은 층이면 격자 BFS", "다른 층이면 (내 층 엘리베이터까지 거리) + |층차| + (상대 엘리베이터까지 거리)"
    //    로 미리 다 구해놓고 시작할 수 있음
    // 2. k(패널 개수) <= 15 -> 선행 제약 있는 외판원 문제 = 비트마스크 DP 확정
    // 3. dp[mask][last] = mask(활성화한 패널 집합) 상태에서 현재 위치가 last 패널일 때 최소 시간
    // 4. 기술자는 항상 1번 패널 "위치"에서 출발하지만, 선행 조건 때문에 도착하자마자
    //    1번 패널을 못 켤 수도 있음 -> dp[0][0] = 0 으로 시작 ("아직 아무것도 안 켰지만 위치는 패널0")
    public int solution(int h, String[] grid, int[][] panels, int[][] seqs) {
        int n = grid.length;
        int m = grid[0].length();
        int k = panels.length;

        int er = -1, ec = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i].charAt(j) == '@') {
                    er = i;
                    ec = j;
                }
            }
        }

        int[][] panelPos = new int[k][3]; // floor, row, col (0-indexed)
        for (int i = 0; i < k; i++) {
            panelPos[i][0] = panels[i][0] - 1;
            panelPos[i][1] = panels[i][1] - 1;
            panelPos[i][2] = panels[i][2] - 1;
        }

        int[] elevDist = new int[k];
        int[][] gridDist = new int[k][k];

        for (int i = 0; i < k; i++) {
            int r = panelPos[i][1];
            int c = panelPos[i][2];
            int[][] dist = bfs(grid, n, m, r, c);
            elevDist[i] = dist[er][ec];
            for (int j = 0; j < k; j++) {
                gridDist[i][j] = dist[panelPos[j][1]][panelPos[j][2]];
            }
        }

        // prereqMask[p] = p번 패널을 켜기 전에 반드시 켜져 있어야 하는 패널들의 비트마스크
        int[] prereqMask = new int[k];
        for (int[] seq : seqs) {
            int a = seq[0], b = seq[1];
            prereqMask[b - 1] |= (1 << (a - 1));
        }

        int full = (1 << k) - 1;
        final int INF = Integer.MAX_VALUE / 2;
        int[][] dp = new int[1 << k][k];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[0][0] = 0;

        for (int mask = 0; mask < (1 << k); mask++) {
            for (int last = 0; last < k; last++) {
                if (mask == 0) {
                    if (last != 0) {
                        continue;
                    }
                } else if ((mask & (1 << last)) == 0) {
                    continue;
                }

                int cur = dp[mask][last];
                if (cur == INF) {
                    continue;
                }

                for (int p = 0; p < k; p++) {
                    if ((mask & (1 << p)) != 0) {
                        continue;
                    }
                    // p의 선행 패널이 전부 mask에 포함돼 있어야만(안전 순서 충족) 다음으로 켤 수 있음
                    if ((prereqMask[p] & mask) != prereqMask[p]) {
                        continue;
                    }
                    int nmask = mask | (1 << p);
                    int cost = travel(panelPos, gridDist, elevDist, last, p);
                    int ncost = cur + cost;
                    if (ncost < dp[nmask][p]) {
                        dp[nmask][p] = ncost;
                    }
                }
            }
        }

        int answer = INF;
        for (int last = 0; last < k; last++) {
            answer = Math.min(answer, dp[full][last]);
        }
        return answer;
    }

    private int travel(int[][] panelPos, int[][] gridDist, int[] elevDist, int i, int j) {
        int fi = panelPos[i][0];
        int fj = panelPos[j][0];
        if (fi == fj) {
            return gridDist[i][j];
        }
        return elevDist[i] + Math.abs(fi - fj) + elevDist[j];
    }

    private int[][] bfs(String[] grid, int n, int m, int sr, int sc) {
        int[][] dist = new int[n][m];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }
        dist[sr][sc] = 0;
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sr, sc});
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];
            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];
                if (nx >= 0 && nx < n && ny >= 0 && ny < m
                        && grid[nx].charAt(ny) != '#' && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[x][y] + 1;
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return dist;
    }
}
