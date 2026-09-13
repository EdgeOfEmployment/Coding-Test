미로 탈출

문제 요약

미로에는 시작 지점 S, 레버 L, 출구 E, 통로 O, 벽 X가 있습니다.

시작 지점에서 출구로 바로 이동하는 것이 아니라, 반드시 레버가 있는 칸에 먼저 도착하여 레버를 작동한 뒤 출구로 이동해야 합니다. 상하좌우로 한 칸 이동할 때마다 1초가 걸리며, 탈출할 수 없다면 -1을 반환합니다.

핵심 아이디어

이 문제는 하나의 이동 경로를 다음 두 구간으로 나누어 생각할 수 있습니다.

시작 지점 S에서 레버 L까지의 최단거리

레버 L에서 출구 E까지의 최단거리

두 구간을 모두 이동할 수 있다면 각각의 최단거리를 더한 값이 정답입니다.

정답 = S → L 최단거리 + L → E 최단거리

둘 중 한 구간이라도 도달할 수 없다면 탈출이 불가능하므로 -1을 반환합니다.

BFS를 사용한 이유

각 칸으로 한 번 이동하는 비용이 모두 1로 동일합니다. 이런 격자 그래프에서 가까운 위치부터 차례대로 탐색하는 BFS를 사용하면 목적지에 처음 도착했을 때의 이동 횟수가 최단거리임을 보장할 수 있습니다.

DFS는 하나의 경로를 끝까지 탐색하므로 처음 발견한 경로가 최단거리라는 보장이 없습니다. 따라서 별도의 최솟값 비교가 필요 없는 BFS가 더 적합합니다.

풀이 과정

1. 주요 지점 탐색

maps의 모든 문자를 확인하여 시작점, 레버, 출구의 좌표를 저장합니다.

if (maps[i].charAt(j) == 'S') {
    start[0] = i;
    start[1] = j;
} else if (maps[i].charAt(j) == 'L') {
    lever[0] = i;
    lever[1] = j;
} else if (maps[i].charAt(j) == 'E') {
    exit[0] = i;
    exit[1] = j;
}

2. 시작점에서 레버까지 BFS

큐에는 현재 좌표와 시작점으로부터 걸린 시간을 저장합니다.

class Miro {
    int x;
    int y;
    int t;
}

현재 칸을 기준으로 상하좌우를 확인하며 다음 조건을 만족하는 칸만 큐에 넣습니다.

미로의 범위 안에 있는 칸

아직 방문하지 않은 칸

벽 X가 아닌 칸

레버를 발견하면 해당 위치까지 걸린 시간을 저장합니다.

3. 레버에서 출구까지 BFS

레버 위치를 새로운 시작점으로 설정하여 같은 방식으로 BFS를 수행합니다.

첫 번째 탐색과 이동 경로가 달라질 수 있으므로 새로운 visited 배열을 사용해야 합니다. 시작점에서 레버까지 이동하면서 방문했던 칸도 레버를 작동한 뒤에는 다시 지나갈 수 있기 때문입니다.

4. 결과 반환

레버에 도달할 수 없다면 -1

레버에서는 출구에 도달할 수 없다면 -1

두 구간 모두 이동 가능하면 두 이동 시간의 합 반환

현재 코드에서 잘한 점

최단거리 탐색에 BFS를 사용했습니다.

S → L과 L → E를 독립적인 탐색으로 분리했습니다.

각 BFS마다 별도의 방문 배열을 생성했습니다.

범위, 방문 여부, 벽 여부를 확인하여 잘못된 이동을 방지했습니다.

개선할 수 있는 점

목적지를 찾으면 즉시 반환하기

BFS는 목적지를 처음 발견한 순간의 거리가 최단거리입니다. 따라서 Math.min()으로 거리를 비교하거나 남은 큐를 계속 탐색할 필요가 없습니다.

현재 코드의 break는 상하좌우를 확인하는 for문만 종료합니다. while문은 계속 실행되므로 정답에는 문제가 없지만 불필요한 탐색이 발생합니다.

방문 처리를 일관되게 하기

현재 코드는 일반 통로를 큐에 넣을 때 방문 처리하지만, 목적지 칸은 방문 처리하지 않습니다. 목적지를 발견한 즉시 반환하도록 구현하면 이 부분도 자연스럽게 단순해집니다.

중복된 BFS 로직을 메서드로 분리하기

레버 탐색과 출구 탐색의 구조가 동일하므로 공통 BFS 메서드를 만들면 코드의 중복을 줄이고 가독성을 높일 수 있습니다.

불필요한 변수 제거하기

answer, flag1, flag2, Integer.MAX_VALUE로 초기화한 시간 변수는 BFS 메서드가 거리 또는 -1을 바로 반환하도록 만들면 필요하지 않습니다.

개선된 코드

import java.util.*;

class Solution {

    static class Miro {
        int x;
        int y;
        int time;

        Miro(int x, int y, int time) {
            this.x = x;
            this.y = y;
            this.time = time;
        }
    }

    private final int[][] directions = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };

    public int solution(String[] maps) {
        int[] start = new int[2];
        int[] lever = new int[2];

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length(); j++) {
                char cell = maps[i].charAt(j);

                if (cell == 'S') {
                    start[0] = i;
                    start[1] = j;
                } else if (cell == 'L') {
                    lever[0] = i;
                    lever[1] = j;
                }
            }
        }

        int leverTime = bfs(maps, start, 'L');
        if (leverTime == -1) {
            return -1;
        }

        int exitTime = bfs(maps, lever, 'E');
        if (exitTime == -1) {
            return -1;
        }

        return leverTime + exitTime;
    }

    private int bfs(String[] maps, int[] start, char target) {
        int rows = maps.length;
        int columns = maps[0].length();

        Queue<Miro> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[rows][columns];

        queue.offer(new Miro(start[0], start[1], 0));
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            Miro current = queue.poll();

            if (maps[current.x].charAt(current.y) == target) {
                return current.time;
            }

            for (int[] direction : directions) {
                int nextX = current.x + direction[0];
                int nextY = current.y + direction[1];

                if (nextX < 0 || nextX >= rows ||
                    nextY < 0 || nextY >= columns) {
                    continue;
                }

                if (visited[nextX][nextY] ||
                    maps[nextX].charAt(nextY) == 'X') {
                    continue;
                }

                visited[nextX][nextY] = true;
                queue.offer(new Miro(nextX, nextY, current.time + 1));
            }
        }

        return -1;
    }
}

정확성 설명

첫 번째 BFS는 시작점에서 이동 가능한 모든 칸을 거리 순서대로 탐색합니다. 따라서 레버를 처음 만났을 때 기록된 시간은 시작점에서 레버까지의 최단거리입니다.

두 번째 BFS도 같은 원리로 레버에서 출구까지의 최단거리를 구합니다. 문제의 조건상 레버를 반드시 먼저 작동해야 하므로 전체 경로는 반드시 두 구간으로 구성됩니다. 따라서 두 최단거리의 합은 조건을 만족하는 전체 경로의 최소 이동 시간입니다.

어느 한 BFS에서 목적지에 도달하지 못하면 조건을 만족하는 전체 경로도 존재하지 않으므로 -1을 반환합니다.

복잡도 분석

미로의 행 개수를 N, 열 개수를 M이라고 하겠습니다.

위치 탐색: O(N × M)

BFS 2회: O(N × M)

전체 시간 복잡도: O(N × M)

방문 배열과 큐의 공간 복잡도: O(N × M)

BFS를 두 번 수행하더라도 상수 배만 증가하므로 전체 시간 복잡도는 O(N × M)입니다.
