# 카운트 다운 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/131129)

## 1. 접근법과 단순 풀이의 한계

한 번에 얻을 수 있는 점수는 아래와 같습니다.

| 구분 | 점수 | 싱글 또는 불 |
| --- | --- | --- |
| 싱글 | 1 ~ 20 | O |
| 더블 | 2, 4, ..., 40 (2의 배수) | X |
| 트리플 | 3, 6, ..., 60 (3의 배수) | X |
| 불 | 50 | O |

`target`점을 **가장 적은 횟수**로 만들고, 그 횟수 안에서 **싱글이나 불을 맞힌 횟수는 가장 많게** 만들어야 합니다.

큰 점수부터 최대한 빼는 방식은 횟수부터 틀립니다.

- `target = 101`일 때 가장 큰 60을 먼저 쓰면 41이 남는데, 41은 한 번에 만들 수 없습니다. (홀수라 더블이 안 되고, 3의 배수가 아니라 트리플도 안 되고, 20보다 커서 싱글도 안 됩니다.)
- 그래서 40 + 1로 나뉘어 **3번**이 되지만, 51 + 50으로 던지면 **2번**이면 끝납니다.

무엇을 먼저 쓸지가 아니라 조합 전체를 봐야 하는 문제입니다.

## 2. 단순 접근 코드

```python
def solution(target):
    ## 큰 점수부터 최대한 빼는 방식
    ## target = 101 에서 60 + 40 + 1 로 [3, 1]을 반환 (정답은 [2, 1])

    scores = sorted({n for n in range(1, 21)} |
                    {n * 2 for n in range(1, 21)} |
                    {n * 3 for n in range(1, 21)} |
                    {50}, reverse=True)

    count = 0
    single = 0
    while target > 0:
        for score in scores:
            if score <= target:
                target -= score
                count += 1
                if score <= 20 or score == 50:
                    single += 1
                break

    return [count, single]
```

## 3. 개선한 접근법

점수를 하나씩 계속 더해서 `target`을 만드는 문제이고 순서는 상관없으므로 **DP**로 풀 수 있습니다.

- **표 두 개를 만듭니다.**
  - `dp_count[i]` : `i`점을 만드는 데 필요한 최소 횟수
  - `dp_single[i]` : 그 최소 횟수일 때 싱글이나 불을 맞힌 최대 횟수
- **채우는 방법**: 마지막에 `score`를 던졌다고 하면 그 전에는 `i - score`점이었으므로, `dp_count[i - score] + 1`과 `dp_single[i - score] + (싱글이면 1)`이 후보가 됩니다. 모든 점수를 후보로 넣어보고 가장 좋은 값을 고릅니다.
- **고르는 기준이 두 개**인 게 이 문제의 핵심입니다. **횟수가 적은 쪽이 먼저**고, **횟수가 같으면 싱글/불이 많은 쪽**을 고릅니다.
- **겹치는 점수 정리**: 같은 점수를 여러 방법으로 만들 수 있으면(예: `6 = 싱글 6 = 더블 3 = 트리플 2`) 싱글로 세는 게 항상 이득입니다. 점수를 딕셔너리에 모아 중복을 없애고, 싱글을 마지막에 넣어 덮어쓰는 방식으로 처리했습니다.

## 4. 해결 코드

```python
def solution(target):
    ## 점수를 하나씩 계속 더해서 target을 만드는 문제라서 DP로 푼다.
    ##   dp_count[i]  = i점을 만드는 데 필요한 최소 횟수
    ##   dp_single[i] = 그 최소 횟수일 때 싱글이나 불을 맞힌 최대 횟수
    ## 큰 점수부터 빼는 방식은 답이 안 된다.
    ## 예) 101점은 60 + 40 + 1 로 3번이 되지만, 51 + 50 으로 던지면 2번이면 된다.

    # 한 번에 얻을 수 있는 점수를 모은다.
    # {점수: 싱글이나 불이면 1, 아니면 0}
    score_table = {}

    # 더블(2배), 트리플(3배)은 싱글도 불도 아니므로 0
    for n in range(1, 21):
        score_table[n * 2] = 0
        score_table[n * 3] = 0

    # 불은 50점
    score_table[50] = 1

    # 싱글은 1 ~ 20점
    # 6점처럼 더블/트리플과 겹치는 점수는 싱글로 세는 게 이득이라 나중에 덮어쓴다.
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

            # 마지막에 score를 던졌다고 하면, 그 전에는 i - score 점이었다.
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
```

## 5. 구현 전략 및 이유

### 점수를 딕셔너리에 모은 이유
싱글 20개 + 더블 20개 + 트리플 20개 + 불 1개는 겹치는 값이 많습니다(`6 = 싱글 6 = 더블 3 = 트리플 2`). 딕셔너리 키로 모으면 중복이 알아서 사라져서 안쪽 반복이 61번에서 **42번**으로 줄어듭니다. 그리고 더블/트리플을 먼저 `0`으로 넣고 싱글을 나중에 `1`로 덮어쓰기 때문에, "겹치면 싱글로 센다"는 규칙도 따로 조건문을 쓸 필요 없이 처리됩니다.

### 표를 두 개로 나눈 이유
횟수와 싱글 횟수를 튜플 하나로 묶어도 되지만, 리스트 두 개로 나누면 튜플을 만들고 푸는 비용이 없어서 `target`이 100,000일 때 속도 차이가 납니다. 대신 두 표를 항상 같이 갱신해야 해서 `best_count`, `best_single`을 함께 구한 뒤 마지막에 한 번만 넣습니다.

### `count < best_count or (count == best_count and single > best_single)`
문제에서 요구하는 순서(횟수 최소 → 싱글/불 최대)를 그대로 옮긴 조건입니다. 순서를 바꿔서 싱글/불을 먼저 비교하면 횟수가 더 많은 답이 뽑혀 틀립니다.

### 검증
예제인 `target = 21 → [1, 0]`, `target = 58 → [2, 2]`를 확인했고, `1 ~ 399`를 따로 만든 DP 결과와 비교해서 전부 같은 답이 나왔습니다. 가장 큰 입력인 `target = 100,000`은 약 0.7초 걸려 `[1667, 2]`를 반환합니다.

### 시간복잡도
- 시간복잡도: $O(\text{target} \times 42)$, 사실상 $O(\text{target})$
- 공간복잡도: $O(\text{target})$
