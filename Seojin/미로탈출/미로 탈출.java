import java.util.*;
class Solution {
    
    class Miro{
        int x;
        int y;
        int t;
        Miro(int x,int y, int t){
            this.x = x;
            this.y = y;
            this.t = t; 
        }
    }
    public int solution(String[] maps) {
        int answer = 0;
        
        //시작점, 레버점, 출구점 찾기
        int[] start = new int[2];
        int[] lever = new int[2];
        int[] exit = new int[2];
        
        for (int i=0;i<maps.length;i++){
            for (int j=0;j<maps[0].length();j++){
                if (maps[i].charAt(j) == 'S'){
                    start[0] = i;
                    start[1] = j;
                }
                else if (maps[i].charAt(j) == 'L'){
                    lever[0] = i;
                    lever[1] = j;
                }
                else if (maps[i].charAt(j) == 'E'){
                    exit[0] = i;
                    exit[1] = j;
                }
            }
        }
        
        //레버까지 이동
        Deque<Miro> d1 = new ArrayDeque<>();
        boolean[][] visited1= new boolean[maps.length][maps[0].length()];
        int[][] dir = {{0,1},{0,-1},{1,0},{-1,0}};
        
        d1.offer(new Miro(start[0],start[1],0));
        visited1[start[0]][start[1]] = true;
        int levTime = Integer.MAX_VALUE;
        int flag1 =1;
        
        while(!d1.isEmpty()){
            Miro cur = d1.poll();
            
            for (int i=0;i<4;i++){
                int cx = cur.x+dir[i][0];
                int cy = cur.y+dir[i][1];
                if (cx>=0 && cx<maps.length && cy>=0 && cy<maps[0].length() && !visited1[cx][cy] && maps[cx].charAt(cy) !='X'){
                    if (maps[cx].charAt(cy) == 'L'){
                        levTime = Math.min(levTime,cur.t+1);
                        flag1 = 0;
                        break;
                    }
                    else {
                        d1.offer(new Miro(cx,cy,cur.t+1));
                        visited1[cx][cy] = true;
                    }
                }
            }
        }
        
        if (flag1 == 1) return -1;
        
        //출구로 이동
        Deque<Miro> d2 = new ArrayDeque<>();
        boolean[][] visited2= new boolean[maps.length][maps[0].length()];
        
        d2.offer(new Miro(lever[0],lever[1],0));
        visited2[lever[0]][lever[1]] = true;
        int exitTime = Integer.MAX_VALUE;
        int flag2 =1;
        
        while(!d2.isEmpty()){
            Miro cur = d2.poll();
            
            for (int i=0;i<4;i++){
                int cx = cur.x+dir[i][0];
                int cy = cur.y+dir[i][1];
                if (cx>=0 && cx<maps.length && cy>=0 && cy<maps[0].length() && !visited2[cx][cy] && maps[cx].charAt(cy) !='X'){
                    if (maps[cx].charAt(cy) == 'E'){
                        exitTime = Math.min(cur.t+1,exitTime);
                        flag2=0;
                        break;
                    }
                    else {
                        d2.offer(new Miro(cx,cy,cur.t+1));
                        visited2[cx][cy] = true;
                    }
                }
            }
        }
        
        if (flag2 ==1) return -1;
        
        answer = levTime + exitTime;
        
        return answer;
    }
}
