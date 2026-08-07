// [[0, 3], [1, 9], [3, 5], [20, 2]]
function solution(jobs) {
    // 1. 요청 시간이 빠른 순서대로 원본 배열 오름차순 정렬
    jobs.sort((a, b) => a[0] - b[0]);

    let currentTime = 0; // 현재 시간
    let totalTime = 0; // 모든 작업의 반환 시간 총합
    let completedJobs = 0; // 완료된 작업의 수
    let jobIndex = 0; // jobs 배열의 인덱스

    const waitingQueue = []; // 대기 큐

    // 2. 모든 작업이 처리될 때까지 반복
    while (completedJobs < jobs.length) {
        // 현재 시간(currentTime)보다 작거나 같은 시점에 요청된 작업을 모두 대기 큐에 삽입
        while (jobIndex < jobs.length && jobs[jobIndex][0] <= currentTime) {
            waitingQueue.push(jobs[jobIndex]);
            jobIndex++;
        }

        if (waitingQueue.length > 0) {
            // 대기 큐에 작업이 있다면 소요 시간이 짧은 순으로 정렬 (SJF 알고리즘)
            waitingQueue.sort((a, b) => a[1] - b[1]);

            // 소요 시간이 가장 짧은 작업을 꺼내서 처리
            const currentJob = waitingQueue.shift();

            currentTime += currentJob[1]; // 현재 시간 갱신
            totalTime += currentTime - currentJob[0]; // 반환 시간 = 완료 시간 - 요청 시간
            completedJobs++; // 완료된 작업 수 증가
        } else {
            // 대기 큐가 비어있다면, 하드디스크가 쉬고 있는 상태이므로
            // 다음 작업의 요청 시간으로 현재 시간을 바로 점프
            currentTime = jobs[jobIndex][0];
        }
    }

    // 3. 평균 반환 시간을 구하고 소수점 이하 버림
    return Math.floor(totalTime / jobs.length);
}
