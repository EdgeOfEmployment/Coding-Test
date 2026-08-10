# 노란불 신호등 문제 풀이 공유

## 1. 문제 접근법

- **주기성 파악**: 신호등 `i`의 주기는 `G+Y+R`이며, 항상 초록 → 노랑 → 빨강 순서로 반복됩니다. 시각 `t`에서의 상태는 `(t-1) % period` 값이 `[0, G)`이면 초록, `[G, G+Y)`이면 노랑, 나머지는 빨강입니다.

- **탐색 범위 한정**: `n`개 신호등의 상태 조합은 각 신호등 주기의 최소공배수(LCM) 시점마다 정확히 반복됩니다. 따라서 답이 존재한다면 반드시 `LCM(period_1, ..., period_n)` 이내에 처음 등장하므로, 이 범위까지만 브루트포스로 탐색하면 충분합니다.

- **제한사항 활용**: `n ≤ 5`, 각 주기가 `3 ~ 20`이므로 LCM이 커도 수십만 수준에 불과해 전수 탐색이 시간 내에 충분히 가능합니다.

## 2. 해결 코드

```python
from math import gcd
from functools import reduce


def solution(signals):
    periods = [g + y + r for g, y, r in signals]
    lcm = reduce(lambda a, b: a * b // gcd(a, b), periods)

    for t in range(1, lcm + 1):
        if all(_is_yellow(t, g, y, g + y + r) for g, y, r in signals):
            return t

    return -1


def _is_yellow(t, green, yellow, period):
    phase = (t - 1) % period
    return green <= phase < green + yellow
```

## 3. 구현 전략 및 이유

### LCM까지만 탐색

각 신호등은 자신의 주기마다 상태가 그대로 반복되는 순환 구조입니다. 여러 개의 순환 구조가 동시에 특정 상태 조합에 도달하는 최초 시점은, 개별 주기들의 LCM을 넘어서면 반드시 그 이전에 이미 등장했거나 영원히 등장하지 않는다는 성질을 가집니다. 이 성질 덕분에 무한히 탐색하지 않고 LCM까지만 브루트포스로 확인해도 정답성을 보장할 수 있습니다.

### 1초부터 시작하는 시간 처리

문제에서 시간은 1초부터 시작하고 각 신호등은 처음에 초록불이라고 했으므로, `t`초 시점의 위상은 `(t-1) % period`로 계산해야 합니다. `t=1`일 때 위상이 0이 되어 초록불 구간의 시작과 정확히 맞아떨어지도록 오프셋을 보정했습니다.

### 존재하지 않는 경우 처리

LCM까지 전부 탐색해도 모든 신호등이 동시에 노란불이 되는 시각을 찾지 못하면, 그런 시각은 존재하지 않는다는 뜻이므로 -1을 반환합니다.
