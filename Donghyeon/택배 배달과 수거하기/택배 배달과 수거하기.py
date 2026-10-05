def solution(cap, n, deliveries, pickups):
    answer = 0
    deliver_cnt = 0
    pickup_cnt = 0
    
    # 가장 먼 집부터 역순으로 탐색
    for i in range(n - 1, -1, -1):
        deliver_cnt += deliveries[i]
        pickup_cnt += pickups[i]
        
        # 현재 위치에서 배달이나 수거해야 할 물량이 남아있다면 트럭이 방문해야 함
        while deliver_cnt > 0 or pickup_cnt > 0:
            deliver_cnt -= cap
            pickup_cnt -= cap
            answer += (i + 1) * 2  # 왕복 거리 추가
            
    return answer