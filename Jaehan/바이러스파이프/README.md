# 바이러스 파이프 문제 풀이 공유

## 1. 문제 접근법

- **상태 정의**: `(현재 감염된 노드 집합, 이전 파이프 타입, 남은 행동 횟수)`를 상태로 정의하여 바깥쪽 BFS를 수행합니다.
- **연쇄 감염**: 특정 파이프 타입을 선택하면, 현재 감염된 노드들을 시작점으로 하는 내부 BFS로 해당 타입의 파이프를 통해 연결된 모든 노드를 탐색하여 감염 상태를 업데이트합니다.
- **제약 조건 처리**:
  - 동일한 타입의 파이프를 연속으로 선택할 수 없습니다.
  - 최대 $k$번의 행동 제한 내에서 감염 노드 수의 최댓값을 구합니다.

## 2. 해결 코드

```python
from collections import deque
def solution(n, infection, edges, k):
    answer = 0

    ## 1. 타입 선택 시 해당 타입의 파이프는 모두 열림
    ## 1-1. k번 수행되는 동안 타입은 연속적으로 같은 타입을 고르면 안됨
    ## 2. A : 1, B : 2, C : 3
    ## 3. start 지점 : infection


    # 1. 그래프 구성 (타입별 인접 리스트)
    adj = {1: [], 2: [], 3: []}
    for u, v, t in edges:
        adj[t].append((u, v))
        adj[t].append((v, u))

    # 2. BFS 준비
    # 큐: (현재 감염 집합, 이전 타입, 남은 횟수)
    queue = deque([( {infection}, 0, k )])
    max_infected = 0

    while queue:
        curr_infected, last_type, remain_k = queue.popleft()

        # 행동 횟수 소진 시 최댓값 갱신
        if remain_k == 0:
            max_infected = max(max_infected, len(curr_infected))
            continue

        # 3. 파이프 타입 선택 (1, 2, 3 중 last_type 제외)
        for next_type in [1, 2, 3]:
            if next_type == last_type:
                continue

            # 해당 타입 파이프를 열었을 때 새롭게 감염되는 노드 BFS 탐색
            new_infected = set(curr_infected)
            q = deque(list(curr_infected))

            while q:
                u = q.popleft()
                for start, end in adj[next_type]:
                    if start == u and end not in new_infected:
                        new_infected.add(end)
                        q.append(end)
                    elif end == u and start not in new_infected:
                        new_infected.add(start)
                        q.append(start)

            # 상태 변화가 없으면 굳이 큐에 넣지 않아도 됨(가지치기)
            if len(new_infected) > len(curr_infected):
                queue.append((new_infected, next_type, remain_k - 1))
            else:
                max_infected = max(max_infected, len(curr_infected))

    return max_infected
```

## 3. 구현 전략 및 이유

### BFS 선택

감염 상태의 확장을 단계별로 추적하고, 행동 횟수 제한 내에서 가능한 모든 파이프 선택 조합의 최댓값을 찾기에 최적입니다. 트리의 깊이와 무관하게 상태 공간을 탐색하는 데 BFS가 적합합니다.

### set 자료구조 활용

감염된 배양체를 `set`으로 관리함으로써, 중복 방문을 방지하고 감염 여부를 $O(1)$ 시간 내에 조회하여 연쇄 감염 로직을 효율적으로 구현했습니다.

### 연쇄 감염 로직

파이프 타입 하나를 열었을 때 해당 타입과 연결된 전체 컴포넌트를 감염시켜야 하므로, 현재 감염된 노드들을 초기값으로 하는 내부 큐(`q`)를 두어 더 이상 새로 감염될 노드가 없을 때까지 인접 노드를 계속 확장하도록 설계했습니다. 이를 통해 파이프 네트워크를 통한 간접 감염을 정확히 반영합니다.

### 가지치기 및 최댓값 갱신

같은 타입을 두 번 연속 선택할 수 없다는 제약과, 감염 범위가 더 이상 늘어나지 않는 선택은 굳이 큐에 넣지 않는 가지치기를 함께 적용했습니다. 최댓값은 행동 횟수를 모두 소진했거나(`remain_k == 0`) 더 이상 감염이 확장되지 않아 가지치기된 시점에 갱신합니다.
