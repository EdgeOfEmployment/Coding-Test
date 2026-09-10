import java.util.*;
class Solution {
    int[][] dxy = {{-1,0}, {0,1}, {1,0}, {0,-1}};//좌표
    boolean[][] visited;
    int n, m;

    public int[] solution(String[] maps) {
        int[] answer = {};
        n = maps.length;
        m = maps[0].length();

        visited = new boolean[n][m];
        // 식량의 합을 리스트에 저장
        ArrayList<Integer> list = new ArrayList<>();

        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                // 방문하지 않았고 단어가 X가 아니라면 dfs 돌리기
                if(!visited[i][j] && maps[i].charAt(j) != 'X'){
                    // 리스트에 dfs값 넣기
                    list.add(dfs(maps, i, j));
                }
            }
        }

        if(list.isEmpty()){
            return new int[]{-1};
        }
        //오름차순 정렬
        Collections.sort(list);

        // 리스트를 배열로 변환
        answer = new int[list.size()];

        for(int i=0; i<list.size(); i++){
            answer[i]=list.get(i);
        }

        return answer;
    }

    public int dfs(String[] maps, int r, int c){
        visited[r][c] = true;

        int sum = maps[r].charAt(c)-'0'; // '5'를 숫자 5로 만들어서 저장

        // 상하좌우 확인
        for(int i=0; i<4; i++){
            int nr = r + dxy[i][0];
            int nc = c + dxy[i][1];

            // 범위 벗어나는지 확인
            if(nr >=0 && nr<n && nc>=0 && nc<m){
                if(!visited[nr][nc] && maps[nr].charAt(nc) != 'X'){
                    sum += dfs(maps, nr, nc);
                }
            }
        }
        return sum;
    }
}