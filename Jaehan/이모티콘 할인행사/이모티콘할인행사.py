from itertools import product

def solution(users, emoticons):
    ## 할인율은 10, 20, 30, 40 네 가지 중 하나만 가능하고 이모티콘은 최대 7개
    ## -> 할인율 조합은 최대 4^7 = 16,384가지뿐이므로 완전탐색
    ## 목표의 우선순위는 1) 플러스 가입자 수, 2) 이모티콘 매출액 순서다.
    ## 가입자가 늘면 그 사람의 구매는 취소되어 매출이 오히려 줄어들 수 있으므로
    ## 매출만 보고 고르는 그리디로는 답을 찾을 수 없다.

    answer = [0, 0]

    # 1. 이모티콘 개수만큼 할인율을 중복 조합으로 뽑아 모든 경우를 생성
    for rates in product([10, 20, 30, 40], repeat=len(emoticons)):

        plus = 0    # 이모티콘 플러스 가입자 수
        total = 0   # 이모티콘 매출액

        # 2. 각 사용자가 이 할인율 조합에서 어떻게 행동하는지 계산
        for want, limit in users:

            # 2-1. 원하는 비율 이상으로 할인하는 이모티콘은 모두 구매
            spent = 0
            for rate, price in zip(rates, emoticons):
                if rate >= want:
                    # 가격은 100의 배수, 할인율도 10의 배수라 항상 정수로 떨어짐
                    spent += price * (100 - rate) // 100

            # 2-2. 구매 비용이 기준 이상이면 구매를 취소하고 플러스에 가입
            if spent >= limit:
                plus += 1
            else:
                total += spent

        # 3. 가입자 수 -> 매출액 순으로 비교 (리스트 비교는 앞 원소부터 차례로 보므로 우선순위와 일치)
        if [plus, total] > answer:
            answer = [plus, total]

    return answer
