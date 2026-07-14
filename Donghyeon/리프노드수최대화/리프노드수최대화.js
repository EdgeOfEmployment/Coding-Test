function solution(dist_limit, split_limit) {
  let max_leaves = 1;

  // a: 2의 개수, b: 3의 개수
  for (let a = 0; a <= 30; a++) {
    for (let b = 0; b <= 20; b++) {
      if (a === 0 && b === 0) continue;

      // 조건 1. 분배도 한계를 넘는지 확인
      let product = Math.pow(2, a) * Math.pow(3, b);
      if (product > split_limit) continue;

      let L = a + b;

      // 조건 2. 최소 요구 분배 노드 수(깊이)를 충족할 수 있는지 확인
      if (dist_limit < L) continue;

      // 항상 최적 형태인 2 -> 3 순서의 조합 배열 생성
      let C = [];
      for (let i = 0; i < a; i++) C.push(2);
      for (let i = 0; i < b; i++) C.push(3);

      // 각 깊이별로 최소 1개의 노드를 배치
      let X = new Array(L).fill(1);
      let rem = dist_limit - L; // 남은 예산(노드 수)

      // Top-Down 방식으로 가능한 최대치까지 각 레벨의 노드 수 갱신
      for (let i = 1; i < L; i++) {
        // 상위 계층의 노드 수 기반으로 현재 계층에서 늘릴 수 있는 최대 노드 수 계산
        let max_add = X[i - 1] * C[i - 1] - X[i];
        let add = Math.min(max_add, rem);
        X[i] += add;
        rem -= add;
      }

      // 리프 노드 수 합산: (기본 리프 1개) + 각 분배 노드가 생성한 추가 리프 수
      let leaves = 1;
      for (let i = 0; i < L; i++) {
        leaves += X[i] * (C[i] - 1);
      }

      // 최댓값 갱신
      if (leaves > max_leaves) {
        max_leaves = leaves;
      }
    }
  }

  return max_leaves;
}
