from collections import deque


def solution(maps):
    rows = len(maps)
    cols = len(maps[0])

    start = None
    lever = None
    end = None

    # 시작점, 레버, 출구 위치 찾기
    for r in range(rows):
        for c in range(cols):
            if maps[r][c] == 'S':
                start = (r, c)
            elif maps[r][c] == 'L':
                lever = (r, c)
            elif maps[r][c] == 'E':
                end = (r, c)

    # 두 지점 사이의 최단거리를 구하는 BFS
    def bfs(start, target):
        queue = deque([(start[0], start[1], 0)])
        visited = [[False] * cols for _ in range(rows)]
        visited[start[0]][start[1]] = True

        dr = [-1, 1, 0, 0]
        dc = [0, 0, -1, 1]

        while queue:
            r, c, dist = queue.popleft()

            if (r, c) == target:
                return dist

            for i in range(4):
                nr = r + dr[i]
                nc = c + dc[i]

                if not (0 <= nr < rows and 0 <= nc < cols):
                    continue

                if maps[nr][nc] == 'X' or visited[nr][nc]:
                    continue

                visited[nr][nc] = True
                queue.append((nr, nc, dist + 1))

        return -1

    to_lever = bfs(start, lever)
    if to_lever == -1:
        return -1

    to_end = bfs(lever, end)
    if to_end == -1:
        return -1

    return to_lever + to_end
