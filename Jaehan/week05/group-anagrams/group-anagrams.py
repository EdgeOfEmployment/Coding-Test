from collections import defaultdict

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        # 정렬된 문자열을 키로 하고, 원래 단어들을 리스트로 묶을 딕셔너리
        anagram_map = defaultdict(list)
        
        for s in strs:
            # 문자열을 알파벳 순으로 정렬한 뒤 튜플로 변환하여 딕셔너리 키로 사용
            # 키와 값을 같은 값(s)으로 넣어도 키 값(정렬 후 튜플형태로 들어감 -> 알파벳 동일)은 알파벳이 같다면 동일하기 때문에 동일한 values (list) 에 값을 넣을 수 있음
            sorted_key = tuple(sorted(s))
            anagram_map[sorted_key].append(s)
            # print(anagram_map)

            
        # 딕셔너리에 모인 값들만 리스트로 반환
        return list(anagram_map.values())