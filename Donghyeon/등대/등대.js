function solution(n, lighthouse) {
    // dfs 백트랙킹
    // 내려가면서 키고 끄고 확인
    // 100,000 <= 콜스택 초과 발생 가능
    // 그리디, 큐

    let answer = 0;

    // 1. 인접 리스트와 각 노드의 차수(연결된 간선 수) 배열 초기화
    const adj = Array.from({ length: n + 1 }, () => []);
    const degree = new Array(n + 1).fill(0);

    // 2. 그래프 연결 상태 및 차수 기록
    for (const [u, v] of lighthouse) {
        adj[u].push(v);
        adj[v].push(u);
        degree[u]++;
        degree[v]++;
    }

    // 3. 리프 노드(차수가 1인 노드)를 찾아 큐에 삽입
    const queue = [];
    for (let i = 1; i <= n; i++) {
        if (degree[i] === 1) queue.push(i);
    }

    // 4. 큐를 순회하며 부모 노드를 켬 (shift() 대신 head 포인터 사용으로 O(1) 유지)
    let head = 0;
    while (head < queue.length) {
        const leaf = queue[head++];

        // 이미 처리되어 차수가 1이 아닌 경우 건너뜀
        if (degree[leaf] !== 1) continue;

        // 리프 노드와 연결된 활성 상태의 부모 노드 찾기
        let parent = -1;
        for (const node of adj[leaf]) {
            if (degree[node] > 0) {
                parent = node;
                break;
            }
        }

        // 부모 노드가 존재한다면 등대를 켜고, 해당 부모 노드와 연결된 간선들을 모두 제거 처리
        if (parent !== -1) {
            answer++; // 등대 ON
            degree[parent] = 0; // 부모 노드 그래프에서 제거
            degree[leaf] = 0; // 리프 노드 그래프에서 제거

            // 부모 노드와 연결되어 있던 다른 노드들의 차수 감소
            for (const neighbor of adj[parent]) {
                if (degree[neighbor] > 0) {
                    degree[neighbor]--;
                    // 차수가 1이 되어 새로운 리프 노드가 되었다면 큐에 삽입
                    if (degree[neighbor] === 1) {
                        queue.push(neighbor);
                    }
                }
            }
        }
    }

    return answer;
}
