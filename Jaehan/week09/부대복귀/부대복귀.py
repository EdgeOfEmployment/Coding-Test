from collections import deque
def solution(n, roads, sources, destination):
    ## 강철부대가 있는 지역은 모두 유일한 번호로 식별 가능
    ## 강철부대가 있는 지역 간 통과 시간은 모두 1로 동일
    ## BFS
    
    ## 방해로 인해 시작 때와 다르게 되돌아오는 경로가 없어져 복귀가 불가능한 경우도 존재함
    
    answer = []
    
    # 인접리스트 생성 (양방향)
    graph = [[] for _ in range(n+1)]
    for a,b in roads:
        graph[a].append(b)
        graph[b].append(a)
    
    # print(graph)
    
    # BFS 정의
    def bfs(start): # destination에서 sources로 세기 (sources의 각 부대원들을 기준으로 모두(최대 500명) 세면 시간 초과남 -> destination에서 출발하여 bfs를 1번만 수행함으로써 모든 지역까지의 최단거리를 계산)
        distances = [-1] * (n+1)
        q = deque([start])
        distances[start] = 0
        
        while q:
            cur = q.popleft()
            
            for nxt in graph[cur]:
                if distances[nxt] == -1: # 방문하지 않은 지역
                    distances[nxt] = distances[cur] + 1 # 거리가 1로 동일하므로 출발점부터 현재지역까지의 거리에서 1을 누적
                    q.append(nxt)
        
        return distances
                    
    
    # BFS 수행
    distances = bfs(destination)
    
    # sources의 각 원소에 해당하는 최단 거리 매핑
    answer = [distances[source] for source in sources]
    
    
    return answer