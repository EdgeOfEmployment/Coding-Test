import java.util.*;

class Solution{
    public int solution(String s){
        // 최솟값을 1로 설정
        int answer = 1;
        int n = s.length();

        for(int i=0; i<n; i++){
            //길이가 홀수일때
            int hleft = i-1;
            int hright = i+1;

            while(hleft>=0 && hright<n){
                //왼쪽문자와 오른쪽 문자가 같다면
                if(s.charAt(hleft) == s.charAt(hright)){
                    //계산
                    int len = hright-hleft+1;
                    //가장 긴 길이 반환
                    answer = Math.max(answer, len);
                    //한칸씩 앞뒤로 이동
                    hleft--;
                    hright++;
                }else{
                    break;
                }
            }

            //길이가 짝수일때
            int zleft = i;
            int zright = i+1;

            while(zleft>=0 && zright<n){
                if(s.charAt(zleft) == s.charAt(zright)){
                    int len = zright -zleft +1;
                    answer = Math.max(answer, len);

                    zleft--;
                    zright++;
                }else{
                    break;
                }
            }
        }

        return answer;
    }
}