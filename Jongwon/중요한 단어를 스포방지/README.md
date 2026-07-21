# 중요한 단어를 스포 방지 - 코드 풀이

## 문제 해결 아이디어

이 문제는 **스포일러 구간에만 등장하는 단어의 개수**를 구하는 문제이다.

같은 단어가 스포일러 구간과 일반 문장에 모두 등장한다면 중요한 단어가 아니므로 제외해야 한다.

따라서 다음과 같은 순서로 해결하였다.

1. 문장에서 단어와 단어의 시작/끝 인덱스를 저장한다.
2. 스포일러 구간에 포함되지 않는 단어들을 `blacklist`에 저장한다.
3. 다시 스포일러 구간을 확인하면서
    - blacklist에 없는 단어만 선택
    - 같은 단어는 한 번만 세기 위해 중복 제거
4. 최종 개수를 반환한다.

---

# 1. 단어와 위치 저장

문장을 처음부터 끝까지 탐색하면서 공백을 기준으로 단어를 분리한다.

각 단어마다

- 단어(String)
- 시작 인덱스
- 끝 인덱스

를 각각 저장한다.

예시

```text
message

I love Harry Potter
```

저장 결과

| 단어 | 시작 | 끝 |
|------|------|----|
| I | 0 | 0 |
| love | 2 | 5 |
| Harry | 7 | 11 |
| Potter | 13 | 18 |

이렇게 저장해두면 나중에 스포일러 구간과 쉽게 비교할 수 있다.

---

# 2. blacklist 만들기

스포일러 구간과 **겹치지 않는 단어**들을 모두 blacklist에 넣는다.

겹치는지 확인하는 조건은

```java
starts.get(i) <= range[1] &&
ends.get(i) >= range[0]
```

이다.

### 이유

두 구간이 하나라도 겹치면

```text
단어 : [-----]
스포 :    [-----]
```

또는

```text
단어 :     [-----]
스포 : [---------]
```

처럼

```
단어 시작 <= 스포 끝
AND
단어 끝 >= 스포 시작
```

이면 된다.

즉,

- 겹치면 스포일러 단어
- 안 겹치면 blacklist에 저장

예시

```text
I love Harry Potter
```

스포일러 구간이

```text
Harry Potter
```

라면

blacklist에는

```
I
love
```

가 들어간다.

---

# 3. 스포일러 단어 찾기

이제 스포일러 구간을 다시 확인한다.

겹치는 단어를 발견하면

### (1) blacklist에 있으면 제외

이미 일반 문장에서도 등장한 단어이므로 중요한 단어가 아니다.

```java
if (blacklist.contains(word)) continue;
```

---

### (2) 이미 센 단어면 제외

같은 단어가 여러 번 등장해도 한 번만 세기 위해

```java
visitedSpoilers
```

를 사용한다.

```java
if (visitedSpoilers.contains(word)) continue;
```

---

### (3) 처음 나온 중요한 단어

조건을 모두 통과하면

```java
answer++;
visitedSpoilers.add(word);
```

를 수행한다.

---

# 예시

문장

```text
I love Harry Potter and Harry is wizard
```

스포일러 구간

```text
Harry Potter
```

단어 목록

|단어|스포 포함|
|---|---|
|I|X|
|love|X|
|Harry|O|
|Potter|O|
|and|X|
|Harry|X|
|is|X|
|wizard|X|

blacklist

```
I
love
and
Harry
is
wizard
```

여기서 Harry는 일반 문장에도 등장했기 때문에 blacklist에 포함된다.

스포일러 구간을 검사하면

- Harry → blacklist에 있으므로 제외
- Potter → blacklist에 없으므로 카운트

결과

```
answer = 1
```

---

# 사용한 자료구조

### ArrayList

```java
List<String> textWords
List<Integer> starts
List<Integer> ends
```

- 단어 저장
- 시작 위치 저장
- 끝 위치 저장

인덱스를 이용하여 서로 대응시킨다.

---

### HashSet (blacklist)

```java
Set<String> blacklist
```

스포일러가 아닌 단어를 저장한다.

HashSet을 사용하여

```
contains()
```

를 빠르게 수행할 수 있다.

---

### HashSet (visitedSpoilers)

```java
Set<String> visitedSpoilers
```

이미 센 중요한 단어를 저장하여

중복 카운트를 방지한다.

---

# 시간복잡도

단어 개수를 **W**, 스포일러 구간 개수를 **R**이라고 하면

- 단어 추출 : **O(N)** (문자열 길이)
- blacklist 생성 : **O(W × R)**
- 중요한 단어 계산 : **O(W × R)**

따라서 전체 시간복잡도는

```
O(N + W × R)
```

이다.

---

# 전체 알고리즘

1. 문장을 탐색하여 단어와 시작/끝 위치를 저장한다.
2. 스포일러 구간과 겹치지 않는 단어를 `blacklist`에 저장한다.
3. 스포일러 구간을 다시 탐색한다.
4. `blacklist`에 없는 단어이면서 아직 세지 않은 단어만 카운트한다.
5. 최종 개수를 반환한다.