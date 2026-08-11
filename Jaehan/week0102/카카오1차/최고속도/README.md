# 최고 속도 문제 풀이 공유

## 1. 문제 접근법

- **카메라를 "노드의 속성"으로 치환**: 카메라는 각 도로의 정중앙(도로 길이가 항상 짝수이므로 좌표는 항상 정수)에 있습니다. 이 좌표를 그래프의 노드로 명시적으로 추가하면, "그 지점을 지나가면 제한을 지켜야 한다"는 규칙은 "그 노드를 방문하는 모든 경로는 그 노드의 제한 속도 이하로만 이동 가능하다"는 **노드 용량 제약**으로 정확히 치환됩니다. 두 카메라가 우연히 같은 좌표에 겹치면 문제에서 명시한 대로 더 낮은 제한을 그 노드의 용량으로 사용합니다.

- **그래프 구성**: 모든 도시 좌표, 도로의 양 끝점, 서로 다른 두 도로의 교차점, 그리고 각 도로의 카메라 좌표를 전부 노드로 모으고, 각 도로를 이 노드들로 잘라 인접한 노드끼리 간선을 만듭니다. 노드 용량을 간선 가중치로 변환할 때는 `min(cap(u), cap(v))`를 사용합니다. 이는 간선을 지나려면 반드시 양 끝 노드를 거쳐야 하므로, 두 노드의 제한 중 더 엄격한 쪽이 그 간선 전체에 적용되기 때문입니다.

- **최대 병목 경로(widest path) 문제로 환원**: 1번 도시에서 출발해 일정한 속도로 목적지까지 가면서 지나치는 모든 카메라의 제한을 지켜야 하므로, 원하는 값은 "경로 위에서 만나는 가장 낮은 제한 속도를 최대화"하는 것, 즉 고전적인 최대 병목 경로(maximum bottleneck path, widest path) 문제입니다. 다익스트라와 유사하게 우선순위 큐를 사용하되, 완화 조건을 `min(현재 병목값, 간선 가중치)`가 더 커지는 경우로 바꾸면 됩니다.

- **카메라를 전혀 만나지 않는 경우**: 그런 경로가 존재하면 속도에 제약이 없으므로, 병목값이 무한대로 계산된 도시는 0을 반환하도록 처리합니다 (문제의 예시에서 "카메라를 지나치지 않고 갈 수 있는" 도시의 답이 0으로 주어지는 것과 일치).

## 2. 해결 코드

