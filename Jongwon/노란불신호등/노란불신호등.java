/*
차량 신호등 n개(초->노->빨 순으로 반복)
처음에는 초록불
각 신호의 지속 시간은 신호등마다 다릅니다
모든 신호가 노란불이면 정전
모든 신호등이 노란불이 되는 가장 빠른시간을 리턴
그런 경우가 없으면 -1리턴
초록불 다음이 노란불이니까
*/
import java.util.*;

public class 노란불신호등 {
    public int solution(int[][] signals) {
        int answer = -1;
        int n = signals.length;
        // t는 흘러가는 실제 시간이고 1초부터 차례대로 돌려봄
        for(int t=1; t<3200000; t++){
            boolean yellow = true;

            // 현재 시간 t에 모든 신호등이 노란불인지 하나씩 검사함
            for(int i=0; i<n; i++){
                int g = signals[i][0];
                int y = signals[i][1];
                int r = signals[i][2];

                int c = g+y+r;
                int rr = t%c;
                if (rr == 0) rr = c;

                // 하나라도 노란불 구간이 아니라면 탈락 처리 후 다음 시간으로
                if(!(rr > g && rr<=g+y)){
                    yellow = false;
                    break;
                }
            }

            if(yellow){
                return t;
            }
        }


        return answer;
    }
}
