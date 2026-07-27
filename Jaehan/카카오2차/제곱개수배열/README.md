# 제곱 개수 배열 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468380)

## 1. 처음 접근법과 시간초과 원인

처음에는 0-based 인덱스 기준으로 잘못 생각하거나 원본 배열 `arr`를 직접 확장하려고 했습니다.

- 0-based 인덱스를 사용하여 1-based 기반 프로그래머스 입력 `[l, r]` 범위 $K$와 $C$ 값이 실제 테스트 케이스 기댓값과 다르게 나왔습니다.

## 2. 처음 작성한 코드

```python
def solution(arr, l, r):
    ## 0-based 인덱스 적용 오차로 테스트케이스 1번에서 [9, 4]가 반환됨 (기댓값 [8, 2])

    brr = []
    for x in arr:
        brr.extend([x] * x)

    k = sum(brr[l:r+1])
    win_len = r - l + 1
    c = 0
    for s in range(len(brr) - win_len + 1):
        if sum(brr[s:s+win_len]) == k:
            c += 1

    return [k, c]
```

## 3. 개선한 접근법

`l`과 `r`이 **1-based 인덱스**임을 반영하여 $O(N)$ 덩어리 슬라이딩 윈도우로 재구성을 시도했지만, 여기서 한 번 더 오답에 빠졌습니다.

- **1-based 덩어리 전처리**: `start_idx[0] = 1`, `end_idx[0] = arr[0]` 기반으로 1-based 구간 정보를 구축합니다.
- **K 계산**: `arr[i]` 덩어리와 `[l, r]` 겹침 구간을 1-based로 계산하여 정확한 $K$를 산출합니다. (예: `arr = [3, 2, 3, 1, 1]`, `l=5, r=7` $\to$ `K = 8`) 이 부분은 항상 정확했습니다.
- **C 계산에서의 오프바이원 버그**: 윈도우가 $s \to s+1$로 한 칸 이동하면 실제로는 "$s$ 위치의 값이 빠지고 $s + \text{win\_len}$ 위치의 값이 새로 들어오는" 것인데, 처음에는 새로 들어오는 값을 **현재 오른쪽 끝 $e$가 속한 덩어리**의 값으로 착각했습니다. $e$가 덩어리 경계의 마지막 칸일 경우 실제로 새로 들어오는 값은 다음 덩어리 것이라 diff가 완전히 틀어졌고, 그 결과 경계 부근에서 C가 실제보다 작게(심하면 0으로) 나오는 오답이 반복됐습니다.
- **수정**: diff를 결정하는 기준을 "$e$가 속한 덩어리"가 아니라 "$s + \text{win\_len}$(다음에 새로 들어올 위치)이 속한 덩어리"로 바꾸고, step 한계도 그 위치 기준으로 다시 계산했습니다. $\text{steps} = \min(\text{end\_idx}[i] - s + 1,\ \text{end\_idx}[j] - (s+\text{win\_len}) + 1,\ \text{max\_s} - s + 1)$ 만큼 한꺼번에 점프하여 $O(N)$에 $C$를 정확히 카운트합니다.

## 4. 해결 코드

