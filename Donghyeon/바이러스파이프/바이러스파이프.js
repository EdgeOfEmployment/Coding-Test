function solution(n, infection, edges, k) {
  // 1. 그래프 구성 및 존재하는 파이프 타입(종류) 수집
  const graph = Array.from({ length: n + 1 }, () => []);
  const types = new Set();

  // 무방향(양방향) 그래프로 초기화
  for (const [u, v, t] of edges) {
    graph[u].push([v, t]);
    graph[v].push([u, t]);
    types.add(t);
  }

  const availableTypes = Array.from(types);
  let maxInfected = 0;

  // 2. DFS를 이용해 파이프를 여는 순서 조합 탐색
  // currentInfected: 현재까지 감염된 배양체들을 담은 Set 객체
  // step: 현재까지 파이프를 열었다 닫은 횟수
  function dfs(currentInfected, step) {
    // 매 단계마다 최대 감염자 수 갱신
    maxInfected = Math.max(maxInfected, currentInfected.size);

    // k번 행동을 모두 소진하면 종료
    if (step === k) return;

    // 존재하는 파이프 종류를 하나씩 열어보기
    for (const t of availableTypes) {
      // 현재 감염 상태를 복사하여 다음 상태를 준비
      const nextInfected = new Set(currentInfected);

      // BFS 탐색을 위한 큐 (JavaScript의 shift()는 느리므로 인덱스 포인터 사용)
      const queue = Array.from(currentInfected);
      let head = 0;

      // 파이프 종류 t가 열렸을 때 퍼져나가는 감염 시뮬레이션
      while (head < queue.length) {
        const u = queue[head++];

        for (const [v, pipeType] of graph[u]) {
          // 열려있는 파이프(t)와 종류가 같고, 아직 감염되지 않은 곳이라면 감염!
          if (pipeType === t && !nextInfected.has(v)) {
            nextInfected.add(v);
            queue.push(v);
          }
        }
      }

      // 파이프를 열어서 새롭게 감염된 배양체가 있을 때만 다음 단계로 넘어감
      // 감염자가 늘지 않았다면 무의미한 행동이므로 탐색 중단
      if (nextInfected.size > currentInfected.size) {
        dfs(nextInfected, step + 1);
      }
    }
  }

  // 3. 초기 감염 상태를 세팅하고 탐색 시작
  const initialInfected = new Set([infection]);
  dfs(initialInfected, 0);

  return maxInfected;
}
