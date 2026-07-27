모르게써요...........

# [level 2] 리프 노드 수 최대화 - 468372

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468372)

### 문제 설명

---

# 🌳 [프로그래머스] 리프 노드 수 최대화 (2025 카카오 하반기 1차)

## 📌 문제 핵심 요약

트리는 다음 세 종류의 노드로 구성됩니다.

- **루트 노드**
- **분배 노드**
- **리프 노드**

---

## 트리 구성 규칙

### 1. 노드 구조

- 루트 노드는 자식 노드 1개를 가짐
- 분배 노드는 자식 노드를 2개 또는 3개 가짐
- 리프 노드는 자식 노드를 가지지 않음

---

### 2. 분배 노드 제한

분배 노드는 최대:

dist_limit

개까지 사용 가능

---

### 3. 같은 깊이의 분배 방식 제한

같은 깊이에 존재하는 분배 노드는 모두 동일한 개수의 자식을 가져야 함.

예)

가능:

깊이 2

분배 노드 A → 자식 2개
분배 노드 B → 자식 2개

불가능:

깊이 2

분배 노드 A → 자식 2개
분배 노드 B → 자식 3개

---

### 4. 분배도 제한

리프 노드의 분배도는

해당 리프 노드의 부모부터 루트까지 지나온 모든 분배 개수의 곱이다.

예)

root
|
2분배
|
3분배
|
leaf

분배도:

2 × 3 × 1 = 6

모든 리프 노드는:

split_limit

이하이어야 한다.

---

# 💡 알고리즘 접근 방식

## 1. DFS + 메모이제이션 (DP)

이 문제는 "현재 분배 노드들을 어떻게 확장할 것인가"를 결정하는 최적화 문제입니다.

현재 상태를 다음과 같이 정의합니다.

dfs(nodes, split, used)

| 변수  | 의미                                       |
| ----- | ------------------------------------------ |
| nodes | 현재 단계에서 존재하는 분배 후보 노드 개수 |
| split | 현재까지 계산된 분배도                     |
| used  | 현재까지 사용한 분배 노드 개수             |

---

# 2. 현재 상태에서 가능한 선택

현재 `nodes`개의 분배 노드가 있을 때 선택지는 두 가지입니다.

---

## 선택 1) 현재 노드를 모두 리프로 종료

더 이상 분배하지 않는 경우입니다.

예:

현재 nodes = 5

↓

리프 5개 생성

따라서:

```cpp
ret = nodes;
선택 2) 분배 노드로 확장

분배 노드는:

2개 분배
3개 분배

두 가지가 가능합니다.

2분배
현재 nodes

↓

nodes * 2개의 자식 생성

분배도:

split * 2
3분배
현재 nodes

↓

nodes * 3개의 자식 생성

분배도:

split * 3
3. 핵심 아이디어

기존 접근의 문제점:

현재 모든 자식을 다음 분배 단계로 이동

이라고 가정하면 최적해를 놓치게 됩니다.

하지만 실제 문제에서는:

생성된 자식 중 일부만 분배 노드가 될 수 있음

이 가능합니다.

예:

        분배 노드
          |
      3개의 자식 생성

      /   |   \
    leaf  분배 leaf


즉,

일부는 리프
일부는 다음 분배 노드

가 될 수 있습니다.

4. 상태 전이

분배 후 생성된 자식 수:

child = nodes * 2
또는
child = nodes * 3

중에서

다음 단계 분배 노드 개수:

0 ~ child

를 선택합니다.

다음 단계로 가지 않는 경우

모든 자식이 리프:

leaves = child
일부만 분배하는 경우

예:

child = 6

다음 분배 노드 = 2

리프 = 4

결과:

4 + dfs(2, nextSplit, used + nodes)
🔄 DFS 탐색 흐름
dfs(nodes, split, used)

        |
        |
        +-- 현재 노드를 모두 리프로 종료
        |
        |
        +-- 2분배 선택
        |       |
        |       +-- 일부 리프
        |       |
        |       +-- 일부 분배
        |
        |
        +-- 3분배 선택
                |
                +-- 일부 리프
                |
                +-- 일부 분배
5. 메모이제이션

같은 상태가 반복해서 계산되는 것을 방지합니다.

상태:

(nodes, split, used)

를 key로 저장합니다.

map<tuple<long long,long long,long long>, long long> memo;
⏱ 시간 복잡도
상태 개수

분배도는:

2^a × 3^b

형태로 증가합니다.

split_limit가 1e9이므로

가능한 깊이는 약 30 이하입니다.

DFS 상태

각 상태마다:

현재 종료
2분배
3분배

를 확인합니다.

따라서:

O(상태 수 × 분기 수)
🧩 구현 전략
자료 구조
DP Cache
map<tuple<long long,long long,long long>, long long> memo;

저장 정보:

(nodes, split, used)
→ 최대 리프 개수
```

시간초과 버전

```
#include <bits/stdc++.h>
using namespace std;

long long D, S;

map<tuple<long long,long long,long long>, long long> memo;


long long dfs(long long nodes, long long split, long long used)
{
    if (used > D || split > S)
        return 0;


    auto key = make_tuple(nodes, split, used);

    if (memo.count(key))
        return memo[key];


    // 현재 노드를 모두 리프로 종료
    long long ret = nodes;


    // 분배 노드 사용
    if (used + nodes <= D)
    {
        for (long long mul : {2LL,3LL})
        {
            long long child = nodes * mul;
            long long nextSplit = split * mul;


            if (nextSplit > S)
                continue;


            /*
                child 중 일부만 다음 분배 노드가 된다.
                나머지는 리프
            */
            for (long long next = 0; next <= child; next++)
            {
                long long leaves = child - next;

                if (next == 0)
                {
                    ret = max(ret, leaves);
                }
                else
                {
                    ret = max(
                        ret,
                        leaves + dfs(
                            next,
                            nextSplit,
                            used + nodes
                        )
                    );
                }
            }
        }
    }


    return memo[key] = ret;
}


int solution(int dist_limit, int split_limit)
{
    D = dist_limit;
    S = split_limit;

    /*
      root의 자식 1개가 최초 후보
    */
    return dfs(1,1,0);
}
```
