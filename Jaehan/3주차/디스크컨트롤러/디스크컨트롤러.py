import heapq


def solution(jobs):
    ## 작업 하나가 걸린 시간 = 끝난 시각 - 요청한 시각. 이 값들의 평균을 가장 작게 만들어야 한다.
    ## 요청이 들어온 순서대로 처리하면, 오래 걸리는 작업 뒤에 짧은 작업들이 전부 밀려서 손go
    ## 그래서 지금까지 요청된 작업들 중에서 가장 짧은 것부터 처리
    ## 짧은 작업을 빨리 꺼내야 하니까 최소 힙 사용

    n = len(jobs)

    # 요청이 들어온 순서대로 확인해야 하므로 요청 시각 기준으로 정렬
    jobs.sort(key=lambda job: job[0])

    # 대기 중인 작업들을 담아둘 최소 힙
    # (소요 시간, 요청 시각) 순서로 넣으면 소요 시간이 짧은 것부터 나온다.
    waiting = []

    # 아직 힙에 넣지 않은 작업의 위치
    idx = 0

    # 현재 시각
    now = 0

    # 모든 작업이 걸린 시간의 합
    total = 0

    # 처리한 작업 수
    done = 0

    while done < n:

        # 지금 시각까지 요청이 들어온 작업은 전부 대기열에 넣는다.
        while idx < n and jobs[idx][0] <= now:
            start, duration = jobs[idx]
            heapq.heappush(waiting, (duration, start))
            idx += 1

        # 대기열이 비었다면 아직 요청된 작업이 없다는 뜻
        # 다음 작업이 요청되는 시각까지 시간 건너뛰기
        if not waiting:
            now = jobs[idx][0]
            continue

        # 대기 중인 작업 중 가장 짧은 것을 꺼내서 처리한다.
        duration, start = heapq.heappop(waiting)

        # 처리를 끝낸 시각
        now += duration

        # 이 작업이 걸린 시간 = 끝난 시각 - 요청한 시각
        total += now - start

        done += 1

    # 평균 (소수점 아래는 버림)
    return total // n
