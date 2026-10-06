import java.util.*;
class Solution {
    public int solution(String[][] book_time) {
        int answer = 0;
        int[][] time = new int[book_time.length][book_time[0].length];
        
        for (int i=0;i<book_time.length;i++){
            for (int j=0;j<book_time[0].length;j++){
                String[] tmp = book_time[i][j].split(":");
                int res = Integer.valueOf(tmp[0])*60+Integer.valueOf(tmp[1]);
                time[i][j]=res;
            }
        }
        
        Arrays.sort(time,(a,b)->a[0]-b[0]);
        
        HashMap<Integer,Integer> room = new HashMap<>();
        int cnt = 0;
        for (int i=0;i<time.length;i++){
            if (room.isEmpty()) room.put(cnt++,time[i][1]);
            else {
                int flag = 0;
                for (int j=0;j<room.size();j++){
                    if ((room.get(j)+10)<=time[i][0]) {
                        room.put(j,time[i][1]);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) room.put(cnt++,time[i][1]);

            }
        }
        
        answer = room.size();
        
        return answer;
    }
}
