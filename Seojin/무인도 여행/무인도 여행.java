import java.util.*;
class Solution {
    public int[] solution(String[] maps) {
        int[] answer = {};
        
        boolean[][] visited = new boolean[maps.length][maps[0].length()];
        
        ArrayList<Integer> iland = new ArrayList<>();
        int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int i=0;i<maps.length;i++){
            for (int j=0;j<maps[0].length();j++){
                if (!visited[i][j] && maps[i].charAt(j) != 'X'){
                    int sum = 0;
                    Deque<int[]> d = new ArrayDeque<>();
                    d.offer(new int[]{i,j});
                    visited[i][j] = true;
        
                    while(!d.isEmpty()){
                        int[] cur = d.poll();
                        sum+=maps[cur[0]].charAt(cur[1])-'0';
                        
                        for (int k=0;k<4;k++){
                            int cx = cur[0]+dir[k][0];
                            int cy = cur[1]+dir[k][1];
                            if (cx>=0 && cx<maps.length && cy>=0 && cy<maps[0].length() && !visited[cx][cy] && maps[cx].charAt(cy) != 'X'){
                                visited[cx][cy] = true;
                                d.offer(new int[]{cx,cy});
                            }
                        }
                    }
                    iland.add(sum);
                }
            }
        }
        
        Collections.sort(iland);
        if (iland.size()>0){
            answer = new int[iland.size()];
            for (int i=0;i<iland.size();i++){
                answer[i]=iland.get(i);
            }
        }
        else answer = new int[]{-1};
        
            
        return answer;
    }
}
