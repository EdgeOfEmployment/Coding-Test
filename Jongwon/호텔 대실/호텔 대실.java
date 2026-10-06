import java.util.*;

class Solution {
    public int solution(String[][] book_time) {
        int answer = 0;
        int n = book_time.length;
        int[][] time = new int[n][2];

        for(int i=0; i<n; i++){
            //입실
            String[] start = book_time[i][0].split(":");
            int sh = Integer.parseInt(start[0]);
            int sm = Integer.parseInt(start[1]);

            int stime = sh * 60 + sm;

            //퇴실
            String[] end = book_time[i][1].split(":");
            int eh = Integer.parseInt(end[0]);
            int em = Integer.parseInt(end[1]);

            // 퇴실시간에는 청소시간 10분 더하기
            int etime = eh * 60 + em + 10;

            time[i][0] = stime;
            time[i][1] = etime;
        }

        // 시간 빠른 순으로 정렬
        Arrays.sort(time, (a, b) -> a[0] - b[0]);

        // 호텔 대실 시간에 맞춰 우선순위를 두어 꺼내기 위해 우선순위 큐 사용
        PriorityQueue<Integer> room = new PriorityQueue<>();

        for(int i=0; i<n; i++){
            int stime = time[i][0];
            int etime = time[i][1];
            // 방이 비어있지 않고 방의 시간이 시작시간보다 작으면 그 방 다시 사용
            if(!room.isEmpty() && room.peek() <= stime){
                room.poll();
            }else{
                answer++;
            }
            room.add(etime);
        }

        return answer;
    }
}