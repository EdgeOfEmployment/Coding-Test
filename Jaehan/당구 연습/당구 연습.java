import java.util.*;

class Solution {
    public int[] solution(int m, int n, int startX, int startY, int[][] balls) {
        int[] answer = new int[balls.length];

        for (int i = 0; i < balls.length; i++) {
            int targetX = balls[i][0];
            int targetY = balls[i][1];
            
            int minDistanceSquare = Integer.MAX_VALUE;

            // 1. 왼쪽 벽 대칭 (-startX, startY)
            // 같은 Y선상이고 타겟 공이 시작 공보다 왼쪽에 있으면 왼쪽 벽쿠션 불가
            if (!(startY == targetY && startX > targetX)) {
                int dist = getDistanceSquare(-startX, startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 2. 오른쪽 벽 대칭 (2 * m - startX, startY)
            // 같은 Y선상이고 타겟 공이 시작 공보다 오른쪽에 있으면 오른쪽 벽쿠션 불가
            if (!(startY == targetY && startX < targetX)) {
                int dist = getDistanceSquare(2 * m - startX, startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 3. 아래쪽 벽 대칭 (startX, -startY)
            // 같은 X선상이고 타겟 공이 시작 공보다 아래쪽에 있으면 아래쪽 벽쿠션 불가
            if (!(startX == targetX && startY > targetY)) {
                int dist = getDistanceSquare(startX, -startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            // 4. 위쪽 벽 대칭 (startX, 2 * n - startY)
            // 같은 X선상이고 타겟 공이 시작 공보다 위쪽에 있으면 위쪽 벽쿠션 불가
            if (!(startX == targetX && startY < targetY)) {
                int dist = getDistanceSquare(startX, 2 * n - startY, targetX, targetY);
                minDistanceSquare = Math.min(minDistanceSquare, dist);
            }

            answer[i] = minDistanceSquare;
        }

        return answer;
    }

    // 두 점 사이의 거리 제곱 계산 함수
    private int getDistanceSquare(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
}