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