import java.util.*;
/*
가장 큰 양의 정수 a의 값
최대공약수 구하기?
*/
class Solution {
    public int solution(int[] arrayA, int[] arrayB) {
        int answer = 0;
        int answerA = 0;
        int answerB = 0;
        int n = arrayA.length;
        int m = arrayB.length;

        // 배열 a의 최대 공약수 구하기
        int gcdA = arrayA[0];
        for(int i=0; i<n; i++){
            gcdA = gcd(gcdA, arrayA[i]);
        }

        // 배열 b의 최대 공약수 구하기
        int gcdB = arrayB[0];
        for(int i=0; i<m; i++){
            gcdB = gcd(gcdB, arrayB[i]);
        }

        // a의 최대 공약수로 b배열의 숫자를 하나라도 나눌 수 있는지 확인하기
        boolean checkA = false;
        for(int i=0; i<m; i++){
            if(arrayB[i] % gcdA == 0){
                checkA = true;
                break;
            }
        }

        if(checkA == false){
            answerA = gcdA;
        }

        // b의 최대 공약수로 a배열릐 숫자를 하나라도 나눌 수 있는지 확인하지
        boolean checkB = false;
        for(int i=0; i<n; i++){
            if(arrayA[i] % gcdB == 0){
                checkB = true;
                break;
            }
        }

        if(checkB == false){
            answerB = gcdB;
        }
        // a와 b중 큰값 리턴하기
        int max = Math.max(answerA, answerB);

        return max;
    }

    public static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}