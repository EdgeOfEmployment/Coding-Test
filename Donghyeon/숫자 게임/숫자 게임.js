function solution(A, B) {
    let answer = 0;

    // 두 배열을 오름차순으로 정렬합니다.
    A.sort((a, b) => a - b);
    B.sort((a, b) => a - b);

    let aIdx = 0;
    let bIdx = 0;

    // B팀의 모든 팀원을 확인할 때까지 반복합니다.
    while (bIdx < B.length) {
        if (B[bIdx] > A[aIdx]) {
            // B팀의 숫자가 더 커서 승리하는 경우
            answer++;
            aIdx++;
            bIdx++;
        } else {
            // B팀이 이길 수 없는 경우, 다음으로 큰 B팀의 숫자를 확인합니다.
            bIdx++;
        }
    }

    return answer;
}
