from collections import deque
def solution(maps):
    answer = 0
    len_row = len(maps)
    len_col = len(maps[0])
    visited = [[False]*len_col for _ in range(len_row)]
    dx =[0,0,-1,1]
    dy =[-1,1,0,0]
    for i in range(len_row):
        for j in range(len_col):
            if maps[i][j]=='S':
                start = (i,j)
            elif maps[i][j]=='L':
                lever=(i,j)
            elif maps[i][j]=='E':
                exit=(i,j)
    
    #레버를 안지난 경우
    q=deque([(start[0],start[1],0)])
    visited[start[0]][start[1]] = True
    first = -1
    while q:
        x,y,dist = q.popleft()
        if (x,y) == lever:
            first = dist
            break
        for i in range(4):
            nx =x + dx[i]
            ny =y + dy[i]
            if 0<=nx<len_row and 0<=ny<len_col:
                if not visited[nx][ny] and maps[nx][ny]!='X':
                    visited[nx][ny]=True
                    q.append((nx,ny,dist+1))
                    
    #레버까지 도달 못하는 경우
    if first == -1:
        return -1
    
    #레버를 지난 경우
    
    visited = [[False]*len_col for _ in range(len_row)]
    q=deque([(lever[0],lever[1],0)])
    visited[lever[0]][lever[1]] = True
    second = -1

    while q:
        x,y,dist = q.popleft()
        if (x,y) == exit:
            second = dist
            break
        for i in range(4):
            nx =x + dx[i]
            ny =y + dy[i]
            if 0<=nx<len_row and 0<=ny<len_col:
                if not visited[nx][ny] and maps[nx][ny]!='X':
                    visited[nx][ny]=True
                    q.append((nx,ny,dist+1))

    
    #출구까지 도달 못하는 경우
    if second == -1:
        return -1
    answer = first+second
    return answer