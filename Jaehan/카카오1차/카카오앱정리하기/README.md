# 카카오 앱 정리하기 문제 풀이 공유

## 1. 문제 접근법

- **상태 표현**: 모든 앱이 정사각형이므로 각 앱을 `(r0, c0, size)`(왼쪽 위 좌표 + 한 변의 길이)로 표현합니다.
- **밀림 연쇄(chain) 탐색**: 앱 하나를 밀면, 그 앱의 진행 방향 앞쪽 칸에 다른 앱이 있을 경우 그 앱도 같은 방향으로 밀려나고, 이 연쇄는 더 이상 새로운 앱과 부딪히지 않을 때까지 계속됩니다. 이를 BFS/큐로 탐색합니다.
- **순환 격자(toroidal) 이동은 "칸 단위 모듈러"**: 크기 2 이상인 앱이 경계를 넘어가면, 좌표를 통째로 반대편 끝(`0`)으로 스냅시키는 게 아니라 **그냥 `(좌표 + 1) % N`(또는 `% M`)으로 한 칸만 이동**시킵니다. 이 결과로 정사각형이 경계에 걸쳐 두 조각으로 "쪼개진" 상태(straddle)가 될 수 있는데, 이는 정상적인 중간 상태입니다.
- **경계에 걸친 앱은 다음 라운드에 한 번 더 민다(웨이브 처리)**: 어떤 앱이 이번 이동으로 경계에 걸치게 됐다면(wrapped), 그 명령이 끝나기 전에 **같은 방향으로 한 번 더** 밀어야 합니다. 이를 반복하면 크기가 `s`인 앱은 최대 `s`번 정도 추가로 밀리면서 결국 다시 완전한 정사각형 모양으로 정착합니다. 한 라운드 안에서는 "정상적으로 옆에 있어서 밀리는 앱들"을 큐로 전부 처리하고, 그 라운드에서 경계에 걸쳤던 앱들만 모아 **다음 라운드**의 큐로 넘깁니다.

## 2. 해결 코드

```python
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
```

## 3. 구현 전략 및 이유

### (초기 버전의 버그 1) "블록 전체를 반대편 끝으로 스냅"은 틀린 규칙이었다

처음에는 크기 2 이상인 앱이 경계를 넘으면 그 즉시 좌표를 반대편 끝(`0` 또는 `N/M - size`)으로 통째로 점프시키는 방식으로 구현했습니다. 문제의 공식 예제 3개는 모두 통과했지만, 이 방식은 여러 앱이 동시에 얽히는 복잡한 케이스에서 두 앱이 같은 칸을 두고 충돌하는 경우가 자주 발생했고, 이를 "충돌하면 진 쪽을 한 칸 더 민다"는 임시방편으로 해소하려 하니 격자가 꽉 찬 경우 무한히 반복되며 끝나지 않는 버그가 있었습니다(자세한 내용은 커밋 히스토리 참고). 우선순위 큐 기반으로 한 번에 순서대로 확정하는 방식으로 바꿔 무한루프는 해결했지만, 여전히 실제 채점에서는 오답이 발생했습니다.

### (초기 버전의 버그 2) 진짜 규칙은 "칸 단위 모듈러 + 경계에 걸치면 다음 라운드에 한 번 더"

문제 게시판에서 다른 사람이 공유한 반례(10×10 보드가 15개의 앱으로 완전히 꽉 차 있고, 그중 하나를 밀면 **아무 것도 움직이지 않는 것**이 정답인 케이스)를 보고 나서야 진짜 규칙을 알게 됐습니다. 크기 `s`인 앱이 경계를 넘어갈 때는 통째로 반대편으로 점프하는 게 아니라, **좌표를 `(좌표 + 1) % N`으로 딱 한 칸만 옮기고**, 그 결과 정사각형이 경계에 걸쳐 있으면(`wrapped`) 이번 명령이 끝나기 전에 **같은 방향으로 한 번 더** 밀어야 합니다. 크기 `s`짜리 앱은 이 과정을 최대 `s`번 반복하고 나서야 다시 완전한 정사각형으로 정착합니다.

이 규칙을 검증하기 위해 문제 설명의 "카카오 웹툰 앱(2x2)이 경계를 넘어가며 카카오 뮤직 앱을 밀어낸다"는 서술과, 예제 #2의 "카카오 웹툰 앱이 이동하게 되어 카카오톡 앱이 아래로 한 칸 더 밀립니다"라는 설명을 다시 대조했습니다. 둘 다 "경계를 넘는 앱은 한 번에 점프하는 게 아니라, 그 자체로 한 칸씩 이동하며 추가로 다른 앱을 밀어낸다"는 그림과 일치했습니다.

### 웨이브(라운드) 단위로 처리하는 이유

한 라운드 안에서 발견되는 "정상적으로 옆에 있어서 밀리는 앱들"은 전부 큐에 넣어 즉시 처리하고, **경계에 걸친(wrapped) 앱들만 따로 모아서 다음 라운드**로 넘깁니다. 이렇게 라운드를 나누는 이유는, 어떤 앱이 이번 라운드에 경계에 걸쳤다면 그 앱은 다음 라운드에 "한 칸 더 미는" 자기 자신의 후속 처리 대상이 되고, 이 후속 처리가 또 다른 앱을 밀어낼 수도 있기 때문입니다. 이 방식은 앱마다 최대 `size`번만 추가로 밀리면 되므로(경계에 걸친 상태를 벗어날 때까지), 항상 유한한 횟수 안에 끝난다는 것을 보장합니다 — 이전 버전의 "우선순위 기반 충돌 해소"처럼 별도의 안전장치(반복 횟수 제한)가 필요 없습니다.

### 검증

이 방식으로 다시 짠 뒤:
- 공식 예제 3개 모두 통과
- 문제 게시판에서 공유된 반례(15개 앱으로 꽉 찬 10×10 보드, 결과가 "변화 없음")도 정확히 일치
- 10×10 보드를 무작위 앱들로 빈틈없이 채운 뒤 명령 1,000개를 실행하는 스트레스 테스트를 30가지 시드로 반복해도 파이썬 기준 약 0.03~0.06초, 자바 기준 약 30ms 안에 항상 종료됨을 확인했습니다.
