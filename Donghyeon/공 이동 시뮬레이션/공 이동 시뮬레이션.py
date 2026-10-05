def solution(n, m, x, y, queries):
    # 목표 지점에서부터 시작 (초기 가능한 시작점의 범위)
    r_min, r_max = x, x
    c_min, c_max = y, y
    
    # 쿼리를 뒤에서부터 역순으로 실행
    for command, dx in reversed(queries):
        if command == 0:  # 원래 쿼리: 왼쪽 이동 -> 역추적: 오른쪽으로 이동
            if c_min != 0: 
                c_min += dx
            c_max = min(m - 1, c_max + dx)
            
        elif command == 1:  # 원래 쿼리: 오른쪽 이동 -> 역추적: 왼쪽으로 이동
            if c_max != m - 1:
                c_max -= dx
            c_min = max(0, c_min - dx)
            
        elif command == 2:  # 원래 쿼리: 위로 이동 -> 역추적: 아래로 이동
            if r_min != 0:
                r_min += dx
            r_max = min(n - 1, r_max + dx)
            
        elif command == 3:  # 원래 쿼리: 아래로 이동 -> 역추적: 위로 이동
            if r_max != n - 1:
                r_max -= dx
            r_min = max(0, r_min - dx)
            
        # 추적 중인 범위가 격자를 벗어나 역전된 경우 (도달 가능한 시작점이 없음)
        if r_min > r_max or c_min > c_max:
            return 0
            
    # 최종적으로 살아남은 시작점 범위의 넓이가 정답
    return (r_max - r_min + 1) * (c_max - c_min + 1)