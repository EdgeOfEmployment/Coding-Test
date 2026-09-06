/*
과제는 시간이 되면 시작
새로운 과제 할 시간 -> 진행중인 과제가 있다면 멈춤
진행 중인 과제 끝내고 멈춘 과제 이어함
만약 과제를 끝낸 시간에 새로 시작해야 되는 과제와 잠시 멈춰둔 과제가 모두 있다면,
새로 시작해야 하는 과제부터 진행
그냥 새로 시작하는게 우선
과제 계획을 담은 이차원 문자열 배열 plans
과제를 끝낸 순서대로 이름을 배열에 담아 return
plans의 원소는 [name, start, playtime]의 구조
시간을 먼저 파악해서 제일 빠른거 먼저 실행
*/

import java.util.*;
class Solution {
    public String[] solution(String[][] plans) {
        String[] answer = {};
        int n = plans.length;

        Deque<String[]> stop = new ArrayDeque<>();
        List<String> task = new LinkedList<>();

        // 시간을 정렬함
        Arrays.sort(plans, (a,b) -> a[1].compareTo(b[1]));
        System.out.println(Arrays.deepToString(plans));

        // 현재 과제 종료시간이랑 다음 과제 시작시간을 비교
        for(int i=0; i<n-1; i++){
            // 시간을 :으로 분리 후 분으로 변환
            String nowTime = plans[i][1];
            String nextTime = plans[i+1][1];

            String[] now = nowTime.split(":");
            String[] next = nextTime.split(":");

            int nowHour = Integer.parseInt(now[0]);
            int nowMin = Integer.parseInt(now[1]);

            int nextHour = Integer.parseInt(next[0]);
            int nextMin = Integer.parseInt(next[1]);

            int nowSum = nowHour * 60 + nowMin;
            int nextSum = nextHour * 60 + nextMin;

            int playtime = Integer.parseInt(plans[i][2]);

            int finishTime = nowSum + playtime;
            int leftTime = nowSum + playtime - nextSum; // 현재 과제 남은 시간
            int remainTime = nextSum - finishTime; //다음 과제 시작 전까지 남는 시간

            // 현재 과제 종료시간 < 다음 과제 시작시간(현재 과제 끝냄)
            // 현재 과제 종료시간 > 다음 과제 시작시간(현재 과제 멈춤)
            // 현재 과제 스택에 넣기
            if(finishTime <= nextSum){
                task.add(plans[i][0]);

                while(remainTime > 0 && !stop.isEmpty()){
                    String[] stopPlans = stop.pop();

                    String name = stopPlans[0];
                    int time = Integer.parseInt(stopPlans[1]);

                    // 멈춤과제 끝냄
                    if(time <= remainTime){
                        task.add(name);
                        remainTime = remainTime-time;
                    }else{
                        stop.push(new String[]{
                                name,
                                String.valueOf(time - remainTime)
                        });
                        remainTime = 0;
                    }

                }

            }else{
                stop.push(new String[]{
                        plans[i][0],
                        String.valueOf(leftTime)
                });
            }
        }
        // 마지막 과제 처리
        task.add(plans[n - 1][0]);


        // 남아있는 멈춘 과제 처리
        while (!stop.isEmpty()) {
            task.add(stop.pop()[0]);
        }


        return task.toArray(new String[0]);
    }
}