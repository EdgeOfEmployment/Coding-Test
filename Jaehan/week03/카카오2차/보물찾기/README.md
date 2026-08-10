# 보물 찾기 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468378)

## 1. 처음 접근법과 시간초과 원인

처음에는 단순히 2차원 이분 탐색(Binary Search)으로 접근했습니다.

- 구간 $[1, N]$에서 항상 중앙 위치를 굴착하여 탐색 영역을 절반으로 줄여나가는 방식을 생각했습니다.
- 인터랙티브 함수 `excavate(col)`을 중앙 위치에 매번 호출하려고 했습니다.

하지만 각 열마다 굴착하는 데 드는 비용 `depth[i]`가 상이하며, 무조건 중앙을 자르는 방식은 최악의 경우 총 비용 제한 `money`를 초과하거나 오답을 내게 됩니다. 굴착 비용과 최악 시나리오의 탐색 비용을 고려한 최적의 굴착 위치 선택 전략이 필요합니다.

## 2. 처음 작성한 코드

```python
def solution(depth, money, excavate):
    ## 단순 중앙 이분 탐색 접근법
    ## 각 열의 depth[i] 비용 차이를 고려하지 못해 실패

    left = 1
    right = len(depth)
    while left <= right:
        mid = (left + right) // 2
        res = excavate(mid)
        if res == 0:
            return mid
        elif res == -1:
            right = mid - 1
        else:
            left = mid + 1
    return 0
```

## 3. 개선한 접근법

구간 DP를 이용한 **Minimax (미니맥스 최댓값의 최솟값)** 전략을 사용합니다.

- **상태 정의**: `dp[i][j]` = 구간 $[i, j]$ 내에 보물이 있다고 판단될 때, 보물의 위치를 확실하게 찾아내는 데 드는 최악의 최소 비용.
- **최적 굴착 위치 결정 (`opt[i][j]`)**:
  - $[i, j]$ 구간에서 $k$번째 열을 먼저 파기로 결정했을 때:
  - 보물이 $k$보다 왼쪽($[i, k-1]$)에 있을 경우 비용: `dp[i][k-1]`
  - 보물이 $k$보다 오른쪽($[k+1, j]$)에 있을 경우 비용: `dp[k+1][j]`
  - $k$를 굴착하는 최악 비용: $depth[k-1] + \max(dp[i][k-1], dp[k+1][j])$
  - 이 중 최악 비용을 최소화하는 위치 $k$를 `opt[i][j]`에 기록합니다.
- **실행**: 완성된 `opt` 테이블을 참조하여 `left`와 `right`를 갱신하며 `excavate(k)`를 호출하여 보물 위치를 반환합니다.

## 4. 해결 코드

```python
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
```

## 5. 구현 전략 및 이유

### Minimax DP 최적화
각 열마다 다르게 설정된 굴착 비용 `depth[i]` 하에서 최악 시나리오의 탐색 비용을 최소화하도록 점화식을 설계했습니다.

### 인터랙티브 함수 연동
전처리된 `opt[left][right]` 테이블을 바탕으로 `excavate`를 최소 호출 횟수 및 최소 비용으로 정답 위치를 찾습니다.

### 시간복잡도
- 구간 DP 테이블 생성: $O(N^3)$ (전처리)
- 굴착 진행: 최대 $O(N)$
- 제한시간 및 메모리 조건 내에 빠르게 수행됩니다.
