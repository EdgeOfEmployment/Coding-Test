def solution(plans):
    answer = []
    #선점스케줄링 / 스택 쓰는 문제
    #시간 -> 분으로 계산해서 시간 빼서 그 시간만큼 전작업 가능
    # 리스트의 [1]를 기준으로 sort해서 정렬하기.
    converted_plans =[]
    plans.sort(key=lambda x: x[1])
    #print(plans)
    for name, start, time in plans:
        #print(name,start,time)
        h, m = map(int,start.split(":"))
        start_m = h * 60 + m
        converted_plans.append([name,start_m,int(time)])
        
    pending_tasks =[] #잠시 멈춘 과제: [과제명, 남은 시간]
    
    #해당 과제의 time에서 rest_time을 스택에 같이 넣어두기
    for i in range(len(converted_plans) - 1):
        current_name, current_start, current_time = converted_plans[i]
        next_name, next_start, next_time = converted_plans[i + 1]

        
        # 다음 과제가 시작될 때까지 사용할 수 있는 시간
        available_time = next_start - current_start
        
        # 현재 과제를 끝내지 못함
        if current_time > available_time:
            
            #rest_time = 기존작업시각 - 다음에 온 과제 시각
            rest_time = current_time - available_time
            pending_tasks.append([current_name, rest_time])

    # 현재 과제를 완료함    
        else: 
            answer.append(current_name)

            # 과제를 끝내고 남은 시간
            extra_time = available_time - current_time

            # 남은 시간 동안 멈춰둔 과제 수행
            while pending_tasks and extra_time > 0:
                pending_name, pending_time = pending_tasks.pop()

                if pending_time <= extra_time:
                    extra_time -= pending_time
                    answer.append(pending_name)
                else:
                    pending_tasks.append([
                        pending_name,
                        pending_time - extra_time
                    ])
                    extra_time = 0

    #마지막 과제는 방해하는 다음 과제가 없으므로 완료
    answer.append(converted_plans[-1][0])

    #멈춘 과제를 최근에 멈춘 순서부터 완료
    while pending_tasks:
        name, remaining_time = pending_tasks.pop()
        answer.append(name)
    return answer