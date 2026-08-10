# 발전소 회로 복구 문제 풀이 공유

## 1. 문제 접근법

- **문제 구조 파악**: 기술자는 `k`개(≤ 15)의 패널을 안전 순서(선행 조건)를 지키며 모두 활성화해야 하고, 목표는 총 이동 시간의 최소화입니다. 이는 "선행 제약이 있는 외판원 문제(TSP with precedence constraints)"의 전형적인 구조이며, `k ≤ 15`라는 제약이 비트마스크 DP를 강하게 암시합니다.

- **이동 비용 사전 계산**: 패널들 사이의 실제 최단 이동 시간을 미리 모두 구해두면, 이후에는 "이미 활성화한 패널 집합"과 "현재 위치한 패널"만으로 상태를 정의해 DP를 돌릴 수 있습니다.

- **층 구조 활용**: 모든 층이 동일한 격자 구조를 갖고, 엘리베이터도 층마다 같은 좌표에 있다는 조건 덕분에, 패널 간 이동 시간을 다음과 같이 단순화할 수 있습니다.
  - 같은 층: 격자 위에서의 BFS 최단 거리
  - 다른 층: (출발 패널 → 엘리베이터 BFS 거리) + `|층 차이|` + (엘리베이터 → 도착 패널 BFS 거리)
  - 층을 여러 번 오가는 것보다 한 번에 목표 층으로 이동하는 것이 삼각부등식에 의해 항상 최적이므로 위 공식이 곧 최단 시간입니다.

- **비트마스크 DP**: `dp[mask][last]` = 활성화된 패널 집합이 `mask`이고 현재 위치가 `last` 패널일 때의 최소 누적 시간으로 정의하고, 안전 순서 제약(선행 패널이 모두 `mask`에 포함되어야 함)을 만족하는 다음 패널로만 전이합니다.

## 2. 해결 코드

```python
from collections import deque


def solution(h, grid, panels, seqs):
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

    prereq_mask = [0] * k
    for a, b in seqs:
        prereq_mask[b - 1] |= (1 << (a - 1))

    full = (1 << k) - 1
    INF = float('inf')
    dp = [[INF] * k for _ in range(1 << k)]
    dp[0][0] = 0  # 시작 위치 = 1번 패널의 좌표, 아직 아무것도 활성화하지 않음

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
```

## 3. 구현 전략 및 이유

### 시작 상태를 "1번 패널의 위치"로 초기화

기술자는 항상 1번 패널의 위치에서 출발하지만, 안전 순서 때문에 도착하자마자 1번 패널을 활성화하지 못할 수도 있습니다 (예시처럼 선행 패널이 필요한 경우). 따라서 `dp[0][0] = 0`으로 두어 "패널 0의 좌표에 서 있지만 아직 아무것도 활성화하지 않은" 상태를 명시적으로 표현했습니다. `last` 인덱스는 활성화 여부와 무관하게 순수히 "현재 위치"만을 의미합니다.

### 패널 간 이동 시간을 BFS로 사전 계산

층 구조가 모두 동일하다는 조건을 이용해, 각 패널에서 한 번의 BFS만으로 (a) 엘리베이터까지의 거리와 (b) 같은 층에 있는 다른 모든 패널까지의 거리를 동시에 구했습니다. `k ≤ 15`이므로 BFS를 최대 15번만 수행하면 모든 패널 쌍의 이동 시간을 얻을 수 있어 효율적입니다.

### 비트마스크 DP로 순서 탐색

`k ≤ 15`라는 제약은 상태 수가 `2^15 × 15 ≈ 49만` 수준이라는 뜻이므로, 모든 활성화 순서를 완전탐색하지 않고 "이미 활성화한 집합 + 현재 위치"라는 압축된 상태로 최적 부분 구조를 이용해 풀 수 있습니다. 각 전이마다 안전 순서 제약(`prereq_mask[p] & mask == prereq_mask[p]`)을 검사해 아직 선행 조건이 충족되지 않은 패널로는 전이하지 않도록 가지치기했습니다.
