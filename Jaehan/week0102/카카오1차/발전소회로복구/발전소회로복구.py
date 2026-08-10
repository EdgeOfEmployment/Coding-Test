from collections import deque


def solution(h, grid, panels, seqs):
    ## 1. 모든 층이 완전히 같은 구조(같은 격자, 같은 엘리베이터 위치) -> 패널 간 이동 거리를
    ##    "같은 층이면 격자 BFS", "다른 층이면 (내 층 엘리베이터까지 거리) + |층차| + (상대 엘리베이터까지 거리)"
    ##    로 미리 다 구해놓고 시작할 수 있음
    ## 2. k(패널 개수) <= 15 -> 선행 제약 있는 외판원 문제 = 비트마스크 DP 확정
    ## 3. dp[mask][last] = mask(활성화한 패널 집합) 상태에서 현재 위치가 last 패널일 때 최소 시간
    ## 4. 기술자는 항상 1번 패널 "위치"에서 출발하지만, 선행 조건 때문에 도착하자마자
    ##    1번 패널을 못 켤 수도 있음 -> dp[0][0] = 0 으로 시작 ("아직 아무것도 안 켰지만 위치는 패널0")
    n = len(grid)
    m = len(grid[0])
    k = len(panels)

    er, ec = -1, -1
    for i in range(n):
        for j in range(m):
            if grid[i][j] == '@':
                er, ec = i, j

    panel_pos = [(f - 1, r - 1, c - 1) for f, r, c in panels]

    elev_dist = [0] * k
    grid_dist = [[0] * k for _ in range(k)]

    for i in range(k):
        _, r, c = panel_pos[i]
        dist = _bfs(grid, n, m, r, c)
        elev_dist[i] = dist[er][ec]
        for j in range(k):
            _, rj, cj = panel_pos[j]
            grid_dist[i][j] = dist[rj][cj]

    def travel(i, j):
        fi = panel_pos[i][0]
        fj = panel_pos[j][0]
        if fi == fj:
            return grid_dist[i][j]
        return elev_dist[i] + abs(fi - fj) + elev_dist[j]

    # prereq_mask[p] = p번 패널을 켜기 전에 반드시 켜져 있어야 하는 패널들의 비트마스크
    prereq_mask = [0] * k
    for a, b in seqs:
        prereq_mask[b - 1] |= (1 << (a - 1))

    full = (1 << k) - 1
    INF = float('inf')
    dp = [[INF] * k for _ in range(1 << k)]
    dp[0][0] = 0  # start position = panel index 0's location, nothing activated yet

    for mask in range(1 << k):
        for last in range(k):
            if mask == 0:
                if last != 0:
                    continue
            elif not (mask & (1 << last)):
                continue

            cur = dp[mask][last]
            if cur == INF:
                continue

            for p in range(k):
                if mask & (1 << p):
                    continue
                # p의 선행 패널이 전부 mask에 포함돼 있어야만(안전 순서 충족) 다음으로 켤 수 있음
                if (prereq_mask[p] & mask) != prereq_mask[p]:
                    continue
                nmask = mask | (1 << p)
                ncost = cur + travel(last, p)
                if ncost < dp[nmask][p]:
                    dp[nmask][p] = ncost

    return min(dp[full])


def _bfs(grid, n, m, sr, sc):
    dist = [[-1] * m for _ in range(n)]
    dist[sr][sc] = 0
    q = deque([(sr, sc)])
    while q:
        x, y = q.popleft()
        for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nx, ny = x + dx, y + dy
            if 0 <= nx < n and 0 <= ny < m and grid[nx][ny] != '#' and dist[nx][ny] == -1:
                dist[nx][ny] = dist[x][y] + 1
                q.append((nx, ny))
    return dist
