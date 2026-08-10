# 힌트 스테이지 문제 풀이 공유

## 1. 처음 접근법과 시간초과 원인

처음에는 재귀 + 메모이제이션으로 접근했습니다.

- 상태를 `(현재 스테이지, 각 스테이지별 힌트권 보유 개수)`로 정의했습니다.
- 현재 스테이지에서 힌트권을 `0장 ~ 보유한 최대 장수`만큼 사용하는 모든 경우를 탐색했습니다.
- 힌트 번들을 구매하는 경우와 구매하지 않는 경우를 각각 재귀 호출해 최솟값을 갱신했습니다.

다만 힌트권 보유 개수 전체를 튜플로 상태에 넣으면, 스테이지마다 가질 수 있는 힌트권 조합이 너무 많아집니다. `n ≤ 16`이어도 상태 수가 급격히 증가해 시간초과가 발생합니다.

또한 `cost[i][j] > cost[i][j + 1]`이므로, 힌트권을 많이 쓸수록 비용은 항상 감소합니다. 즉, 힌트권을 가지고 있다면 적게 쓸 이유가 없어서 “몇 장을 사용할지”를 전부 탐색할 필요가 없습니다.

## 2. 처음 작성한 코드

```python
def solution(cost, hint):
    ## 스테이지#1은 무조건 힌트권 없이 수행해야함
    ## 각 스테이지별로 해당 스테이지 이후에 나오는 스테이지들에 대한 힌트권을 K개 얻을 수 있음
    ## 힌트권 판매가격과 힌트를 썼을 때의 스테이지 해결 비용을 고려해야함
    ## ㄴ> 재귀+DP (힌트권을 쓰거나 쓰지 않거나로) 연산 이후 최솟값 갱신

    n = len(cost)

    # memo[i][cnt] : i번째 스테이지에 도달했을 때, i번 힌트권이 'cnt'장 남아있을 때의 최소비용
    # 최대 힌트권 개수는 n-1이므로 크기를 n으로 설정
    memo = {}

    # 상태 정의 : 현재 스테이지, 현재 보유하고 있는 힌트권의 개수 상태
    def dfs(stage, hints):
        if stage == n:
            return 0

        # 힌트 개수가 n장을 넘을 필요가 없으므로 상한을 n-1로 클램핑하여 상태 개수 축소
        # 튜플 연산 속도를 높이기 위해 미리 리스트 컴프리헨션 사용
        hints = tuple(h if h < n else n - 1 for h in hints)
        state = (stage, hints)

        if state in memo:
            return memo[state]

        min_cost = float('inf')
        max_usable = min(n - 1, hints[stage])

        # 번들 정보 미리 추출
        has_bundle = stage < n - 1
        if has_bundle:
            price = hint[stage][0]
            tickets = hint[stage][1:]

        # 1. 현재 스테이지에서 힌트권을 u장 사용할 경우
        for u in range(max_usable + 1):
            solve_cost = cost[stage][u]

            # 다음 힌트 상태 계산 (리스트 변환 최소화)
            if u == 0:
                base_next = hints
            else:
                base_next = list(hints)
                base_next[stage] -= u
                base_next = tuple(base_next)

            if has_bundle:
                # Case A: 번들 안 살 때
                res_a = solve_cost + dfs(stage + 1, base_next)
                if res_a < min_cost:
                    min_cost = res_a

                # Case B: 번들 살 때
                bought_hints = list(base_next)
                for t in tickets:
                    bought_hints[t - 1] += 1
                res_b = solve_cost + price + dfs(stage + 1, tuple(bought_hints))
                if res_b < min_cost:
                    min_cost = res_b
            else:
                res = solve_cost + dfs(stage + 1, base_next)
                if res < min_cost:
                    min_cost = res

        memo[state] = min_cost
        return min_cost

    return dfs(0, tuple([0]*n))
```

## 3. 개선한 접근법

힌트권은 보유하고 있다면 항상 최대한 사용하는 것이 최적입니다. 따라서 고려해야 할 선택은 다음 하나뿐입니다.

- 각 스테이지에서 힌트 번들을 구매할지 여부

번들을 판매하는 스테이지는 마지막을 제외한 `n-1`개입니다. 따라서 모든 구매 조합은 최대 `2^(n-1)`개이며, `n ≤ 16`이므로 최대 `2^15 = 32768`개만 확인하면 됩니다.

`mask`의 각 비트로 특정 스테이지에서 번들을 구매했는지를 표현합니다.

- `dp[mask]`: `mask`에 포함된 번들을 구매했을 때의 전체 비용
- `cnt[stage][mask]`: 해당 구매 조합에서 `stage` 스테이지에 사용할 수 있는 힌트권 수

이전 구매 조합에 번들 하나를 추가했을 때, 그 번들이 이후 스테이지의 비용을 얼마나 줄이는지만 반영해서 DP 값을 갱신합니다.

## 4. 해결 코드

