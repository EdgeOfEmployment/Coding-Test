function solution(board, commands) {
    const N = board.length,
        M = board[0].length;
    const apps = extractApps(board);
    const dirs = { 1: [0, 1], 2: [1, 0], 3: [0, -1], 4: [-1, 0] };
    for (const [id, arrow] of commands) {
        if (!apps.has(id)) continue;
        const [dr, dc] = dirs[arrow];
        runCommand(apps, N, M, id, dr, dc);
    }
    // 커맨드 처리 중에는 apps 맵(좌표)만 갱신하고, 보드는 마지막에 한 번에 재구성.
    // (이동 도중엔 앱끼리 일시적으로 겹칠 수 있어서 2차원 배열로는 표현이 안 되기 때문)
    const out = Array.from({ length: N }, () => new Array(M).fill(0));
    for (const [id, a] of apps) {
        for (let i = 0; i < a.size; i++) for (let j = 0; j < a.size; j++) out[a.r + i][a.c + j] = id;
    }
    return out;
}

/** 보드를 스캔해서 각 앱의 좌상단 좌표와 한 변 길이를 추출 */
function extractApps(board) {
    const n = board.length,
        m = board[0].length;
    const apps = new Map();
    for (let r = 0; r < n; r++)
        for (let c = 0; c < m; c++) {
            const id = board[r][c];
            if (id !== 0 && !apps.has(id)) {
                let size = 1;
                while (c + size < m && board[r][c + size] === id) size++;
                apps.set(id, { id, r, c, size });
            }
        }
    return apps;
}

/** 1칸 이동 후 좌표 + wrap 여부. 한 칸이라도 밖으로 나가면 통째로 반대편 끝으로 */
function stepOnce(r, c, size, dr, dc, N, M) {
    let nr = r + dr,
        nc = c + dc,
        wrapped = false;
    if (nr < 0) {
        nr = N - size;
        wrapped = true;
    } else if (nr + size > N) {
        nr = 0;
        wrapped = true;
    }
    if (nc < 0) {
        nc = M - size;
        wrapped = true;
    } else if (nc + size > M) {
        nc = 0;
        wrapped = true;
    }
    return [nr, nc, wrapped];
}

function boxesOverlap(ar, ac, asz, br, bc, bsz) {
    return ar < br + bsz && br < ar + asz && ac < bc + bsz && bc < ac + asz;
}

/**
 * 커맨드 하나 = 여러 "웨이브"의 반복.
 *   웨이브: 이동 대상 전원이 동시에 정확히 1칸 이동.
 *   1. closure: 이동할 앱의 목표 칸에 있는 다른 앱도 연쇄적으로 이동 대상에 포함
 *   2. 전원 동시 1칸 이동 (wrap 발생 시 기록)
 *   3. 이동 후 겹침 검사: wrap해서 "도착한" 앱은 자리를 지키고, 겹친 상대가
 *      다음 웨이브의 이동 대상이 됨 → 겹침이 없어질 때까지 반복
 */
function runCommand(apps, N, M, startId, dr, dc) {
    // lastWrap: id -> 가장 최근에 wrap한 웨이브 번호.
    // 겹친 두 앱 중 "더 최근에 wrap해서 도착한" 쪽이 자리를 지킨다.
    // (문제 규칙: 반대편으로 이동해서 도착한 앱이 그 자리의 앱을 밀어냄)
    const lastWrap = new Map();
    let waveNum = 0;

    // 셀 점유 그리드. 이동 도중엔 한 칸에 두 앱이 겹칠 수 있으므로 칸마다 id "배열"
    function buildGrid() {
        const grid = new Array(N * M);
        for (const [id, a] of apps) {
            for (let i = 0; i < a.size; i++)
                for (let j = 0; j < a.size; j++) {
                    const k = (a.r + i) * M + (a.c + j);
                    (grid[k] || (grid[k] = [])).push(id);
                }
        }
        return grid;
    }

    // seed에서 시작해 "같이 밀려야 하는" 앱 전체를 구한다.
    // 각 멤버의 1칸 이동 목표 칸에 있는 다른 앱을 재귀적으로 추가.
    // 단, "현재 이미 나와 겹쳐 있는" 앱은 제외 — 걔는 내 앞을 막는 게 아니라
    // 나를 밀고 있는(내 위에 올라탄) 앱이라서, 내가 걔를 미는 관계가 아님.
    function closure(seed) {
        const grid = buildGrid();
        const s = new Set(seed);
        const queue = [...seed];
        while (queue.length > 0) {
            const id = queue.pop();
            const a = apps.get(id);
            const [tr, tc] = stepOnce(a.r, a.c, a.size, dr, dc, N, M);
            for (let i = 0; i < a.size; i++)
                for (let j = 0; j < a.size; j++) {
                    const cell = grid[(tr + i) * M + (tc + j)];
                    if (!cell) continue;
                    for (const oid of cell) {
                        if (oid === id || s.has(oid)) continue;
                        const o = apps.get(oid);
                        if (!boxesOverlap(a.r, a.c, a.size, o.r, o.c, o.size)) {
                            s.add(oid);
                            queue.push(oid);
                        }
                    }
                }
        }
        return s;
    }

    let moving = closure(new Set([startId]));
    let guard = 0;
    while (true) {
        if (++guard > 100000) throw new Error('무한 루프 감지');
        waveNum++;

        // 웨이브: 전원 동시에 1칸 이동
        for (const id of moving) {
            const a = apps.get(id);
            const [nr, nc, w] = stepOnce(a.r, a.c, a.size, dr, dc, N, M);
            a.r = nr;
            a.c = nc;
            if (w) lastWrap.set(id, waveNum);
        }

        // 이동 후 겹침 검사: 2개 이상 id가 있는 칸만 확인
        const grid = buildGrid();
        const pushed = new Set();
        for (let k = 0; k < N * M; k++) {
            const cell = grid[k];
            if (!cell || cell.length < 2) continue;
            for (let x = 0; x < cell.length; x++)
                for (let y = x + 1; y < cell.length; y++) {
                    const ida = cell[x],
                        idb = cell[y];
                    const wa = lastWrap.get(ida) || 0,
                        wb = lastWrap.get(idb) || 0;
                    let loser;
                    // 더 최근에 wrap해서 도착한 쪽이 자리를 지키고, 상대가 밀린다.
                    // (wrap 없이 겹침이 생기는 경우는 없음 — 같은 방향 동시 이동은 간격을 보존하고,
                    //  정지한 앱과의 충돌은 closure가 사전에 흡수하므로)
                    if (wa !== wb) loser = wa > wb ? idb : ida;
                    else loser = moving.has(ida) ? ida : idb; // 이론상 도달하지 않는 안전 분기
                    pushed.add(loser);
                }
        }
        if (pushed.size === 0) return; // 겹침 없음 = 커맨드 완료
        moving = closure(pushed); // 밀려난 앱들이 다음 웨이브의 시작점
    }
}
