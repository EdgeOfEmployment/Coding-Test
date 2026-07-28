# 선인장 숨기기 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468379?language=python3)

## 1. 처음 접근법과 시간초과 원인

처음에는 완전 탐색(Brute Force)으로 접근했습니다.

- $M \times N$ 격자의 모든 가능한 $H \times W$ 부분 영역의 좌상단 위치 $(r, c)$를 탐색합니다.
- 각 위치에서 $H \times W$ 크기 내부의 모든 칸을 순회하며 비를 맞는 시각의 최솟값을 구했습니다.
- 동률 조건(최솟값이 같을 때 상단/좌측 우선순위) 처리가 미흡했거나 완전 탐색 시 $O(M \times N \times H \times W)$가 되어 시간초과가 발생했습니다.

## 2. 처음 작성한 코드

```python
def solution(m, n, h, w, drops):
    ## 모든 (r, c) 좌상단 위치에 대해 H x W 영역을 전부 조사
    ## 시간복잡도 O(M * N * H * W) -> 시간초과 발생

    k = len(drops)
    inf = k + 1
    time = [[inf] * n for _ in range(m)]
    for idx, (r, c) in enumerate(drops):
        if time[r][c] == inf:
            time[r][c] = idx + 1

    max_rain_time = -1
    answer = [0, 0]

    for r in range(m - h + 1):
        for c in range(n - w + 1):
            min_val = inf
            for i in range(r, r + h):
                for j in range(c, c + w):
                    if time[i][j] < min_val:
                        min_val = time[i][j]

            if min_val > max_rain_time:
                max_rain_time = min_val
                answer = [r, c]

    return answer
```

## 3. 개선한 접근법

슬라이딩 윈도우와 **단조 덱(Monotonic Deque)**을 활용하여 2차원 구간 최솟값을 $O(M \times N)$ 시간에 구하도록 개선합니다.

- **전처리 (`drops`)**: 각 칸 $(r, c)$에 비가 최초로 떨어지는 1-based 시각 `time[r][c]`를 구축합니다. ($INF = len(drops)+1$)
- **1단계 (가로 윈도우)**: 각 행에 대해 크기 $W$의 슬라이딩 윈도우를 적용하여 $[c, c+W-1]$ 구간의 최솟값 `row_min`을 단조 덱으로 구합니다. ($O(M \times N)$)
- **2단계 (세로 윈도우)**: `row_min` 배열에 대해 각 열마다 크기 $H$의 슬라이딩 윈도우를 적용하여 영역별 최솟값 `col_min`을 구합니다. ($O(M \times N)$)
- **우선순위 보장**: `r` (0..m-h) $\to$ `c` (0..n-w) 행 우선 순서로 순회하면서 `val > max_val` 일 때만 갱신하여 동률 발생 시 가장 위쪽, 그 다음으로 가장 왼쪽 위치 $[r, c]$가 선택되도록 보장합니다.

## 4. 해결 코드

```python
from collections import deque

def solution(m, n, h, w, drops):
    ## 처음에는 4중 for문 완전 탐색으로 접근하려 했으나
    ## M, N이 최대 1000일 때 H * W 연산이 중복 수행되어 O(M * N * H * W) -> 시간 초과

    ## 여기서 중요한 점
    ## 2차원 영역의 최솟값 계산은 1차원 단조 덱(Monotonic Deque) 슬라이딩 윈도우를
    ## 가로(W 크기) -> 세로(H 크기) 2단계로 연속 적용하면 O(M * N)에 해결 가능
    ## 동률 발생 시 가장 위쪽 행, 그 다음 가장 왼쪽 열 좌표를 선택해야 하므로
    ## r (0..m-h) -> c (0..n-w) 행 우선 순서로 순회하며 val > max_val 일 때만 갱신

    ## 결국 고민해야 하는 건
    ## 1) drops를 기반으로 각 격자 칸 (r, c)에 비가 최초로 떨어지는 시각 time[r][c] 전처리 (비가 안 내리면 INF)
    ## 2) 행별 가로 슬라이딩 윈도우(W)로 row_min 배열 구축
    ## 3) 열별 세로 슬라이딩 윈도우(H)로 rect_min 최솟값 계산 후 행 우선 순회로 최댓값을 보장하는 좌상단 [r, c] 좌표 추적

    k = len(drops)
    inf = k + 1

    # 1. 빗방울 떨어지는 최초 시각 (1-based) 기록
    time = [[inf] * n for _ in range(m)]
    for idx, (r, c) in enumerate(drops):
        if time[r][c] == inf:
            time[r][c] = idx + 1

    # 2. 가로 슬라이딩 윈도우 (크기 w) -> row_min[r][c]
    cols_w = n - w + 1
    row_min = [[0] * cols_w for _ in range(m)]
    for r in range(m):
        dq = deque()
        row = time[r]
        for c in range(n):
            while dq and row[dq[-1]] >= row[c]:
                dq.pop()
            dq.append(c)
            if dq[0] <= c - w:
                dq.popleft()
            if c >= w - 1:
                row_min[r][c - w + 1] = row[dq[0]]

    # 3. 세로 슬라이딩 윈도우 (크기 h) -> [r, c] 선택
    # r (행) outer loop, c (열) inner loop 순서로 탐색하여 동률 시 가장 위쪽/왼쪽 유지
    rows_h = m - h + 1
    max_val = -1
    best_pos = [0, 0]

    # 각 열별 세로 슬라이딩 윈도우 결과를 col_min[r][c]에 저장
    col_min = [[0] * cols_w for _ in range(rows_h)]
    for c in range(cols_w):
        dq = deque()
        for r in range(m):
            while dq and row_min[dq[-1]][c] >= row_min[r][c]:
                dq.pop()
            dq.append(r)
            if dq[0] <= r - h:
                dq.popleft()
            if r >= h - 1:
                col_min[r - h + 1][c] = row_min[dq[0]][c]

    # 행 우선 순회하여 최댓값과 좌표 [r, c] 선정
    for r in range(rows_h):
        for c in range(cols_w):
            val = col_min[r][c]
            if val > max_val:
                max_val = val
                best_pos = [r, c]

    return best_pos
```

## 5. 구현 전략 및 이유

### 동률 좌표 선택 보장
`col_min` 결과 생성 후 `r` outer, `c` inner loop로 순회하며 strictly greater (`val > max_val`) 조건으로만 갱신하여 동률 위치 발생 시 상단/좌측 우선순위를 보장합니다.

### 2차원 슬라이딩 윈도우 단조 덱
차원별 슬라이딩 윈도우 단조 덱을 사용하여 $O(M \times N)$ 시간에 효율적으로 정답을 구합니다.

### 시간복잡도
- 시간복잡도: $O(M \times N)$
- 공간복잡도: $O(M \times N)$
