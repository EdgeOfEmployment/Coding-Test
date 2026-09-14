이 코드는 프로그래머스 「숫자 카드 나누기」 문제를 약수를 직접 탐색하는 방식으로 해결한 코드입니다.

1. 코드의 핵심 동작

다음 두 조건을 각각 검사합니다.

arrayA의 모든 원소를 나누면서 arrayB의 모든 원소는 나누지 못하는 수
arrayB의 모든 원소를 나누면서 arrayA의 모든 원소는 나누지 못하는 수

각 조건을 만족하는 가장 큰 수를 aNum, bNum에 저장한 뒤 더 큰 값을 반환합니다.

answer = Math.max(aNum, bNum);
2. A 배열을 기준으로 한 처리 과정
① A 배열의 최솟값 탐색
int minA = Integer.MAX_VALUE;

for (int i = 0; i < arrayA.length; i++) {
    minA = Math.min(minA, arrayA[i]);
}

arrayA의 모든 원소를 나눌 수 있는 수는 반드시 배열의 최솟값도 나눌 수 있어야 합니다. 따라서 최솟값의 약수만 후보로 검사합니다.

② 최솟값의 모든 약수 구하기
ArrayList<Integer> arrA1 = new ArrayList<>();

for (int i = 1; i < minA + 1; i++) {
    if (minA % i == 0) {
        arrA1.add(i);
    }
}

1부터 minA까지 확인하면서 minA의 약수를 arrA1에 저장합니다.

반복문이 작은 수부터 진행되기 때문에 arrA1은 자동으로 오름차순이 됩니다. 따라서 주석의 “정렬”과 달리 별도의 정렬 코드는 필요하지 않습니다.

③ A의 모든 원소를 나누는 약수만 선별
ArrayList<Integer> arrA2 = new ArrayList<>();

for (int i = 0; i < arrA1.size(); i++) {
    int cur = arrA1.get(i);
    int flag = 0;

    for (int j = 0; j < arrayA.length; j++) {
        if (arrayA[j] % cur != 0) {
            flag = 1;
            break;
        }
    }

    if (flag == 0) {
        arrA2.add(cur);
    }
}

최솟값의 약수 중에서 arrayA의 모든 원소를 나눌 수 있는 약수만 arrA2에 저장합니다.

즉, arrA2에는 사실상 arrayA의 최대공약수에 포함되는 약수들이 들어갑니다.

④ B의 어떤 원소도 나누지 못하는 가장 큰 수 탐색
int aNum = 0;

for (int i = arrA2.size() - 1; i >= 0; i--) {
    int cur = arrA2.get(i);
    int flag = 0;

    for (int j = 0; j < arrayB.length; j++) {
        if (arrayB[j] % cur == 0) {
            flag = 1;
            break;
        }
    }

    if (flag == 0) {
        aNum = cur;
        break;
    }
}

arrA2의 뒤에서부터 검사하므로 큰 약수부터 확인합니다.

arrayB의 원소 중 하나라도 cur로 나누어지면 사용할 수 없습니다. 모든 원소가 나누어지지 않는 첫 번째 수를 aNum으로 저장합니다.

B 배열에 대해서도 같은 과정을 반대로 수행합니다.

3. 예시
arrayA = [10, 17]
arrayB = [5, 20]

A의 최솟값은 10이고 약수는 다음과 같습니다.

1, 2, 5, 10

이 중 10과 17을 모두 나누는 수는 1뿐입니다. 하지만 1은 B의 모든 숫자도 나누므로 조건을 만족하지 않습니다.

따라서 다음과 같습니다.

aNum = 0

B의 모든 숫자를 나누는 최대공약수는 5입니다. 5는 A의 10을 나누기 때문에 조건을 만족하지 않습니다.

bNum = 0

최종 결과는 0입니다.

4. 코드에서 잘한 점
문제의 두 조건을 정확하게 분리했습니다.
최솟값의 약수만 후보로 사용하여 모든 정수를 검사하는 것보다 범위를 줄였습니다.
큰 약수부터 검사해 조건을 만족하는 순간 탐색을 종료했습니다.
조건을 만족하는 수가 없으면 초기값인 0을 반환하도록 처리했습니다.
5. 개선할 점
주석이 일부 부정확함
// A의 최대공약수를 B의 원소들로 모두 나눌수 없는지 확인

실제로 확인하는 것은 “A의 최대공약수를 B의 원소들로 나누는 것”이 아니라 다음 조건입니다.

A의 모든 원소를 나눌 수 있고,
B의 어떤 원소도 나눌 수 없는 수

따라서 이렇게 적는 것이 정확합니다.

// A의 모든 원소를 나누면서 B의 어떤 원소도 나누지 못하는 수 탐색

두 번째 부분의 주석도 A가 아니라 B여야 합니다.

// 기존
// A의 모든 원소 나누어지는지 확인

// 수정
// B의 모든 원소가 나누어지는지 확인
flag 대신 boolean 사용 가능

현재는 성공과 실패를 0, 1로 표현합니다.

int flag = 0;

의미를 명확하게 하려면 다음처럼 작성할 수 있습니다.

boolean valid = true;

for (int value : arrayA) {
    if (value % cur != 0) {
        valid = false;
        break;
    }
}
중복 코드가 많음

A와 B의 역할만 바뀌는데 같은 로직이 두 번 작성되어 있습니다. 별도의 메서드로 분리하면 코드가 짧고 관리하기 쉬워집니다.

최대공약수를 이용하면 훨씬 간단함

A의 모든 원소를 나누는 가장 큰 수는 A의 최대공약수입니다.

예를 들어 A의 최대공약수가 12라면, 가능한 수는 모두 12의 약수입니다. 그런데 12가 B의 어떤 원소도 나누지 못한다면 가장 큰 정답은 바로 12입니다.

반대로 12가 B의 어떤 원소 하나를 나눈다면 12의 약수도 해당 원소를 나누기 때문에 더 작은 약수를 찾을 필요가 없습니다.

따라서 각 배열의 최대공약수만 검사하면 됩니다.

6. 개선 코드
class Solution {

    public int solution(int[] arrayA, int[] arrayB) {
        int gcdA = arrayA[0];
        int gcdB = arrayB[0];

        for (int value : arrayA) {
            gcdA = gcd(gcdA, value);
        }

        for (int value : arrayB) {
            gcdB = gcd(gcdB, value);
        }

        int aNum = canDivideNone(gcdA, arrayB) ? gcdA : 0;
        int bNum = canDivideNone(gcdB, arrayA) ? gcdB : 0;

        return Math.max(aNum, bNum);
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }

        return a;
    }

    private boolean canDivideNone(int divisor, int[] array) {
        for (int value : array) {
            if (value % divisor == 0) {
                return false;
            }
        }

        return true;
    }
}
7. 시간 복잡도

기존 코드는 최솟값을 M, 배열 길이를 N이라고 하면 약수를 찾는 과정에서 최대 M번 반복합니다.

O(M + M의 약수 개수 × N)

최악의 경우 최솟값의 크기에 영향을 크게 받습니다.

최대공약수를 이용한 개선 코드는 유클리드 호제법을 사용하므로 대략 다음과 같습니다.

O(N log M)

결론적으로 기존 코드도 정답을 정확하게 구하지만, 이 문제는 각 배열의 최대공약수만 구해 상대 배열을 나누는지 검사하는 방식으로 훨씬 간결하고 효율적으로 해결할 수 있습니다.
