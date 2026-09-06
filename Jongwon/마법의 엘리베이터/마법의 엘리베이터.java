import java.util.*;
/*
민수의 세계에서는 0층이 가장 아래층
 마법의 엘리베이터가 있는 층을 나타내는 정수 storey
 마법의 돌의 최소값을 return
*/
class Solution {
    public int solution(int storey) {
        int answer = 0;

        while(storey > 0){
            // 나머지연산자를 이용해 오른쪽 숫자를 가져옴
            int num = storey % 10;

            // 나머지가 5미만 일때는 -1을 한다.
            if(num < 5){
                answer += num;
            }else if(num > 5){
                answer += 10-num;
                storey += 10; // 6부터는 숫자를 +1하는데 자연스레 앞자리 수가 바뀌니까 더해줌
            }else{ // 5일때
                int first = (storey/10)%10; // 5를 -1, +1하고 난 뒤의 앞자리 확인

                if(first<5){
                    answer += num;
                }else{
                    answer += 10-num;
                    storey += 10;
                }
            }
            // 다음 자리로 넘어가기
            storey = storey / 10;
        }

        return answer;
    }
}