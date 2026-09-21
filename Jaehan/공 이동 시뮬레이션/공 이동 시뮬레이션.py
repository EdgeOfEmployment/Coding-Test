def solution(n, m, x, y, queries):
    ## 커맨드
    ## 열 번호 감소 : 0, dx
    ## 열 번호 증가 : 1, dx
    ## 행 번호 감소 : 2, dx
    ## 행 번호 증가 : 3, dx
    
    ## 쿼리들을 순서대로 시뮬레이션, (x, y)에 도착하는 시작점의 개수를 return
    ## n * m의 격자 내 모든 좌표에서 모든 시뮬레이션이 (x,y) 도착을 만족해야함
    ## n-1, m-1을 넘어갈 수 없음
    ## 격자가 최대 10^9 x 10^9라 시작점을 하나씩 돌리면 시간초과
    ## -> 도착점 (x, y)에서 쿼리를 거꾸로 되감으며 "시작점이 될 수 있는 범위"를 추적
    
    last_n = n-1
    last_m = m-1
    
    # 도착점(x, y)에서 시작해 역추적할 시작점의 행/열 범위 초기화
    row_min, row_max = x, x
    col_min, col_max = y, y
    
    # 시뮬레이션 역순 진행    
    for command, dx in reversed(queries):
        if command == 0:  # 원래: 왼쪽(열 감소) -> 역순: 오른쪽으로 이동/확장
            if col_min != 0:
                col_min += dx
            col_max += dx
            if col_max > last_m:
                col_max = last_m
                
        elif command == 1:  # 원래: 오른쪽(열 증가) -> 역순: 왼쪽으로 이동/확장
            if col_max != last_m:
                col_max -= dx
            col_min -= dx
            if col_min < 0:
                col_min = 0
                
        elif command == 2:  # 원래: 위쪽(행 감소) -> 역순: 아래쪽으로 이동/확장
            if row_min != 0:
                row_min += dx
            row_max += dx
            if row_max > last_n:
                row_max = last_n
                
        elif command == 3:  # 원래: 아래쪽(행 증가) -> 역순: 위쪽으로 이동/확장
            if row_max != last_n:
                row_max -= dx
            row_min -= dx
            if row_min < 0:
                row_min = 0
        
        # 만약 유효한 범위를 완전히 벗어난다면 가능한 시작점은 없음
        if row_min > last_n or row_max < 0 or col_min > last_m or col_max < 0:
            return 0
            
    # 최종적으로 가능한 행 개수와 열 개수를 곱한 값 반환 (시작점의 가장 위,아래,왼쪽,오른쪽 끝을 구해서 리턴)
    return (row_max - row_min + 1) * (col_max - col_min + 1)
