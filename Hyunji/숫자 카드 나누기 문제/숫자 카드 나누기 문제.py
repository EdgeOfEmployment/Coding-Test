from math import gcd


def solution(arrayA, arrayB):
    # arrayA 전체의 최대공약수
    gcdA = arrayA[0]
    for num in arrayA[1:]:
        gcdA = gcd(gcdA, num)

    # arrayB 전체의 최대공약수
    gcdB = arrayB[0]
    for num in arrayB[1:]:
        gcdB = gcd(gcdB, num)

    answer = 0

    # gcdA가 arrayB의 어떤 수도 나누지 못한다면 후보
    if all(num % gcdA != 0 for num in arrayB):
        answer = gcdA

    # gcdB가 arrayA의 어떤 수도 나누지 못한다면 후보
    if all(num % gcdB != 0 for num in arrayA):
        answer = max(answer, gcdB)

    return answer
