import math
def solution(arrayA, arrayB):
    answer = 0
    
    result1 = math.gcd(*arrayA)
    for i in arrayB:
        if i%result1==0:
            result1 = -1
            break
        
    result2 = math.gcd(*arrayB)
    for i in arrayA:
        if i%result2==0:
            result2 = -1
            break
    
    if result1==-1 and result2 == -1:
        answer = 0
    else:
        answer = max(result1,result2)    
    return answer