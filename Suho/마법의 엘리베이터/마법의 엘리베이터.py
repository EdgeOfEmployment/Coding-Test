def solution(storey):
    answer = 0
    #그리디 같음
    #5보다 크면, + / 5보다 작으면 -
    while True:
        R=storey%10
        storey=storey//10
        if R<5:
            #빼기
            answer +=R
        elif R > 5:
            #더하기
            answer += (10-R)
            storey= storey+1
        else: #R==5인 경우
            Q=storey %10
            if Q<5:
                answer +=R
            else:
                answer += (10-R)
                storey= storey+1
        
        if storey == 0:
            break
    return answer