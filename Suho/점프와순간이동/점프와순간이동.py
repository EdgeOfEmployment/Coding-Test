def solution(n):
    ans = 0
    #dp인 것 같음. -> XXXX
    
    while n >0:
        if n%2 ==0: #짝수면
            n =n//2 
        else:
            n-=1
            ans +=1

    return ans