# 바이러스 파이프 문제 풀이 공유

## 1. 문제 접근법

- **상태 정의**: `(현재 감염된 노드 집합, 이전 파이프 타입, 남은 행동 횟수)`를 상태로 정의하여 BFS를 수행합니다.
- **연쇄 감염**: 특정 파이프 타입을 선택하면, 해당 타입의 파이프를 통해 연결된 모든 노드를 재귀적으로 탐색하여 감염 상태를 업데이트합니다.
- **제약 조건 처리**:
  - 동일한 타입의 파이프를 연속으로 선택할 수 없습니다.
  - 최대 $k$번의 행동 제한 내에서 감염 노드 수의 최댓값을 구합니다.

## 2. 해결 코드

```python
from collections import deque

def solution(n, infection, edges, k):
    # 1. 그래프 구성 (타입별 인접 리스트)
    adj = {1: [], 2: [], 3: []}
    for u, v, t in edges:
        adj[t].append((u, v))
        adj[t].append((v, u))

    # 2. BFS 탐색
    # 큐: (현재 감염된 노드 집합, 마지막 파이프 타입, 남은 행동 횟수)
    queue = deque([({infection}, 0, k)])
    max_infected = 0

    while queue:
        curr_infected, last_type, remain_k = queue.popleft()

        # 현재 감염된 배양체 수 갱신
        max_infected = max(max_infected, len(curr_infected))

        # 행동 횟수 소진 시 종료
        if remain_k == 0:
            continue

        # 3. 다음 파이프 타입 선택 (1: A, 2: B, 3: C)
        for t in [1, 2, 3]:
            if t == last_type:
                continue

            # 해당 타입 파이프를 열었을 때의 감염 결과 계산
            next_infected = set(curr_infected)
            changed = True

            # 연쇄 감염 확인
            while changed:
                changed = False
                for u, v in adj[t]:
                    if (u in next_infected and v not in next_infected) or \
                       (v in next_infected and u not in next_infected):
                        next_infected.add(u)
                        next_infected.add(v)
                        changed = True

            # 감염 범위가 확장된 경우에만 큐에 추가
            if len(next_infected) > len(curr_infected):
                queue.append((next_infected, t, remain_k - 1))

    return max_infected
```

## 3. 구현 전략 및 이유

### BFS 선택

감염 상태의 확장을 단계별로 추적하고, 행동 횟수 제한 내에서 가능한 모든 파이프 선택 조합의 최댓값을 찾기에 최적입니다. 트리의 깊이와 무관하게 상태 공간을 탐색하는 데 BFS가 적합합니다.

### set 자료구조 활용

감염된 배양체를 `set`으로 관리함으로써, 중복 방문을 방지하고 감염 여부를 $O(1)$ 시간 내에 조회하여 연쇄 감염 로직을 효율적으로 구현했습니다.

### 연쇄 감염 로직

파이프 타입 하나를 열었을 때 해당 타입과 연결된 전체 컴포넌트를 감염시켜야 하므로, `changed` 플래그를 사용하여 감염된 노드가 더 이상 없을 때까지 반복하도록 설계했습니다. 이를 통해 파이프 네트워크를 통한 간접 감염을 정확히 반영합니다.
