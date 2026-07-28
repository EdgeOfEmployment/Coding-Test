def solution(depth, money, excavate):
    ## 처음에는 단순 이분 탐색(Binary Search)으로 중앙을 굴착하려 했음
    ## 그러나 각 열마다 굴착 비용(depth[i])이 다르고, 중앙을 자르는 것이 최악의 비용을 최소화하지 못함 -> 시간초과 및 실패

    ## 여기서 중요한 점
    ## Minimax DP를 활용하여 구간 [i, j]에서 최악의 비용을 최소화하는 최적의 굴착 위치 opt[i][j]를 사전 계산
    ## dp[i][j] = i~j 구간에서 보물을 확정하는 최악의 최소 굴착 비용

    ## 결국 고민해야 하는 건
    ## 1) 구간 DP로 dp[i][j]와 opt[i][j] 테이블 구축
    ## 2) excavate 인터랙티브 반환값(0: 정답, -1: 왼쪽, 1: 오른쪽)에 따라 굴착 구간 [left, right] 갱신하며 보물 위치 반환

    n = len(depth)
    
    # 1-indexed DP 테이블 (1 <= i <= j <= n)
    dp = [[0] * (n + 2) for _ in range(n + 2)]
    opt = [[0] * (n + 2) for _ in range(n + 2)]

    # 길이 len_sz = 1 ~ n 구간 DP
    for len_sz in range(1, n + 1):
        for i in range(1, n - len_sz + 2):
            j = i + len_sz - 1
            if i == j:
                dp[i][j] = depth[i - 1]
                opt[i][j] = i
            else:
                min_cost = float('inf')
                best_k = i
                for k in range(i, j + 1):
                    left_cost = dp[i][k - 1] if k - 1 >= i else 0
                    right_cost = dp[k + 1][j] if k + 1 <= j else 0
                    worst = depth[k - 1] + max(left_cost, right_cost)
                    if worst < min_cost:
                        min_cost = worst
                        best_k = k
                dp[i][j] = min_cost
                opt[i][j] = best_k

    # 굴착 진행
    left = 1
    right = n
    while left <= right:
        k = opt[left][right]
        res = excavate(k)
        if res == 0:
            return k
        elif res == -1:
            right = k - 1
        else:
            left = k + 1

    return 0