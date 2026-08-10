# 중요한 단어를 스포 방지 문제 풀이 공유

## 1. 문제 접근법

- **단어 분리**: 메시지를 공백 기준으로 분리하여 각 단어의 시작/끝 인덱스와 텍스트를 기록합니다.

- **스포일러 단어 판별**: 각 단어에 대해, 겹치는 스포 방지 구간들의 인덱스를 모두 찾습니다. 겹치는 구간이 하나도 없으면 그 단어는 "스포 방지 구간이 아닌 곳에 등장한 단어"로 분류합니다 (조건 2 위반 대상).

- **공개 시점 결정**: 한 단어가 여러 스포 방지 구간에 걸쳐 있을 수 있으므로, 겹치는 구간들 중 **가장 나중에 클릭되는 구간**(배열에서 가장 뒤에 있는 구간, 배열이 이미 왼쪽→오른쪽 클릭 순서로 정렬되어 주어짐)의 시점에 그 단어가 완전히 공개된다고 봅니다.

- **좌→우, 중복 처리**: 같은 구간 클릭 시점에 동시에 공개되는 단어가 여러 개 있으면 왼쪽부터 순서대로 판정하며, 이미 공개된 스포 단어와 텍스트가 같으면 건너뜁니다.

## 2. 해결 코드

```python
def solution(message, spoiler_ranges):
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
```

## 3. 구현 전략 및 이유

### "마지막으로 겹치는 구간"을 공개 시점으로 사용

한 단어가 여러 스포 방지 구간에 걸쳐 있을 수 있다는 문제 조건 때문에, 단어가 완전히 공개되려면 그 단어와 겹치는 **모든** 구간이 클릭되어야 합니다. 구간은 이미 왼쪽부터 순서대로(클릭 순서대로) 정렬되어 주어지므로, 겹치는 구간 인덱스 중 최댓값이 곧 그 단어가 실제로 완전히 공개되는 시점이 됩니다. 이 시점보다 앞선 구간들만 클릭된 상태에서는 단어의 일부만 보이므로 아직 판정 대상이 아닙니다.

### `outside_texts`로 조건 2를 미리 계산

"스포 방지 구간이 아닌 곳에서 등장한 적이 있는지"는 전체 단어 목록을 한 번 훑어 겹치는 구간이 전혀 없는 단어들의 텍스트를 모아두면 O(1)에 판별할 수 있습니다. 이렇게 사전 계산해두면 각 구간 처리 시점마다 매번 전체 메시지를 다시 스캔할 필요가 없습니다.

### 같은 시점에 공개되는 단어의 좌→우 순서 처리

동일한 구간 클릭으로 동시에 여러 단어가 공개될 수 있으므로, 이 단어들을 시작 인덱스 기준으로 정렬한 뒤 순서대로 처리하면서 `revealed_texts`를 그때그때 갱신합니다. 이렇게 하면 같은 시점에 동일한 텍스트의 단어가 두 번 등장해도 왼쪽 것만 중요한 단어로 인정되고 오른쪽 것은 자동으로 중복 처리됩니다.
