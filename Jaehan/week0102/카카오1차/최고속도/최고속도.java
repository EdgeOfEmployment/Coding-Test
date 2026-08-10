import java.util.*;

class Solution {
    // 1. 카메라는 도로 "정중앙" 좌표 하나에만 존재 (길이가 항상 짝수라 좌표가 정수로 딱 떨어짐)
    // 1-1. 한 좌표에 카메라가 여러 개 겹치면 그 중 제한 속도가 가장 낮은 값을 그 좌표의 제한으로 사용
    // 2. 카메라 좌표 = 노드, "그 좌표를 지나가는 모든 경로는 그 제한을 지켜야 한다" = 노드 용량 제약
    // 3. 도로/도로 교차점, 도로 끝점, 도시 좌표도 전부 노드로 쪼개야 그래프가 안 끊김
    // 4. 출발~도착까지 속도 하나로 고정 -> "경로 위 최저 제한을 최대화" = 최대 병목(widest path) 문제
    // 5. m(도로 개수) <= 1000이라 O(m^2) 쌍 비교 자체는 빠르지만, Long 박싱/TreeSet/List<Long>처럼
    //    객체를 많이 만들면 (특히 JIT 워밍업이 없는 1회성 실행에서) 확 느려짐
    //    -> 전부 원시 타입 long[] 배열 + 직접 정렬로 처리 (성능 개선 포인트, 실측 후 반영)
    private static final long OFFSET = 2_000_000_000L;

    public int[] solution(int[][] city, int[][] road) {
        int n = city.length;
        int m = road.length;
        final long INF = Long.MAX_VALUE / 4;

        boolean[] horiz = new boolean[m];
        for (int i = 0; i < m; i++) horiz[i] = road[i][1] == road[i][3];

        int hCount = 0;
        for (boolean b : horiz) if (b) hCount++;
        int[] hIdx = new int[hCount];
        int[] vIdx = new int[m - hCount];
        for (int i = 0, hp = 0, vp = 0; i < m; i++) {
            if (horiz[i]) hIdx[hp++] = i; else vIdx[vp++] = i;
        }

        // 6. cuts[i] = i번 도로를 분할할 "한 축 좌표"들의 모음 (수평 도로는 x, 수직 도로는 y만 저장)
        //    -> 원시 long[] 버퍼에 개수만 세서 채우고, 끝나면 실제 크기로 잘라서 정렬한다
        long[] camVal = new long[m];
        int[] cutCount = new int[m];
        long[][] cutBuf = new long[m][8]; // 끝점2 + 카메라1 + 교차점들, 필요하면 늘림

        for (int i = 0; i < m; i++) {
            int[] r = road[i];
            if (horiz[i]) {
                camVal[i] = (r[0] + r[2]) / 2;
                cutBuf[i][0] = r[0];
                cutBuf[i][1] = r[2];
            } else {
                camVal[i] = (r[1] + r[3]) / 2;
                cutBuf[i][0] = r[1];
                cutBuf[i][1] = r[3];
            }
            cutBuf[i][2] = camVal[i];
            cutCount[i] = 3;
        }

        // 7. 수평 x 수직 교차점 찾기 (H x V 쌍만 보면 됨, O(H*V) <= O(m^2/4))
        for (int i : hIdx) {
            long y = road[i][1];
            long x1 = road[i][0], x2 = road[i][2];
            for (int j : vIdx) {
                long x = road[j][0];
                long y1 = road[j][1], y2 = road[j][3];
                if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
                    cutBuf[i] = pushCut(cutBuf[i], cutCount, i, x);
                    cutBuf[j] = pushCut(cutBuf[j], cutCount, j, y);
                }
            }
        }

