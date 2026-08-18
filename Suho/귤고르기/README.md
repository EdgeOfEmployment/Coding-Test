# 귤 고르기

- 문제 링크: [프로그래머스 138476번 - 귤 고르기](https://school.programmers.co.kr/learn/courses/30/lessons/138476)
- 사용 알고리즘/자료구조: 그리디, 해시맵(`Counter`), 정렬

## 1. 문제 이해

귤 `k`개를 선택할 때, 포함되는 귤 크기의 종류를 최소화하는 문제다. 한 크기의 귤을 많이 선택할수록 필요한 종류가 줄어들기 때문에, 크기별 개수를 센 다음 개수가 많은 종류부터 선택한다.

## 2. 풀이 과정

1. `Counter`로 크기별 귤의 개수를 구한다.
2. `(귤 크기, 개수)`를 개수 기준 내림차순으로 정렬한다.
3. 개수가 많은 종류부터 누적한다.
4. 누적 개수가 `k` 이상이 되는 순간, 지금까지 선택한 종류 수를 반환한다.

```python
from collections import Counter

def solution(k, tangerine):
    answer = 0
    total = 0

    c_tangerine = sorted(
        Counter(tangerine).items(),
        key=lambda x: x[1],
        reverse=True
    )

    for key, val in c_tangerine:
        total += val
        answer += 1

        if total >= k:
            return answer

    return answer
```

## 3. AI에게 도움받은 부분

`sorted()`로 `Counter.items()`를 정렬하는 과정과 `lambda` 표현식 사용법은 AI의 도움을 받았다.

```python
sorted(Counter(tangerine).items(), key=lambda x: x[1], reverse=True)
```

- `Counter(tangerine).items()`는 각 항목을 `(귤 크기, 개수)` 형태로 가져온다.
- `x[0]`은 귤의 크기, `x[1]`은 해당 크기의 개수다.
- `key=lambda x: x[1]`은 두 번째 값인 개수를 정렬 기준으로 사용한다는 의미다.
- `reverse=True`는 개수가 많은 순서로 정렬하기 위해 사용한다.

## 4. 시간복잡도

`tangerine`의 길이를 `n`, 서로 다른 귤 크기의 수를 `m`이라고 하면:

- `Counter(tangerine)`: 모든 귤을 한 번 확인하므로 `O(n)`
- 크기별 개수 정렬: `m`개 항목을 정렬하므로 `O(m log m)`
- 정렬된 항목 순회: 최악의 경우 모든 종류를 확인하므로 `O(m)`

따라서 전체 시간복잡도는 `O(n + m log m)`이다. `m <= n`이므로 최악의 경우 `O(n log n)`으로 표현할 수 있다.

공간복잡도는 `Counter`와 정렬 결과에 최대 `m`개의 항목을 저장하므로 `O(m)`, 최악의 경우 `O(n)`이다.
