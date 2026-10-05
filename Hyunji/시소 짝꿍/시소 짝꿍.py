from collections import Counter


def solution(weights):
    # 몸무게별 인원 수를 저장한다.
    counts = Counter(weights)
    answer = 0

    for weight, count in counts.items():
        # 같은 몸무게인 사람 중 서로 다른 두 명을 고르는 경우의 수
        answer += count * (count - 1) // 2

        # weight가 더 가벼운 쪽일 때 가능한 무거운 몸무게 비율
        # (가벼운 사람의 거리, 무거운 사람의 거리): (3, 2), (4, 2), (4, 3)
        for numerator, denominator in ((3, 2), (2, 1), (4, 3)):
            heavier_times = weight * numerator

            # 몸무게는 정수이므로 나누어떨어지지 않으면 짝꿍이 없다.
            if heavier_times % denominator != 0:
                continue

            heavier = heavier_times // denominator
            answer += count * counts.get(heavier, 0)

    return answer
