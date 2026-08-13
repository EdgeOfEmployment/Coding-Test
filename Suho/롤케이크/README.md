# 롤케이크 자르기

## 문제 접근

롤케이크를 왼쪽과 오른쪽으로 나누었을 때, 양쪽에 존재하는 **토핑 종류의 수**가 같은 지점을 찾는 문제다.

- 왼쪽 토핑 종류: `set`
- 오른쪽 토핑 종류와 개수: `Counter`

## 1차 풀이

처음에는 왼쪽 토핑을 `set`에 넣고, 오른쪽 리스트에서는 해당 토핑을 직접 삭제했다.

```python
from collections import Counter

def solution(topping):
    answer = 0
    left = set()

    for current_topping in topping[:]:
        left.add(current_topping)
        topping.remove(current_topping)

        if len(left) == len(set(topping)):
            answer += 1

    return answer
```

작은 입력에서는 답을 구할 수 있지만, 큰 입력에서는 시간 초과가 발생했다.

### 순회 중 리스트를 수정해서 생긴 문제

처음에는 다음과 같이 원본 리스트를 그대로 순회했다.

```python
for current_topping in topping:
    topping.remove(current_topping)
```

하지만 `for`문으로 `topping`을 순회하는 도중 같은 리스트의 원소를 삭제하자 일부 토핑을 건너뛰는 문제가 생겼다. 원소가 삭제되면 뒤의 원소들이 앞으로 한 칸씩 당겨지지만, `for`문의 인덱스는 다음 위치로 이동하기 때문이다.

예를 들어 `[1, 2, 1, 3]`에서 첫 번째 `1`을 삭제하면 리스트는 `[2, 1, 3]`이 된다. 이때 다음 순회 위치는 인덱스 1이므로, 앞으로 당겨진 `2`를 건너뛰고 다음 `1`을 읽게 된다.

이를 해결하기 위해 리스트를 얕은 복사한 `topping[:]`을 순회했다.

```python
for current_topping in topping[:]:
    topping.remove(current_topping)
```

이제 `for`문은 복사본을 기준으로 순서대로 돌고, 원본 `topping`에서만 원소를 삭제하므로 원소를 건너뛰지 않는다. 토핑 값은 정수이므로 얕은 복사만으로 충분하다.

다만 이 방법은 **순회 중 리스트 수정 문제만 해결**한다. `topping[:]` 자체에도 `O(n)`의 시간과 추가 공간이 필요하고, 반복문 안의 `remove()`와 `set(topping)` 비용은 그대로이므로 시간 초과는 해결되지 않았다.

## 1차 풀이가 시간 초과된 이유

`for`문은 토핑의 개수를 `n`이라고 할 때 약 `n`번 실행된다. 문제는 반복문 안의 연산이다.

| 코드                              |  시간복잡도 | 이유                                             |
| --------------------------------- | ----------: | ------------------------------------------------ |
| `left.add(current_topping)`       | 평균 `O(1)` | 해시 기반 `set`에 추가                           |
| `topping.remove(current_topping)` |      `O(n)` | 리스트 앞에서부터 값을 찾고, 삭제 후 원소를 당김 |
| `set(topping)`                    |      `O(n)` | 남은 리스트 전체를 다시 순회하여 집합 생성       |
| `len(left)`, `len(...)`           |      `O(1)` | 저장된 크기 확인                                 |

반복문 한 번에 `O(n)`이 들고, 이를 `n`번 반복하므로 전체 시간복잡도는 다음과 같다.

$$
O(n) \times O(n) = O(n^2)
$$

또한 `topping[:]`으로 복사본을 만드는 데 처음 한 번 `O(n)`의 시간과 공간이 필요하지만, 전체 시간복잡도 `O(n²)`에는 큰 영향을 주지 않는다.

이 문제는 `topping`의 길이가 최대 1,000,000이므로 `O(n²)` 방식은 처리하기 어렵다. 최악의 경우 대략 $10^{12}$번 규모의 연산이 될 수 있다.

## 2차 풀이

리스트에서 토핑을 직접 삭제하지 않고, `Counter`에 저장된 오른쪽 토핑의 **개수만 감소**시킨다.

```python
from collections import Counter

def solution(topping):
    answer = 0
    left = set()
    right = Counter(topping)

    for current_topping in topping:
        left.add(current_topping)
        right[current_topping] -= 1

        if right[current_topping] == 0:
            del right[current_topping]

        if len(left) == len(right):
            answer += 1

    return answer


`Counter`에서 값이 0이 된 키를 삭제해야 `len(right)`가 실제 오른쪽에 남아 있는 토핑 종류의 수가 된다.

## 2차 풀이의 시간복잡도

1. `Counter(topping)` 생성: `O(n)`
2. 전체 토핑 순회: `O(n)`
3. 반복문 내부의 `set` 추가, `Counter` 조회·감소·삭제: 해시 구조이므로 각각 평균 `O(1)`

따라서 전체 시간복잡도는 다음과 같다.


O(n) + O(n) = O(n)
```

서로 다른 토핑 종류의 수를 `k`라고 하면 `set`과 `Counter`에 최대 `k`개의 키를 저장하므로 공간복잡도는 `O(k)`이며, 최악의 경우 `O(n)`이다.

## 정리

- 원본 리스트를 순회하면서 삭제하자 원소가 건너뛰어짐
- `topping[:]`을 순회해 반복 기준과 삭제 대상을 분리하여 순회 문제 해결
- 하지만 얕은 복사는 시간복잡도 개선 방법이 아니므로 시간 초과는 계속 발생
- 1차 풀이: 리스트를 반복해서 탐색·삭제하고 집합을 다시 생성하므로 `O(n²)`
- 2차 풀이: 토핑별 개수만 갱신하므로 평균 `O(n)`
- 입력 크기가 최대 1,000,000이므로 한 번의 선형 순회로 해결해야 한다.

## AI 활용 범위

- 1차 풀이의 시간 초과 원인 분석
- 순회 중인 리스트를 수정할 때 원소가 건너뛰어지는 원인 확인
- `topping[:]` 얕은 복사를 이용한 임시 해결 방법 확인
- `remove()`와 `set(topping)`의 시간복잡도 확인
- `Counter`를 이용해 오른쪽 토핑의 개수를 관리하는 방향 참고
- 전체 시간복잡도와 공간복잡도 계산
