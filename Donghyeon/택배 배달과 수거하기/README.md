# [level 2] 택배 배달과 수거하기 - 150369

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/150369)

### 성능 요약

메모리: 13 MB, 시간: 145.85 ms

### 구분

코딩테스트 연습 > 2023 KAKAO BLIND RECRUITMENT

### 채점결과

정확성: 100.0<br/>합계: 100.0 / 100.0

### 제출 일자

2026년 09월 09일 22:29:25

### 문제 설명

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/files/production/7ce63a07-3abd-40a1-87cc-c1f664393aa0/%E1%84%8C%E1%85%A2%E1%84%92%E1%85%A1%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20%E1%84%90%E1%85%A2%E1%86%A8%E1%84%87%E1%85%A2%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%8C%E1%85%A1.png" title="" alt="재할용 택배 상자.png"></p>

<p>당신은 일렬로 나열된 <code>n</code>개의 집에 택배를 배달하려 합니다. 배달할 물건은 모두 크기가 같은 재활용 택배 상자에 담아 배달하며, 배달을 다니면서 빈 재활용 택배 상자들을 수거하려 합니다. <br>
배달할 택배들은 모두 재활용 택배 상자에 담겨서 물류창고에 보관되어 있고, <code>i</code>번째 집은 물류창고에서 거리 <code>i</code>만큼 떨어져 있습니다. 또한 <code>i</code>번째 집은 <code>j</code>번째 집과 거리 <code>j - i</code>만큼 떨어져 있습니다. (1 ≤ <code>i</code> ≤ <code>j</code> ≤ <code>n</code>) <br>
트럭에는 재활용 택배 상자를 최대 <code>cap</code>개 실을 수 있습니다. 트럭은 배달할 재활용 택배 상자들을 실어 물류창고에서 출발해 각 집에 배달하면서, 빈 재활용 택배 상자들을 수거해 물류창고에 내립니다. 각 집마다 배달할 재활용 택배 상자의 개수와 수거할 빈 재활용 택배 상자의 개수를 알고 있을 때, 트럭 하나로 모든 배달과 수거를 마치고 물류창고까지 돌아올 수 있는 최소 이동 거리를 구하려 합니다. <strong>각 집에 배달 및 수거할 때, 원하는 개수만큼 택배를 배달 및 수거할 수 있습니다.</strong></p>

<p>다음은 <code>cap</code>=4 일 때, 최소 거리로 이동하면서 5개의 집에 배달 및 수거하는 과정을 나타낸 예시입니다. </p>

<p><strong>배달 및 수거할 재활용 택배 상자 개수</strong> </p>
<table class="table">
        <thead><tr>
<th></th>
<th>집 #1</th>
<th>집 #2</th>
<th>집 #3</th>
<th>집 #4</th>
<th>집 #5</th>
</tr>
</thead>
        <tbody><tr>
<td>배달</td>
<td>1개</td>
<td>0개</td>
<td>3개</td>
<td>1개</td>
<td>2개</td>
</tr>
<tr>
<td>수거</td>
<td>0개</td>
<td>3개</td>
<td>0개</td>
<td>4개</td>
<td>0개</td>
</tr>
</tbody>
      </table>
<p><strong>배달 및 수거 과정</strong></p>
<table class="table">
        <thead><tr>
<th></th>
<th>집 #1</th>
<th>집 #2</th>
<th>집 #3</th>
<th>집 #4</th>
<th>집 #5</th>
<th>설명</th>
</tr>
</thead>
        <tbody><tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/3</td>
<td>3/0</td>
<td>1/4</td>
<td>2/0</td>
<td>물류창고에서 택배 3개를 트럭에 실어 출발합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/3</td>
<td>3/0</td>
<td><strong>0</strong>/4</td>
<td><strong>0</strong>/0</td>
<td>물류창고에서 5번째 집까지 이동하면서(거리 5) 4번째 집에 택배 1개를 배달하고, 5번째 집에 택배 2개를 배달합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/3</td>
<td>3/0</td>
<td>0/<strong>0</strong></td>
<td>0/0</td>
<td>5번째 집에서 물류창고까지 이동하면서(거리 5) 4번째 집에서 빈 택배 상자 4개를 수거한 후, 수거한 빈 택배 상자를 물류창고에 내리고 택배 4개를 트럭에 싣습니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td><strong>0</strong>/0</td>
<td>0/3</td>
<td><strong>0</strong>/0</td>
<td>0/0</td>
<td>0/0</td>
<td>물류창고에서 3번째 집까지 이동하면서(거리 3) 1번째 집에 택배 1개를 배달하고, 3번째 집에 택배 3개를 배달합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>0/0</td>
<td>0/<strong>0</strong></td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>3번째 집에서 물류창고까지 이동하면서(거리 3) 2번째 집에서 빈 택배 상자 3개를 수거한 후, 수거한 빈 택배 상자를 물류창고에 내립니다.</td>
</tr>
</tbody>
      </table>
