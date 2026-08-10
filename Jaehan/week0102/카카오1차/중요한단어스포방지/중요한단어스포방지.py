def solution(message, spoiler_ranges):
    ## 1. 한 단어가 여러 스포 구간에 걸쳐 있을 수 있음 -> 겹치는 구간 중 "가장 나중에 클릭되는
    ##    구간"(배열에서 제일 뒤 인덱스, 이미 왼쪽->오른쪽 클릭 순서로 정렬돼서 옴) 시점에 완전히 공개됨
    ## 2. 겹치는 구간이 하나도 없는 단어 = 스포 방지 구간 밖에서 등장한 단어 (조건 2 위반용으로 미리 모아둠)
    ## 3. 중요한 단어 조건: 스포 단어 O, 밖에서 등장한 적 X, 이전에 공개된 스포 단어와 중복 X
    ## 4. 같은 시점에 여러 단어가 동시에 공개되면 왼쪽부터 순서대로 판정 (동일 텍스트 중복 처리 때문에 순서 중요)
    words = _split_words(message)
    ranges = spoiler_ranges

    outside_texts = set()
    last_range_of = {}  # word index -> last overlapping range index

    for idx, (ws, we, text) in enumerate(words):
        overlaps = [
            r for r, (rs, re) in enumerate(ranges)
            if rs <= we and ws <= re
        ]
        if overlaps:
            last_range_of[idx] = max(overlaps)
        else:
            outside_texts.add(text)

    # 구간 인덱스별로 "이 구간이 클릭되는 순간 완전히 공개되는 단어들"을 모아둠
    words_by_last_range = [[] for _ in range(len(ranges))]
    for idx, r in last_range_of.items():
        words_by_last_range[r].append(idx)

    revealed_texts = set()
    answer = 0

    for r in range(len(ranges)):
        for idx in sorted(words_by_last_range[r]):  # left to right
            text = words[idx][2]
            if text in outside_texts or text in revealed_texts:
                continue
            revealed_texts.add(text)
            answer += 1

    return answer


def _split_words(message):
    words = []
    n = len(message)
    i = 0
    while i < n:
        if message[i] == ' ':
            i += 1
            continue
        j = i
        while j < n and message[j] != ' ':
            j += 1
        words.append((i, j - 1, message[i:j]))
        i = j
    return words
