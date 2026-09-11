/*
모든 트럭이 다리를 건너려면 최소 몇초?
트럭이 최대 bridge_length 까지 올라감
다리는 weight이하의 무게를 견딤
선입선출 - 큐
일단 트럭랭스를 큐에 담음 그리고 브릿지랭스를 넘어가면 안돼
그리고 무게도 넘어가면 안돼
맨 처음엔 그냥 트럭 넣고 무게 넘으면 앞 트럭을 빼는 형식으로 구현함
근데 오류나고 테케 안돌아감
그래서 q.offer(0)을 넣었더니 테스트 하나는 통과함
*/
import java.util.*;

class Solution {
    public int solution(int bridge_length, int weight, int[] truck_weights) {
        Queue<Integer> q = new ArrayDeque<>();
        int n = truck_weights.length;
        int time = 0; // 시간
        int sum = 0; // 총 트럭 무게

        //트럭을 해당 큐에 넣음
        //넣고 시간 증가
        for(int i=0; i<n; i++){
            while(true){
                time++;

                // 트럭 갯수가 다리 길이랑 같다면 빼기
                if(q.size() == bridge_length){
                    sum -= q.poll();
                }

                // 트럭 무게 확인 후 추가
                if(sum + truck_weights[i] <= weight){
                    q.offer(truck_weights[i]);
                    sum += truck_weights[i];
                    break;
                }
                // 무게가 커서 트럭이 못들어갈때
                // 시간이 흐르는 것을 보여주기 위해 0삽입
                q.offer(0);

            }
        }
        // 마지막으로 남은 트럭 추가
        time += bridge_length;

        return time;
    }
}