# 숫자 카드 나누기

## 문제 링크

[프로그래머스 - 숫자 카드 나누기](https://school.programmers.co.kr/learn/courses/30/lessons/135807)

## 문제 해결 방법

처음에는 두 배열의 숫자들을 하나씩 비교하면서 조건을 만족하는 숫자를 찾아야 하나 생각했다. 하지만 배열의 길이가 최대 500,000이기 때문에 모든 숫자를 비교하는 방법은 비효율적일 것 같았다.

문제를 다시 보니 한 배열의 모든 숫자를 나눌 수 있는 가장 큰 수를 구하는 것이 중요했고, 이것이 최대공약수와 같다는 것을 이용했다.

먼저 `arrayA`의 첫 번째 숫자를 `gcdA`에 저장하고, 반복문을 통해 나머지 숫자들과 최대공약수를 계속 구했다. 이렇게 해서 `arrayA` 전체의 최대공약수를 구했다.

```text
gcdA = arrayA[0]

arrayA의 다음 숫자와 gcdA의 최대공약수를 구함
→ 그 결과를 다시 gcdA에 저장
→ 배열의 끝까지 반복
```

`arrayB`도 같은 방법으로 `gcdB`를 구했다.

그다음 `gcdA`가 문제의 조건을 만족하는지 확인했다. `gcdA`는 `arrayA`의 모든 숫자를 나눌 수 있으므로, `arrayB`의 숫자 중 `gcdA`로 나눌 수 있는 숫자가 하나라도 있는지만 확인하면 된다.

반복문에서 `arrayB[i] % gcdA == 0`인 숫자를 발견하면 `checkA`를 `true`로 변경했다. 하나라도 나눠지는 숫자가 있다면 조건을 만족하지 못하기 때문에 더 이상 확인할 필요가 없어 `break`를 사용했다.

끝까지 확인했는데 `checkA`가 `false`라면 `arrayB`의 숫자 중 `gcdA`로 나눌 수 있는 숫자가 하나도 없다는 뜻이다. 따라서 `gcdA`를 정답 후보인 `answerA`에 저장했다.

`gcdB`도 같은 방법으로 `arrayA`를 확인해서 조건을 만족하면 `answerB`에 저장했다.

마지막에는 `answerA`와 `answerB` 중 더 큰 값을 `Math.max()`를 이용해서 선택했다. 둘 다 조건을 만족하지 않는 경우에는 두 값이 모두 0이므로 0을 반환하게 된다.

## 최대공약수 구하는 방법

최대공약수는 유클리드 호제법을 사용했다.

두 숫자 `a`, `b`가 있을 때 `a`를 `b`로 나눈 나머지를 구하고, 다시 `b`와 나머지의 최대공약수를 구하는 방법이다.

나머지가 0이 되면 그때의 `a`가 최대공약수가 된다.

예를 들어 `20`과 `12`의 최대공약수를 구하면

```text
20 % 12 = 8
12 % 8 = 4
8 % 4 = 0
```

따라서 최대공약수는 `4`가 된다.

배열의 최대공약수도 같은 방법으로 앞에서부터 하나씩 계산했다.

## 어려웠던 점

처음에는 최대공약수를 어떻게 구하는지 몰라서 어려웠다. 특히 배열에 있는 여러 숫자의 최대공약수를 어떻게 구해야 하는지 헷갈렸다.

두 숫자의 최대공약수를 구한 결과를 다음 숫자와 다시 비교하면 배열 전체의 최대공약수를 구할 수 있다는 것을 알게 되었다.

또한 상대 배열에서 숫자가 하나라도 나눠지는 경우 조건을 만족하지 않는다는 것을 구현하는 것도 어려웠다. 처음에는 반복문 안에서 바로 `return`하려고 했지만, 그렇게 하면 첫 번째 숫자만 확인하고 끝날 수 있었다.

그래서 `boolean` 변수를 사용해서 나눠지는 숫자를 발견했는지 저장하고, 반복문이 끝난 후 조건을 확인하도록 구현했다.

## 코드

```java
import java.util.*;

class Solution {
    public int solution(int[] arrayA, int[] arrayB) {
        int answer = 0;
        int n = arrayA.length;
        int m = arrayB.length;
        
        // 배열 A의 최대공약수 구하기
        int gcdA = arrayA[0];
        
        for(int i = 0; i < n; i++){
            gcdA = gcd(gcdA, arrayA[i]);
        }

        // 배열 B의 최대공약수 구하기
        int gcdB = arrayB[0];
        
        for(int i = 0; i < m; i++){
            gcdB = gcd(gcdB, arrayB[i]);
        }
        
        // A의 최대공약수로 B의 숫자를 하나라도 나눌 수 있는지 확인
        boolean checkA = false;
        
        for(int i = 0; i < m; i++){
            if(arrayB[i] % gcdA == 0){
                checkA = true;
                break;
            }
        }
        
        int answerA = 0;
        
        if(checkA == false){
            answerA = gcdA;
        }
        
        // B의 최대공약수로 A의 숫자를 하나라도 나눌 수 있는지 확인
        boolean checkB = false;
        
        for(int i = 0; i < n; i++){
            if(arrayA[i] % gcdB == 0){
                checkB = true;
                break;
            }
        }
        
        int answerB = 0;
        
        if(checkB == false){
            answerB = gcdB;
        }
        
        // 두 후보 중 큰 값 선택
        answer = Math.max(answerA, answerB);
        
        return answer;
    }
    
    // 유클리드 호제법으로 최대공약수 구하기
    public static int gcd(int a, int b) {
        if(b == 0) return a;
        return gcd(b, a % b);
    }
}
```
