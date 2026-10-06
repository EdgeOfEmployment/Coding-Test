# 당구 연습 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/169198)

## 1. 접근법과 단순 풀이의 한계

가로 $m$, 세로 $n$인 당구대에서 $(startX, startY)$에 있는 공을 쳐서 **반드시 벽을 한 번 맞힌 뒤** 목표 공을 맞혀야 합니다. 목표 공마다 공이 굴러간 **최소 거리의 제곱**을 구하는 문제입니다.

- **발사 각도를 탐색하는 접근의 한계**: 각도를 조금씩 바꿔가며 반사 경로를 시뮬레이션하는 방법이 먼저 떠오릅니다. 하지만 각도는 **연속적인 값이라 후보를 유한하게 나눌 수 없고**, 쪼개는 간격을 좁혀도 정확한 최솟값에 도달한다는 보장이 없습니다. 문제가 요구하는 답은 **정수인 거리의 제곱**인데, 실수 각도로 근사한 경로는 그 정수를 정확히 되돌려주지 못합니다.
- **입사각과 반사각으로 접점을 구하는 접근의 한계**: 각도를 포기하고 "벽의 어느 지점에서 튕기는가"를 변수로 두어도, 입사각과 반사각이 같다는 조건에서 삼각 비례식을 세워 접점을 구하고 **꺾인 두 선분의 길이를 각각 더해야** 합니다. 네 벽마다 식의 형태가 달라지고 제곱근이 중간에 끼어들어 실수 오차가 누적됩니다.
- **네 벽의 대칭점을 모두 후보로 쓰는 접근의 한계**: 대칭 이동으로 경로를 직선화하면 계산은 간단해지지만, **벽에 맞기 전에 목표 공을 먼저 맞히는 경우**가 걸러지지 않습니다. 예를 들어 시작점 $(3, 7)$과 목표 공 $(2, 7)$은 같은 Y선상에 있는데, 왼쪽 벽을 노려 공을 쏘면 벽에 닿기도 전에 목표 공을 때리므로 반칙입니다. 이 후보를 그대로 최솟값에 넣으면 답이 실제보다 작아집니다.
- **방향 수정**: 시작점을 **네 벽에 대해 대칭 이동**시켜 꺾인 경로를 직선 하나로 바꾸고, **같은 선상에서 목표 공이 벽보다 앞에 놓인 방향만 후보에서 제외**하는 방식으로 접근을 정리했습니다.

## 2. 해결 코드

```java
import java.util.*;

class Solution {
    public int[] solution(int m, int n, int startX, int startY, int[][] balls) {
        int[] answer = new int[balls.length];

        for (int i = 0; i < balls.length; i++) {
            int targetX = balls[i][0];
            int targetY = balls[i][1];
            
            int minDistanceSquare = Integer.MAX_VALUE;

            // 1. 왼쪽 벽 대칭 (-startX, startY)
            // 같은 Y선상이고 타겟 공이 시작 공보다 왼쪽에 있으면 왼쪽 벽쿠션 불가
            if (!(startY == targetY && startX > targetX)) {
                int dist = getDistanceSquare(-startX, startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 2. 오른쪽 벽 대칭 (2 * m - startX, startY)
            // 같은 Y선상이고 타겟 공이 시작 공보다 오른쪽에 있으면 오른쪽 벽쿠션 불가
            if (!(startY == targetY && startX < targetX)) {
                int dist = getDistanceSquare(2 * m - startX, startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 3. 아래쪽 벽 대칭 (startX, -startY)
            // 같은 X선상이고 타겟 공이 시작 공보다 아래쪽에 있으면 아래쪽 벽쿠션 불가
            if (!(startX == targetX && startY > targetY)) {
                int dist = getDistanceSquare(startX, -startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 4. 위쪽 벽 대칭 (startX, 2 * n - startY)
            // 같은 X선상이고 타겟 공이 시작 공보다 위쪽에 있으면 위쪽 벽쿠션 불가
            if (!(startX == targetX && startY < targetY)) {
                int dist = getDistanceSquare(startX, 2 * n - startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            answer[i] = minDistanceSquare;
        }

        return answer;
    }

    // 두 점 사이의 거리 제곱 계산 함수
    private int getDistanceSquare(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
}
```

## 3. 구현 전략 및 이유

### 대칭 이동으로 꺾인 경로를 직선으로 바꾸는 이유

