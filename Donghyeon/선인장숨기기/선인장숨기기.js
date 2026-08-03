// function solution(m, n, h, w, drops) {
//     var answer = [];

//     // 전체 순환
//     // 크기만큼 2중 확인
//     // 해당 영역중 가장 빨리 떨어지는 시간을 좌상단에 표시
//     // 가장 느린곳을 찾음
//     // 동일하면 제일 처음 좌표

//     let graph = Array.from({ length : m }, () => Array(n).fill(0));
//     let record = Array.from({ length : m }, () => Array(n).fill(0));

//     for (let i = 0; i < drops.length; i++) {
//         graph[drops[i][0]][drops[i][1]] = i + 1;
//     }

//     for (let i = 0; i < m - h + 1; i++) {
//         for (let j = 0; j < n - w + 1; j++) {

//             let min = Infinity;

//             for (let p = i; p < i + h; p++) {
//                 for (let q = j; q < j + w; q++) {
//                     if  (graph[p][q] != 0 && min > graph[p][q])
//                         min = graph[p][q];
//                 }
//             }
//             if (min != Infinity)
//                 record[i][j] = min;
//         }
//     }

//     let max = 0;

//     for (let i = m - h; i >= 0; i--) {
//         for (let j = n - w; j >= 0; j--) {
//             if (record[i][j] == 0) {
//                 answer = [i, j];
//                 max = -1;
//             }

//             if (max != -1 && max <= record[i][j]) {
//                 answer = [i, j];
//                 max = record[i][j];
//             }
//         }
//     }

//     return answer;
// }

function solution(m, n, h, w, drops) {
    // 1. 격자 초기화: 비가 오지 않는 칸은 가장 늦은 시간보다 큰 Infinity로 설정
    const graph = Array.from({ length: m }, () => Array(n).fill(Infinity));
    for (let i = 0; i < drops.length; i++) {
        const [r, c] = drops[i];
        graph[r][c] = i + 1;
    }

    // 2. 가로 방향 슬라이딩 윈도우 최솟값 계산 (길이 w)
    // rowMin[i][j]: i번째 행에서 j열부터 j+w-1열 사이의 최솟값
    const rowMin = Array.from({ length: m }, () => Array(n - w + 1));

    for (let i = 0; i < m; i++) {
        const deque = []; // 열 인덱스를 저장하는 모노토닉 큐
        let head = 0; // shift() 대신 사용할 앞쪽 포인터 (O(1) 성능 유지)

        for (let j = 0; j < n; j++) {
            // (1) 윈도우 범위(j - w)를 벗어난 인덱스는 앞에서 제거
            if (head < deque.length && deque[head] <= j - w) {
                head++;
            }
            // (2) 새로 들어올 값보다 작거나 같은 값이 나올 때까지 뒤에서부터 제거 (오름차순 유지)
            while (deque.length > head && graph[i][deque[deque.length - 1]] >= graph[i][j]) {
                deque.pop();
            }
            deque.push(j);

            // (3) 윈도우 크기 w가 채워진 시점부터 rowMin에 기록
            if (j >= w - 1) {
                rowMin[i][j - w + 1] = graph[i][deque[head]];
            }
        }
    }

    // 3. 세로 방향 슬라이딩 윈도우 최솟값 계산 (길이 h)
    // rectMin[i][j]: (i, j)를 좌상단으로 하는 h x w 구역 내의 최솟값
    const rectMin = Array.from({ length: m - h + 1 }, () => Array(n - w + 1));

    for (let j = 0; j <= n - w; j++) {
        const deque = []; // 행 인덱스를 저장하는 모노토닉 큐
        let head = 0;

        for (let i = 0; i < m; i++) {
            // (1) 윈도우 범위(i - h)를 벗어난 인덱스는 앞에서 제거
            if (head < deque.length && deque[head] <= i - h) {
                head++;
            }
            // (2) 새로 들어올 값보다 작거나 같은 값이 나올 때까지 뒤에서부터 제거
            while (deque.length > head && rowMin[deque[deque.length - 1]][j] >= rowMin[i][j]) {
                deque.pop();
            }
            deque.push(i);

            // (3) 윈도우 크기 h가 채워진 시점부터 rectMin에 기록
            if (i >= h - 1) {
                rectMin[i - h + 1][j] = rowMin[deque[head]][j];
            }
        }
    }

    // 4. 최적의 좌상단 좌표 탐색
    let maxVal = -1;
    let answer = [0, 0];

    // 행 우선(Top -> Down), 열 우선(Left -> Right) 순서로 탐색
    for (let i = 0; i <= m - h; i++) {
        for (let j = 0; j <= n - w; j++) {
            // 이전 최댓값보다 '엄격히 클 때만(>)' 갱신해야
            // 가장 위쪽 행, 그리고 가장 왼쪽 열의 조건이 자연스럽게 보장됨
            if (rectMin[i][j] > maxVal) {
                maxVal = rectMin[i][j];
                answer = [i, j];
            }
        }
    }

    return answer;
}
