function solution(n) {
    let ans = 0;

    while (n > 0) {
        // 홀수일 경우 1을 더하고, 짝수일 경우 0을 더함 (n % 2의 결과 활용)
        ans += n % 2;
        // 2로 나눈 몫을 취함 (소수점 이하 버림)
        n = Math.floor(n / 2);
    }

    return ans;
}
