from itertools import combinations

def solution(n, q, ans):
    ## 코드 길이는 5(오름차순 정렬)
    ## 비밀 코드가 존재하지 않는(답이 0인) 경우는 주어지지 않음
    
    ## 접근법
    ## 1. 1부터 n까지의 수 중에서 5개를 고르는 모든 조합을 생성
    ## 2. 각 조합이 모든 시도(q)와 시스템 응답(ans)을 정확히 만족하는지 완전탐색
    
    answer = 0
    
    # 1. 1부터 n까지의 수 중에서 5개를 고르는 모든 조합 생성
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