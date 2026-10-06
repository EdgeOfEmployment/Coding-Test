# 호텔 대실

## 문제 링크

[프로그래머스 - 호텔 대실](https://school.programmers.co.kr/learn/courses/30/lessons/155651)

## 문제 해결 방법

처음에는 예약 시간들이 문자열로 주어져 있어서 시간끼리 어떻게 비교해야 할지 생각했다. `"15:00"`과 같은 문자열을 그대로 비교하기보다는 시간과 분을 분리해서 분 단위의 숫자로 변환하면 쉽게 비교할 수 있을 것 같았다.

예를 들어 `15:00`은

```text
15 × 60 + 0 = 900
```

으로 변환했다.

또한 한 번 사용한 방은 퇴실 후 10분 동안 청소를 해야 하므로 퇴실 시간에 10분을 더해서 방을 다시 사용할 수 있는 시간을 저장했다.

예약을 처리할 때는 먼저 입실 시간이 빠른 순서대로 정렬했다. 그래야 손님이 들어오는 순서대로 방을 배정할 수 있기 때문이다.

그다음 각 방이 언제 다시 사용할 수 있는지를 관리하기 위해 `PriorityQueue`를 사용했다.

`PriorityQueue`에는 각 방의 사용 가능 시간을 저장했다. 우선순위 큐는 가장 작은 값부터 꺼낼 수 있기 때문에 현재 사용 중인 방 중에서 가장 빨리 비는 방을 쉽게 확인할 수 있었다.

현재 예약의 입실 시간보다 가장 빨리 비는 방의 시간이 빠르거나 같다면 그 방을 다시 사용할 수 있다.

```text
방 사용 가능 시간 <= 현재 입실 시간
```

조건을 만족하면 `poll()`을 사용해서 해당 방을 큐에서 꺼내고 현재 예약의 퇴실 시간 + 10분을 다시 큐에 넣었다.

반대로 사용할 수 있는 방이 없다면 새로운 방이 필요한 것이므로 `answer`를 증가시켰다.

결국 `PriorityQueue`를 이용해서 가장 빨리 사용할 수 있는 방부터 확인하면서 필요한 최소 객실 수를 구할 수 있었다.

## 어려웠던 점

처음에는 예약 시간을 분 단위로 변환하고 시작 시간이 빠른 순서대로 정렬하는 것까지는 생각했지만, 여러 개의 방을 어떻게 관리해야 하는지 생각하는 것이 어려웠다.

특히 처음에는 `Queue`를 사용해야 한다는 생각은 들었지만, 일반적인 큐는 먼저 들어온 순서대로 데이터를 꺼내기 때문에 호텔 대실 문제에서 필요한 **가장 빨리 비는 방을 찾는 것**과 맞지 않는다는 것을 알게 되었다.

그래서 `PriorityQueue`를 사용해야 하는지 처음에는 몰랐고, 방의 사용 가능 시간을 어떤 방식으로 관리해야 할지 막혀서 AI의 도움을 받았다.

AI의 설명을 통해 `PriorityQueue`는 먼저 들어온 순서가 아니라 우선순위가 높은 값부터 꺼낼 수 있다는 것을 이해했다. 이 문제에서는 가장 작은 시간이 가장 빨리 비는 방을 의미하기 때문에 `PriorityQueue`를 사용하면 가장 빨리 사용할 수 있는 방을 쉽게 찾을 수 있었다.


이번 문제를 통해 단순한 `Queue`와 `PriorityQueue`의 차이를 이해했고, 여러 개의 방 중에서 가장 빨리 사용할 수 있는 방을 관리할 때 우선순위 큐를 사용할 수 있다는 것을 배웠다.

## 코드

```java
import java.util.*;

class Solution {
    public int solution(String[][] book_time) {
        int answer = 0;
        int n = book_time.length;
        int[][] time = new int[n][2];
        
        for(int i = 0; i < n; i++){
            // 입실
            String[] start = book_time[i][0].split(":");
            int sh = Integer.parseInt(start[0]);
            int sm = Integer.parseInt(start[1]);
            
            int stime = sh * 60 + sm;
            
            // 퇴실
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
        
        // 가장 빨리 비는 방을 확인하기 위해 우선순위 큐 사용
        PriorityQueue<Integer> room = new PriorityQueue<>();
        
        for(int i = 0; i < n; i++){
            int stime = time[i][0];
            int etime = time[i][1];
            
            // 사용할 수 있는 방이 있으면 기존 방 재사용
            if(!room.isEmpty() && room.peek() <= stime){
                room.poll();
            } else {
                // 사용할 수 있는 방이 없으면 새 방 필요
                answer++;
            }
            
            // 현재 방의 사용 가능 시간 저장
            room.add(etime);
        }
        
        return answer;
    }
}
```