<p>16(=5+5+3+3)의 거리를 이동하면서 모든 배달 및 수거를 마쳤습니다. 같은 거리로 모든 배달 및 수거를 마치는 다른 방법이 있지만, 이보다 짧은 거리로 모든 배달 및 수거를 마치는 방법은 없습니다. </p>

<p>트럭에 실을 수 있는 재활용 택배 상자의 최대 개수를 나타내는 정수 <code>cap</code>, 배달할 집의 개수를 나타내는 정수 <code>n</code>, 각 집에 배달할 재활용 택배 상자의 개수를 담은 1차원 정수 배열 <code>deliveries</code>와 각 집에서 수거할 빈 재활용 택배 상자의 개수를 담은 1차원 정수 배열 <code>pickups</code>가 매개변수로 주어집니다. 이때, 트럭 하나로 모든 배달과 수거를 마치고 물류창고까지 돌아올 수 있는 최소 이동 거리를 return 하도록 solution 함수를 완성해 주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>1 ≤ <code>cap</code> ≤ 50</li>
<li>1 ≤ <code>n</code> ≤ 100,000</li>
<li><code>deliveries</code>의 길이 = <code>pickups</code>의 길이 = <code>n</code>

<ul>
<li><code>deliveries[i]</code>는 i+1번째 집에 배달할 재활용 택배 상자의 개수를 나타냅니다.</li>
<li><code>pickups[i]</code>는 i+1번째 집에서 수거할 빈 재활용 택배 상자의 개수를 나타냅니다. </li>
<li>0 ≤ <code>deliveries</code>의 원소 ≤ 50</li>
<li>0 ≤ <code>pickups</code>의 원소 ≤ 50</li>
</ul></li>
<li>트럭의 초기 위치는 물류창고입니다.</li>
</ul>

<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>cap</th>
<th>n</th>
<th>deliveries</th>
<th>pickups</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>4</td>
<td>5</td>
<td>[1, 0, 3, 1, 2]</td>
<td>[0, 3, 0, 4, 0]</td>
<td>16</td>
</tr>
<tr>
<td>2</td>
<td>7</td>
<td>[1, 0, 2, 0, 1, 0, 2]</td>
<td>[0, 2, 0, 1, 0, 2, 0]</td>
<td>30</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong></p>

<ul>
<li>문제 예시와 동일합니다.</li>
</ul>

<p><strong>입출력 예 #2</strong></p>

<p><strong>배달 및 수거할 재활용 택배 상자 개수</strong> </p>
<table class="table">
        <thead><tr>
<th></th>
<th>집 #1</th>
<th>집 #2</th>
<th>집 #3</th>
<th>집 #4</th>
<th>집 #5</th>
<th>집 #6</th>
<th>집 #7</th>
</tr>
</thead>
        <tbody><tr>
<td>배달</td>
<td>1개</td>
<td>0개</td>
<td>2개</td>
<td>0개</td>
<td>1개</td>
<td>0개</td>
<td>2개</td>
</tr>
<tr>
<td>수거</td>
<td>0개</td>
<td>2개</td>
<td>0개</td>
<td>1개</td>
<td>0개</td>
<td>2개</td>
<td>0개</td>
</tr>
</tbody>
      </table>
<p><strong>배달 및 수거 과정</strong></p>
<table class="table">
        <thead><tr>
<th></th>
<th>집 #1</th>
<th>집 #2</th>
<th>집 #3</th>
<th>집 #4</th>
<th>집 #5</th>
<th>집 #6</th>
<th>집 #7</th>
<th>설명</th>
</tr>
</thead>
        <tbody><tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/2</td>
