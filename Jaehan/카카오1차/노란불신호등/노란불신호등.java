class Solution {
    // 1. 신호등 하나의 주기 = G + Y + R, 항상 초록 -> 노랑 -> 빨강 순서로 반복
    // 2. 시간은 1초부터 시작, 처음엔 무조건 초록불 -> (t-1) % period 로 위상을 맞춰야 함
    // 3. n개 신호등이 동시에 특정 상태가 되는 시점은 각 주기의 LCM 이내에서 반드시 처음 등장
    //    (LCM을 넘어가면 그 이전 패턴이 그대로 반복되니까 더 볼 필요 없음)
    // 4. n <= 5, 주기 3~20 -> LCM 커봐야 수십만 수준이라 그냥 1초씩 전부 검사해도 시간 안에 충분
    public int solution(int[][] signals) {
        int n = signals.length;
        int[] periods = new int[n];
        for (int i = 0; i < n; i++) {
            periods[i] = signals[i][0] + signals[i][1] + signals[i][2];
        }

        long lcm = 1;
        for (int p : periods) {
            lcm = lcm / gcd(lcm, p) * p;
        }

        for (long t = 1; t <= lcm; t++) {
            boolean allYellow = true;
            for (int[] s : signals) {
                int period = s[0] + s[1] + s[2];
                long phase = (t - 1) % period;
                if (!(phase >= s[0] && phase < s[0] + s[1])) {
                    allYellow = false;
                    break;
                }
            }
            if (allYellow) {
                return (int) t;
            }
        }

        // 5. LCM까지 다 돌았는데도 못 찾으면 그런 시각은 아예 존재하지 않는다는 뜻
        return -1;
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