벽에서 튕기는 경로는 **입사각과 반사각이 같은 꺾인 선분 두 개**입니다. 그런데 시작점을 그 벽에 대해 거울처럼 뒤집은 점 $S'$를 잡으면, **$S'$에서 목표 공까지의 직선**이 벽과 만나는 지점이 정확히 공이 튕기는 지점이 되고, 그 직선의 길이가 **꺾인 두 선분의 길이 합과 같아집니다.** 거울에 비친 상을 향해 똑바로 바라보는 것과 같은 원리로, 대칭축을 기준으로 뒤쪽 선분을 접어 올린 것이기 때문입니다.

덕분에 "어느 지점에서 튕겨야 가장 짧은가"를 탐색할 필요가 사라집니다. 벽 하나당 대칭점은 하나로 정해지고, 그 벽을 쓰는 **최단 경로의 길이도 하나로 확정**됩니다. 네 벽의 대칭점은 다음과 같습니다.

| 벽 | 대칭점 | 비고 |
| --- | --- | --- |
| 왼쪽 ($x = 0$) | $(-startX,\ startY)$ | $x$좌표의 부호를 뒤집음 |
| 오른쪽 ($x = m$) | $(2m - startX,\ startY)$ | 벽까지의 거리 $m - startX$를 벽 바깥으로 한 번 더 |
| 아래쪽 ($y = 0$) | $(startX,\ -startY)$ | $y$좌표의 부호를 뒤집음 |
| 위쪽 ($y = n$) | $(startX,\ 2n - startY)$ | 벽까지의 거리 $n - startY$를 벽 바깥으로 한 번 더 |

대칭점은 당구대 밖의 음수 좌표까지 나오지만, **대칭점과 목표 공을 잇는 선분은 항상 그 벽의 범위 안을 지납니다.** 예를 들어 왼쪽 벽 대칭에서 선분이 $x = 0$을 통과하는 지점의 $y$값은 $startY$와 $targetY$ 사이에 있고 둘 다 $0$ 이상 $n$ 이하이기 때문입니다. 그래서 "쿠션 지점이 벽을 벗어나지 않는지"를 따로 검사하지 않아도 됩니다.

### 같은 선상인 경우만 후보에서 제외하는 이유

문제는 **벽을 먼저 맞힌 뒤** 목표 공을 맞혀야 한다고 못 박고 있습니다. 공이 벽으로 가는 도중에 목표 공을 때리는 경우는 **시작 공과 목표 공이 같은 선상에 있고, 목표 공이 그 벽 쪽에 더 가까이 있을 때**만 생깁니다.

왼쪽 벽을 예로 들면, 같은 Y선상($startY == targetY$)에서 목표 공이 더 왼쪽에 있으면($startX > targetX$) 공은 왼쪽 벽으로 가는 길에 목표 공을 먼저 지나칩니다. 반대로 목표 공이 더 오른쪽에 있으면 공은 벽을 맞고 되돌아온 뒤에 목표 공을 만나므로 **정상적인 원쿠션**입니다. 선상에서 벗어나 있으면 애초에 가는 길이 겹치지 않습니다. 코드의 네 조건이 각각 이 판정을 그대로 옮긴 것입니다.

제외는 **최대 한 방향에서만** 일어납니다. $startX == targetX$와 $startY == targetY$가 동시에 성립하면 두 공이 같은 자리에 있다는 뜻이고, 제한사항이 이를 금지하기 때문입니다. 즉 네 후보 중 적어도 세 개는 항상 살아남아, `Integer.MAX_VALUE`로 초기화한 `minDistanceSquare`가 갱신되지 않은 채 답으로 나가는 일은 없습니다.

### 제곱근을 씌우지 않고 거리의 제곱으로만 비교하는 이유

문제가 요구하는 값 자체가 **거리의 제곱**입니다. 거리는 음수가 될 수 없고 제곱은 그 범위에서 **단조 증가**하므로, 제곱 상태로 비교해 고른 최솟값은 실제 거리로 비교해 고른 최솟값과 항상 같습니다. 중간에 `Math.sqrt`를 끼워 넣으면 실수 오차가 생기고 마지막에 다시 제곱해야 하는데, 처음부터 정수 연산만 쓰면 그 과정이 전부 사라집니다.

좌표가 최대 $1{,}000$이므로 대칭점과 목표 공의 좌표 차이는 최대 $2{,}000$ 수준이고, 거리의 제곱도 수백만대에 머물러 `int` 범위에서 안전합니다.

### 시간 및 공간 복잡도

- **시간 복잡도**: $O(B)$ ($B$는 목표 공의 개수입니다. 공 하나마다 네 개의 대칭점에 대해 상수 번의 산술 연산만 하므로 공의 개수에 정비례합니다.)
- **공간 복잡도**: $O(B)$ (반환할 정답 배열 외에 추가 자료구조를 쓰지 않고, 공마다 쓰는 변수는 상수 개입니다.)
