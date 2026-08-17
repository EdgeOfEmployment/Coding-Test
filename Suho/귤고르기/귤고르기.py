from collections import Counter

def solution(k, tangerine):
    answer = 0
    #Counter쓰는 문제일 거 같음.
    #가장 많은 갯수 가진 귤을 조합하면 될 것 같음.
    total =0
    c_tangerine = sorted(Counter(tangerine).items(),key=lambda x: x[1],reverse=True)
    for key,val in c_tangerine:
        #갯수 더해서 k보다 커지는 시점에 return
        total += val
        answer +=1
        if k <= total:
            return answer
    return answer