```python
import heapq
from collections import defaultdict


def solution(city, road):
    ## 1. 카메라는 도로 "정중앙" 좌표 하나에만 존재 (길이가 항상 짝수라 좌표가 정수로 딱 떨어짐)
    ## 1-1. 한 좌표에 카메라가 여러 개 겹치면 그 중 제한 속도가 가장 낮은 값을 그 좌표의 제한으로 사용
    ## 2. 카메라 좌표 = 노드, "그 좌표를 지나가는 모든 경로는 그 제한을 지켜야 한다" = 노드 용량 제약
    ## 3. 도로/도로 교차점, 도로 끝점, 도시 좌표도 전부 노드로 쪼개야 그래프가 안 끊김
    ## 4. 출발~도착까지 속도 하나로 고정 -> "경로 위 최저 제한을 최대화" = 최대 병목(widest path) 문제
    ## 5. m(도로 개수) <= 1000이라 O(m^2) 쌍 비교 자체는 괜찮지만, 튜플/집합으로 다루면 상수가 커져서
    ##    시간초과가 남 -> 좌표 하나(정수)만 다루는 리스트로 압축해서 처리 (성능 개선 포인트)
    n = len(city)
    m = len(road)
    INF = float('inf')

    horiz = [road[i][1] == road[i][3] for i in range(m)]
    h_idx = [i for i in range(m) if horiz[i]]
    v_idx = [i for i in range(m) if not horiz[i]]

    ## 6. cuts[i] = i번 도로를 분할할 "한 축 좌표"들의 모음 (수평 도로는 x, 수직 도로는 y만 저장)
    ##    -> (x, y) 튜플을 매번 만들지 않고 정수만 다루므로 훨씬 빠름
    cuts = [[] for _ in range(m)]
    cam_val = [0] * m
    for i in range(m):
        x1, y1, x2, y2, limit = road[i]
        if horiz[i]:
            cam_val[i] = (x1 + x2) // 2
            cuts[i].extend((x1, x2, cam_val[i]))
        else:
            cam_val[i] = (y1 + y2) // 2
            cuts[i].extend((y1, y2, cam_val[i]))

    ## 7. 수평 x 수직 교차점 찾기 (H x V 쌍만 보면 됨, O(H*V) <= O(m^2/4))
    for i in h_idx:
        y = road[i][1]
        x1, x2 = road[i][0], road[i][2]
        for j in v_idx:
            x = road[j][0]
            y1, y2 = road[j][1], road[j][3]
            if x1 <= x <= x2 and y1 <= y <= y2:
                cuts[i].append(x)
                cuts[j].append(y)

    ## 8. 같은 축 위의 도로끼리 "끝점만 맞닿는" 경우도 처리 (문제 조건상 겹치는 구간은 없고 한 점만 가능)
    by_y = defaultdict(list)
    for i in h_idx:
        by_y[road[i][1]].append(i)
    for idxs in by_y.values():
        for a in range(len(idxs)):
            for b in range(a + 1, len(idxs)):
                i, j = idxs[a], idxs[b]
                lo, hi = max(road[i][0], road[j][0]), min(road[i][2], road[j][2])
                if lo == hi:
                    cuts[i].append(lo)
                    cuts[j].append(lo)

    by_x = defaultdict(list)
    for i in v_idx:
        by_x[road[i][0]].append(i)
    for idxs in by_x.values():
        for a in range(len(idxs)):
            for b in range(a + 1, len(idxs)):
                i, j = idxs[a], idxs[b]
                lo, hi = max(road[i][1], road[j][1]), min(road[i][3], road[j][3])
                if lo == hi:
                    cuts[i].append(lo)
                    cuts[j].append(lo)

    ## 9. 도시는 도로 위 임의의 지점일 수 있으므로, 소속된 도로의 분할점으로 반드시 추가해야 함
    ##    (안 그러면 그 좌표가 그래프에서 고립돼서 못 찾음)
    for x, y in city:
        for i in range(m):
            x1, y1, x2, y2, _ = road[i]
            if horiz[i]:
                if y == y1 and x1 <= x <= x2:
                    cuts[i].append(x)
            else:
                if x == x1 and y1 <= y <= y2:
                    cuts[i].append(y)

    for i in range(m):
        cuts[i] = sorted(set(cuts[i]))

    ## 10. 정수 좌표 -> 실제 (x, y) 점으로 변환하면서 노드 id 부여 (여기서만 튜플 생성)
    point_id = {}

    def get_id(pt):
        pid = point_id.get(pt)
        if pid is None:
            pid = len(point_id)
            point_id[pt] = pid
        return pid

    for x, y in city:
        get_id((x, y))

    road_pts = [None] * m
    for i in range(m):
        x1, y1, x2, y2, _ = road[i]
        if horiz[i]:
            pts = [(v, y1) for v in cuts[i]]
        else:
            pts = [(x1, v) for v in cuts[i]]
        road_pts[i] = [get_id(p) for p in pts]

    node_count = len(point_id)
    cap = [INF] * node_count
    for i in range(m):
        x1, y1, _, _, limit = road[i]
        cam_pt = (cam_val[i], y1) if horiz[i] else (x1, cam_val[i])
        pid = point_id[cam_pt]
        if limit < cap[pid]:
            cap[pid] = limit

    ## 11. 간선 가중치 = min(양 끝 노드 용량) : 간선을 지나려면 양 끝 좌표를 반드시 거치므로
    adj = [[] for _ in range(node_count)]
    for i in range(m):
        ids = road_pts[i]
        for a in range(len(ids) - 1):
            u, v = ids[a], ids[a + 1]
            w = min(cap[u], cap[v])
            adj[u].append((v, w))
            adj[v].append((u, w))

    ## 12. 최대 병목 경로(widest path) 다익스트라: 완화 조건이 "더하기"가 아니라 "min으로 갱신"
    start = point_id[(city[0][0], city[0][1])]
    best = [-1] * node_count
    best[start] = INF
    pq = [(-INF, start)]

    while pq:
        neg_bottleneck, u = heapq.heappop(pq)
        bottleneck = -neg_bottleneck
        if bottleneck < best[u]:
            continue
        for v, w in adj[u]:
            nb = min(bottleneck, w)
            if nb > best[v]:
                best[v] = nb
                heapq.heappush(pq, (-nb, v))

    ## 13. 카메라를 아예 안 만나는 경로가 있으면 속도 제한이 없다는 뜻 -> 0으로 반환
    answer = []
    for i in range(1, n):
        pid = point_id[(city[i][0], city[i][1])]
        b = best[pid]
        answer.append(0 if b == INF else b)

    return answer
```

