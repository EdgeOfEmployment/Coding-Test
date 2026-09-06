/*
m,n의 사막지도
가로 w, 세로 h크기의 선인장 구역을 만드려함
선인장이 가능한 비를 늦게 맞도록 함
선인장 구역에 포함된 가장 왼쪽의 좌표를 return
처음엔 격자 나오길래 bfs인 줄 알았음
*/

import java.util.*;

class Solution {
    public int[] solution(int m, int n, int h, int w, int[][] drops) {

        // 각 칸에 몇 번째 비가 내리는지 저장
        int[][] rain = new int[m][n];

        // 처음에는 모든 칸을 "비가 오지 않는 상태"로 설정
        for (int i = 0; i < m; i++) {
            Arrays.fill(rain[i], Integer.MAX_VALUE);
        }

        // 비가 내린 순서 기록
        for (int i = 0; i < drops.length; i++) {
            int r = drops[i][0];
            int c = drops[i][1];

            rain[r][c] = i + 1;
        }

        // 가장 늦게 비를 맞는 시간
        int max = 0;

        // 정답 좌표
        int answerR = 0;
        int answerC = 0;

        // 선인장 구역을 놓을 위치
        for (int r = 0; r <= m - h; r++) {
            for (int c = 0; c <= n - w; c++) {

                // 현재 선인장 구역에서 가장 먼저 비를 맞는 시간
                int min = Integer.MAX_VALUE;

                // 현재 h x w 영역 확인
                for (int i = r; i < r + h; i++) {
                    for (int j = c; j < c + w; j++) {

                        min = Math.min(min, rain[i][j]);

                    }
                }

                // 현재 영역이 지금까지보다 더 늦게 비를 맞는다면 갱신
                if (min > max) {
                    max = min;
                    answerR = r;
                    answerC = c;
                }
            }
        }

        return new int[]{answerR, answerC};
    }
}