function solution(depth, money, excavate) {
    // 무조건 가운데 파기 시작해서 오른쪽 왼쪽을 구분
    // 절반을 파고 들어가기

    //let fin = depth.length;
    //let start = 1;

    //let mid = Math.floor((fin + start)/2);

    // for(let i = 1; i <= depth.length; i++) {
    //     let res = excavate(mid);
    //         console.log(mid)
    //     if (res == 0) return mid;
    //     else if (res == -1) {
    //         fin = mid - 1;
    //         mid = Math.floor((fin + start)/2);
    //     } else if (res == 1) {
    //         start = mid + 1;
    //         mid = Math.ceil((fin + start)/2);
    //     }
    // }
    // depth가 큰지점을 파고 들어가는 경우 예외 처리를 못함

    const n = depth.length;

    // dp[L][R]: [L, R] 구간에서 100% 보물을 찾기 위한 최소 비용
    // bestK[L][R]: [L, R] 구간에서 가장 먼저 파야 하는 최적의 열
    const dp = Array.from({ length: n + 2 }, () => Array(n + 2).fill(0));
    const bestK = Array.from({ length: n + 2 }, () => Array(n + 2).fill(0));

    // 1. 구간의 길이(len)를 1부터 n까지 늘려가며 바텀업(Bottom-up)으로 DP 테이블 계산
    for (let len = 1; len <= n; len++) {
        for (let L = 1; L <= n - len + 1; L++) {
            const R = L + len - 1;
            let minCost = Infinity;
            let optK = L;

            for (let k = L; k <= R; k++) {
                // k열을 팠을 때 최악의 경우 비용 (왼쪽 vs 오른쪽 중 큰 값 + 현재 굴착 비용)
                const cost = depth[k - 1] + Math.max(dp[L][k - 1], dp[k + 1][R]);
                if (cost < minCost) {
                    minCost = cost;
                    optK = k;
                }
            }
            dp[L][R] = minCost;
            bestK[L][R] = optK;
        }
    }

    // 2. 미리 계산해둔 최적 전략(bestK)을 바탕으로 실제 굴착 시뮬레이션 진행
    let L = 1;
    let R = n;

    while (L <= R) {
        // 현재 남은 구간 [L, R]에서 가장 최적인 열을 선택
        const target = bestK[L][R];
        const res = excavate(target);

        if (res === 0) {
            return target; // 보물 발견!
        } else if (res === -1) {
            // 보물이 왼쪽(더 작은 열)에 위치
            R = target - 1;
        } else if (res === 1) {
            // 보물이 오른쪽(더 큰 열)에 위치
            L = target + 1;
        }
    }
}
