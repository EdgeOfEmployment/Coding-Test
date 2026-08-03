// function solution(arr, l, r) {
//     var answer = [];

//     let brr = [];
//     let K = 0;
//     let len = r - l + 1;
//     let C = 0;

//     arr.forEach(n => {
//        for (let i =0; i < n; i++)
//            brr.push(n);
//     });

//     for (let i = l-1; i < r; i++)
//         K += brr[i];

//     let temp = 0;

//     for (let i = 0; i < len; i++)
//         temp += brr[i];

//     if (temp == K) C++;

//     for (let i = len; i < brr.length; i++){
//         temp -= brr[i - len];
//         temp += brr[i];

//         if (temp == K) C++;
//     }

//     return [K, C];
// }

function solution(arr, l, r) {
    const N = arr.length;

    // 1. 블록별 누적 길이(prefix_len)와 누적 합(prefix_sum) 미리 계산
    const prefix_len = new Array(N + 1).fill(0);
    const prefix_sum = new Array(N + 1).fill(0);

    for (let i = 0; i < N; i++) {
        prefix_len[i + 1] = prefix_len[i] + arr[i];
        prefix_sum[i + 1] = prefix_sum[i] + arr[i] * arr[i];
    }

    const total_len = prefix_len[N];
    const L = r - l + 1; // 찾고자 하는 부분 배열의 길이

    // 0부터 idx-1까지(길이 idx)의 원소 합을 구하는 함수 O(log N)
    function getSum(idx) {
        if (idx <= 0) return 0;
        if (idx >= total_len) return prefix_sum[N];

        // 이분 탐색으로 idx번째 원소가 속한 블록 탐색
        let low = 0,
            high = N - 1;
        let block = 0;
        while (low <= high) {
            let mid = (low + high) >> 1;
            if (prefix_len[mid] < idx) {
                block = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return prefix_sum[block] + (idx - prefix_len[block]) * arr[block];
    }

    // [1단계] K 계산 (l번째부터 r번째 원소까지의 합, 1-based index)
    const K = getSum(r) - getSum(l - 1);

    // [2단계] 임계점(Cut points) 생성 및 정렬
    // 윈도우 시작점 s 또는 끝점 s + L - 1이 블록 경계를 넘는 시점들 모으기
    const max_s = total_len - L;
    const cuts = [];

    for (let i = 0; i <= N; i++) {
        if (prefix_len[i] <= max_s) cuts.push(prefix_len[i]);
        if (prefix_len[i] - L >= 0 && prefix_len[i] - L <= max_s) cuts.push(prefix_len[i] - L);
    }
    cuts.push(max_s + 1); // 마지막 구간 처리를 위한 끝 인덱스 추가

    cuts.sort((a, b) => a - b);

    // 중복된 임계점 제거
    const unique_cuts = [cuts[0]];
    for (let i = 1; i < cuts.length; i++) {
        if (cuts[i] !== cuts[i - 1]) unique_cuts.push(cuts[i]);
    }

    // 투 포인터로 인덱스가 현재 어느 블록에 속하는지 빠르게 추적
    let pLeft = 0,
        pRight = 0;
    function getBlockLeft(idx) {
        while (pLeft + 1 < N && prefix_len[pLeft + 1] <= idx) pLeft++;
        return pLeft;
    }
    function getBlockRight(idx) {
        if (idx >= total_len) return -1;
        while (pRight + 1 < N && prefix_len[pRight + 1] <= idx) pRight++;
        return pRight;
    }

    let C = 0;
    let current_sum = getSum(L); // 0번 인덱스에서 시작하는 길이 L 윈도우의 초기 합

    // 정렬된 임계점 사이의 구간들을 순회 O(N)
    for (let k = 0; k < unique_cuts.length - 1; k++) {
        const s_start = unique_cuts[k];
        const s_end = unique_cuts[k + 1];
        const len_interval = s_end - s_start; // 현재 구간의 길이

        const b_left = getBlockLeft(s_start);
        const b_right = getBlockRight(s_start + L);

        const v_left = arr[b_left];
        const v_right = b_right !== -1 ? arr[b_right] : 0;
        const delta = v_right - v_left; // 윈도우를 오른쪽으로 1칸 이동할 때마다 변하는 합의 변화량

        if (delta === 0) {
            // 변화량이 0이면 구간 내 모든 윈도우의 합이 일정함
            if (current_sum === K) {
                C += len_interval;
            }
        } else {
            // 등차수열에서 합이 정확히 K가 되는 이동 횟수 t 탐색 (current_sum + t * delta == K)
            const diff = K - current_sum;
            if (diff % delta === 0) {
                const t = diff / delta;
                // 계산된 t가 현재 구간 길이 내에 유효하게 존재하는지 확인
                if (t >= 0 && t < len_interval) {
                    C++;
                }
            }
        }

        // 다음 구간으로 넘어가기 위해 현재 윈도우 합 누적 업데이트
        current_sum += len_interval * delta;
    }

    return [K, C];
}
