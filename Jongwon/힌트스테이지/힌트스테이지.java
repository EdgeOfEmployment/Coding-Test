/*
힌트권은 1-n번
i번 힌트권은 i번 스테이지에서 사용가능
하나의 스테이지에서 사용할 수 있는 힌트권의 최대 개수는 n-1개
마지막 스테이지를 제외한 각 스테이지에서는 해당 스테이지에서 판매하는 힌트 번들을 최대 1개 구매 가능

*/

import java.util.*;
class Solution {
    int answer = Integer.MAX_VALUE;

    public int solution(int[][] cost, int[][] hint) {
        int n = cost.length;
        int[] count = new int[n];

        dfs(0, 0, count, cost, hint);

        return answer;
    }

    void dfs(int stage, int totalCost, int[] inventory, int[][] cost, int[][] hint) {
        int n = cost.length;

        int usedHints = Math.min(inventory[stage], n - 1);
        totalCost += cost[stage][usedHints];

        if (stage == n - 1) {
            answer = Math.min(answer, totalCost);
            return;
        }

        // 이번 스테이지 힌트 번들 "안 사고" 다음 단계 가기
        dfs(stage + 1, totalCost, inventory, cost, hint);

        // 이번 스테이지 힌트 번들 "사고" 다음 단계 가기
        int price = hint[stage][0]; // 번들 가격

        for (int j = 1; j < hint[stage].length; j++) {
            inventory[hint[stage][j] - 1]++;
        }

        // 가격 지불하고 다음 단계 탐색
        dfs(stage + 1, totalCost + price, inventory, cost, hint);

        //다녀왔으니 챙겼던 힌트권 다시 제자리에 (원상복구/백트래킹)
        for (int j = 1; j < hint[stage].length; j++) {
            inventory[hint[stage][j] - 1]--;
        }
    }
}