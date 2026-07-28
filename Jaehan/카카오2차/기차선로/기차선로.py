def solution(grid):
    n = len(grid)
    m = len(grid[0])

    # 방향
    # 0: 위
    # 1: 오른쪽
    # 2: 아래
    # 3: 왼쪽
    dr = [-1, 0, 1, 0]
    dc = [0, 1, 0, -1]

    # 각 칸을 몇 번 방문했는지
    visited = [[0] * m for _ in range(n)]

    # 시작점은 이미 1번 선로
    visited[0][0] = 1

    answer = 0

    # 현재 방향으로 진입했을 때
    # 빈칸에 설치할 수 있는 선로 종류
    def get_tracks(direction):

        if direction == 0:
            # 위쪽으로 이동해서 들어옴
            return [2, 3, 6, 7]

        if direction == 1:
            # 오른쪽으로 이동해서 들어옴
            return [1, 3, 4, 7]

        if direction == 2:
            # 아래쪽으로 이동해서 들어옴
            return [2, 3, 4, 5]

        # 왼쪽으로 이동해서 들어옴
        return [1, 3, 5, 6]

    # 현재 방향으로 선로에 들어갔을 때
    # 선로를 빠져나가는 방향
    #
    # -1 : 연결되지 않음
    def get_next_direction(track, direction):

        # 1번 선로: ─
        if track == 1:
            if direction == 1 or direction == 3:
                return direction
            return -1

        # 2번 선로: │
        if track == 2:
            if direction == 0 or direction == 2:
                return direction
            return -1

        # 3번 선로: #
        # 모든 방향이 연결되어 있고
        # 기차는 직진한다고 생각한다.
        if track == 3:
            return direction

        # 4번 선로
        if track == 4:
            if direction == 1:
                return 0
            if direction == 2:
                return 3

        # 5번 선로
        if track == 5:
            if direction == 2:
                return 1
            if direction == 3:
                return 0

        # 6번 선로
        if track == 6:
            if direction == 0:
                return 1
            if direction == 3:
                return 2

        # 7번 선로
        if track == 7:
            if direction == 0:
                return 3
            if direction == 1:
                return 2

        return -1

    # 도착했을 때 정답 조건을 만족하는지 검사
    def validate(direction):

        # 목적지 선로와 연결되지 않는다면 실패
        if get_next_direction(grid[n - 1][m - 1], direction) == -1:
            return False

        for r in range(n):
            for c in range(m):

                # 설치된 선로인데 한 번도 지나지 않았다면 실패
                if grid[r][c] > 0 and visited[r][c] == 0:
                    return False

                # 3번 선로는 가로, 세로 방향을 모두 지나야 한다.
                # 따라서 정확히 2번 방문해야 한다.
                if grid[r][c] == 3 and visited[r][c] != 2:
                    return False

        return True

    def dfs(r, c, direction):

        nonlocal answer

        # 목적지 도착
        if r == n - 1 and c == m - 1:

            if validate(direction):
                answer += 1

            return

        # 다음 칸
        nr = r + dr[direction]
        nc = c + dc[direction]

        # 격자 밖
        if nr < 0 or nr >= n or nc < 0 or nc >= m:
            return

        # 장애물
        if grid[nr][nc] == -1:
            return

        # 3번 선로는 최대 2번까지만 방문 가능
        if visited[nr][nc] > 1 and grid[nr][nc] == 3:
            return

        # 이미 선로가 있는 경우
        if grid[nr][nc] > 0:
            tracks = [grid[nr][nc]]

        # 빈칸인 경우
        else:
            tracks = get_tracks(direction)

        # 가능한 선로를 하나씩 시도
        for track in tracks:

            # 현재 선로를 통과할 수 있는지 확인
            next_direction = get_next_direction(
                track,
                direction
            )

            # 연결되지 않는 선로
            if next_direction == -1:
                continue

            # 기존 값 저장
            original_track = grid[nr][nc]

            # 선로 설치
            grid[nr][nc] = track

            # 방문 횟수 증가
            visited[nr][nc] += 1

            # 다음 칸으로 이동
            dfs(
                nr,
                nc,
                next_direction
            )

            # 백트래킹
            visited[nr][nc] -= 1

            # 원래 상태 복구
            grid[nr][nc] = original_track

    # (0, 0)에서 오른쪽 방향으로 출발
    dfs(0, 0, 1)

    return answer