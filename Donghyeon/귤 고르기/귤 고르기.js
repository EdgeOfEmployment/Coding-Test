function solution(k, tangerine) {
    var answer = 0;
    let map = new Map();

    // 1. 크기별로 귤의 개수 세기
    for (let i = 0; i < tangerine.length; i++) {
        let size = tangerine[i];

        if (!map.has(size)) {
            map.set(size, 1);
        } else {
            map.set(size, map.get(size) + 1);
        }
    }

    // 2. Map에서 개수(value)만 추출하여 내림차순으로 정렬
    // Array.from(map.values())를 사용하여 값들만 배열로 만듭니다.
    let counts = Array.from(map.values()).sort((a, b) => b - a);

    // 3. 개수가 가장 많은 귤부터 담기
    for (let count of counts) {
        answer++; // 귤 종류 1가지 추가
        k -= count; // 필요한 귤의 개수(k)에서 현재 종류의 귤 개수 빼기

        // k가 0 이하가 되면 목표한 개수를 다 채운 것이므로 종료
        if (k <= 0) {
            break;
        }
    }

    return answer;
}
