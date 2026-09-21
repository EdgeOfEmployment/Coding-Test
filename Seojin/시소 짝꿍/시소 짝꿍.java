import java.util.*;
class Solution {
    public long solution(int[] weights) {
        long answer = 0;
        Arrays.sort(weights);
        
        //4 2, 3 2, 4 3
        for (int i=0;i<weights.length-1;i++){
            int cur = weights[i];
            for (int j=i+1;j<weights.length;j++){
                if (cur == weights[j]) answer++;
                else if (cur*4 == weights[j]*2) answer++;
                else if (cur*3 == weights[j]*2) answer++;
                else if (cur*4 == weights[j]*3) answer++;
            }
        }
        return answer;
    }
}
