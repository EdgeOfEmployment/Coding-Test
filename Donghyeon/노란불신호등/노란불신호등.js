function solution(signals) {
    var answer = 0;

    // 제한 조건이 매우 작음
    // time을 반복해서 순회 하면서 (G+y+R)의 나머지가 단순히, G, G+Y 사이에 있는지 확인
    // time이 최소 공배수를 넘어가면 -1

    // 최소공배수 구하기
    // 1. 최대공약수(GCD) 구하기
    const getGCD = (a, b) => {
        while (b !== 0) {
            let temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    };

    // 2. 최소공배수(LCM) 구하기
    const getLCM = (a, b) => {
        return (a * b) / getGCD(a, b);
    };

    // 3. 배열의 최소공배수 구하기
    const getArrayLCM = (arr) => {
        // 배열의 첫 번째 요소를 초기값으로 설정하고 누적해서 LCM을 계산
        return arr.reduce((acc, cur) => getLCM(acc, cur));
    };

    // 4. 각 신호등의 주기(G + Y + R) 배열 생성
    const cycles = signals.map((signal) => signal[0] + signal[1] + signal[2]);

    // 5. 탐색해야 할 최대 시간 (모든 신호등 주기의 최소공배수)
    const maxTime = getArrayLCM(cycles);

    // 6. 1초부터 최대 주기(maxTime)까지 탐색
    for (let time = 1; time <= maxTime; time++) {
        // signals.every()를 사용해 모든 신호등이 노란불 조건을 만족하는지 확인
        const allYellow = signals.every(([g, y, r]) => {
            const cycle = g + y + r;
            // 시간은 1초부터 시작하므로 time - 1 에 나머지를 구함
            const current = (time - 1) % cycle;

            // 현재 시간이 G(초록불) 이상이고, G + Y(초록+노란불 끝나는 시간) 미만인지 확인
            return current >= g && current < g + y;
        });

        // 모든 신호등이 노란불이라면 해당 시간을 즉시 반환
        if (allYellow) {
            return time;
        }
    }

    // 최소공배수(전체 주기)를 다 돌았는데도 모두 노란불인 경우가 없다면 -1 반환
    return -1;
}