<td>2/0</td>
<td>0/1</td>
<td>1/0</td>
<td>0/2</td>
<td>2/0</td>
<td>물류창고에서 택배 2개를 트럭에 실어 출발합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/2</td>
<td>2/0</td>
<td>0/1</td>
<td>1/0</td>
<td>0/2</td>
<td><strong>0</strong>/0</td>
<td>물류창고에서 7번째 집까지 이동하면서(거리 7) 7번째 집에 택배 2개를 배달합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/2</td>
<td>2/0</td>
<td>0/1</td>
<td>1/0</td>
<td>0/<strong>0</strong></td>
<td><strong>0</strong>/0</td>
<td>7번째 집에서 물류창고까지 이동하면서(거리 7) 6번째 집에서 빈 택배 상자 2개를 수거한 후, 수거한 빈 택배 상자를 물류창고에 내리고 택배 2개를 트럭에 싣습니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/2</td>
<td><strong>1</strong>/0</td>
<td>0/1</td>
<td><strong>0</strong>/0</td>
<td>0/0</td>
<td>0/0</td>
<td>물류창고에서 5번째 집까지 이동하면서(거리 5) 3번째 집에 택배 1개를 배달하고, 5번째 집에 택배 1개를 배달합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>1/0</td>
<td>0/<strong>1</strong></td>
<td>1/0</td>
<td>0/<strong>0</strong></td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>5번째 집에서 물류창고까지 이동하면서(거리 5) 4번째 집에서 빈 택배 상자 1개를 수거하고 2번째 집에서 빈 택배 상자 1개를 수거한 후, 수거한 빈 택배 상자를 물류창고에 내리고 택배 2개를 트럭에 싣습니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td><strong>0</strong>/0</td>
<td>0/1</td>
<td><strong>0</strong>/0</td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>물류창고에서 3번째 집까지 이동하면서(거리 3) 1번째 집에 택배 1개를 배달하고, 3번째 집에 택배 1개를 배달합니다.</td>
</tr>
<tr>
<td>남은 배달/수거</td>
<td>0/0</td>
<td>0/<strong>0</strong></td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>0/0</td>
<td>3번째 집에서 물류창고까지 이동하면서(거리 3) 2번째 집에서 빈 택배 상자 1개를 수거한 후, 수거한 빈 택배 상자를 물류창고에 내립니다.</td>
</tr>
</tbody>
      </table>
<p>30(=7+7+5+5+3+3)의 거리를 이동하면서 모든 배달 및 수거를 마쳤습니다. 같은 거리로 모든 배달 및 수거를 마치는 다른 방법이 있지만, 이보다 짧은 거리로 모든 배달 및 수거를 마치는 방법은 없습니다. <br>
따라서, 30을 return 하면 됩니다.</p>

## 풀이와 생각

### 접근 방법

- 그리디(Greedy) + 뒤에서부터 탐색
- `i`번째 집(0-indexed)은 물류창고로부터 거리 `i + 1`만큼 떨어져 있음
- `i = n - 1`부터 `0`까지 역순으로 순회하면서, 그 지점까지 아직 처리하지 못한 배달량(`deliver_cnt`)과 수거량(`pickup_cnt`)을 계속 누적
- 누적된 배달량 또는 수거량이 남아있는 동안, 트럭이 `cap`만큼씩 싣고 현재 위치(`i + 1`)까지 왕복한다고 보고 `(i + 1) * 2`를 정답에 더하며 두 값에서 `cap`씩 차감
- 마지막에 값이 음수가 되어도 반복문을 종료시키는 역할만 하므로 로직에는 영향이 없음

### 핵심 아이디어

- 트럭은 결국 아직 배달/수거가 끝나지 않은 것 중 가장 먼 지점까지 왕복해야만 함 → 그 왕복 한 번에 최대한 많은 물량(`cap`개)을 함께 처리하는 것이 항상 최선
- 배달과 수거를 하나의 시뮬레이션으로 동시에 처리하지 않고 각각 독립적으로 "그 지점까지 왕복이 몇 번 필요한가"만 계산해도 정답이 되는데, 이는 각 집에서 원하는 개수만큼 자유롭게 배달·수거할 수 있다는 문제 제약 덕분
- 즉, 배달에 필요한 왕복 횟수와 수거에 필요한 왕복 횟수를 따로 구해 더해도, 트럭이 실제로 한 번의 이동으로 배달과 수거를 동시에 수행하는 최적해와 동일한 총 이동 거리가 나옴

### 느낀 점

- "가장 먼 곳부터 그리디하게 처리한다"는 전형적인 아이디어를 배달·수거라는 두 축에 동시에 독립적으로 적용해도 답이 맞아떨어지는 것이 흥미로웠음
- 문제의 제약(원하는 만큼 배달·수거 가능)이 얼마나 문제를 단순한 그리디 문제로 환원시켜 주는지 체감함
