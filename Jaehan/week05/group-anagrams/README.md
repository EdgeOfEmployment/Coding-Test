# Group Anagrams 문제 풀이 공유

[문제 링크](https://leetcode.com/problems/group-anagrams/)

## 1. 접근법과 단순 풀이의 한계 (처음 시도한 스택 기반 접근)

문자열 배열 `strs`를 순회하며 애너그램 관계인 단어들을 하나로 묶기 위해, 처음에는 스택(Stack)을 이용해 값을 비교하고 누적하려 했습니다.

- 스택의 마지막 값과 현재 단어를 각각 정렬(`sorted()`)하여 비교한 뒤, 같으면 스택에 쌓고 다르면 결과를 리스트에 담는 방식을 생각했습니다.
- 하지만 이 방식은 두 가지 치명적인 한계에 부딪혔습니다.

1. **무한 루프 발생**: 조건이 일치할 때 `stack.append(str)`만 실행될 뿐 스택이 줄어들거나 비워지는 로직이 없어, 반복문 안에서 상태가 유지되면서 시간 초과(무한 루프)가 발생했습니다.
2. **시간 복잡도 폭발 ($O(N^2)$ 이상)**: 반복문 안에서 매번 단어를 꺼내 정렬하고 비교를 거듭하다 보니, 데이터 크기가 커질수록 연산량이 감당할 수 없을 정도로 늘어났습니다.

## 2. 처음 시도했던 코드 (시간 초과 코드)

```python
from typing import List

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        stack = []
        answer = []
        for str in strs:
            while stack:
                compare = sorted(stack[-1])
                sorted_str = sorted(str)
                if compare == sorted_str:
                    stack.append(str)
                else:
                    answer.append(stack)
                    stack = []
                    stack.append(str)
        return answer

```

## 3. 개선한 접근법 (해시 맵 활용)

애너그램의 핵심 성질인 "알파벳 구성이 같은 단어들은 정렬했을 때 결과가 같다"는 점에 착안하여, 스택 대신 해시 맵(딕셔너리)으로 접근 방식을 완전히 전환했습니다.

- **정렬된 문자열을 키로 지정**: 각 단어를 알파벳 순으로 정렬한 결과를 딕셔너리의 키(`key`)로 삼습니다.
- **튜플 변환**: 파이썬 리스트는 가변 객체여서 딕셔너리 키로 사용할 수 없으므로, 정렬된 결과인 리스트를 튜플(`tuple`)로 변환하여 키로 활용합니다.
- **단 한 번의 순회($O(N)$)**: `defaultdict(list)`를 활용해 전체 배열을 단 한 번만 순회하면서, 동일한 정렬 키를 가진 원래 단어들을 리스트에 자동으로 묶어줍니다.

## 4. 해결 코드

```python
from collections import defaultdict
from typing import List

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        # 정렬된 문자열을 키로 하고, 원래 단어들을 리스트로 묶을 딕셔너리
        anagram_map = defaultdict(list)

        for s in strs:
            # 문자열을 알파벳 순으로 정렬한 뒤 튜플로 변환하여 딕셔너리 키로 사용
            sorted_key = tuple(sorted(s))
            anagram_map[sorted_key].append(s)

        # 딕셔너리에 모인 값들만 리스트로 반환
        return list(anagram_map.values())

```

## 5. 구현 전략 및 이유

### 리스트 대신 튜플을 키로 사용한 이유

파이썬 딕셔너리는 내부적으로 해시 값을 이용해 키를 관리하기 때문에, 값이 변하지 않는 불변 객체(Immutable)만 키로 지정할 수 있습니다. `sorted(s)`는 가변 객체인 리스트(`list`)를 반환하므로 그대로 키로 쓰면 `TypeError`가 발생합니다. 따라서 값이 변하지 않는 튜플(`tuple`)로 변환하여 안전하게 키로 사용했습니다.

### `defaultdict`를 활용한 간결한 그룹화

일반 딕셔너리를 사용하면 새로운 키가 들어올 때마다 존재 여부를 확인하고 빈 리스트를 초기화해 주어야 하지만, `collections.defaultdict(list)`를 사용하면 존재하지 않는 키에 접근할 때 자동으로 빈 리스트(`[]`)를 만들어 주어 코드가 훨씬 깔끔해집니다.

### 시간복잡도

- 시간복잡도: $O(N \cdot K \log K)$ (여기서 $N$은 `strs`의 길이, $K$는 문자열의 최대 길이)
- 공간복잡도: $O(N \cdot K)$
