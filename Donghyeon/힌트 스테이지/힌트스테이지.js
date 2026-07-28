function solution(cost, hint) {
    const n = cost.length;
    let minTotalCost = Infinity;

    // 각 스테이지별로 획득한 힌트 개수를 기록하는 배열 (0-based)
    const hintCount = Array(n).fill(0);

    /**
     * @param {number} stageIdx - 현재 결정을 내릴 스테이지 인덱스 (0 ~ n-2)
     * @param {number} currentBundleCost - 현재까지 구매한 번들 비용의 총합
     */
    function dfs(stageIdx, currentBundleCost) {
        // 모든 번들에 대한 구매 여부 결정을 마쳤을 때 (마지막 스테이지 도달)
        if (stageIdx === n - 1) {
            let totalCost = currentBundleCost;

            // 각 스테이지별 해결 비용 합산
            for (let i = 0; i < n; i++) {
                // 한 스테이지에서 사용할 수 있는 최대 힌트 수는 n-1개이므로 제한
                const usedHints = Math.min(hintCount[i], n - 1);
                totalCost += cost[i][usedHints];
            }

            // 최솟값 갱신
            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
            }
            return;
        }

        // ----------------------------------------------------
        // 선택 1. 현재 스테이지(stageIdx)의 힌트 번들을 구매하지 않는 경우
        // ----------------------------------------------------
        dfs(stageIdx + 1, currentBundleCost);

        // ----------------------------------------------------
        // 선택 2. 현재 스테이지(stageIdx)의 힌트 번들을 구매하는 경우
        // ----------------------------------------------------
        const bundle = hint[stageIdx];
        const price = bundle[0];

        // 번들에 포함된 힌트 개수 증가
        for (let i = 1; i < bundle.length; i++) {
            const targetStage = bundle[i] - 1;
            hintCount[targetStage]++;
        }

        dfs(stageIdx + 1, currentBundleCost + price);

        // 백트래킹 (다른 분기를 탐색하기 위해 획득했던 힌트 개수 복구)
        for (let i = 1; i < bundle.length; i++) {
            const targetStage = bundle[i] - 1;
            hintCount[targetStage]--;
        }
    }

    // 0번 스테이지부터 번들 구매 여부 탐색 시작
    dfs(0, 0);

    return minTotalCost;
}
