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
