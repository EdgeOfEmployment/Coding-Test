def solution(dist_limit, split_limit):
    max_steps = 1
    while 2 ** max_steps <= split_limit:
        max_steps += 1
    max_steps += 2

    def simulate(c2, c3):
        m, p, D, leaves = 1, 1, dist_limit, 0
        for i in range(c2 + c3):
            b = 2 if i < c2 else 3      # 2를 먼저, 3을 나중에
            if D == 0:
                break
            if p * b > split_limit:      # 분배도 초과 방지
                break
            n = min(m, D)                # 가능한 만큼 최대로 분배
            leaves += m - n              # 분배 못한 노드는 리프로 확정
            m = n * b
            p = p * b
            D -= n
        leaves += m
        return leaves

    best = 0
    for c2 in range(max_steps + 1):
        for c3 in range(max_steps - c2 + 1):
            best = max(best, simulate(c2, c3))
    return best