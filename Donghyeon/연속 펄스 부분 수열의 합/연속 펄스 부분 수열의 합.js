function solution(sequence) {
    let prefixSum = 0;
    let maxPrefix = 0;
    let minPrefix = 0;

    for (let i = 0; i < sequence.length; i++) {
        // 인덱스가 짝수면 1, 홀수면 -1을 곱해 누적 합 계산
        const pulse = i % 2 === 0 ? 1 : -1;
        prefixSum += sequence[i] * pulse;

        // 누적 합의 최댓값과 최솟값 지속적으로 갱신
        maxPrefix = Math.max(maxPrefix, prefixSum);
        minPrefix = Math.min(minPrefix, prefixSum);
    }

    // 가장 큰 누적 합과 가장 작은 누적 합의 차이가 정답
    return maxPrefix - minPrefix;
}
