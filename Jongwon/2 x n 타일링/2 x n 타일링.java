import java.util.*;
/*
가로 2, 세로 1인 직사각경
가로 n, 세로 2인 바닥을 채워야함
직사각형을 채울 수 있는 방법의 수를 return
n이 1이면 세로 직사 1개
n이 2이면 가로 직사 2개 또는 세로 직사 2개
n이 3이면 가로 직사 2랑 세로 직사 1개 * 2 / 세로 직사 3개
n이 4면 5개
n이 5면 8개
*/

class Solution {
    public int solution(int n) {
        int answer = 0;
        int[] a = new int[n+1];
        a[1] = 1;
        a[2] = 2;

        // n = n-1 + n-2가 계속 반복 됨
        for(int i=3; i<=n; i++){
            a[i] = (a[i-1] + a[i-2]) % 1000000007;
        }

        return a[n];
    }
}