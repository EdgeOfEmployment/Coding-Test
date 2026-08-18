/*
n개의 배양체를 n-1개의 파이프로 이음
파이프는 a,b,c 3종류
배양체의 개수 : n
감염된 배양체의 노드 번호 : infection
파이프의 정보 : edges
최대 행동 수 : k
k번 파이프를 열었다 닫고 감염된 배양체의 최댓값을 return하라
*/

import java.util.*;

class 바이러스파이프 {

    static class GameState{
        boolean[] infected;
        int turn;

        GameState(boolean[] infected, int turn){
            this.infected = infected;
            this.turn = turn;
        }
    }

    public int solution(int n, int infection, int[][] edges, int k) {
        int answer = 0;

        Map<Integer, List<int[]>> map = new HashMap<>();
        for(int i=1; i<=n; i++){
            map.put(i, new ArrayList<>());
        }
        for(int[] e : edges){
            map.get(e[0]).add(new int[]{e[1], e[2]});
            map.get(e[1]).add(new int[]{e[0], e[2]});
        }

        Queue<GameState> q = new ArrayDeque<>();
        boolean[] startInfected = new boolean[n+1];
        startInfected[infection] = true;
        q.add(new GameState(startInfected, 0));

        Set<String>[] visited = new HashSet[k+1];
        for(int i=0; i<=k; i++){
            visited[i] = new HashSet<>();
        }
        visited[0].add(Arrays.toString(startInfected));

        while(!q.isEmpty()){
            GameState curState = q.poll();

            int currentCount = countInfected(curState.infected, n);
            answer = Math.max(answer, currentCount);

            if(curState.turn == k || currentCount == n) {
                continue;
            }

            for(int type=1; type<=3; type++) {
                boolean[] nextInfected = curState.infected.clone();

                Queue<Integer> sq = new ArrayDeque<>();
                for(int i=1; i<=n; i++){
                    if(nextInfected[i]) sq.add(i);
                }
                while(!sq.isEmpty()) {
                    int cur = sq.poll();

                    for(int[] edge : map.get(cur)) {
                        int nextNode = edge[0];
                        int edgeType = edge[1];

                        if(edgeType == type && !nextInfected[nextNode]){
                            nextInfected[nextNode] = true;
                            sq.add(nextNode);
                        }
                    }
                }

                String stateKey = Arrays.toString(nextInfected);
                if(!visited[curState.turn + 1].contains(stateKey)) {
                    visited[curState.turn + 1].add(stateKey);
                    q.add(new GameState(nextInfected, curState.turn + 1));
                }
            }
        }

        return answer;
    }

    private int countInfected(boolean[] infected, int n) {
        int cnt = 0;
        for(int i = 1; i <= n; i++) {
            if(infected[i]) cnt++;
        }
        return cnt;
    }
}