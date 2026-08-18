def solution(enroll, referral, seller, amount):
    ## 판매원이 칫솔을 팔면 이익의 10%를 자신의 추천인에게 넘기고 나머지를 가진다.
    ## 넘겨받은 추천인도 똑같이 10%를 자신의 추천인에게 넘긴다.
    ## 즉 판매원부터 시작해서 추천인을 타고 위로 올라가며 금액이 1/10씩 줄어드는 구조다.
    ## 민호("-")까지 올라가거나, 넘길 금액이 1원 미만이 되면 분배가 끝난다.

    # 각 판매원의 추천인을 바로 찾을 수 있도록 이름 -> 추천인 매핑을 만든다.
    # 추천인이 "-"인 사람은 민호가 추천인이므로 더 올라갈 곳이 없다는 뜻으로 None을 넣는다.
    parent = {}
    for i, name in enumerate(enroll):
        parent[name] = None if referral[i] == "-" else referral[i]

    # 각 판매원이 최종적으로 가지게 되는 금액
    total = {name: 0 for name in enroll}

    # 판매 기록을 하나씩 처리한다.
    for name, count in zip(seller, amount):

        # 칫솔 한 개의 가격은 100원이다.
        money = count * 100

        # 판매원 본인부터 시작해서 추천인을 타고 위로 올라간다.
        curr = name

        # 민호에게 도달했거나(curr is None), 넘겨받은 금액이 0이 되면 분배를 멈춘다.
        while curr is not None and money > 0:

            # 추천인에게 넘길 10% (1원 미만은 버림 -> 정수 나눗셈)
            fee = money // 10

            # 넘기고 남은 금액은 자신이 가진다.
            # money가 10원 미만이면 fee가 0이 되어 전액을 본인이 가지고 반복이 끝난다.
            total[curr] += money - fee

            # 넘길 금액을 들고 추천인에게 올라간다.
            money = fee
            curr = parent[curr]

    # enroll에 등장하는 순서대로 금액을 반환한다.
    return [total[name] for name in enroll]
