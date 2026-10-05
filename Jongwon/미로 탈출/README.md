# 미로 탈출

## 문제 링크

[프로그래머스 - 미로 탈출](https://school.programmers.co.kr/learn/courses/30/lessons/159993)

## 문제 해결 방법

처음에는 미로에서 `S`에서 `E`까지 가장 빠르게 이동하면 된다고 생각했다. 하지만 문제를 다시 보니 출구로 가기 전에 반드시 레버 `L`을 먼저 당겨야 했다.

그래서 한 번에 `S`에서 `E`까지 이동하는 것이 아니라,

```text
S → L → E
```

두 구간으로 나누어서 생각했다.

각 구간에서 가장 빠르게 이동해야 하고, 한 칸 이동할 때 1초가 걸리기 때문에 최단거리를 구하는 BFS를 사용했다.

먼저 이중 for문을 사용해서 미로를 처음부터 끝까지 확인하면서 `S`, `L`, `E`의 위치를 각각 배열에 저장했다.

```text
start → S의 위치
lever → L의 위치
exit → E의 위치
```

그다음 BFS를 두 번 사용했다.

첫 번째 BFS에서는 `S`에서 시작해서 `L`까지 가는 최단거리를 구했다.

```text
bfs(maps, start, lever)
```

두 번째 BFS에서는 `L`에서 시작해서 `E`까지 가는 최단거리를 구했다.

```text
bfs(maps, lever, exit)
```

두 BFS에서 구한 거리를 더하면 전체 탈출 시간이 된다.

```text
S → L 거리 + L → E 거리
```

만약 `S`에서 `L`까지 갈 수 없거나 `L`에서 `E`까지 갈 수 없다면 BFS에서 `-1`을 반환하도록 했다. 둘 중 하나라도 `-1`이면 탈출할 수 없는 것이므로 최종적으로 `-1`을 반환한다.

## BFS를 사용한 이유

BFS는 시작점에서 가까운 곳부터 차례대로 탐색하기 때문에 목적지에 처음 도착했을 때의 거리가 최단거리가 된다.

이 문제에서는 한 칸 이동할 때마다 1초가 걸리기 때문에 이동한 칸 수를 구하면 곧 걸린 시간이 된다.

그래서 Queue에 현재 위치와 이동한 거리를 함께 저장했다.

```text
[행, 열, 거리]
```

예를 들어 시작점에서는 아직 이동하지 않았기 때문에

```text
[0, 0, 0]
```

처럼 거리를 0으로 시작한다.

한 칸 이동하면

```text
[0, 1, 1]
```

처럼 거리에 1을 더해서 Queue에 넣는다.

목표 위치에 도착하면 현재 `dist`를 반환한다.

## BFS 동작 방법

먼저 시작 위치를 Queue에 넣고 방문했다는 표시를 한다.

```text
Queue에 시작 위치 넣기
↓
현재 위치 꺼내기
↓
상하좌우 확인
↓
미로 안에 있는지 확인
↓
벽이 아닌지 확인
↓
방문하지 않은 곳이면 방문 처리
↓
거리 + 1 해서 Queue에 넣기
```

상하좌우 이동은 `dxy` 배열을 사용했다.

```java
int[][] dxy = {{-1,0}, {0,1}, {1,0}, {0,-1}};
```

각각 위, 오른쪽, 아래, 왼쪽을 의미한다.

BFS를 두 번 사용하기 때문에 `visited`도 BFS가 시작될 때마다 새로 만들어서 이전 BFS의 방문 기록이 다음 BFS에 영향을 주지 않도록 했다.

## 어려웠던 점

처음에는 BFS를 어떻게 사용해야 하는지 헷갈렸다. 특히 `S`에서 `L`로 이동한 다음 다시 `L`에서 `E`로 이동해야 하기 때문에 BFS를 한 번만 사용해야 하는지 두 번 사용해야 하는지 고민했다.

문제를 다시 생각해보니 반드시

```text
S → L → E
```

순서로 이동해야 하기 때문에 `S → L`과 `L → E`를 각각 BFS로 구하면 된다는 것을 알게 되었다.


## 코드

```java
import java.util.*;

class Solution {
    int[][] dxy = {{-1,0}, {0,1}, {1,0}, {0,-1}};
    boolean[][] visited;
    int n,m;
    
    public int solution(String[] maps) {
        n = maps.length;
        m = maps[0].length();
        
        int[] start = new int[2];
        int[] lever = new int[2];
        int[] exit = new int[2];
        
        // S, L, E 위치 찾기
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                int c = maps[i].charAt(j);
                
                if(c == 'S'){
                    start[0] = i;
                    start[1] = j;
                }else if(c == 'L'){
                    lever[0] = i;
                    lever[1] = j;
                }else if(c == 'E'){
                    exit[0] = i;
                    exit[1] = j;
                }
            }
        }
        
        // S에서 L까지 거리
        int sl = bfs(maps, start, lever);
        
        // L에서 E까지 거리
        int le = bfs(maps, lever, exit);
        
        // 둘 중 하나라도 갈 수 없는 경우
        if(sl == -1 || le == -1){
            return -1;
        }
        
        return sl + le;
    }
    
    public int bfs(String[] maps, int[] start, int[] target){
        visited = new boolean[n][m];
        
        Queue<int[]> q = new ArrayDeque<>();
        
        // [행, 열, 거리]
        q.offer(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;
        
        while(!q.isEmpty()){
            int[] cur = q.poll();
            
            int r = cur[0];
            int c = cur[1];
            int dist = cur[2];
            
            // 목적지에 도착하면 거리 반환
            if(r == target[0] && c == target[1]){
                return dist;
            }
            
            // 상하좌우 확인
            for(int i=0; i<4; i++){
                int nr = r + dxy[i][0];
                int nc = c + dxy[i][1];
                
                // 미로 범위 안에 있는지 확인
                if(nr >= 0 && nr < n && nc >= 0 && nc < m){
                    
                    // 방문하지 않았고 벽이 아닌 경우
                    if(!visited[nr][nc] && maps[nr].charAt(nc) != 'X'){
                        visited[nr][nc] = true;
                        q.offer(new int[]{nr, nc, dist + 1});
                    }
                }
            }
        }
        
        // 목적지까지 갈 수 없는 경우
        return -1;
    }
}
```
