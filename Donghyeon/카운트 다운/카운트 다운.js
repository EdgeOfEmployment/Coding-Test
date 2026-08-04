// DP를 생각해야겠죠? 당연히?

function solution(target) {
    // dp[i] = [최소 다트 수, 최대 싱글/불 횟수]
    // 목표 점수까지 구하기 위해 target + 1 길이의 배열을 만들고, 초기값은 무한대로 설정
    const dp = Array.from({ length: target + 1 }, () => [Infinity, 0]);
    dp[0] = [0, 0]; // 0점을 만드는 데 필요한 다트는 0개

    // 다트 한 발로 낼 수 있는 점수와 싱글/불 보너스 여부 정의
    const throws = [];
    for (let i = 1; i <= 20; i++) {
        throws.push([i, 1]); // 싱글 (보너스 1)
        throws.push([i * 2, 0]); // 더블 (보너스 0)
        throws.push([i * 3, 0]); // 트리플 (보너스 0)
    }
    throws.push([50, 1]); // 불 (보너스 1)

    // 1점부터 target 점수까지 DP 테이블 채우기
    for (let i = 1; i <= target; i++) {
        for (const [score, bonus] of throws) {
            // 현재 점수(i)에서 다트 점수(score)를 뺐을 때 0 이상인 경우만 확인
            if (i - score >= 0) {
                const prevDarts = dp[i - score][0];
                const prevBonus = dp[i - score][1];

                const nextDarts = prevDarts + 1;
                const nextBonus = prevBonus + bonus;

                // 1. 다트 던진 횟수가 더 적은 경우 우선 갱신
                if (nextDarts < dp[i][0]) {
                    dp[i] = [nextDarts, nextBonus];
                }
                // 2. 다트 던진 횟수가 같다면, 싱글/불 횟수가 더 많은 경우로 갱신
                else if (nextDarts === dp[i][0] && nextBonus > dp[i][1]) {
                    dp[i] = [nextDarts, nextBonus];
                }
            }
        }
    }

    // target 점수를 만들 때의 [최소 다트 수, 싱글/불 횟수] 반환
    return dp[target];
}