        // 8. 같은 축 위의 도로끼리 "끝점만 맞닿는" 경우도 처리 (겹치는 구간은 없고 한 점만 가능)
        Map<Integer, List<Integer>> byY = new HashMap<>();
        for (int i : hIdx) byY.computeIfAbsent(road[i][1], k -> new ArrayList<>()).add(i);
        for (List<Integer> idxs : byY.values()) {
            for (int a = 0; a < idxs.size(); a++) {
                for (int b = a + 1; b < idxs.size(); b++) {
                    int i = idxs.get(a), j = idxs.get(b);
                    int lo = Math.max(road[i][0], road[j][0]);
                    int hi = Math.min(road[i][2], road[j][2]);
                    if (lo == hi) {
                        cutBuf[i] = pushCut(cutBuf[i], cutCount, i, lo);
                        cutBuf[j] = pushCut(cutBuf[j], cutCount, j, lo);
                    }
                }
            }
        }
        Map<Integer, List<Integer>> byX = new HashMap<>();
        for (int i : vIdx) byX.computeIfAbsent(road[i][0], k -> new ArrayList<>()).add(i);
        for (List<Integer> idxs : byX.values()) {
            for (int a = 0; a < idxs.size(); a++) {
                for (int b = a + 1; b < idxs.size(); b++) {
                    int i = idxs.get(a), j = idxs.get(b);
                    int lo = Math.max(road[i][1], road[j][1]);
                    int hi = Math.min(road[i][3], road[j][3]);
                    if (lo == hi) {
                        cutBuf[i] = pushCut(cutBuf[i], cutCount, i, lo);
                        cutBuf[j] = pushCut(cutBuf[j], cutCount, j, lo);
                    }
                }
            }
        }

        // 9. 도시는 도로 위 임의의 지점일 수 있으므로, 소속된 도로의 분할점으로 반드시 추가해야 함
        //    (안 그러면 그 좌표가 그래프에서 고립돼서 못 찾음)
        for (int[] c : city) {
            for (int i = 0; i < m; i++) {
                int[] r = road[i];
                if (horiz[i]) {
                    if (c[1] == r[1] && r[0] <= c[0] && c[0] <= r[2]) {
                        cutBuf[i] = pushCut(cutBuf[i], cutCount, i, c[0]);
                    }
                } else {
                    if (c[0] == r[0] && r[1] <= c[1] && c[1] <= r[3]) {
                        cutBuf[i] = pushCut(cutBuf[i], cutCount, i, c[1]);
                    }
                }
            }
        }

        // 10. 원시 long[] 그대로 정렬 + 중복 제거 (박싱 없는 Arrays.sort 사용)
        long[][] cuts = new long[m][];
        for (int i = 0; i < m; i++) {
            long[] arr = Arrays.copyOf(cutBuf[i], cutCount[i]);
            Arrays.sort(arr);
            int w = 0;
            for (int r = 0; r < arr.length; r++) {
                if (w == 0 || arr[r] != arr[w - 1]) arr[w++] = arr[r];
            }
            cuts[i] = Arrays.copyOf(arr, w);
        }

        // 11. 정수 좌표 -> 실제 (x, y) 점으로 변환하면서 노드 id 부여 (여기서만 좌표쌍 생성)
        //     java.util.HashMap<Long,Integer>는 Long 박싱 때문에 (특히 JIT 워밍업 없는 1회성 실행에서)
        //     엄청 느려서, 원시 long[]/int[] 배열로 직접 만든 오픈 어드레싱 해시맵을 사용한다
        long totalCuts = n;
        for (int i = 0; i < m; i++) totalCuts += cuts[i].length;
        PointIdMap pointId = new PointIdMap((int) totalCuts);

        for (int[] c : city) pointId.getId(key(c[0], c[1]));

        int[][] roadPts = new int[m][];
        for (int i = 0; i < m; i++) {
            int[] r = road[i];
            long[] cl = cuts[i];
            roadPts[i] = new int[cl.length];
            for (int k = 0; k < cl.length; k++) {
                long v = cl[k];
                long pk = horiz[i] ? key(v, r[1]) : key(r[0], v);
                roadPts[i][k] = pointId.getId(pk);
            }
        }

        int nodeCount = pointId.size();
        long[] cap = new long[nodeCount];
        Arrays.fill(cap, INF);
        for (int i = 0; i < m; i++) {
            int[] r = road[i];
            long pk = horiz[i] ? key(camVal[i], r[1]) : key(r[0], camVal[i]);
            int pid = pointId.getId(pk);
            if (r[4] < cap[pid]) cap[pid] = r[4];
        }

