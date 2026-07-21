from math import gcd

def solution(signals):
    answer = 0
    #1열 : G / 2열: Y / 3열: R
    #주기가 존재 -> 첫번째 주기만 확인하면됨
    # 노란불이 겹치지 않으면 -1
    
    cycle = 1
    # 1. 각 신호등의 전체 주기를 구한다.
    for G,Y,R in signals:
        temp_cycle = G + Y + R
        cycle = cycle // gcd(cycle,temp_cycle)*temp_cycle
    # 2. 모든 주기의 최소공배수를 구한다.
    # 최소공배수 = a × b ÷ 최대공약수
    # 3. 0부터 전체 반복 주기 직전까지 확인한다.
    for time in range(cycle):
        # 4.현재 시간에 모든 신호등이 노란불인지 검사한다.
        for G,Y,R in signals:
            #현재 신호등의 주기
            temp_cycle = G + Y + R
            #현재 신호등의 주기 안에서 현재 위치
            current_spot = time % temp_cycle
            #현재 시간 중 하나의 신호등이라도 노란색이 아니면 현재 신호등 종료시키고 다음 시간으로 넘어감.
            if not (G < current_spot<=G+Y):
                break
        # 5. 모두 노란불이면 time을 반환한다.
        #내부 반복문이 break 없이 종료되었다면 모든 신호등이 노란불이라는 뜻이므로 time을 반환한다.
        else:
            return time
    # 6. 끝까지 없으면 -1을 반환한다.
    return -1