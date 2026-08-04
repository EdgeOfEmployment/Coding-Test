// 우선 시작 시간 순으로 정렬
// stack에 넣고 다음 차례가 들어갈때 시작시간 + 작업시간 < 다음 시작시간이면 pop 후 push
// 시작시간 + 작업시간 > 다음 시작시간 남은 작업시간 업데이트 후 push
function solution(plans) {
    var answer = [];

    // "hh:mm" 포맷의 시간을 분 단위 숫자로 변환해주는 헬퍼 함수
    const timeToMinutes = (time) => {
        const [h, m] = time.split(':').map(Number);
        return h * 60 + m;
    };

    // 1. 우선 시작 시간 순으로 정렬을 위해 데이터 가공 및 정렬
    const sortedPlans = plans
        .map(([name, start, playtime]) => [name, timeToMinutes(start), Number(playtime)])
        .sort((a, b) => a[1] - b[1]);

    // 잠시 멈춘 과제를 담아둘 stack: [과제명, 남은 작업시간]
    const stack = [];

    // 2. 작업 진행 로직
    for (let i = 0; i < sortedPlans.length - 1; i++) {
        const [currName, currStart, currPlaytime] = sortedPlans[i];
        const nextStart = sortedPlans[i + 1][1];

        // 다음 과제 시작 전까지 내가 쓸 수 있는 잉여 시간
        let availableTime = nextStart - currStart;

        // [조건 1] 시작시간 + 작업시간 <= 다음 시작시간 (현재 과제를 끝낼 수 있는 경우)
        if (currPlaytime <= availableTime) {
            answer.push(currName);
            availableTime -= currPlaytime; // 현재 과제를 끝내고 남은 시간

            // 남은 시간이 있고, 멈춰둔 과제(스택)가 있다면 꺼내서(pop) 진행
            while (availableTime > 0 && stack.length > 0) {
                const [pausedName, pausedPlaytime] = stack.pop();

                if (pausedPlaytime <= availableTime) {
                    // 멈춰둔 과제도 다 끝낼 수 있다면
                    answer.push(pausedName);
                    availableTime -= pausedPlaytime;
                } else {
                    // 멈춰둔 과제를 다 못 끝낸다면 남은 시간 업데이트 후 다시 push
                    stack.push([pausedName, pausedPlaytime - availableTime]);
                    availableTime = 0; // 남은 시간을 다 썼으므로 종료
                }
            }
        }
        // [조건 2] 시작시간 + 작업시간 > 다음 시작시간 (현재 과제를 다 못 끝내는 경우)
        else {
            // 남은 작업시간 업데이트 후 push
            stack.push([currName, currPlaytime - availableTime]);
        }
    }

    // 3. 루프가 끝나면 마지막 남은 새 과제를 마저 끝냄
    answer.push(sortedPlans[sortedPlans.length - 1][0]);

    // 4. 스택에 남아있는(멈춰둔) 과제들을 최근에 멈춘 순서(LIFO)대로 마저 끝냄
    while (stack.length > 0) {
        answer.push(stack.pop()[0]);
    }

    return answer;
}
