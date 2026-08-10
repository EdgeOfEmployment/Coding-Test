import sys
def solution(n, lighthouse):
    ## 모든 등대들은 이어져있음
    ## 모든 등대들은 1개 이상의 뱃길로 이어져있으며 한 뱃길의 양쪽 끝 등대 중 적어도 하나는 켜져 있도록 해야함
    
    # 파이썬 재귀 깊이 제한 해제 (최대 10만 개 노드 대응)
    sys.setrecursionlimit(200000)
    
    # 양방향 dict(인접 리스트) 만들기
    graph = {}
    
    for a, b in lighthouse:
        if a not in graph:
            graph[a] = []
        if b not in graph:
            graph[b] = []

        graph[a].append(b)
        graph[b].append(a)
        
    # print(graph)

    answer = 0
    visited = [False] * (n + 1)
    
    # DFS : 'node'가 켜져 있는지 (True) 꺼져 있는지 (False)를 반환한다.
    def dfs(curr):
        nonlocal answer
        visited[curr] = True
        
        is_on = False # 불이 켜져있는지
        has_off_child = False
        
        for nxt in graph[curr]:
            if not visited[nxt]:
                # 자식을 방문했을 때, 자식 등대가 꺼져서 돌아오면 ?
                child_on = dfs(nxt)
                if not child_on :
                    has_off_child = True
                    
        # 자식 중 꺼져 있는 등대가 있다면, 현재 등대를 무조건 켜야한다.
        if has_off_child:
            is_on = True
            answer += 1
            
        return is_on
    
    # dfs 수행
    dfs(1)
        
    
    return answer