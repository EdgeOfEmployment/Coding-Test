import java.util.*;
class Solution {
    public int solution(int[] topping) {
        int answer = 0;
        //총 토핑의 개수를 셈

        HashMap<Integer, Integer> top = new HashMap<>();
        for (int i=0;i<topping.length;i++){
            if (!top.containsKey(topping[i])){
                top.put(topping[i],0);
            }
        }
        int[] left = new int[topping.length];
        int[] right = new int[topping.length];
        
        left[0] = 1;
        top.put(topping[0],1);
        for (int i=1;i<left.length;i++){
            if (top.get(topping[i]) == 0) {
                left[i] = left[i-1]+1;
                top.put(topping[i],1);
            }
            else left[i] = left[i-1];
        }
        
        right[0] = 1;
        top.put(topping[topping.length-1],0);
        for (int i=1;i<right.length;i++){
            if (top.get(topping[topping.length-1-i]) == 1) {
                right[i] = right[i-1]+1;
                top.put(topping[topping.length-1-i],0);
            }
            else right[i] = right[i-1];
        }
        
        if (topping.length == 1) return 1;
        else {
            for (int i=0;i<topping.length-1;i++){
                if (left[i] == right[topping.length-i-2]) answer++;
            }
        } 

        

        return answer;
    }
}
