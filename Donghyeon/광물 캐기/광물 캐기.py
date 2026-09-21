def solution(picks, minerals):
    answer = 0
    
    # 1. 가지고 있는 곡괭이로 캘 수 있는 최대 광물 수 계산
    total_picks = sum(picks)
    max_minerals = total_picks * 5
    
    # 곡괭이가 부족하여 캘 수 없는 광물은 미리 제외
    minerals = minerals[:max_minerals]
    
    # 2. 광물을 5개씩 묶어서 다이아몬드, 철, 돌의 개수를 카운트
    chunks = []
    for i in range(0, len(minerals), 5):
        chunk = minerals[i:i+5]
        dia = chunk.count("diamond")
        iron = chunk.count("iron")
        stone = chunk.count("stone")
        chunks.append((dia, iron, stone))
        
    print(chunks)
        
    # 3. 피로도가 가장 많이 소모되는 순서(다이아몬드 > 철 > 돌)로 내림차순 정렬
    chunks.sort(key=lambda x: (x[0], x[1], x[2]), reverse=True)
    
    print(chunks)
    
    # 4. 정렬된 광물 묶음에 대해 다이아 -> 철 -> 돌 곡괭이 순서대로 사용
    for dia, iron, stone in chunks:
        if picks[0] > 0:  # 다이아몬드 곡괭이 사용
            answer += dia * 1 + iron * 1 + stone * 1
            picks[0] -= 1
        elif picks[1] > 0:  # 철 곡괭이 사용
            answer += dia * 5 + iron * 1 + stone * 1
            picks[1] -= 1
        elif picks[2] > 0:  # 돌 곡괭이 사용
            answer += dia * 25 + iron * 5 + stone * 1
            picks[2] -= 1
        else:
            break  # 곡괭이를 모두 사용한 경우
            
    return answer