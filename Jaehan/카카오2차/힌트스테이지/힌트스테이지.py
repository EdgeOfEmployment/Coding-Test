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