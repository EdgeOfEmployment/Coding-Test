# 🌟 [프로그래머스] 힌트 스테이지 풀이

> **핵심 아이디어 : DFS(백트래킹)를 이용한 모든 힌트 번들 구매 조합 탐색**

---

## 문제 분석

각 스테이지에는 하나의 **힌트 번들**이 존재한다.

번들을 구매하면

- 일정 비용을 지불하고
- 여러 종류의 힌트권을 획득한다.

힌트권은 해당 번호의 스테이지에서 사용할 수 있으며,

많이 사용할수록 해당 스테이지의 클리어 비용이 감소한다.

목표는

- 어떤 힌트 번들을 구매할지 결정하여
- **전체 비용(번들 구매 비용 + 스테이지 클리어 비용)**

을 최소화하는 것이다.

---

# 핵심 관찰

각 힌트 번들은

```
구매한다.

또는

구매하지 않는다.
```

두 가지 선택만 존재한다.

즉,

번들의 개수가 `n-1`개라면

가능한 모든 경우의 수는

```
2^(n-1)
```

개이다.

따라서 모든 구매 조합을 탐색해도 충분하다.

---

# DFS(백트래킹)

현재 번들 번호를

```
idx
```

라고 하자.

각 번들에서는

두 가지 선택을 한다.

```
① 구매한다.

② 구매하지 않는다.
```

각 선택 이후

다음 번들을 계속 탐색한다.

```
dfs(idx)

        │
        │
        ├── 구매
        │      │
        │      └── dfs(idx+1)
        │
        └── 미구매
               │
               └── dfs(idx+1)
```

모든 번들에 대한 선택이 끝나면

현재 조합의 총 비용을 계산한다.

---

# 힌트권 관리

DFS 과정에서

현재까지 구매한 번들로 얻은 힌트권 개수를 저장한다.

```
ticket_count[i]
```

=

```
i번 힌트권 보유 개수
```

번들을 구매하면

해당 번들에 포함된 모든 힌트권을 증가시킨다.

재귀가 끝나면

다시 감소시켜 원래 상태로 복구한다.

즉,

백트래킹을 이용하여

항상 올바른 상태를 유지한다.

---

# 비용 계산

모든 번들 선택이 끝나면

각 스테이지마다

사용 가능한 힌트권 수를 확인한다.

하지만

사용 가능한 힌트권보다

비용 배열의 크기가 더 작을 수 있으므로

실제로 사용할 힌트권 개수는

```cpp
min(
    ticket_count[stage],
    cost[stage].size()-1
)
```

이다.

이 값을 이용해

해당 스테이지의 클리어 비용을 더한다.

마지막으로

```
번들 구매 비용
+
모든 스테이지 비용
```

을 계산하여

최솟값을 갱신한다.

---

# 알고리즘 흐름

```
DFS

        │
        │
        ├── 현재 번들 구매
        │
        │      힌트권 추가
        │
        │      ↓
        │
        │   다음 번들 탐색
        │
        │      ↓
        │
        │   힌트권 복구
        │
        └── 현재 번들 미구매
               │
               ↓
          다음 번들 탐색
```

모든 번들에 대한 선택이 끝나면

```
현재 조합의 총 비용 계산
```

을 수행한다.

---

# 시간 복잡도

번들의 개수를

```
m = n-1
```

이라고 하면

가능한 구매 조합은

```
2^m
```

개이다.

각 조합마다

모든 스테이지의 비용을 계산하므로

전체 시간 복잡도는

```
O(2^(n-1) × n)
```

이다.

---

# 구현 코드

```cpp
#include <string>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

int solution(vector<vector<int>> cost, vector<vector<int>> hint) {
    int n = cost.size();
    long long min_total_cost = LLONG_MAX;

    int num_bundles = n - 1;
    int total_masks = 1 << num_bundles;

    // 모든 번들 구매 조합 탐색
    for (int mask = 0; mask < total_masks; ++mask) {

        long long current_cost = 0;

        // 현재 보유한 힌트권 개수
        vector<int> ticket_count(n + 1, 0);

        // 구매한 번들 처리
        for (int i = 0; i < num_bundles; ++i) {

            if ((mask >> i) & 1) {

                current_cost += hint[i][0];

                for (size_t j = 1; j < hint[i].size(); ++j) {
                    ticket_count[hint[i][j]]++;
                }
            }
        }

        // 스테이지 클리어 비용 계산
        for (int i = 0; i < n; ++i) {

            int stage = i + 1;

            int usable =
                min(
                    ticket_count[stage],
                    (int)cost[i].size() - 1
                );

            current_cost += cost[i][usable];
        }

        min_total_cost =
            min(min_total_cost, current_cost);
    }

    return (int)min_total_cost;
}
```
