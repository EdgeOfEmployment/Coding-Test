function solution(bridge_length, weight, truck_weights) {
    let answer = 0;

    // 다리의 길이만큼 0으로 채워진 큐 생성
    let queue = Array(bridge_length).fill(0);
    let sum = 0; // 현재 다리 위의 트럭 무게 합
    let i = 0; // 다리에서 빠져나갈 요소의 인덱스 (큐의 맨 앞)
    let i_t = 0; // 대기 중인 트럭의 인덱스

    while (truck_weights.length > i_t) {
        answer++; // 1초 경과

        // 1. 다리 끝에 도달한 트럭(또는 0)이 먼저 빠져나감
        sum -= queue[i];

        let temp = truck_weights[i_t];

        // 2. 새로운 트럭이 다리에 올라갈 수 있는지 확인 (<= 사용)
        if (sum + temp <= weight) {
            sum += temp; // 다리 위 무게 증가
            queue.push(temp); // 다리에 트럭 진입
            i_t++; // 다음 대기 트럭으로 순서 넘김
        } else {
            queue.push(0); // 무게 초과 시 빈 공간(0)만 진입
        }

        // 3. 다리 배열에 요소가 하나 추가되었으므로, 다음 빠져나갈 인덱스도 1 증가
        i++;
    }

    // while문이 끝나면 마지막 트럭이 막 다리에 진입한 상태임.
    // 마지막 트럭이 다리를 완전히 건너는 시간(bridge_length)을 더해서 반환
    return answer + bridge_length;
}
