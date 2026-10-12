# 순위 문제 풀이 공유

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/49191)

## 1. 접근법과 단순 풀이의 한계

$n$명의 권투 선수가 1:1로 맞붙은 결과 일부가 `[A, B]`(A가 B를 이김) 형태로 주어집니다. 실력이 높은 선수는 항상 이기므로 승패에는 **전이성**이 있습니다. A가 B를 이기고 B가 C를 이겼다면 둘이 붙지 않았어도 A가 C보다 강합니다. 이 정보로 **순위를 정확히 확정할 수 있는 선수가 몇 명인지** 구하는 문제입니다.

- **간선을 지날 때마다 양쪽 선수의 횟수를 세는 접근의 한계**: 처음에는 "나를 이긴 선수" 방향 그래프 하나만 만들고, 모든 선수에서 BFS를 돌면서 간선을 지날 때마다 **현재 선수와 다음 선수의 카운트를 둘 다 1씩** 올렸습니다. 승패와 상관없이 "관계가 밝혀진 상대 수"를 한 번에 세려는 의도였습니다.

  ```java
  // 처음 접근 코드 (탐색 부분)
  for (int k = 1; k < n + 1; k++) {
      Deque<Integer> q = new ArrayDeque<>();
      q.offer(k);
      while(!q.isEmpty()){
          int current = q.poll();
          for (int nxt_node : graph.get(current)){
              player_cnt.put(current, player_cnt.getOrDefault(current, 0) + 1); // 현재 선수 대결 횟수 카운트
              player_cnt.put(nxt_node, player_cnt.getOrDefault(nxt_node, 0) + 1); // 다음 선수 대결 횟수 카운트
              q.offer(nxt_node);
          }
      }
  }
  ```

  그런데 카운트가 **탐색을 시작한 선수 `k`가 아니라 간선 양 끝의 선수**에게 쌓입니다. 같은 간선 `2 → 1`도 `k = 2`에서 출발할 때, `k = 5`에서 2를 거쳐 올 때 등 **출발점마다 반복해서** 지나가므로 같은 관계가 여러 번 더해집니다. `visited`가 없어서 여러 경로로 닿는 선수도 중복으로 세집니다. 결국 값이 "확정된 상대 수"와 무관하게 부풀어, 출력해 봐도 대결 횟수가 제대로 나오지 않았습니다.
- **방향 수정**: 한 선수 입장에서 순위가 확정되려면 **나보다 강한 게 확실한 선수 수 + 나보다 약한 게 확실한 선수 수 = $n - 1$** 이어야 합니다. 그래서 그래프를 **정방향(나를 이긴 선수)과 역방향(나에게 진 선수)** 두 개로 나누고, 각 선수에서 `visited`를 둔 BFS로 **도달 가능한 선수 수를 출발 선수 `k`에게만** 더하는 방식으로 접근을 정리했습니다.

## 2. 해결 코드

```java
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
```

## 3. 구현 전략 및 이유

### 정방향·역방향 그래프를 따로 탐색하는 이유

`graph`에서 선수 `k`를 출발해 닿는 선수들은 **`k`를 직접 또는 간접으로 이긴 선수**, 즉 `k`보다 강한 게 확실한 선수입니다. `rev_graph`에서 닿는 선수들은 반대로 **`k`보다 약한 게 확실한 선수**입니다. 전이성 덕분에 BFS로 도달한다는 사실 자체가 "실력 관계가 확정됐다"는 뜻이 됩니다.

두 방향을 한 그래프에 섞으면 안 됩니다. 무방향으로 탐색하면 `k`를 이긴 선수(A)에게 진 다른 선수(B)까지 닿는데, A > k, A > B만으로는 **k와 B의 관계를 알 수 없습니다.** 방향을 지킨 채 두 번 나눠 탐색해야 "확실히 강한 쪽"과 "확실히 약한 쪽"만 정확히 모입니다.

두 집합은 겹칠 수 없으므로(어떤 선수가 `k`보다 강하면서 약할 수는 없음) 단순히 더해도 중복이 생기지 않습니다. 합이 $n - 1$이면 나머지 모든 선수와의 관계가 밝혀진 것이므로 순위가 하나로 정해집니다.

### 카운트를 출발 선수 `k`에게만 더하는 이유

처음 코드가 실패한 지점이 여기였습니다. 알고 싶은 값은 **"`k` 입장에서 관계가 확정된 상대 수"** 이므로, BFS 중에 새 선수를 발견하면 그 선수가 아니라 **탐색을 시작한 `k`의 카운트**를 올려야 합니다. 상대 선수 쪽의 관계는 그 선수를 출발점으로 하는 BFS에서 따로 세어지므로, 한 번의 탐색에서 양쪽을 모두 챙길 필요가 없습니다.

### `visited`를 출발 선수마다 새로 만드는 이유

여러 경로로 같은 선수에 닿을 수 있습니다. 예를 들어 1 → 2 → 5, 1 → 3 → 5처럼 갈래가 합쳐지면 `visited` 없이는 5를 두 번 세어 카운트가 부풉니다. 반대로 `visited`를 모든 출발점이 공유하면 앞선 탐색에서 방문한 선수를 다음 출발점이 세지 못합니다. 그래서 `k`마다 `boolean[n+1]`을 새로 만들어 **한 번의 탐색 안에서만 중복을 막습니다.**

### 시간 및 공간 복잡도

- **시간 복잡도**: $O(N \times (N + E))$ ($N$은 선수 수, $E$는 경기 결과 수입니다. 선수마다 두 그래프에서 BFS를 한 번씩 돌고, BFS 한 번은 $O(N + E)$입니다. $N \le 100$, $E \le 4{,}500$이라 충분히 빠릅니다.)
- **공간 복잡도**: $O(N + E)$ (두 인접 리스트가 $O(N + E)$, 카운트 맵과 BFS의 큐·`visited`가 $O(N)$입니다.)
