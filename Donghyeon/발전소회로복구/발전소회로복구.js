// bfs사용하면됨
// 층마다의 패널을 까지 최소거리 계산(이건 고정된 값)
// 패널 <> 패널, 패널<>앨베 거리를 리스트로 저장
// 층간의 이동시간 계산(패널 활성화 순서 고려)
// 층간 이동 최소 시간을 구해서 더하면됨

// 자바스크립트는 내장 우선순위 큐가 없으므로 최소 힙(Min-Heap)을 직접 구현합니다.
class MinHeap {
    constructor() {
        this.heap = [];
    }
    push(val) {
        this.heap.push(val);
        this.bubbleUp(this.heap.length - 1);
    }
    pop() {
        if (this.heap.length === 1) return this.heap.pop();
        const min = this.heap[0];
        this.heap[0] = this.heap.pop();
        this.sinkDown(0);
        return min;
    }
    bubbleUp(idx) {
        while (idx > 0) {
            let parentIdx = Math.floor((idx - 1) / 2);
            if (this.heap[parentIdx].time <= this.heap[idx].time) break;
            [this.heap[parentIdx], this.heap[idx]] = [this.heap[idx], this.heap[parentIdx]];
            idx = parentIdx;
        }
    }
    sinkDown(idx) {
        const length = this.heap.length;
        while (true) {
            let leftIdx = 2 * idx + 1;
            let rightIdx = 2 * idx + 2;
            let smallest = idx;

            if (leftIdx < length && this.heap[leftIdx].time < this.heap[smallest].time) smallest = leftIdx;
            if (rightIdx < length && this.heap[rightIdx].time < this.heap[smallest].time) smallest = rightIdx;

            if (smallest === idx) break;
            [this.heap[idx], this.heap[smallest]] = [this.heap[smallest], this.heap[idx]];
            idx = smallest;
        }
    }
    isEmpty() {
        return this.heap.length === 0;
    }
}

function solution(h, grid, panels, seqs) {
    const n = grid.length;
    const m = grid[0].length;
    const k = panels.length;

    // 1. 엘리베이터 위치 찾기
    let er = -1,
        ec = -1;
    for (let r = 0; r < n; r++) {
        for (let c = 0; c < m; c++) {
            if (grid[r][c] === '@') {
                er = r;
                ec = c;
                break;
            }
        }
    }

    // 주요 거점(POI) 세팅: 0번 인덱스는 엘리베이터, 1~k번은 각 패널
    const pois = [{ r: er, c: ec }];
    for (let i = 0; i < k; i++) {
        // 문제의 좌표는 1-based 이므로 배열 접근을 위해 0-based로 변환합니다.
        pois.push({ f: panels[i][0], r: panels[i][1] - 1, c: panels[i][2] - 1 });
    }

    // 2. BFS로 거점 간 2D 평면상 최단 거리 계산
    const distMap2D = Array.from({ length: k + 1 }, () => Array(k + 1).fill(Infinity));
    const dirs = [
        [-1, 0],
        [1, 0],
        [0, -1],
        [0, 1],
    ];

    for (let i = 0; i <= k; i++) {
        const sr = pois[i].r;
        const sc = pois[i].c;

        const q = [[sr, sc, 0]];
        const visited = Array.from({ length: n }, () => Array(m).fill(false));
        visited[sr][sc] = true;

        const tempDist = Array.from({ length: n }, () => Array(m).fill(Infinity));
        tempDist[sr][sc] = 0;

        let head = 0;
        while (head < q.length) {
            const [r, c, d] = q[head++];

            for (let [dr, dc] of dirs) {
                const nr = r + dr,
                    nc = c + dc;
                // 이동 가능 구역인지 체크
                if (nr >= 0 && nr < n && nc >= 0 && nc < m && grid[nr][nc] !== '#' && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    tempDist[nr][nc] = d + 1;
                    q.push([nr, nc, d + 1]);
                }
            }
        }

        // 계산된 2D 거리를 다른 거점들과 매핑
        for (let j = 0; j <= k; j++) {
            distMap2D[i][j] = tempDist[pois[j].r][pois[j].c];
        }
    }

    // 3. 실제 이동 비용(수평 + 수직) 계산을 2차원 배열로 압축
    const cost = Array.from({ length: k + 1 }, () => Array(k + 1).fill(Infinity));
    for (let u = 1; u <= k; u++) {
        for (let v = 1; v <= k; v++) {
            if (u === v) {
                cost[u][v] = 0;
                continue;
            }

            const fu = pois[u].f;
            const fv = pois[v].f;

            if (fu === fv) {
                // 같은 층이면 바로 이동
                cost[u][v] = distMap2D[u][v];
            } else {
                // 다른 층이면: [u -> 엘리베이터] + [층간 이동 시간] + [엘리베이터 -> v]
                cost[u][v] = distMap2D[u][0] + Math.abs(fu - fv) + distMap2D[0][v];
            }
        }
    }

    // 4. 패널 활성화 선행 조건 파싱
    const deps = Array.from({ length: k + 1 }, () => []);
    for (let [a, b] of seqs) {
        deps[b].push(a); // b를 켜기 위해 a가 필요함
    }

    // 5. 다익스트라 (문자열 상태 방문 처리)
    const pq = new MinHeap();

    // 비트마스크 대신 "000...0" 형태의 문자열로 패널 상태 관리
    const startState = '0'.repeat(k);

    // 방문 처리 맵. Key: "현재패널번호,상태문자열" (예: "1,0001") -> Value: 최소시간
    const dist = new Map();

    // 항상 1번 패널 위치에서 시작합니다 (시간 0, 켜진 패널 0개)
    pq.push({ curr: 1, time: 0, stateStr: startState, count: 0 });
    dist.set(`1,${startState}`, 0);

    while (!pq.isEmpty()) {
        const { curr, time, stateStr, count } = pq.pop();

        // 모든 패널을 켰다면 종료 (다익스트라이므로 첫 도달이 최소 시간)
        if (count === k) {
            return time;
        }

        // 이미 더 빠른 경로로 해당 상태에 도달했다면 스킵
        if (time > (dist.get(`${curr},${stateStr}`) || Infinity)) continue;

        // 다음에 켤 패널 탐색
        for (let next = 1; next <= k; next++) {
            // 이미 켜진 패널은 무시 (0-based 인덱스 접근을 위해 next-1)
            if (stateStr[next - 1] === '1') continue;

            // 선행 조건이 모두 충족되었는지 확인
            let canActivate = true;
            for (let req of deps[next]) {
                if (stateStr[req - 1] === '0') {
                    canActivate = false; // 하나라도 안 켜져 있으면 활성화 불가
                    break;
                }
            }

            if (canActivate) {
                // 이동 시간 계산 및 상태 문자열 갱신
                const nextTime = time + cost[curr][next];
                const nextStateStr = stateStr.substring(0, next - 1) + '1' + stateStr.substring(next);
                const key = `${next},${nextStateStr}`;

                // 더 짧은 시간에 해당 상태에 도달할 수 있는 경우만 큐에 추가
                if (nextTime < (dist.get(key) || Infinity)) {
                    dist.set(key, nextTime);
                    pq.push({ curr: next, time: nextTime, stateStr: nextStateStr, count: count + 1 });
                }
            }
        }
    }

    return -1;
}