        // 12. 간선 가중치 = min(양 끝 노드 용량) : 간선을 지나려면 양 끝 좌표를 반드시 거치므로
        //     인접 리스트도 배열 크기를 먼저 세고 한 번에 채워서 ArrayList<long[]> 오토박싱을 피한다
        int[] deg = new int[nodeCount];
        for (int i = 0; i < m; i++) {
            int len = roadPts[i].length - 1;
            for (int a = 0; a < len; a++) {
                deg[roadPts[i][a]]++;
                deg[roadPts[i][a + 1]]++;
            }
        }
        int[][] adjTo = new int[nodeCount][];
        long[][] adjW = new long[nodeCount][];
        for (int i = 0; i < nodeCount; i++) {
            adjTo[i] = new int[deg[i]];
            adjW[i] = new long[deg[i]];
        }
        int[] fillPos = new int[nodeCount];
        for (int i = 0; i < m; i++) {
            int[] ids = roadPts[i];
            for (int a = 0; a < ids.length - 1; a++) {
                int u = ids[a], v = ids[a + 1];
                long w = Math.min(cap[u], cap[v]);
                adjTo[u][fillPos[u]] = v; adjW[u][fillPos[u]++] = w;
                adjTo[v][fillPos[v]] = u; adjW[v][fillPos[v]++] = w;
            }
        }

        // 13. 최대 병목 경로(widest path) 다익스트라: 완화 조건이 "더하기"가 아니라 "min으로 갱신"
        int start = pointId.getId(key(city[0][0], city[0][1]));
        long[] best = new long[nodeCount];
        Arrays.fill(best, -1);
        best[start] = INF;

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));
        pq.add(new long[]{INF, start});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long bottleneck = cur[0];
            int u = (int) cur[1];
            if (bottleneck < best[u]) continue;
            int[] to = adjTo[u];
            long[] wArr = adjW[u];
            for (int k = 0; k < to.length; k++) {
                int v = to[k];
                long nb = Math.min(bottleneck, wArr[k]);
                if (nb > best[v]) {
                    best[v] = nb;
                    pq.add(new long[]{nb, v});
                }
            }
        }

        // 14. 카메라를 아예 안 만나는 경로가 있으면 속도 제한이 없다는 뜻 -> 0으로 반환
        int[] answer = new int[n - 1];
        for (int i = 1; i < n; i++) {
            int pid = pointId.getId(key(city[i][0], city[i][1]));
            long b = best[pid];
            answer[i - 1] = (b == INF) ? 0 : (int) b;
        }
        return answer;
    }

    private long[] pushCut(long[] buf, int[] cutCount, int idx, long value) {
        if (cutCount[idx] == buf.length) {
            buf = Arrays.copyOf(buf, buf.length * 2);
        }
        buf[cutCount[idx]++] = value;
        return buf;
    }

    private long key(long x, long y) {
        return ((x + OFFSET) << 32) | ((y + OFFSET) & 0xFFFFFFFFL);
    }

    // 좌표(long 키) -> 노드 id, 오픈 어드레싱(선형 탐사) 방식의 원시 타입 전용 해시맵.
    // 빈 슬롯 표시로 Long.MIN_VALUE를 쓰는데, key()는 항상 OFFSET을 더한 좌표만 만들어서 절대 겹치지 않는다.
    private static final class PointIdMap {
        private final long[] keys;
        private final int[] vals;
        private final int mask;
        private int count;

        PointIdMap(int expected) {
            int cap = 8;
            while (cap < expected * 2) cap <<= 1;
            keys = new long[cap];
            Arrays.fill(keys, Long.MIN_VALUE);
            vals = new int[cap];
            mask = cap - 1;
        }

        int getId(long k) {
            int idx = (int) mix(k) & mask;
            while (keys[idx] != Long.MIN_VALUE) {
                if (keys[idx] == k) return vals[idx];
                idx = (idx + 1) & mask;
            }
            keys[idx] = k;
            vals[idx] = count;
            return count++;
        }

        int size() {
            return count;
        }

        // key()가 만드는 좌표는 짝수 격자처럼 규칙적인 패턴이 많아서, Long.hashCode()의 단순
        // XOR 폴딩(x ^ (x>>>32))을 그대로 쓰면 특정 입력에서 버킷이 한쪽으로 쏠려 선형 탐사가
        // O(n)까지 늘어난다 (실제로 이 문제 때문에 시간초과가 났었음). MurmurHash3 finalizer로
        // 비트를 충분히 섞어서 이런 쏠림을 방지한다.
        private static long mix(long z) {
            z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
            z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
            return z ^ (z >>> 33);
        }
    }
}
