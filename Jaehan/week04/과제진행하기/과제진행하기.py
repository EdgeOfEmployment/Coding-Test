from collections import deque 

def solution(plans):
    result = []
    
    # 1. 시간 통일 함수
    def HourToMin(times):
        hour = int(times.split(':')[0]) * 60
        minute = int(times.split(':')[1])
        return hour + minute
    
    # 2. 시간순 정렬 및 시간 분(min) 단위로 변환
    sorted_plans = sorted(plans, key=lambda x: x[1])
    for i in range(len(sorted_plans)):
        sorted_plans[i][1] = HourToMin(sorted_plans[i][1])
        sorted_plans[i][2] = int(sorted_plans[i][2]) # 소요 시간도 정수로 변환
        
    stopped = deque()
    
    for i in range(len(sorted_plans) - 1):
        cur_name, cur_time, cur_play = sorted_plans[i]
        nxt_name, nxt_time, nxt_play = sorted_plans[i + 1]
        
        # 현재 과제를 끝낸 시각
        finish_time = cur_time + cur_play
        
        # 1) 현재 과제를 끝낸 시각이 다음 과제 시작 시각보다 늦을 때 (시간 초과)
        if finish_time > nxt_time:
            stopped.append([cur_name, finish_time - nxt_time])
            cur_time = nxt_time # 시각을 다음 과제 시작 시각으로 동기화
        
        # 2) 현재 과제를 끝내고도 다음 과제 시작까지 시간이 남을 때
        else:
            result.append(cur_name)
            cur_time = finish_time # 현재 과제가 끝난 시각부터 여유 시간 활용
            
            # 남은 시간 동안 멈춰둔 과제(stopped) 처리
            while stopped and cur_time < nxt_time:
                stop_name, stop_time = stopped.pop()
                
                # 멈춰둔 과제를 마저 끝냈을 때 다음 과제 시작 시각보다 빠르거나 같으면
                if cur_time + stop_time <= nxt_time:
                    cur_time += stop_time
                    result.append(stop_name)
                # 다 못 끝내면 남은 시간만 갱신해서 다시 스택에 삽입
                else:
                    stopped.append([stop_name, stop_time - (nxt_time - cur_time)])
                    cur_time = nxt_time
                    break

    # 마지막 과제 처리
    result.append(sorted_plans[-1][0])
    
    # 마지막 과제까지 끝낸 후 스택에 남아 있는 과제들을 역순(최근에 멈춘 것부터)으로 처리
    while stopped:
        stop_name, stop_time = stopped.pop()
        result.append(stop_name)
        
    return result