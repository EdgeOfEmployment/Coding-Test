function solution(topping) {
    let answer = 0;

    // 1. 철수(왼쪽)가 가진 토핑의 종류를 저장할 Set
    // Set의 size를 통해 고유한 토핑의 가짓수를 O(1)로 알 수 있습니다.
    const leftSet = new Set();

    // 2. 동생(오른쪽)이 가진 토핑의 개수와 종류를 저장
    // 토핑 번호가 최대 10,000이므로 크기가 10,001인 배열을 사용하면 빠릅니다. (Map 객체를 써도 무방합니다)
    const rightCount = new Array(10001).fill(0);
    let rightUnique = 0; // 동생이 가진 고유 토핑 가짓수

    // 3. 처음에는 동생이 롤케이크 전체를 통째로 들고 있다고 가정합니다.
    for (const t of topping) {
        if (rightCount[t] === 0) {
            rightUnique++; // 처음 보는 토핑이면 가짓수(종류) 1 증가
        }
        rightCount[t]++; // 해당 토핑의 총 개수 증가
    }

    // 4. 롤케이크를 왼쪽부터 오른쪽으로 하나씩 잘라가며(철수에게 넘겨주며) 확인합니다.
    for (const t of topping) {
        // 철수에게 토핑을 1개 넘겨줍니다.
        leftSet.add(t);

        // 동생은 해당 토핑을 1개 뺏겼으므로 개수를 줄여줍니다.
        rightCount[t]--;

        // 만약 동생에게서 해당 토핑이 완전히 다 떨어졌다면, 고유 가짓수를 1 줄입니다.
        if (rightCount[t] === 0) {
            rightUnique--;
        }

        // 5. 철수가 가진 토핑 가짓수(leftSet.size)와 동생의 가짓수(rightUnique)가 같으면 공평하게 잘린 것입니다.
        if (leftSet.size === rightUnique) {
            answer++;
        }
    }

    return answer;
}
