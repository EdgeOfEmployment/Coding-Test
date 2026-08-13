import math
def solution(fees, records):
   
    in_time = {}  # 차량번호: 입차 시간
    total_time = {} # 차량번호: 누적 주차 시간
    answer = []
    # 차량 기준으로 다시 정렬.
    # out에서 가장 최근의 in시간을 빼야함.
    for record in records:
        time,car_num,history = record.split()
        #시간 -> 분으로 바꾸기
        hour, minute = map(int, time.split(":"))
        time_min = hour * 60 + minute

        if history == "IN":
            # 입차 시간 저장
            in_time[car_num] =time_min

        else:
            # 주차 시간 계산
            park_t = time_min-in_time[car_num] 
            # total_time에 누적
            total_time[car_num] = total_time.get(car_num, 0) + park_t
            # in_time에서 해당 차량 제거
            del in_time[car_num]
    # in_time에 남아 있는 미출차 차량 처리
    # 만약 출차 되지 않았다면, 23:59에서 입차한 시간을 빼야함.
    if in_time:
        for car_num in in_time:
            park_t = 23*60+59-in_time[car_num] 
            total_time[car_num] = total_time.get(car_num, 0) + park_t
        
    answer = []

    # 차량별 요금 계산
    #올림함수
    #math.ceil(숫자)
    # 기본 시간 180분(fees[0]) 보다 작으면 5000원(fees[1])
    #180분 보다 크면 5000원 + math.ceil((누적주차시간-180)/10 =(fees[2])) * 600 =(fees[3])
    for car_num in sorted(total_time):
        v = total_time[car_num]
        if fees[0]>=v:
            total_fee = fees[1]
        else:
            total_fee = fees[1] +  math.ceil((v-fees[0])/fees[2]) * fees[3]
        # answer에 추가
        answer.append(total_fee)
    
    return answer