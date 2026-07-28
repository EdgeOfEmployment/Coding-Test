from collections import deque
def solution(n, infection, edges, k):
    answer = 0
    
    ## 1. 타입 선택 시 해당 타입의 파이프는 모두 열림
    ## 1-1. k번 수행되는 동안 타입은 연속적으로 같은 타입을 고르면 안됨
    ## 2. A : 1, B : 2, C : 3
    ## 3. start 지점 : infection
    
    
    # 1. 그래프 구성 (타입별 인접 리스트)
    adj = {1: [], 2: [], 3: []}
    for u, v, t in edges:
        adj[t].append((u, v))
        adj[t].append((v, u))
        
    # 2. BFS 준비
    # 큐: (현재 감염 집합, 이전 타입, 남은 횟수)
    queue = deque([( {infection}, 0, k )])
    max_infected = 0
    
    while queue:
        curr_infected, last_type, remain_k = queue.popleft()
        
        # 행동 횟수 소진 시 최댓값 갱신
        if remain_k == 0:
            max_infected = max(max_infected, len(curr_infected))
            continue
            
        # 3. 파이프 타입 선택 (1, 2, 3 중 last_type 제외)
        for next_type in [1, 2, 3]:
            if next_type == last_type:
                continue
            
            # 해당 타입 파이프를 열었을 때 새롭게 감염되는 노드 BFS 탐색
            new_infected = set(curr_infected)
            q = deque(list(curr_infected))
            
            while q:
                u = q.popleft()
                for start, end in adj[next_type]:
                    if start == u and end not in new_infected:
                        new_infected.add(end)
                        q.append(end)
                    elif end == u and start not in new_infected:
                        new_infected.add(start)
                        q.append(start)
            
            # 상태 변화가 없으면 굳이 큐에 넣지 않아도 됨(가지치기)
            if len(new_infected) > len(curr_infected):
                queue.append((new_infected, next_type, remain_k - 1))
            else:
                max_infected = max(max_infected, len(curr_infected))
                
    return max_infected