```python
def solution(arr, l, r):
    ## 1-based 인덱싱 보정 및 RLE 덩어리 기반 O(N) 완벽 알고리즘
    ## 1. 1-based [l, r] 구간 합 K 계산
    ## 2. 길이가 win_len = r - l + 1 인 윈도우의 합이 K인 개수 C를 덩어리 단위 수학적 슬라이딩 윈도우로 O(N) 산출

    ## 여기서 중요한 점
    ## 윈도우가 s -> s+1로 한 칸 이동할 때 실제로 벌어지는 일은
    ## "s 위치의 값이 빠지고, s+win_len 위치의 값이 새로 들어온다"는 것!
    ## 즉 diff를 결정하는 두 값은 (s를 소유한 덩어리, s+win_len을 소유한 덩어리)이다.
    ## 예전 시도에서는 새로 들어오는 위치를 (현재 오른쪽 끝 e)를 소유한 덩어리로 착각해서
    ## 경계에서 한 칸씩 밀리는 오프바이원 버그가 있었음 (e가 아니라 e+1 = s+win_len 기준이어야 함)
    ## 이 착각 때문에 C가 실제보다 작게(때로는 0으로) 계산되는 오답이 반복해서 발생했었다.

    ## 결국 고민해야 하는 건
    ## 1) 1-based 누적 구간 인덱스 start_idx, end_idx 전처리
    ## 2) [l, r] 구간의 합 K 계산
    ## 3) 윈도우 왼쪽 끝 s(빠지는 값)와 s + win_len(새로 들어오는 값)이
    ##    각각 자신이 속한 덩어리를 벗어나지 않는 한계 step을 계산하고
    ##    diff == 0 및 diff != 0 일 때 수학적으로 C를 카운팅

    n = len(arr)
    start_idx = [0] * n
    end_idx = [0] * n
    curr = 1
    for i in range(n):
        start_idx[i] = curr
        curr += arr[i]
        end_idx[i] = curr - 1

    total_len = end_idx[-1]

    # 1. K 계산 (1-based [l, r] 구간 합)
    k = 0
    for i in range(n):
        s = max(l, start_idx[i])
        e = min(r, end_idx[i])
        if s <= e:
            k += (e - s + 1) * arr[i]

    # 2. C 계산 (길이가 win_len인 윈도우 중 합이 k인 개수)
    win_len = r - l + 1
    max_s = total_len - win_len + 1
    if max_s < 1:
        return [k, 0]

    # 초기 윈도우 curr_s = 1 일 때의 윈도우 합 curr_sum 계산
    curr_sum = 0
    for idx in range(n):
        os = max(1, start_idx[idx])
        oe = min(win_len, end_idx[idx])
        if os <= oe:
            curr_sum += (oe - os + 1) * arr[idx]

    c = 0
    curr_s = 1
    i = 0  # curr_s(빠지는 값)가 속한 덩어리
    j = 0  # curr_s + win_len(새로 들어오는 값)이 속한 덩어리

    while curr_s <= max_s:
        # curr_s가 속한 덩어리 i 찾기
        while i < n and end_idx[i] < curr_s:
            i += 1

        # 새로 들어올 위치 (현재 오른쪽 끝 다음 칸)
        next_pos = curr_s + win_len

        # 더 이상 새로 들어올 위치가 없다면 (배열 끝에 닿은 마지막 윈도우) 바로 확인하고 종료
        if next_pos > total_len:
            if curr_sum == k:
                c += 1
            curr_s += 1
            continue

        # next_pos가 속한 덩어리 j 찾기
        while j < n and end_idx[j] < next_pos:
            j += 1

        # 현재 덩어리 i, j를 유지할 수 있는 최대 이동 step 수
        step_s = end_idx[i] - curr_s + 1
        step_next = end_idx[j] - next_pos + 1
        step_limit = max_s - curr_s + 1

        steps = min(step_s, step_next, step_limit)

        diff = arr[j] - arr[i]
        if diff == 0:
            if curr_sum == k:
                c += steps
        else:
            rem = k - curr_sum
            if rem % diff == 0:
                t = rem // diff
                if 0 <= t < steps:
                    c += 1

        curr_sum += steps * diff
        curr_s += steps

    return [k, c]
```

## 5. 구현 전략 및 이유

### 1-based 인덱싱 적용
프로그래머스의 입력 `l`과 `r`이 1-based 인덱스임을 반영하여 테스트케이스 1번 `[3, 2, 3, 1, 1]`의 `l=5, r=7`에서 정확한 합 `K = 8` 및 부분 배열 개수 `C = 2`를 산출합니다.

### 오프바이원 버그 수정: "e가 속한 덩어리"가 아니라 "s + win_len이 속한 덩어리"
윈도우가 한 칸 이동할 때 새로 들어오는 값은 현재 오른쪽 끝 `curr_e`가 아니라 그 다음 칸 `curr_s + win_len`에 있습니다. 이전 시도는 `curr_e`를 소유한 덩어리 `j`를 기준으로 diff와 step 한계를 계산해서, `curr_e`가 하필 덩어리의 마지막 칸일 때 실제로 들어오는 값(다음 덩어리 값)과 다른 값을 사용하는 오류가 있었습니다. `j`가 `curr_s + win_len`을 소유한 덩어리를 가리키도록 바꾸고 `step_next = end_idx[j] - next_pos + 1`로 한계를 다시 계산해 문제를 해결했습니다. 또한 `curr_s + win_len`이 배열 범위를 벗어나는 마지막 윈도우(`curr_s == max_s`)는 더 들어올 값이 없으므로 별도로 한 칸씩만 확인하고 끝냅니다.

### 검증
무작위 배열/구간 5만 건에 대해 브루트포스(`brr`를 직접 만들어 슬라이딩)와 결과를 비교하여 일치함을 확인했습니다.

### 시간복잡도
- 시간복잡도: $O(N)$
- 공간복잡도: $O(N)$