```python
def solution(cost, hint):
    ## 처음에는 재귀 + DP로
    ## "현재 스테이지, 각 스테이지별 힌트권 보유 개수"를 상태로 잡으려고 했음
    ## 그런데 n이 최대 16이고, 힌트권 개수도 스테이지별로 계속 달라질 수 있어서
    ## 상태 개수가 너무 많이 늘어남 -> 시간초과

    ## 여기서 중요한 점
    ## cost[i][j]는 힌트권을 많이 쓸수록 항상 감소함
    ## ㄴ> 어떤 스테이지에서 힌트권을 가지고 있다면, 남겨둘 이유 없이 무조건 최대한 쓰는 게 이득

    ## 결국 고민해야 하는 건
    ## "현재 스테이지에서 힌트권을 몇 장 쓸까?"가 아니라
    ## "각 스테이지에서 힌트 번들을 살까 / 안 살까?"만 남음

    ## 번들을 판매하는 스테이지는 n-1개
    ## ㄴ> 모든 구매 경우의 수는 최대 2^(n-1) = 2^15개
    ## ㄴ> 비트마스크 DP로 충분히 가능

    n = len(cost)
    bundle_count = n - 1
    size = 1 << bundle_count
    max_hint = n - 1

    ## gain[i][j]
    ## = i번 스테이지에서 번들을 샀을 때
    ##   j번 스테이지에서 얻는 힌트권 개수
    gain = [[0] * n for _ in range(bundle_count)]

    for i in range(bundle_count):
        for ticket in hint[i][1:]:
            gain[i][ticket - 1] += 1

    ## cnt[stage][mask]
    ## = mask에 포함된 번들을 모두 구매했을 때
    ##   stage번 스테이지에서 사용할 수 있는 힌트권 개수
    ##
    ## 힌트권은 최대 n-1장까지만 쓸 수 있으므로
    ## 그 이상은 전부 n-1로 처리해도 됨
    cnt = [bytearray(size) for _ in range(n)]

    ## dp[mask]
    ## = mask에 포함된 번들을 구매했을 때의 전체 해결 비용
    ##
    ## mask의 i번째 비트가 1이면
    ## i번 스테이지에서 판매하는 힌트 번들을 구매했다는 의미
    dp = [0] * size

    ## 아무 번들도 사지 않은 경우
    ## ㄴ> 모든 스테이지에서 힌트권 0장 사용
    dp[0] = sum(cost[i][0] for i in range(n))
    answer = dp[0]

    for mask in range(1, size):
        ## mask에서 새로 추가된 번들 하나만 찾음
        lowbit = mask & -mask
        bought_stage = lowbit.bit_length() - 1

        ## 새 번들을 제외한 이전 구매 상태
        prev_mask = mask ^ lowbit

        ## 이전 상태 비용 + 새로 구매한 번들 가격
        current_cost = dp[prev_mask] + hint[bought_stage][0]

        ## 새 번들이 이후 각 스테이지에 주는 힌트권을 반영
        for stage in range(1, n):
            old_hint_count = cnt[stage][prev_mask]

            new_hint_count = min(
                max_hint,
                old_hint_count + gain[bought_stage][stage]
            )

            cnt[stage][mask] = new_hint_count

            ## 힌트권 개수가 바뀌면서 해당 스테이지 해결 비용도 바뀜
            ## 기존 비용을 빼고, 새 비용을 더해서 갱신
            current_cost += (
                cost[stage][new_hint_count]
                - cost[stage][old_hint_count]
            )

        dp[mask] = current_cost
        answer = min(answer, current_cost)

    return answer
```

## 5. 구현 전략 및 이유

### 힌트권 사용량 탐색 제거

각 스테이지의 해결 비용은 힌트권 사용량이 많을수록 항상 감소합니다. 따라서 가진 힌트권을 일부만 사용하고 남기는 선택은 최적일 수 없으며, 해당 스테이지에서 사용할 수 있는 만큼 모두 사용하면 됩니다.

### 번들 구매 조합을 비트마스크로 표현

힌트권 수 자체를 상태로 저장하는 대신, 번들을 구매한 스테이지 조합만 저장합니다. 구매 여부는 스테이지당 두 가지이고 최대 15개만 결정하면 되므로 상태 수가 최대 32768개로 제한됩니다.

### 비용 변화분만 갱신

`mask`에 새 번들을 하나 추가하면, 그 번들이 제공하는 힌트권으로 인해 이후 스테이지들의 해결 비용만 달라집니다. 이전 조합의 비용에서 번들 가격을 더하고, 바뀐 스테이지 비용 차이만 더하는 방식으로 계산합니다.

### 시간복잡도

각 구매 조합마다 최대 `n`개 스테이지를 확인합니다.

- 시간복잡도: `O(n × 2^(n-1))`
- 공간복잡도: `O(n × 2^(n-1))`

`n ≤ 16`이므로 충분히 시간 내에 처리할 수 있습니다.
