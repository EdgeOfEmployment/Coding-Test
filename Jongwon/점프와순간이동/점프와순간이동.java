import java.util.*;
/*
k칸 앞으로 이동 또는 (현재까지 온 거리) x 2를 순간이동
순간이동은 건전지 사용량 줄지 않음
k칸 앞으로 이동하면 k만틈 건전지 사용량 줌
n만큼 떨어진 곳으로 이동하려 함
점프로 이동하는 것은 최소로 하려 함
이동하려는 거리 N
사용해야 하는 건전지 사용량의 최솟값을 return
거리는 0부터 시작
N을 거꾸로 판단해서 풀자(5->0)
*/
public class Solution {
    public int solution(int n) {
        int ans = 0;

        // n이 0보다 클때 까지만 돌림
        while(n>0){
            // 짝수일때
            if(n%2 == 0){
                // 순간이동
                n = n/2;
            }else{
                // 홀수 이면
                // 점프하고 사용량 ++
                n = n-1;
                ans++;
            }
        }

        return ans;
    }
}