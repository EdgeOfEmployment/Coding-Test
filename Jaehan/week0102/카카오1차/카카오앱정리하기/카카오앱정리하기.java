import java.util.*;

class Solution {
    // 1. 앱은 전부 정사각형 -> (r0, c0, size)로 표현 가능 (왼쪽 위 좌표 + 한 변 길이)
    // 2. 앱 하나를 밀면 앞쪽에 다른 앱이 있으면 그 앱도 밀림 -> BFS로 "같이 움직일 앱들" 찾기
    // 3. (중요, 여기서 계속 틀렸었음) 이동은 "칸 단위 모듈러"로 한 칸씩 처리한다. 즉 크기 2 이상 앱이
    //    경계를 넘어가면 일단 좌표만 (좌표+1) % n(또는 m) 으로 넘어가고, 그 결과 정사각형이 두 조각으로
    //    "걸쳐있는" 상태(straddle)가 될 수 있다. 통째로 반대편 끝(0)으로 스냅시키는 게 아니다!
    // 4. 이번 이동으로 앱이 경계에 걸치게 됐으면(wrapped), 이번 라운드가 끝난 뒤 "다음 라운드"에
    //    그 앱을 같은 방향으로 한 번 더 민다 -> 이걸 반복하면 크기가 s인 앱은 최대 s번 정도 더 밀리면서
    //    결국 다시 완전한 정사각형 모양으로 정착한다. (실제 문제 게시판에 공유된 반례로 검증한 규칙)
    // 5. 한 라운드 안에서 "정상적으로 옆 칸에 있어서 밀리는 앱들"을 큐로 전부 처리하고 나서야,
    //    그 라운드에서 경계에 걸쳤던 앱들을 다음 라운드 큐로 넘겨 처리한다(웨이브 방식).
    private int n, m, dx, dy;
    private int[][] grid;
    private Map<Integer, int[]> apps; // id -> [r0, c0, size]

    public int[][] solution(int[][] board, int[][] commands) {
        n = board.length;
        m = board[0].length;
        grid = new int[n][m];
        for (int i = 0; i < n; i++) grid[i] = board[i].clone();

        apps = findApps();

        int[][] dirs = {{}, {0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] cmd : commands) {
            int id = cmd[0];
            int arrow = cmd[1];
            dx = dirs[arrow][0];
            dy = dirs[arrow][1];
            pushCommand(id);
        }

        return grid;
    }

    private Map<Integer, int[]> findApps() {
        Map<Integer, int[]> result = new HashMap<>();
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int id = grid[i][j];
                if (id == 0 || seen.contains(id)) continue;
                seen.add(id);
                int size = 1;
                while (j + size < m && grid[i][j + size] == id) {
                    size++;
                }
                result.put(id, new int[]{i, j, size});
            }
        }
        return result;
    }

    private int[][] cells(int r0, int c0, int size) {
        int[][] result = new int[size * size][2];
        int idx = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[idx][0] = ((r0 + i) % n + n) % n;
                result[idx][1] = ((c0 + j) % m + m) % m;
                idx++;
            }
        }
        return result;
    }

    // 한 칸(모듈러) 이동시키고, 새로 걸리는 다른 앱들(blockers)과 경계에 걸쳤는지(wrapped)를 반환
    private boolean shiftOnce(int appId, Set<Integer> blockersOut) {
        int[] p = apps.get(appId);
        int r0 = p[0], c0 = p[1], size = p[2];
        int[][] oldCells = cells(r0, c0, size);

        int nr0 = ((r0 + dx) % n + n) % n;
        int nc0 = ((c0 + dy) % m + m) % m;
        int[][] newCells = cells(nr0, nc0, size);

        Set<Long> oldSet = new HashSet<>();
        for (int[] c : oldCells) oldSet.add(c[0] * 1000L + c[1]);

        for (int[] c : newCells) {
            long key = c[0] * 1000L + c[1];
            if (oldSet.contains(key)) continue;
            int occ = grid[c[0]][c[1]];
            if (occ != 0 && occ != appId) blockersOut.add(occ);
        }

        for (int[] c : oldCells) {
            if (grid[c[0]][c[1]] == appId) grid[c[0]][c[1]] = 0;
        }
        for (int[] c : newCells) {
            grid[c[0]][c[1]] = appId;
        }

        apps.put(appId, new int[]{nr0, nc0, size});

        return (dx != 0 && nr0 + size > n) || (dy != 0 && nc0 + size > m);
    }

    private void pushCommand(int startId) {
        Deque<Integer> queue = new ArrayDeque<>();
        Set<Integer> queued = new HashSet<>();
        queue.add(startId);
        queued.add(startId);
        Deque<Integer> wrappedNext = new ArrayDeque<>();

        while (!queue.isEmpty() || !wrappedNext.isEmpty()) {
            while (!queue.isEmpty()) {
                int appId = queue.poll();
                queued.remove(appId);
                Set<Integer> blockers = new HashSet<>();
                boolean wrapped = shiftOnce(appId, blockers);
                for (int b : blockers) {
                    if (!queued.contains(b)) {
                        queue.add(b);
                        queued.add(b);
                    }
                }
                if (wrapped) wrappedNext.add(appId);
            }
            queue = wrappedNext;
            queued = new HashSet<>(queue);
            wrappedNext = new ArrayDeque<>();
        }
    }
}
