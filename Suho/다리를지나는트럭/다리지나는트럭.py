from collections import deque
def solution(bridge_length, weight, truck_weights):
    answer = 0
    # 다리지난 트럭
    complete_truck = []
    # 다리 건너는 트럭
    ing_trucks = deque([0] * bridge_length)
    # 대기 트럭 -> 다리 지났으면 대기 트럭에서 pop
    time =0
    waiting_trucks = deque(truck_weights)
    
    # 다리 위의 총 무게
    current_weight = 0

    while waiting_trucks:
        time += 1

        completed_truck = ing_trucks.popleft()
        current_weight -= completed_truck

        # 다음 트럭이 다리에 올라갈 수 있는 경우
        if current_weight + waiting_trucks[0] <= weight:
            truck = waiting_trucks.popleft()
            ing_trucks.append(truck)
            current_weight += truck

        # 다음 트럭이 올라갈 수 없는 경우 빈 공간 추가
        else:
            ing_trucks.append(0)

    # 마지막 트럭이 다리에 올라간 뒤, 다리를 완전히 빠져나가는 시간
    return time + bridge_length