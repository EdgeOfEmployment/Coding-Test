# 부대복귀 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/132266)

## 1. 접근법과 단순 풀이의 한계 (처음 시도한 출발지별 BFS 접근)

`n`개의 지역이 양방향 도로로 연결되어 있고 도로 하나를 지나는 시간은 모두 1입니다. `sources`의 각 지역에서 `destination`까지의 최단 시간을 구하되, 갈 수 없으면 `-1`을 담아야 합니다.

간선 가중치가 모두 1이므로 **BFS**가 맞는 도구라는 판단까지는 바로 갔습니다. 다만 처음에는 **`sources`의 각 지역마다 BFS를 한 번씩 돌려 `destination`을 찾는** 방식으로 구현했습니다.

- **시간 초과**: `sources`는 최대 500개, `n`은 최대 100,000, `roads`는 최대 500,000개입니다. BFS 한 번이 $O(n + m)$인데 이걸 500번 반복하면 **약 5억 5천만 번**의 연산이 됩니다. 매번 `visited` 배열을 새로 만드는 비용($500 \times 100{,}001$)만 따로 계산해도 5천만입니다.
- **도달 불가능한 경우를 `-1`로 처리하지 못함**: 큐가 전부 비어 `while`이 끝나면 함수에 명시적인 `return`이 없어 파이썬이 **`None`을 반환**합니다. `answer`에 `-1` 대신 `None`이 들어갑니다.
- **`if not graph[cur_dest]: return -1`의 위치 문제**: 길이 없다고 해서 곧바로 반환해 버리면 큐에 남아 있는 다른 경로를 버리게 됩니다. 다만 무방향 그래프에서는 간선을 타고 들어온 노드의 차수가 반드시 1 이상이므로, 이 분기는 실제로 **출발지 자신이 완전히 고립된 경우에만** 걸립니다. 즉 우려했던 오답은 나지 않는 대신, **정작 필요한 "큐 소진 시 `-1`" 처리를 대신해 주지도 못합니다.**
- **방향 수정**: 도로는 양방향이고 통과 시간이 모두 1이므로 `A→B` 최단 거리와 `B→A` 최단 거리가 같습니다. 이 성질을 이용해 **`destination`에서 BFS를 딱 한 번만 수행**하는 방식으로 전환했습니다.

## 2. 처음 접근 코드

```python
from collections import deque

def solution(n, roads, sources, destination):
    answer = []

    # 인접리스트 생성 (양방향)
    graph = [[] for _ in range(n + 1)]
    for a, b in roads:
        graph[a].append(b)
        graph[b].append(a)

    # BFS
    def bfs(start, dest):
        visited = [False for _ in range(n + 1)]
        visited[start] = True
        q = deque()
        q.append([start, 0])

        while q:
            cur_dest, dist = q.popleft()
            if cur_dest == destination:
                return dist

            ## 길이 없다고해서 다른 경로에서의 방문을 안하면 정답을 보장할 수 없음 (간과한점)
            if not graph[cur_dest]:
                return -1

            for nxt_dest in graph[cur_dest]:
                if not visited[nxt_dest]:
                    visited[nxt_dest] = True
                    q.append([nxt_dest, dist + 1])
        # 큐가 비면 아무것도 반환하지 않음 -> None

    # BFS 수행 (sources마다 한 번씩)
    for source in sources:
        cnt = bfs(source, destination)
        answer.append(cnt)

    return answer
```

### 두 입출력 예를 통과한 것은 우연입니다

입출력 예 #2의 `sources`에 있는 `3`은 어떤 도로에도 등장하지 않는 **완전히 고립된 지역**이라, 하필 `if not graph[cur_dest]: return -1` 분기에 정확히 걸려 `-1`이 나왔습니다.

하지만 **고립되지는 않았는데 `destination`과 연결 요소가 다른 경우**에는 그 분기를 지나쳐 큐만 비우고 끝나 `None`이 반환됩니다.

- `n = 4`, `roads = [[1, 2], [3, 4]]`, `sources = [2, 3, 4]`, `destination = 1`
- 기대값 `[1, -1, -1]` → **처음 접근은 `[1, None, None]`**

`3`과 `4`는 서로 연결되어 있어 차수가 1 이상이므로 `-1` 분기에 걸리지 않고, `1`에는 도달할 수 없어 큐가 그대로 소진됩니다.

## 3. 개선한 접근법

**"각 출발지에서 목적지로"를 "목적지에서 모든 지역으로"로 뒤집었습니다.**

