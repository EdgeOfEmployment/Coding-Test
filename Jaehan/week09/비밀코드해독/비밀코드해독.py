from itertools import combinations

def solution(n, q, ans):
    ## 코드 길이는 5(오름차순 정렬)
    ## 비밀 코드가 존재하지 않는(답이 0인) 경우는 주어지지 않음
    
    ## 접근법
    ## 1. 1부터 n까지의 수 중에서 5개를 고르는 모든 조합을 생성
    ## 2. 각 조합이 모든 시도(q)와 시스템 응답(ans)을 정확히 만족하는지 완전탐색
    
    answer = 0
    
    # 1. 1부터 n까지의 수 중에서 5개를 고르는 모든 조합 생성 ($n\mathrm{C}_5$)
    all_comb = combinations(range(1, n + 1), 5)
    
    # 2. 모든 조합을 순회하며 조건 검증
    for new_comb in all_comb:
        
        # 2-1. q의 모든 요소들과 매칭하여 모든 ans를 만족하는지 관리할 상태 값
        is_possible = True 
        
        for idx in range(len(q)):
            # 교집합을 구해서 길이로 시스템 응답(ans)값과 매칭하기
            common_count = len(set(q[idx]) & set(new_comb)) 
            
            if common_count != ans[idx]:
                is_possible = False
                break  # 하나라도 틀리면 더 볼 필요 없으므로 탈출
        
        # 2-2. 응답 개수가 모두 ans와 같음을 만족하면 정답 카운트 증가
        if is_possible:
            answer += 1
            
    return answer



## 첫번째 접근법
from itertools import combinations
from collections import Counter
def solution(n, q, ans):
    ## 코드 길이는 5(오름차순 정렬)
    ## 비밀 코드가 존재하지 않는(답이 0인) 경우는 주어지지 않음
    
    ## 접근법
    ## 1. 시도한 모든 배열에서 가장 많이 나타난대로 수를 조합
    
    ## 테스트 케이스 1에서 가장 많이 나온 수는 7 (4) -> 3, 9, 10 (3) -> 2, 4, 5, 6, 8 (2) -> 1 (1)
    ## 조합에서 가장 많이 나온 수는 3, 7 (3) -> 5, 8, 9, 10 (2) -> 4 (1)
    ## 1. 시스템 응답 수가 가장 큰 시도에서 응답 수에 맞는 조합 추출 ex) 시스템 응답이 4면 -> 5c4
    ## 2. 가장 많이 나온 수 순서대로 조합을 만들어서 시스템 응답과 매칭
    
    answer = 0
    
    # 1. 시스템 응답 수가 가장 큰 시도에서 조합 추출
    r = max(ans) # 선택할 최대 수
    idx = ans.index(max(ans)) # 시스템 응답 수가 가장 큰 시도의 인덱스
    most_res = q[idx] # 시스템 응답 수가 가장 큰 시도(배열)
    comb = list(combinations(most_res, r)) # 조합 추출

    # print(comb)
    
    # 2. 시도의 각 요소들 나온 횟수 구하기
    counta = [] # 카운터 사용을 위한 1차원 배열 생성
    for trial in q:
        for t in trial:
            counta.append(t)
    # print(counta)
    
    counter = Counter(counta) # 가장 많이 나온 수를 위한 카운터 생성
    # print(counter)
    sequence = [k for k, v in sorted(counter.items(), key=lambda x : -x[1])] # 빈도 수대로 내림차순 정렬
    # print(sequence)
    
    # max_val = max(counter.values()) # 가장 큰 values 값
    # top_keys = [k for k, v in counter.items() if v == max_val] # 가장 큰 values에 해당하는 keys 값
    # print(top_keys)
    
    # 3. 추출한 조합별로 반복문 안에서 가장 많이 나온 수와 조합 만들기
    for com in comb:
        for seq in sequence :
            if seq in com: # seq가 이미 조합에 있으면 다음 seq로
                continue
            else:
                new_comb = list(com) + [seq] # com은 tuple이므로 append 연산을 위해 list로 변환
            # print(new_comb)
            
            # 3-1. new_comb로 응답 개수 매칭
            is_possible = True # q의 요소들과 매칭하여 모든 ans를 만족하는 경우를 관리할 상태 값
            for idx in range(len(q)):
                common_count = len(set(q[idx]) & set(new_comb)) # 교집합을 구해서 길이로 ans값 매칭하기
                if common_count != ans[idx] :
                    is_possible = False
            
            # 3-2. 응답 개수가 모두 ans와 같음을 만족하면 answer += 1
            if is_possible:
                answer += 1
                

    return answer