class MaxHeap {
    constructor() {
        this.data = [];
    }
    push(val) {
        this.data.push(val);
        this.up(this.data.length - 1);
    }
    pop() {
        if (this.data.length === 0) return null;
        if (this.data.length === 1) return this.data.pop();
        const max = this.data[0];
        this.data[0] = this.data.pop();
        this.down(0);
        return max;
    }
    up(i) {
        while (i > 0) {
            const p = (i - 1) >> 1;
            if (this.data[p].w >= this.data[i].w) break;
            const tmp = this.data[p];
            this.data[p] = this.data[i];
            this.data[i] = tmp;
            i = p;
        }
    }
    down(i) {
        const len = this.data.length;
        while ((i << 1) + 1 < len) {
            let left = (i << 1) + 1;
            let right = left + 1;
            let max = left;
            if (right < len && this.data[right].w > this.data[left].w) {
                max = right;
            }
            if (this.data[i].w >= this.data[max].w) break;
            const tmp = this.data[i];
            this.data[i] = this.data[max];
            this.data[max] = tmp;
            i = max;
        }
    }
}

function solution(city, road) {
    const nodeMap = new Map();
    const nodes = [];

    // 유의미한 좌표를 노드로 관리
    function getOrMakeNode(x, y) {
        const key = `${x},${y}`;
        if (!nodeMap.has(key)) {
            const node = { id: nodes.length, x, y, limit: Infinity };
            nodeMap.set(key, node.id);
            nodes.push(node);
            return node;
        }
        return nodes[nodeMap.get(key)];
    }

    const hRoads = [];
    const vRoads = [];

    // 도로 분류 (가로/세로)
    for (const [x1, y1, x2, y2, limit] of road) {
        if (y1 === y2) hRoads.push({ x1: Math.min(x1, x2), x2: Math.max(x1, x2), y: y1, limit });
        else vRoads.push({ x: x1, y1: Math.min(y1, y2), y2: Math.max(y1, y2), limit });
    }

    const hPoints = Array.from({ length: hRoads.length }, () => []);
    const vPoints = Array.from({ length: vRoads.length }, () => []);

    // 1. 가로 도로 위 점 수집 (양 끝점, 카메라)
    hRoads.forEach((hr, i) => {
        hPoints[i].push(getOrMakeNode(hr.x1, hr.y));
        hPoints[i].push(getOrMakeNode(hr.x2, hr.y));
        const cNode = getOrMakeNode((hr.x1 + hr.x2) / 2, hr.y);
        cNode.limit = Math.min(cNode.limit, hr.limit);
        hPoints[i].push(cNode);
    });

    // 2. 세로 도로 위 점 수집 (양 끝점, 카메라)
    vRoads.forEach((vr, i) => {
        vPoints[i].push(getOrMakeNode(vr.x, vr.y1));
        vPoints[i].push(getOrMakeNode(vr.x, vr.y2));
        const cNode = getOrMakeNode(vr.x, (vr.y1 + vr.y2) / 2);
        cNode.limit = Math.min(cNode.limit, vr.limit);
        vPoints[i].push(cNode);
    });

    // 3. 교차점 수집
    for (let i = 0; i < hRoads.length; i++) {
        for (let j = 0; j < vRoads.length; j++) {
            const hr = hRoads[i],
                vr = vRoads[j];
            if (vr.x >= hr.x1 && vr.x <= hr.x2 && hr.y >= vr.y1 && hr.y <= vr.y2) {
                const inode = getOrMakeNode(vr.x, hr.y);
                hPoints[i].push(inode);
                vPoints[j].push(inode);
            }
        }
    }

    // 4. 도시 수집
    for (const [cx, cy] of city) {
        const cNode = getOrMakeNode(cx, cy);
        hRoads.forEach((hr, i) => {
            if (cy === hr.y && cx >= hr.x1 && cx <= hr.x2) hPoints[i].push(cNode);
        });
        vRoads.forEach((vr, i) => {
            if (cx === vr.x && cy >= vr.y1 && cy <= vr.y2) vPoints[i].push(cNode);
        });
    }

    // 5. 인접 리스트 기반 그래프 구성
    const adj = Array.from({ length: nodes.length }, () => []);

    const addEdges = (points, isHorizontal) => {
        for (let i = 0; i < points.length; i++) {
            points[i].sort((a, b) => (isHorizontal ? a.x - b.x : a.y - b.y));
            for (let j = 0; j < points[i].length - 1; j++) {
                const u = points[i][j].id;
                const v = points[i][j + 1].id;
                if (u !== v) {
                    adj[u].push(v);
                    adj[v].push(u);
                }
            }
        }
    };
    addEdges(hPoints, true);
    addEdges(vPoints, false);

    // 6. 변형된 다익스트라 (최대 병목 경로)
    const dist = new Float64Array(nodes.length).fill(-1);
    const startId = nodeMap.get(`${city[0][0]},${city[0][1]}`);

    dist[startId] = nodes[startId].limit;
    const pq = new MaxHeap();
    pq.push({ id: startId, w: dist[startId] });

    while (pq.data.length > 0) {
        const { id: u, w } = pq.pop();
        if (w < dist[u]) continue;

        for (const v of adj[u]) {
            const nextLimit = Math.min(w, nodes[v].limit);
            if (nextLimit > dist[v]) {
                dist[v] = nextLimit;
                pq.push({ id: v, w: nextLimit });
            }
        }
    }

    // 7. 결과 반환 처리
    const result = [];
    for (let i = 1; i < city.length; i++) {
        const targetId = nodeMap.get(`${city[i][0]},${city[i][1]}`);
        const maxSpeed = dist[targetId];
        result.push(maxSpeed === Infinity ? 0 : maxSpeed);
    }

    return result;
}