도로는 양방향이고 통과 시간이 모두 1이므로 `dist(source, destination) == dist(destination, source)`입니다. 따라서 `destination`을 시작점으로 BFS를 **한 번만** 돌리면 모든 지역까지의 최단 거리가 한꺼번에 구해지고, `sources`는 그 결과를 조회만 하면 됩니다.

- 처음 접근: $O(|sources| \times (n + m))$ → 약 5억 5천만
- 해결 코드: $O(n + m)$ → 약 110만

`-1` 처리도 같이 해결됩니다. 거리 배열을 `-1`로 초기화해두면 **방문 여부 표시와 도달 불가 표시가 같은 값**이 되어, BFS가 끝난 뒤 도달하지 못한 지역에는 `-1`이 그대로 남습니다.

## 4. 해결 코드

```python
from collections import deque

def solution(n, roads, sources, destination):
    ## 강철부대가 있는 지역은 모두 유일한 번호로 식별 가능
    ## 강철부대가 있는 지역 간 통과 시간은 모두 1로 동일
    ## BFS

    ## 방해로 인해 시작 때와 다르게 되돌아오는 경로가 없어져 복귀가 불가능한 경우도 존재함

    answer = []

    # 인접리스트 생성 (양방향)
    graph = [[] for _ in range(n + 1)]
    for a, b in roads:
        graph[a].append(b)
        graph[b].append(a)

    # BFS 정의
    # destination에서 sources로 세기
    # (sources의 각 부대원들을 기준으로 모두(최대 500명) 세면 시간 초과남
    #  -> destination에서 출발하여 bfs를 1번만 수행함으로써 모든 지역까지의 최단거리를 계산)
    def bfs(start):
        distances = [-1] * (n + 1)
        q = deque([start])
        distances[start] = 0

        while q:
            cur = q.popleft()

            for nxt in graph[cur]:
                if distances[nxt] == -1:  # 방문하지 않은 지역
                    # 거리가 1로 동일하므로 출발점부터 현재지역까지의 거리에서 1을 누적
                    distances[nxt] = distances[cur] + 1
                    q.append(nxt)

        return distances

    # BFS 수행
    distances = bfs(destination)

    # sources의 각 원소에 해당하는 최단 거리 매핑
    answer = [distances[source] for source in sources]

    return answer
```

## 5. 구현 전략 및 이유

### 목적지에서 거꾸로 한 번만 탐색하는 이유

`sources`가 최대 500개인 반면 목적지는 **하나뿐**입니다. 탐색의 시작점을 개수가 적은 쪽에 두면 반복 자체가 사라집니다.

이 뒤집기가 가능한 근거는 두 가지입니다. 도로가 **양방향**이라 경로를 그대로 되짚을 수 있고, 통과 시간이 **모두 1**이라 방향에 따른 비용 차이가 없기 때문입니다. 둘 중 하나라도 깨지면(단방향이거나 가중치가 다르면) 이 방법은 성립하지 않습니다.

입출력 예 #2(`destination = 5`)에서 BFS를 한 번 돌리면 `distances`가 아래처럼 한꺼번에 채워지고, `sources = [1, 3, 5]`는 조회만 하면 됩니다.

| 지역        | 1   | 2   | 3      | 4   | 5   |
| ----------- | --- | --- | ------ | --- | --- |
| `distances` | 2   | 1   | **-1** | 1   | 0   |

### 거리 배열 하나로 방문 여부까지 관리하는 이유

처음 접근은 `visited` 배열과 큐에 실어 나르는 `dist` 값을 따로 관리했습니다. 해결 코드는 `distances` 하나로 둘을 겸합니다.

- `distances[nxt] == -1`이면 아직 방문하지 않은 지역이므로 **방문 체크가 그대로 대체**됩니다.
- BFS가 끝난 뒤에도 `-1`로 남아 있는 지역은 곧 도달 불가능한 지역이므로, **문제가 요구하는 `-1` 반환이 별도 분기 없이 자동으로 처리**됩니다.

처음 접근에서 `None`이 반환됐던 문제가 여기서 원천적으로 사라집니다. 배열을 하나 덜 만들기 때문에 메모리도 절약됩니다.

### 시간 및 공간 복잡도

- **시간 복잡도**: $O(n + m)$ ($m$은 `roads`의 길이입니다. BFS를 단 한 번만 수행하고, 마지막 매핑은 `sources`의 길이만큼인 $O(500)$이라 무시할 수 있습니다.)
- **공간 복잡도**: $O(n + m)$ (인접리스트가 양방향이라 간선 정보를 $2m$개 저장하고, 거리 배열과 큐가 각각 최대 $n + 1$개를 차지합니다.)
