import java.util.*;
class Solution {
    public int solution(int[] arrayA, int[] arrayB) {
        int answer = 0;
        
        //1. A의 최대공약수를 B의 원소들로 모두 나눌수 없는지 확인 
        
        //최솟값 구하기
        int minA = Integer.MAX_VALUE;
        for (int i=0;i<arrayA.length;i++){
            minA = Math.min(minA, arrayA[i]);
        }
        
        //최솟값의 약수들 구해서 배열에 삽입, 정렬 
        ArrayList<Integer> arrA1 = new ArrayList<>();
        for (int i=1;i<minA+1;i++){
            if (minA%i == 0){
                arrA1.add(i);
            }
        }
        
        //A의 모든 원소 나누어지는지 확인. 나누어지면 최종 배열에 삽입 
        ArrayList<Integer> arrA2 = new ArrayList<>();
        for (int i=0;i<arrA1.size();i++){
            int cur = arrA1.get(i);
            int flag = 0;
            for (int j=0;j<arrayA.length;j++){
                if (arrayA[j]%cur !=0) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) arrA2.add(cur);
        }
        
        //큰수부터 B의 모든 원소 안나누어지는지 확인. 모두 안나누어지면 그수, break 
        int aNum = 0;
        for (int i=arrA2.size()-1;i>=0;i--){
            int flag = 0;
            int cur = arrA2.get(i);
            for (int j=0;j<arrayB.length;j++){
                if (arrayB[j]%cur ==0){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                aNum = cur;
                break;
            }
        }
        
        //2. B의 최대공약수를 A의 원소들로 모두 나눌수 없는지 확인 
        //최솟값 구하기
        int minB = Integer.MAX_VALUE;
        for (int i=0;i<arrayB.length;i++){
            minB = Math.min(minB, arrayB[i]);
        }
        
        //최솟값의 약수들 구해서 배열에 삽입, 정렬 
        ArrayList<Integer> arrB1 = new ArrayList<>();
        for (int i=1;i<minB+1;i++){
            if (minB%i == 0){
                arrB1.add(i);
            }
        }
        
        //A의 모든 원소 나누어지는지 확인. 나누어지면 최종 배열에 삽입 
        ArrayList<Integer> arrB2 = new ArrayList<>();
        for (int i=0;i<arrB1.size();i++){
            int cur = arrB1.get(i);
            int flag = 0;
            for (int j=0;j<arrayB.length;j++){
                if (arrayB[j]%cur !=0) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) arrB2.add(cur);
        }
        
        
        //큰수부터 B의 모든 원소 안나누어지는지 확인. 모두 안나누어지면 그수, break 
        int bNum = 0;
        for (int i=arrB2.size()-1;i>=0;i--){
            int flag = 0;
            int cur = arrB2.get(i);
            for (int j=0;j<arrayA.length;j++){
                if (arrayA[j]%cur ==0){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                bNum = cur;
                break;
            }
        }
        
        //두개다 존재하면 max 값 
        answer = Math.max(aNum,bNum);
        return answer;
    }
}
