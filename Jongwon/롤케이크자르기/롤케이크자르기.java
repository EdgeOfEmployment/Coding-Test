//롤케이크를 공평하게 자르는 방법의 수를 return
// 처음에는 배열을 이용해서 푸려했음
// 셋을 이용한다는 힌트를 얻었을때 풀다가 count를 해주지 않아 헷갈렸음
import java.util.*;

class Solution {
    public int solution(int[] topping) {
        int answer = 0;
        int n = topping.length;

        Set<Integer> left = new HashSet<>();
        Set<Integer> right = new HashSet<>();

        // 토핑 번호별로 오른쪽에 남아 있는 토핑의 개수를 저장
        int[] count = new int[10001];

        // 오른쪽 셋으로 케이크 토핑을 다 넣기
        for (int i = 0; i < n; i++) {
            right.add(topping[i]);
            count[topping[i]]++;
        }

        // 왼쪽으로 하나씩 넘기기
        for (int i = 0; i < n - 1; i++) {

            left.add(topping[i]);
            // 오른쪽 토핑 해당 종류 차감
            count[topping[i]]--;

            // 오른쪽에 해당 토핑이 0개라면 제거
            if (count[topping[i]] == 0) {
                right.remove(topping[i]);
            }

            // 양쪽의 토핑 종류가 같다면
            if (left.size() == right.size()) {
                answer++;
            }
        }

        return answer;
    }
}