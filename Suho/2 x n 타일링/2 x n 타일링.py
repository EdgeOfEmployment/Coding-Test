def solution(n):
    answer = 0
    #기억상 dp문제
    #가로의 길이만 고려. 2와 1로 이루어진 덧셈
    # n = 1 /1= 1
    # n = 2 / 2= 1+1, 2
    # n = 3 / 3= 1+2, 2+1, 1+1+1
    # n = 4 / 4= 2+2, 1+1+2, 2+1+1, 1+2+1, 1+1+1+1
    dp = [0]*n
    #print(dp)
    dp[0] = 1
    dp[1] = 2
    for i in range(2,n):
        dp[i] = (dp[i-1] + dp[i-2])%1000000007
    #print(dp)
    
    return dp[-1]