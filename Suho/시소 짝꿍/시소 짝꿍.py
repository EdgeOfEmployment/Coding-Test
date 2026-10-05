def solution(weights):
    answer = 0
    # 270/360 = 3/4
    # 1 , 2/3, 3/4, 1/2 중 하나면 됨
    v = [1,2/3,3/4,1/2] 
    weights.sort()
    #print(weights)
    count = {} # 지나온 몸무개 갯수
    
    for i in range(len(weights)):
        weights_v = []
        for j in range(4):
            weights_v.append(weights[i]*v[j])
        #print(weights_v)
        for candidate in weights_v:
            #count배열에 후보군이 없으면 0
            answer += count.get(candidate, 0)
        count[weights[i]] = count.get(weights[i], 0) + 1
        #print("count=",count)
        
    return answer