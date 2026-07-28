# [level 1] 노란불 신호등 - 468371

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468371)

### 성능 요약

메모리: 51.9 MB, 시간: 14.44 ms

### 구분

코딩테스트 연습 > 2025 카카오 하반기 1차

### 채점결과

정확성: 100.0<br/>합계: 100.0 / 100.0

### 제출 일자

2026년 07월 18일 23:07:04

### 문제 설명

<p>어떤 도로에 차량 신호등이 <code>n</code>개 있습니다. 모든 신호등은 항상 초록불 → 노란불 → 빨간불 순서로 반복되며, 각 신호의 지속 시간은 신호등마다 다릅니다. 시간은 1초부터 시작하며, 각 신호등은 처음에는 초록불 상태로 시작합니다.</p>

<p>이 도로에서는 가끔 정전이 일어나는데, 모든 신호등이 모두 노란불이 되면 정전이 발생한다는 사실이 밝혀졌습니다.</p>

<p>예를 들어 신호등이 2개이고, 각 신호등의 주기가 다음과 같다고 가정해 보겠습니다.</p>
<table class="table">
        <thead><tr>
<th>신호등</th>
<th>초록불</th>
<th>노란불</th>
<th>빨간불</th>
</tr>
</thead>
        <tbody><tr>
<td>1번</td>
<td>2초</td>
<td>1초</td>
<td>2초</td>
</tr>
<tr>
<td>2번</td>
<td>5초</td>
<td>1초</td>
<td>1초</td>
</tr>
</tbody>
      </table>
<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/69bc0131-0c4f-4609-9c47-4b649c40f860/%E1%84%89%E1%85%B5%E1%86%AB%E1%84%92%E1%85%A9%E1%84%83%E1%85%B3%E1%86%BC-1.drawio.png" title="" alt="신호등-1.drawio.png"></p>

<p>위 그림과 같이 13초에 처음으로 두 신호등이 모두 노란불이 됩니다. </p>

<p>신호등 <code>n</code>개의 신호 주기를 담은 2차원 정수 배열 <code>signals</code>가 매개변수로 주어집니다. 모든 신호등이 노란불이 되는 가장 빠른 시각(초)을 return 하도록 solution 함수를 완성해 주세요. 만약 모든 신호등이 노란불이 되는 경우가 존재하지 않는다면 -1을 return 해주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>2 ≤ <code>signals</code>의 길이 = <code>n</code> ≤ 5

<ul>
<li><code>signals</code>의 원소는 <code>[G, Y, R]</code> 형태의 길이가 3인 정수 배열입니다. 순서대로 초록불, 노란불, 빨간불의 지속 시간을 의미합니다.</li>
<li>1 ≤ <code>G</code>, <code>Y</code>, <code>R</code> ≤ 18</li>
<li>3 ≤ <code>G + Y + R</code> ≤ 20</li>
</ul></li>
</ul>

<hr>

<h5>테스트 케이스 구성 안내</h5>

<p>아래는 테스트 케이스 구성을 나타냅니다. 각 그룹은 하나 이상의 하위 그룹으로 이루어져 있으며, 하위 그룹의 모든 테스트 케이스를 통과하면 해당 그룹에 할당된 점수를 획득할 수 있습니다.</p>
<table class="table">
        <thead><tr>
<th>그룹</th>
<th>총점</th>
<th>추가 제한 사항</th>
</tr>
</thead>
        <tbody><tr>
<td>#1</td>
<td>30%</td>
<td>신호등이 모두 노란불이 되는 시각이 20 이하인 정답이 존재합니다.</td>
</tr>
<tr>
<td>#2</td>
<td>30%</td>
<td>신호등이 모두 노란불이 되는 경우가 존재합니다.</td>
</tr>
<tr>
<td>#3</td>
<td>40%</td>
<td>추가 제한 사항 없음</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>signals</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>[[2, 1, 2], [5, 1, 1]]</td>
<td>13</td>
</tr>
<tr>
<td>[[2, 3, 2], [3, 1, 3], [2, 1, 1]]</td>
<td>11</td>
</tr>
<tr>
<td>[[3, 3, 3], [5, 4, 2], [2, 1, 2]]</td>
<td>193</td>
</tr>
<tr>
<td>[[1, 1, 4], [2, 1, 3], [3, 1, 2], [4, 1, 1]]</td>
<td>-1</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong></p>

