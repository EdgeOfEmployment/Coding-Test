from collections import deque

DIRS = {
    1: (0, 1),   # right
    2: (1, 0),   # down
    3: (0, -1),  # left
    4: (-1, 0),  # up
}


def solution(board, commands):
    ## 1. 앱은 전부 정사각형 -> (r0, c0, size)로 표현 가능 (왼쪽 위 좌표 + 한 변 길이)
    ## 2. 앱 하나를 밀면 앞쪽에 다른 앱이 있으면 그 앱도 밀림 -> BFS로 "같이 움직일 앱들" 찾기
    ## 3. (중요, 여기서 계속 틀렸었음) 이동은 "칸 단위 모듈러"로 한 칸씩 처리한다. 즉 크기 2 이상 앱이
    ##    경계를 넘어가면 일단 좌표만 (좌표+1) % n(또는 m) 으로 넘어가고, 그 결과 정사각형이 두 조각으로
    ##    "걸쳐있는" 상태(straddle)가 될 수 있다. 통째로 반대편 끝(0)으로 스냅시키는 게 아니다!
    ## 4. 이번 이동으로 앱이 경계에 걸치게 됐으면(wrapped), 이번 라운드가 끝난 뒤 "다음 라운드"에
    ##    그 앱을 같은 방향으로 한 번 더 민다 -> 이걸 반복하면 크기가 s인 앱은 최대 s번 정도 더 밀리면서
    ##    결국 다시 완전한 정사각형 모양으로 정착한다. (실제 문제 게시판에 공유된 반례로 검증한 규칙)
    ## 5. 한 라운드 안에서 "정상적으로 옆 칸에 있어서 밀리는 앱들"을 큐로 전부 처리하고 나서야,
    ##    그 라운드에서 경계에 걸쳤던 앱들을 다음 라운드 큐로 넘겨 처리한다(웨이브 방식).
    n = len(board)
    m = len(board[0])
    grid = [row[:] for row in board]

    apps = _find_apps(grid, n, m)

    for app_id, arrow in commands:
        dx, dy = DIRS[arrow]
        _push_command(grid, apps, n, m, app_id, dx, dy)

    return grid


def _find_apps(grid, n, m):
    apps = {}
    seen = set()
    for i in range(n):
        for j in range(m):
            app_id = grid[i][j]
            if app_id == 0 or app_id in seen:
                continue
            seen.add(app_id)
            size = 1
            while j + size < m and grid[i][j + size] == app_id:
                size += 1
            apps[app_id] = [i, j, size]
    return apps


def _cells(r0, c0, size, n, m):
    return [((r0 + i) % n, (c0 + j) % m) for i in range(size) for j in range(size)]


def _shift_once(grid, apps, n, m, app_id, dx, dy):
    r0, c0, size = apps[app_id]
    old_cells = _cells(r0, c0, size, n, m)

    nr0 = (r0 + dx) % n
    nc0 = (c0 + dy) % m
    new_cells = _cells(nr0, nc0, size, n, m)

    old_set = set(old_cells)
    blockers = set()
    for r, c in new_cells:
        if (r, c) in old_set:
            continue
        occ = grid[r][c]
        if occ != 0 and occ != app_id:
            blockers.add(occ)

    for r, c in old_cells:
        if grid[r][c] == app_id:
            grid[r][c] = 0
    for r, c in new_cells:
        grid[r][c] = app_id

    apps[app_id] = [nr0, nc0, size]

    wrapped = (dx != 0 and nr0 + size > n) or (dy != 0 and nc0 + size > m)
    return blockers, wrapped


def _push_command(grid, apps, n, m, start_id, dx, dy):
    queue = deque([start_id])
    queued = {start_id}
    wrapped_next = deque()

    while queue or wrapped_next:
        while queue:
            app_id = queue.popleft()
            queued.discard(app_id)
            blockers, wrapped = _shift_once(grid, apps, n, m, app_id, dx, dy)
            for b in blockers:
                if b not in queued:
                    queue.append(b)
                    queued.add(b)
            if wrapped:
                wrapped_next.append(app_id)

        queue = wrapped_next
        queued = set(queue)
        wrapped_next = deque()
