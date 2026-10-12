import java.util.*;
class Solution {
    public int solution(int n, int[][] results) {
        int answer = 0;
        // 접근 방법
        // 그래프 생성 후 주어진 번호에서 연결된 모든 노드 탐색 -> cnt +1
        List<List<Integer>> graph = new ArrayList<>(); // 정방향
        List<List<Integer>> rev_graph = new ArrayList<>(); // 역방향
        for (int i = 0; i < n+1; i++){
            graph.add(new ArrayList<>());
            rev_graph.add(new ArrayList<>());
        }
        
        for (int[] result : results){
            int a = result[0];
            int b = result[1];
        
            graph.get(b).add(a); // 나를 이긴 선수를 담는 배열로 생성
            rev_graph.get(a).add(b); // 내가 진 선수를 담는 배열로 생성
        }
        
        // System.out.println(graph);
                
        // 대결 횟수를 세야하므로 인접된 다음 노드가 나오면 무조건 해당 노드에도 cnt +1
        // ex) 인덱스 2번(선수 2번)은 1,3,4에게 패했음, 인덱스 5번(선수 5번)은 2번에게 패했음 -> 2번은 1,3,4에게 졌고 5에겐 이겼으므로 cnt = 4가 돼야함 --> 그래프를 정방향, 역방향에서 두 번 탐색
        // 탐색을 수행하면서 cnt가 5인 선수를 카운트해서 리턴
        
        // cnt 관리를 위한 Map 선언
        Map<Integer,Integer> player_cnt = new HashMap<>();
        for (int j = 1; j < n+1; j++){
            player_cnt.put(j,0);
        }
        
        // 탐색
        bfs(n, results, graph, player_cnt);
        bfs(n, results, rev_graph, player_cnt);

        // System.out.println(player_cnt); 
        
        for (int p = 1; p < n + 1; p++){
            if (player_cnt.get(p) == n - 1) {
                answer += 1;
            }
        }
        
        return answer;
    }
    public void bfs(int n, int[][] results, List<List<Integer>> graph, Map<Integer,Integer> player_cnt) {
        for (int k = 1; k < n + 1; k++) {
        Deque<Integer> q = new ArrayDeque<>();
        boolean[] visited = new boolean[n+1];
        q.offer(k);
        visited[k] = true;
            
        while(!q.isEmpty()){
            int current = q.poll();
            for (int nxt_node : graph.get(current)){
                if (!visited[nxt_node]) {
                    visited[nxt_node] = true;
                    q.offer(nxt_node);                    
                    player_cnt.put(k, player_cnt.getOrDefault(k, 0) + 1); // 대결 횟수 카운트
                    }
                }
            }
        }
    }
}