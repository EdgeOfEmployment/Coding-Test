def solution(n):
    # 가로 길이가 1인 바닥과 2인 바닥을 채우는 방법의 수
    if n == 1:
        return 1

    prev2, prev1 = 1, 2
    MOD = 1_000_000_007

    # 마지막에 세로 타일 하나 또는 가로 타일 두 개를 놓는 경우를 합산
    for _ in range(3, n + 1):
        prev2, prev1 = prev1, (prev1 + prev2) % MOD

    return prev1
