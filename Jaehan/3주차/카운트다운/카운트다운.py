def solution(target):
    ## 최소하느이 다트로 0점, 싱글 or 불을 최대한 많이
    ## 점수를 하나씩 계속 더해서 target을 만드는 문제 -> DP로
    ##   dp_count[i]  = i점을 만드는 데 필요한 최소 횟수
    ##   dp_single[i] = 그 최소 횟수일 때 싱글이나 불을 맞힌 최대 횟수
    ## 큰 점수부터 빼는 방식은 답이 안 된다.
    ## 예) 101점은 60 + 40 + 1 로 3번이 되지만, 51 + 50 으로 던지면 2번이면 된다.

    # 한 번에 얻을 수 있는 점수 모으기, 점수 테이블(해시 생성)ㄴ
    # {점수: 싱글이나 불이면 1, 아니면 0}
    score_table = {}

    # 더블(2배), 트리플(3배)은 싱글도 불도 아니므로 0
    for n in range(1, 21):
        score_table[n * 2] = 0
        score_table[n * 3] = 0

    # 불은 50점
    score_table[50] = 1

    # 싱글은 1 ~ 20점
    # 6점처럼 더블/트리플과 겹치는 점수는 싱글로 세는 게 이득 -> 더블, 트리플 값에서 덮어쓰기.
    for n in range(1, 21):
        score_table[n] = 1

    # (점수, 싱글 여부) 목록
    scores = list(score_table.items())

    INF = float('inf')

    # dp 테이블
    dp_count = [INF] * (target + 1)
    dp_single = [0] * (target + 1)

    # 0점은 한 번도 던지지 않은 상태
    dp_count[0] = 0
    dp_single[0] = 0

    for i in range(1, target + 1):

        best_count = INF
        best_single = 0

        for score, is_single in scores:

            # 마지막에 score를 던졌다고 하면, 그 전에는 i - score 점
            if score > i:
                continue

            count = dp_count[i - score] + 1
            single = dp_single[i - score] + is_single

            # 던진 횟수가 적은 쪽이 우선
            # 횟수가 같으면 싱글/불이 많은 쪽을 고른다.
            if count < best_count or (count == best_count and single > best_single):
                best_count = count
                best_single = single

        dp_count[i] = best_count
        dp_single[i] = best_single

    return [dp_count[target], dp_single[target]]