## 3. 구현 전략 및 이유

### 카메라를 "간선"이 아닌 "노드"에 붙인 이유

카메라는 도로의 정중앙, 즉 하나의 특정 좌표에만 존재합니다. 이 좌표를 다른 교차점들과 마찬가지로 그래프의 노드로 명시적으로 쪼개어 넣으면, 그 노드에 인접한 (즉, 그 좌표를 실제로 지나가야 하는) 간선들만 자동으로 카메라의 영향을 받게 됩니다. 반대로 카메라가 있는 좌표를 그냥 지나쳐 가는 다른 경로(그 노드를 거치지 않는 경로)는 전혀 영향을 받지 않는다는 점이 이 모델로 정확히 표현됩니다. 두 카메라가 같은 좌표에서 만나는 경우도 `cap[pid] = min(cap[pid], limit)`로 자연스럽게 처리됩니다.

### 도시 좌표를 반드시 도로의 분할점으로 추가

도시는 항상 도로 위에 있지만, 다른 도로와의 교차점이거나 도로의 끝점이 아닌 "도로 중간의 임의의 지점"일 수 있습니다. 이를 그래프 노드로 추가하지 않으면 그 지점이 고립되어 아예 그래프에 연결되지 않는 문제가 발생하므로, 모든 도시 좌표를 그 도시가 속한 도로의 분할점 집합에 반드시 포함시켰습니다.

### 최대 병목 경로 알고리즘 선택

"출발부터 도착까지 하나의 속도를 유지해야 하며, 그 속도는 경로 위 모든 카메라의 제한 중 최솟값을 넘을 수 없다"는 조건은, 정확히 "경로의 병목(최솟값)을 최대화"하는 문제입니다. 이는 다익스트라의 완화 조건을 `dist[v] = dist[u] + w` 대신 `dist[v] = min(dist[u], w)`로 바꾼 변형으로 풀 수 있으며, 항상 더 큰 병목값을 우선 처리하는 최대 힙을 사용해 그리디하게 확정해 나가면 정확한 답을 구할 수 있습니다.

### 시간초과 원인 분석과 해결

처음 버전은 `(x, y)` 튜플을 담은 `set`으로 도로 위의 분할점을 관리했는데, `m = 1000`인 최악의 경우(수평/수직 도로 500개씩이 서로 전부 교차하는 격자 형태) 교차점만 25만 개가 만들어지고, 이 많은 튜플을 집합에 넣고 정렬하는 오버헤드가 누적되어 시간초과가 발생했습니다. 이를 해결하기 위해 각 도로마다 "변하지 않는 축 좌표(수평 도로는 y, 수직 도로는 x)"는 따로 떼어두고, 분할점은 정수 하나(반대편 축 좌표)만 리스트에 모았다가 마지막에 딱 한 번만 `(x, y)` 튜플로 조립하도록 바꿔서 불필요한 객체 생성을 크게 줄였습니다.

자바 버전은 이 최적화 이후에도 여전히 느렸는데, 원인을 프로파일링해보니 `HashMap<Long, Integer>`로 좌표 id를 매핑하는 부분이 압도적으로 오래 걸리고 있었습니다. 격자형 테스트 케이스처럼 좌표가 전부 짝수인 경우, `Long.hashCode()`의 기본 구현(`x ^ (x >>> 32)`)이 만들어내는 해시값이 특정 비트 패턴으로 쏠려서 오픈 어드레싱 해시맵의 선형 탐사 길이가 사실상 `O(n)`까지 늘어나는 것을 직접 좌표 25만 개로 재현해 확인했습니다. 이를 MurmurHash3 finalizer 방식의 비트 혼합 함수로 해시값을 한 번 더 섞어주는 것으로 해결했습니다(원인 재현 시 25만 건에 약 2초, 수정 후 10ms 이내). 이 문제는 이 문제처럼 좌표가 규칙적인(격자형) 값을 많이 다루는 문제에서 `Long`/`Integer` 박싱 타입을 해시 키로 쓸 때 흔히 발생할 수 있는 함정이라 기록해둡니다.
