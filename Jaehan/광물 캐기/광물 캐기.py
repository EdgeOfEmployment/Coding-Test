def solution(picks, minerals):
    ## 피로도는 5의 제곱수, 다이아몬드 > 철 > 돌
    ## 각 곡괭이는 종류에 무관하게 5회까지 사용 가능
    ## 곡괭이 사용 시 다 쓸 때까지 사용해야함
    ## 광물은 주어진 순서대로만 캘 수 있음
    ## 모든 광물을 다 캐거나, 곡괭이를 더 못 쓸 때까지 광물을 캠
    ## 한 번 시작한 작업에서 최소한의 피로도를 만족 -> 그리디 같아 보임
    ## 현재 남아있는 가장 강력한 곡괭이로, 지금 처리해야 할 가장 '부하가 큰(피로도가 많이 쌓이는) 5개 묶음'을 처리한다"는 매 순간의 선택이 전체 피로도를 최소화하는 최적의 선택
    
    answer = 0
    
    # 1. 슬라이싱 (곡괭이를 다 사용하는 횟수 기준)
    total_picks = sum(picks)
    max_mineral_cnt = total_picks * 5
    
    if len(minerals) > max_mineral_cnt:
        minerals = minerals[:max_mineral_cnt]
        
    # 2. 5개씩 묶기
    bundles = []
    for i in range(0, len(minerals), 5):
        bundle = minerals[i:i+5]
        dia = bundle.count("diamond")
        iron = bundle.count("iron")
        stone = bundle.count("stone")
        bundles.append((dia, iron, stone)) # 각 묶음별 광물 수
        
    bundles.sort(key = lambda x : (x[0], x[1], x[2]), reverse=True) # 내림차순 정렬 (다이아 > 철 > 돌 순서)
    
    # 3. 정렬된 묶음에 대해 피로도 연산 수행
    for dia, iron, stone in bundles:
        if picks[0] > 0:
            picks[0] -= 1
            answer += (dia + iron + stone)
        elif picks[1] > 0:
            picks[1] -= 1
            answer += (dia * 5 + iron + stone)
        else:
            picks[2] -= 1
            answer += (dia * 25 + iron * 5 + stone)
            
    return answer