<p>문제 설명의 예시와 같습니다.</p>

<p><strong>입출력 예 #2</strong></p>
<table class="table">
        <thead><tr>
<th>신호등</th>
<th>초록불</th>
<th>노란불</th>
<th>빨간불</th>
</tr>
</thead>
        <tbody><tr>
<td>1번</td>
<td>2초</td>
<td>3초</td>
<td>2초</td>
</tr>
<tr>
<td>2번</td>
<td>3초</td>
<td>1초</td>
<td>3초</td>
</tr>
<tr>
<td>3번</td>
<td>2초</td>
<td>1초</td>
<td>1초</td>
</tr>
</tbody>
      </table>
<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/3011656a-b47d-40ea-baf4-15413beb7d44/%E1%84%89%E1%85%B5%E1%86%AB%E1%84%92%E1%85%A9%E1%84%83%E1%85%B3%E1%86%BC-2.drawio.png" title="" alt="신호등-2.drawio.png"></p>

<p>11초에 3개의 신호등이 모두 노란불이 됩니다.</p>

<p><strong>입출력 예 #3</strong></p>
<table class="table">
        <thead><tr>
<th>신호등</th>
<th>초록불</th>
<th>노란불</th>
<th>빨간불</th>
</tr>
</thead>
        <tbody><tr>
<td>1번</td>
<td>3초</td>
<td>3초</td>
<td>3초</td>
</tr>
<tr>
<td>2번</td>
<td>5초</td>
<td>4초</td>
<td>2초</td>
</tr>
<tr>
<td>3번</td>
<td>2초</td>
<td>1초</td>
<td>2초</td>
</tr>
</tbody>
      </table>
<p>193초에 3개의 신호등이 모두 노란불이 됩니다.</p>

<p><strong>입출력 예 #4</strong></p>

<p>모든 신호등이 노란불이 되는 경우가 존재하지 않으므로 -1을 return 해야 합니다.</p>

## 풀이

제한사항을 보면 n ≤ 5, G+Y+R ≤ 20으로 값의 범위가 매우 작다. 이런 경우 굳이 복잡한 알고리즘을 고민하기보다, 가능한 시간을 처음부터 하나씩 순회하며 조건을 만족하는 순간을 찾는 완전탐색으로 접근하는
것이 가장 빠르고 확실하다.

문제는 "언제까지 탐색해야 멈출 수 있는가"였다. 각 신호등은 (G+Y+R) 주기로 계속 반복되므로, 전체 신호등의 상태 조합은 모든 신호등 주기의 최소공배수(LCM) 시점마다 정확히 반복된다. 즉 1초부터 LCM까지
탐색해서 조건을 만족하는 시각이 없다면, 그 이후에도 영원히 나타나지 않으므로 -1을 반환하면 된다는 결론을 내렸다.

이 아이디어를 정리하여 다음과 같은 순서로 풀이

1. 최대공약수(GCD) → 최소공배수(LCM) → 배열 전체의 LCM을 구하는 유틸 함수를 차례로 만든다.
2. 각 신호등의 주기 (G+Y+R)를 구해 배열로 만들고, 이 배열의 LCM을 tql탐색 최대 시간(maxTime)으로 삼는다.
3. 1초부터 maxTime까지 time을 1씩 늘려가며, 모든 신호등이 노란불인지 확인한다.
    - 시간이 1초부터 시작하므로 (time - 1) % cycle로 "신호가 시작된 이후 경과 시간"을 구한다.
    - 이 값이 G 이상, G+Y 미만이면 해당 신호등은 노란불 상태이다.
    - signals.every()로 모든 신호등이 동시에 이 조건을 만족하는지 검사한다.
4. 조건을 만족하는 첫 시각을 찾으면 즉시 반환하고, maxTime까지 못 찾으면 -1을 반환한다.

생각의 흐름을 요약하면: "제한이 작다 → 완전탐색이 가능하겠다 → 그런데 무한히 탐색할 수는 없으니 탐색 종료 시점을 어떻게 정하지? → 신호등의 상태는 주기적으로 반복되므로 전체 주기의 최소공
배수를 탐색 상한으로 잡으면 된다"는 순서로 접근함
