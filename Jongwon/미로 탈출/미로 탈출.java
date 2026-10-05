import java.util.*;
/*
문은 레버를 당겨야 열 수 있음
출발 지점에서 먼저 레버가 있는 칸으로 이동하여 레버를 당긴 후 미로를 빠져나가는 문이 있는 칸으로 이동
한칸 이동 = 1초
최대한 빠르게 지나가기
S : 시작 지점
E : 출구
L : 레버
O : 통로
X : 벽
bfs
*/
class Solution {
    int[][] dxy = {{-1,0}, {0,1}, {1,0}, {0,-1}};
    boolean[][] visited;
    int n,m;

    public int solution(String[] maps) {
        int answer = 0;
        n = maps.length;
        m = maps[0].length();

        int[] start = new int[2]; // start가 어디있는지 찾고 넣을 배열
        int[] lever = new int[2];
        int[] exit = new int[2];

        // s,l,e 위치 찾기
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

        // bfs 두번
        // s에서 l로 가는거 더하기
        int sl = bfs(maps, start, lever);

        // l에서 e로 가는거 더하기
        int le = bfs(maps, lever, exit);

        if(sl == -1 || le == -1){
            return -1;
        }

        return sl + le;
    }

    public int bfs(String[] maps, int[] start, int[] target){
        visited = new boolean[n][m];
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;

        while(!q.isEmpty()){
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];
            int dist = cur[2];

            // 목적지에 도달하면 멈춤
            if(r == target[0] && c == target[1]){
                return dist;
            }

            for(int i=0; i<4; i++){
                int nr = r+dxy[i][0];
                int nc = c+dxy[i][1];

                if(nr>=0 && nr<n && nc>=0 && nc<m){
                    if(!visited[nr][nc] && maps[nr].charAt(nc) != 'X'){
                        visited[nr][nc] = true;
                        q.offer(new int[]{nr, nc, dist+1});
                    }
                }
            }
        }
        return -1;
    }
}