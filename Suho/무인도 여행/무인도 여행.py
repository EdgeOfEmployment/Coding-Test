from collections import deque
def solution(maps):
    #bfs문제
    answer = []
    map_row = len(maps)
    map_col = len(maps[0])
    visited=[[0]*map_col for _ in range(map_row)]
    dx=[0,0,-1,1]
    dy=[-1,1,0,0]
    
    def bfs(x,y):
        q = deque([(x, y)])
        visited[x][y] = 1
        total = int(maps[x][y])
        while q:
            x, y = q.popleft()
            for i in range(4):
                nx = x+ dx[i]
                ny = y+ dy[i]
                if 0<=nx< map_row and 0<=ny< map_col:
                    if not visited[nx][ny]and maps[nx][ny] !='X':
                        visited[nx][ny]=1
                        q.append((nx,ny))
                        total+=int(maps[nx][ny])
        return total
    for i in range(map_row):
        for j in range(map_col):
            if not visited[i][j] and maps[i][j] !='X':
                total=bfs(i,j)
                answer.append(total)
   #만약 지낼 수 있는 무인도가 없다면 
    if not answer:
        return [-1]
    #오름차순배열
    return sorted(answer)

    