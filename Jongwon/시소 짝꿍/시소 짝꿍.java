import java.util.*;

class Solution {
    public long solution(int[] weights) {
        long answer = 0;

        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < weights.length; i++){
            int w = weights[i];

            // 같은 몸무게
            if(map.containsKey(w)){
                answer += map.get(w);
            }

            // 2 : 3
            if(w * 2 % 3 == 0){
                int a = w * 2 / 3;

                if(map.containsKey(a)){
                    answer += map.get(a);
                }
            }

            // 3 : 2
            if(w * 3 % 2 == 0){
                int b = w * 3 / 2;

                if(map.containsKey(b)){
                    answer += map.get(b);
                }
            }

            // 2 : 4
            if(w * 2 % 4 == 0){
                int c = w * 2 / 4;

                if(map.containsKey(c)){
                    answer += map.get(c);
                }
            }

            // 4 : 2
            int d = w * 4 / 2;

            if(map.containsKey(d)){
                answer += map.get(d);
            }

            // 3 : 4
            if(w * 3 % 4 == 0){
                int e = w * 3 / 4;

                if(map.containsKey(e)){
                    answer += map.get(e);
                }
            }

            // 4 : 3
            if(w * 4 % 3 == 0){
                int f = w * 4 / 3;

                if(map.containsKey(f)){
                    answer += map.get(f);
                }
            }

            // 현재 몸무게 저장
            map.put(w, map.getOrDefault(w, 0) + 1);
        }

        return answer;
    }
}