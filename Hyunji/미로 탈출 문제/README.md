# 미로 탈출 문제 풀이 공유

[문제 링크: 프로그래머스 159993 미로 탈출](https://school.programmers.co.kr/learn/courses/30/lessons/159993)

## 1. 접근법과 단순 풀이의 한계

미로에서 `S`는 시작점, `L`은 레버, `E`는 출구입니다. 출구에 도착하기 전에 반드시 레버를 당겨야 하므로 이동 순서는 **S → L → E**입니다. 한 칸을 이동하는 비용은 모두 1초이므로 각 구간의 최단거리를 구해야 합니다. 레버를 당기기 전에도 출구 칸을 지나갈 수 있습니다.

- **DFS의 한계:** 처음 목적지에 도착한 경로가 최단 경로라고 보장할 수 없습니다. 모든 경로를 확인할 수도 있지만, 이동 비용이 같은 격자 최단거리에는 BFS가 적합합니다.
- **S → E만 탐색하는 방법의 한계:** 레버를 반드시 거쳐야 하므로 S → L과 L → E가 모두 가능한지 확인해야 합니다.
- **레버 상태를 BFS 상태로 관리하는 방법:** `(행, 열, 레버 여부)`를 상태로 두고 한 번에 탐색할 수도 있습니다. 레버가 하나이므로 두 구간으로 나누면 구현이 간단합니다.

따라서 S → L과 L → E를 각각 BFS로 탐색합니다. 어느 한 구간이라도 도달할 수 없으면 `-1`, 모두 가능하면 두 거리를 더합니다.

## 2. 해결 코드

```python
from collections import deque


def solution(maps):
    rows = len(maps)
    cols = len(maps[0])

    start = None
    lever = None
    end = None

    for r in range(rows):
        for c in range(cols):
            if maps[r][c] == 'S':
                start = (r, c)
            elif maps[r][c] == 'L':
                lever = (r, c)
            elif maps[r][c] == 'E':
                end = (r, c)

    def bfs(start, target):
        queue = deque([(start[0], start[1], 0)])
        visited = [[False] * cols for _ in range(rows)]
        visited[start[0]][start[1]] = True

        dr = [-1, 1, 0, 0]
        dc = [0, 0, -1, 1]

        while queue:
            r, c, dist = queue.popleft()

            if (r, c) == target:
                return dist

            for i in range(4):
                nr = r + dr[i]
                nc = c + dc[i]

                if not (0 <= nr < rows and 0 <= nc < cols):
                    continue
                if maps[nr][nc] == 'X' or visited[nr][nc]:
                    continue

                visited[nr][nc] = True
                queue.append((nr, nc, dist + 1))

        return -1

    to_lever = bfs(start, lever)
    if to_lever == -1:
        return -1

    to_end = bfs(lever, end)
    if to_end == -1:
        return -1

    return to_lever + to_end
```

실행할 코드는 [미로 탈출 문제.py](./미로%20탈출%20문제.py)에 있습니다.

## 3. 구현 전략 및 이유

### BFS를 두 번 실행하는 이유

BFS는 거리 0, 1, 2, … 순서로 칸을 탐색합니다. 따라서 목적지에 처음 도착한 순간의 거리가 최단거리입니다. 최종 목적지는 E이지만 먼저 L을 거쳐야 하므로 S → L과 L → E를 따로 탐색합니다.

두 탐색은 `visited`를 공유하지 않습니다. S → L에서 지나온 칸을 L → E에서 다시 지나갈 수 있기 때문입니다. `bfs()`를 호출할 때마다 방문 배열을 새로 만듭니다.

### 큐와 이동 처리

큐에는 `(행, 열, 현재까지의 거리)`를 넣고, 한 칸 이동할 때 거리를 1 늘립니다. 새 칸은 큐에 넣는 즉시 방문 처리하여 같은 칸이 중복으로 들어가지 않도록 합니다. 이동할 수 없는 칸은 `X`뿐이며 `S`, `L`, `E`, `O`는 모두 통과할 수 있습니다.

### 복잡도

행 수를 R, 열 수를 C라고 하면 각 BFS에서 칸을 최대 한 번씩 방문합니다. BFS를 두 번 실행해도 시간 복잡도는 **O(R × C)**이고, 방문 배열과 큐의 공간 복잡도도 **O(R × C)**입니다.

> 격자에서 모든 이동 비용이 같고 최소 이동 횟수를 구한다면 BFS를 떠올립니다. 반드시 거쳐야 할 지점이 있다면 구간을 나누어 최단거리를 구할 수 있습니다.
