import java.util.*;
/*
귤의 개수 k
귤의 크기를 담은 배열 tangerine
서로 다른 종류의 수의 최솟값을 리턴
중복되는 귤의 종류가 있는지 판단?
1인 귤이 몇개?이렇게 판단 => map사용?
*/
class Solution {
    public int solution(int k, int[] tangerine) {
        int answer = 0;
        Map<Integer, Integer> map = new HashMap<>(); //귤을 담을 리스트
        int n = tangerine.length;

        // 귤을 탐색함
        for(int i=0; i<n; i++){
            // 맵에 귤을 집어넣음
            map.put(tangerine[i], map.getOrDefault(tangerine[i], 0) +1);
        }

        // 귤 개수(value)를 리스트로 받아오기
        List<Integer> list = new ArrayList<>(map.values());
        // 내림차순 정렬(개수가 많은거 부터)
        Collections.sort(list, Collections.reverseOrder());

        // 리스트에서 귤을 하나씩 빼보기
        for(int i=0; i<list.size(); i++){
            // 상자의 담으려는 귤의 갯수랑 리스트에 있는 귤의 개수를 비교
            if(k <= list.get(i)){
                answer++;
                break;
            }else{
                k = k - list.get(i);
                answer++;
            }
        }

        return answer;
    }
}