# 기차 선로 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468381)

## 1. 처음 접근법과 시간초과 원인

처음에는 일반적인 BFS/DFS 단순 방문 탐색으로 접근했습니다.

- 단순 위치 `visited[r][c]`  boolean 방문 처리로 경로를 탐색했습니다.
- 이동 방향과 1~7번 각 선로별 진출 방향 맵핑을 정확히 반영하지 못했습니다.

다만 이 문제는 다음 특수 조건을 만족해야 합니다.
1. 기차가 이동하는 진입 방향(`direction`)에 따라 빈칸에 놓을 수 있는 선로 조각 및 나가는 다음 방향이 달라집니다.
2. 기존 `grid`에 미리 배치된 1~7번 선로들을 최소 1번 이상 지나야 합니다.
3. 특히 3번 선로(# 모양 교차로)는 가로 방향과 세로 방향을 모두 통과해야 하므로 **정확히 2번 방문**해야 합니다.

단순 위치 방문 체크만으로는 3번 선로의 2회 방문 및 선로 간의 정밀한 방향 연결 구조를 다루지 못해 오답이 발생하게 됩니다.

## 2. 처음 작성한 코드

```python
from collections import deque

def solution(grid):
    ## 단순 BFS 접근법
    ## 선로별 진입/진출 방향 맵핑 및 3번 선로 2회 방문 검증 미흡으로 오답 발생

    n = len(grid)
    m = len(grid[0])
    visited = [[False] * m for _ in range(n)]

    queue = deque([(0, 0)])
    visited[0][0] = True
    answer = 0

    while queue:
        r, c = queue.popleft()
        if r == n - 1 and c == m - 1:
            answer += 1
            continue

        for dr, dc in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
            nr, nc = r + dr, c + dc
            if 0 <= nr < n and 0 <= nc < m and not visited[nr][nc]:
                visited[nr][nc] = True
                queue.append((nr, nc))

    return answer
```

## 3. 개선한 접근법

방향별 선로 배치 및 진출 방향 맵핑 함수, 그리고 백트래킹 검증 함수를 도입하여 개선했습니다.

- **방향 정의**: `0: 위`, `1: 오른쪽`, `2: 아래`, `3: 왼쪽`으로 진행 방향을 정의합니다.
- **`get_tracks(direction)`**: 빈칸 진입 시 설치 가능한 선로 후보 목록을 반환합니다.
- **`get_next_direction(track, direction)`**: 1~7번 각 선로에 진입했을 때 빠져나가는 다음 진행 방향을 반환합니다. (연결 불가능 시 `-1`)
- **`validate(direction)`**: 목적지 $(N-1, M-1)$ 도착 시, 기존 선로들이 1번 이상 방문되었는지, 3번 선로가 정확히 2번 방문되었는지 검증합니다.
- **백트래킹 DFS**: `visited[nr][nc]` 방문 횟수를 1 증가시킨 뒤 다음 칸으로 이동하며, 3번 선로는 최대 2번, 일반 선로/빈칸은 1번까지만 탐색하도록 백트래킹을 수행합니다.

## 4. 해결 코드

```python
def solution(grid):
    n = len(grid)
    m = len(grid[0])

    # 방향
    # 0: 위
    # 1: 오른쪽
    # 2: 아래
    # 3: 왼쪽
    dr = [-1, 0, 1, 0]
    dc = [0, 1, 0, -1]

    # 각 칸을 몇 번 방문했는지
    visited = [[0] * m for _ in range(n)]

    # 시작점은 이미 1번 선로
    visited[0][0] = 1

    answer = 0

    # 현재 방향으로 진입했을 때
    # 빈칸에 설치할 수 있는 선로 종류
    def get_tracks(direction):

        if direction == 0:
            # 위쪽으로 이동해서 들어옴
            return [2, 3, 6, 7]

        if direction == 1:
            # 오른쪽으로 이동해서 들어옴
            return [1, 3, 4, 7]

        if direction == 2:
            # 아래쪽으로 이동해서 들어옴
            return [2, 3, 4, 5]

        # 왼쪽으로 이동해서 들어옴
        return [1, 3, 5, 6]

    # 현재 방향으로 선로에 들어갔을 때
    # 선로를 빠져나가는 방향
    #
    # -1 : 연결되지 않음
    def get_next_direction(track, direction):

        # 1번 선로: ─
        if track == 1:
            if direction == 1 or direction == 3:
                return direction
            return -1

        # 2번 선로: │
        if track == 2:
            if direction == 0 or direction == 2:
                return direction
            return -1

        # 3번 선로: #
        # 모든 방향이 연결되어 있고
        # 기차는 직진한다고 생각한다.
        if track == 3:
            return direction

        # 4번 선로
        if track == 4:
            if direction == 1:
                return 0
            if direction == 2:
                return 3

        # 5번 선로
        if track == 5:
            if direction == 2:
                return 1
            if direction == 3:
                return 0

        # 6번 선로
        if track == 6:
            if direction == 0:
                return 1
            if direction == 3:
                return 2

        # 7번 선로
        if track == 7:
            if direction == 0:
                return 3
            if direction == 1:
                return 2

        return -1

    # 도착했을 때 정답 조건을 만족하는지 검사
    def validate(direction):

        # 목적지 선로와 연결되지 않는다면 실패
        if get_next_direction(grid[n - 1][m - 1], direction) == -1:
            return False

        for r in range(n):
            for c in range(m):

                # 설치된 선로인데 한 번도 지나지 않았다면 실패
                if grid[r][c] > 0 and visited[r][c] == 0:
                    return False

                # 3번 선로는 가로, 세로 방향을 모두 지나야 한다.
                # 따라서 정확히 2번 방문해야 한다.
                if grid[r][c] == 3 and visited[r][c] != 2:
                    return False

        return True

    def dfs(r, c, direction):

        nonlocal answer

        # 목적지 도착
        if r == n - 1 and c == m - 1:

            if validate(direction):
                answer += 1

            return

        # 다음 칸
        nr = r + dr[direction]
        nc = c + dc[direction]

        # 격자 밖
        if nr < 0 or nr >= n or nc < 0 or nc >= m:
            return

        # 장애물
        if grid[nr][nc] == -1:
            return

        # 3번 선로는 최대 2번까지만 방문 가능
        if visited[nr][nc] > 1 and grid[nr][nc] == 3:
            return

        # 이미 선로가 있는 경우
        if grid[nr][nc] > 0:
            tracks = [grid[nr][nc]]

        # 빈칸인 경우
        else:
            tracks = get_tracks(direction)

        # 가능한 선로를 하나씩 시도
        for track in tracks:

            # 현재 선로를 통과할 수 있는지 확인
            next_direction = get_next_direction(
                track,
                direction
            )

            # 연결되지 않는 선로
            if next_direction == -1:
                continue

            # 기존 값 저장
            original_track = grid[nr][nc]

            # 선로 설치
            grid[nr][nc] = track

            # 방문 횟수 증가
            visited[nr][nc] += 1

            # 다음 칸으로 이동
            dfs(
                nr,
                nc,
                next_direction
            )

            # 백트래킹
            visited[nr][nc] -= 1

            # 원래 상태 복구
            grid[nr][nc] = original_track

    # (0, 0)에서 오른쪽 방향으로 출발
    dfs(0, 0, 1)

    return answer
```

## 5. 구현 전략 및 이유

### 방향 기준 헬퍼 함수 (`get_tracks`, `get_next_direction`)
현재 기차가 진입하는 방향(`direction`)에 따라 선택 가능한 선로 후보와 진출 방향을 명시적 함수로 분리하여 복잡한 방향 조건 판단을 직관적이고 오류 없이 처리하도록 모듈화했습니다.

### 백트래킹 복구 및 `visited` 카운트 관리
빈칸에 시도한 선로 조각을 `grid[nr][nc] = track`으로 직접 갱신한 뒤, 백트래킹 시 `original_track`으로 원복함으로써 메모리 추가 할당 없이 최적의 상태 탐색을 유지합니다.

### `validate` 도달 검증
목적지 도달 시 `visited[r][c]` 횟수를 확인하여 미리 깔려 있던 1~7번 선로가 최소 1번 통과되었는지, 3번 선로가 정확히 가로/세로 2회 통과되었는지를 일괄 검증합니다.

### 시간복잡도
- 시간복잡도: 백트래킹 가지치기 적용으로 제한시간 내 빠른 수행.
- 공간복잡도: $O(N \times M)$
