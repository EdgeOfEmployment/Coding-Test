function solution(enroll, referral, seller, amount) {
    // 1. 결과를 담을 배열 초기화 (enroll의 길이만큼 0으로 채움)
    const answer = new Array(enroll.length).fill(0);

    // 2. 판매원의 이름으로 부모(추천인)와 인덱스를 빠르게 찾기 위한 해시맵 생성
    const parentMap = new Map();

    for (let i = 0; i < enroll.length; i++) {
        parentMap.set(enroll[i], { parent: referral[i], index: i });
    }

    // 3. 판매 기록을 순회하며 이익금 분배
    for (let i = 0; i < seller.length; i++) {
        let current = seller[i];
        let profit = amount[i] * 100; // 칫솔 한 개당 100원 이익

        // 추천인이 없거나("-"일 경우), 분배할 이익금이 0원이 되면 종료
        while (current !== '-' && profit > 0) {
            // 추천인에게 줄 10% 금액 (원 단위 절사)
            const distribute = Math.floor(profit / 10);

            // 내가 가질 금액 (전체 수익 - 추천인에게 줄 금액)
            const mine = profit - distribute;

            // 내 수익을 정답 배열에 누적
            const node = parentMap.get(current);
            answer[node.index] += mine;

            // 다음 반복을 위해 현재 사람을 추천인으로, 수익을 분배금으로 업데이트
            current = node.parent;
            profit = distribute;
        }
    }

    return answer;
}
