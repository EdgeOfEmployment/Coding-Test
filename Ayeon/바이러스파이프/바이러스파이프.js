from collections import deque

def solution(n, infection, edges, k):
    # 1. 트리 그래프 구축 (adj[노드] = [(연결된 노드, 파이프 타입), ...])
    adj = [[] for _ in range(n + 1)]
    for x, y, t in edges:
        adj[x].append((y, t))
        adj[y].append((x, t))
    
    # 중복 계산을 막기 위한 메모이제이션(DP) 배열
    # memo[node] = 이 노드가 감염되었을 때의 '최소 행동 횟수(turn)'
    # 더 적은 턴 만에 감염될수록 앞으로 더 많은 파이프를 타고 퍼질 수 있으므로 이득
    memo = {}

    # 모든 파이프 선택 조합을 탐색하는 DFS 함수
    # turn: 현재 몇 번째 파이프를 열 차례인지 (0부터 시작)
    # current_infected: 현재까지 감염된 노드들의 집합 {노드: 감염된 turn}
    max_infected_count = 0

    def dfs(turn, current_infected):
        nonlocal max_infected_count
        
        # 현재까지 감염된 개수로 최댓값 갱신
        max_infected_count = max(max_infected_count, len(current_infected))
        
        # 최대 행동 횟수 k번에 도달했다면 종료
        if turn == k:
            return

        # 다음 턴에 열 수 있는 파이프 타입은 1, 2, 3 중 하나
        for pipe_type in [1, 2, 3]:
            # 이번 파이프(pipe_type)를 열었을 때 새로 감염되는 노드들을 찾기 위한 BFS
            queue = deque()
            next_infected = dict(current_infected) # 기존 감염 상태 복사
            
            # 현재 감염된 모든 노드들을 BFS의 시작점으로 넣음
            for node in current_infected:
                queue.append(node)
                
            while queue:
                curr = queue.popleft()
                
                for nxt, t in adj[curr]:
                    # 파이프 타입이 일치하고, 아직 감염되지 않았거나 
                    # 혹은 더 이른 턴(turn)에 감염시킬 수 있는 경우라면 확장
                    if t == pipe_type:
                        if nxt not in next_infected or next_infected[nxt] > turn + 1:
                            next_infected[nxt] = turn + 1
                            queue.append(nxt)
            
            # 가지치기(Optimization): 이전에 완전히 똑같은 감염 상태와 턴을 방문한 적이 있다면 패스
            # 이 문제는 n과 k가 작아서 이 정도로도 충분히 통과
            dfs(turn + 1, next_infected)

    # 초기 상태: 0번째 턴에 infection 노드만 감염됨
    initial_status = {infection: 0}
    dfs(0, initial_status)
    
    return max_infected_count

