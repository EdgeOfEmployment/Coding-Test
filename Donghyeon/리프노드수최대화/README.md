# [level 2] 리프 노드 수 최대화 - 468372

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468372)

### 문제 설명

<p>루트 노드, 리프 노드, 분배 노드로 구성되는 트리가 있습니다. 루트 노드는 자식 노드를 하나만 가지며, 루트 노드가 아닌 노드는 자식 노드를 2개, 3개 또는 0개를 가질 수 있습니다. 자식 노드가 0개인 노드는 리프 노드입니다. 당신은 제한된 조건 하에서 트리를 하나 구성하여 리프 노드를 가능한 한 많이 만들려고 합니다.</p>

<p>트리를 구성하는 규칙은 아래와 같습니다.</p>

<ol>
<li>루트 노드와 리프 노드를 제외한 나머지 노드를 분배 노드라고 하며, 분배 노드는 자식 노드를 2개 또는 3개를 갖습니다.</li>
<li>분배 노드는 최대 <code>dist_limit</code>개 존재할 수 있습니다.</li>
<li>트리에서 같은 깊이에 있는 분배 노드의 자식 노드 수는 모두 같아야 합니다. 노드의 깊이는 루트 노드부터 해당 노드까지의 최단 경로 길이와 같습니다.</li>
<li>모든 리프 노드는 분배도라는 값을 갖습니다. 분배도는 해당 리프 노드의 부모 노드에서 루트 노드까지의 최단 경로 상에 있는 모든 노드의 자식 노드 개수의 곱과 같습니다. 예를 들어 아래 그림과 같이 트리가 주어진 경우, 1번 리프 노드의 분배도는 3 × 1 = 3이며, 2번 리프 노드의 분배도는 2 × 3 × 3 × 1 = 18입니다.</li>
<li>모든 리프 노드의 분배도는 <code>split_limit</code>보다 작거나 같아야 합니다.</li>
</ol>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/9f068cda-13dc-4a0a-982a-92bd69912604/split_score.png" title="" alt="split_score.png"></p>

<p>예를 들어 <code>dist_limit</code>가 3이고 <code>split_limit</code>가 6인 경우, 아래와 같이 트리를 구성할 수 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/0c0df805-d9cc-40f5-b8ab-d9b8d45c16dd/convery_ex0.png" title="" alt="convery_ex0.png"></p>

<p>위와 같이 구성하면 리프 노드의 수는 6이 되며, 분배 노드의 수는 3, 모든 리프 노드의 분배도는 3 × 2 × 1 = 6이므로 조건을 만족합니다. 주어진 조건 하에 리프 노드의 수를 6개보다 많이 만드는 트리 구성은 존재하지 않습니다.</p>

<p>최대 몇 개의 분배 노드를 놓을 수 있는지 나타내는 정수 <code>dist_limit</code>, 분배도의 최댓값을 나타내는 정수 <code>split_limit</code>가 매개변수로 주어집니다. 주어진 조건 하에서 만들 수 있는 트리의 리프 노드 수의 최댓값을 return 하도록 solution 함수를 완성해 주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>0 ≤ <code>dist_limit</code> ≤ 10<sup>9</sup></li>
<li>1 ≤ <code>split_limit</code> ≤ 10<sup>9</sup></li>
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
<td><code>dist_limit ≤ 10</code>, <code>split_limit ≤ 50</code></td>
</tr>
<tr>
<td>#2</td>
<td>70%</td>
<td>추가 제한 사항 없음</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>dist_limit</th>
<th>split_limit</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>3</td>
<td>6</td>
<td>6</td>
</tr>
<tr>
<td>0</td>
<td>10</td>
<td>1</td>
</tr>
<tr>
<td>3</td>
<td>100</td>
<td>7</td>
</tr>
<tr>
<td>5</td>
<td>16</td>
<td>9</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong></p>

<p>문제 예시와 같습니다.</p>

<p><strong>입출력 예 #2</strong></p>

<p>아래 그림과 같이 트리를 구성할 수 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/277c28e5-04d9-4fe5-a77e-1344671b6552/convery_ex2.png" title="" alt="convery_ex2.png"></p>

<p>분배 노드를 놓을 수 없으므로 루트 노드의 자식 노드 1개가 트리에서 유일한 리프 노드입니다. 따라서 1을 return 해야 합니다.</p>

<p><strong>입출력 예 #3</strong></p>

<p>아래 그림과 같이 트리를 구성할 수 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/f7d47634-f0d3-4569-9b13-277530a93814/convey_ex3.png" title="" alt="convey_ex3.png"></p>

<p>다른 트리 구성으로도 리프 노드를 7개 만들 수 있지만 7개보다 많은 리프 노드를 만들 수 있는 트리 구성은 존재하지 않습니다. 따라서 7을 return 해야 합니다.</p>

<p><strong>입출력 예 #4</strong></p>

<p>아래 그림과 같이 트리를 구성할 수 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/d8fdf0ee-3520-46a1-be8d-7e7956d59052/convey_ex4.png" title="" alt="convey_ex4.png"></p>

<p>분배 노드는 최대 5개까지 놓을 수 있지만, 위와 같이 4개까지만 놓는 것도 가능합니다. 9개보다 많은 리프 노드를 만들 수 있는 다른 트리 구성은 존재하지 않습니다. 따라서 9를 return 해야 합니다.</p>

<hr>

## 풀이 방식과 생각의 방향

### 1. 문제를 어떻게 재구성했는가

`dist_limit`, `split_limit`이 각각 10^9까지 커질 수 있어 트리를 직접 만들어보는 시뮬레이션은 불가능. 그래서 트리를 직접 다루는 대신, "루트에서 리프까지 내려가는 경로 위에서 분배 노드들이 갖는 자식 수의 시퀀스"로 문제를 추상화함.

같은 깊이의 분배 노드는 자식 수가 모두 같아야 하므로, 트리 전체는 결국 **깊이별로 "이 층은 자식을 2개 낳는 층인가, 3개 낳는 층인가"를 나열한 시퀀스**로 결정됨. 그리고 분배도(리프까지 곱해지는 값)는 "몇 개 층이 2를 곱하고 몇 개 층이 3을 곱하는가"에만 의존할 뿐, 층의 순서 자체와는 무관하다는 점을 발견 -> 즉 문제를 정수 두 개, `a`(자식 2개짜리 층 수)와 `b`(자식 3개짜리 층 수)의 조합을 찾는 문제로 압축.

- `2^a * 3^b ≤ split_limit` 를 만족해야 하고
- `split_limit ≤ 10^9`이므로 `2^a ≤ 10^9 → a ≤ 30`, `3^b ≤ 10^9 → b ≤ 20` 정도로 탐색 범위가 줄어, `a, b`에 대한 완전 탐색(약 30×20가지)이 충분히 가능해짐.

이렇게 "트리의 자유도가 사실은 두 개의 작은 정수뿐"이라는 것을 알아낸 게 이 문제의 핵심.

### 2. (a, b)가 정해졌을 때 리프 수를 최대화하는 방법

- 층 수 `L = a + b`개의 분배 노드 층이 필요하므로, 최소 `dist_limit ≥ L`이어야 이 조합을 쓸 수 있음.
- 각 층에는 분배도를 만들어내기 위해 최소 1개의 분배 노드가 있어야 하므로, 모든 층에 우선 1개씩(`X[i] = 1`) 배정하고 남는 예산 `rem = dist_limit - L`을 어떻게 분배할지를 생각.
- 한 층의 분배 노드 수는 물리적으로 **바로 위 층의 분배 노드 수 × 그 층의 자식 수**를 넘을 수 없음(부모 없이 자식만 존재할 수 없으므로). 그래서 얕은 층부터 깊은 층 순서로(top-down) 그 시점까지의 용량 한도 안에서 남은 예산을 최대한 채워나가는 방식으로 진행. 순서를 거꾸로 하면 상위 층 용량이 아직 정해지지 않은 채로 하위 층을 채우게 되어 계산 자체가 성립하지 않음.
- 자식 수 배열(`C`)의 순서는 **2를 얕은 층에, 3을 깊은 층에** 두도록 고정. 리프 수는 `Σ X[i] * (C[i]-1)`로 계산되므로, 배율이 큰 자식 수(3)일수록 노드 하나당 얻는 리프 수 이득이 큼. top-down 채우기 방식에서는 남는 예산이 뒤쪽(깊은) 층부터 먼저 한도를 다 채우고 넘어가는 구조이기 때문에, 이득이 큰 배율을 깊은 층에 두어야 같은 예산으로 더 많은 리프를 만들 수 있음. (실제로 손으로 `a=1, b=1, dist_limit=3`을 넣어 비교해보면 `2 → 3` 순서가 `3 → 2` 순서보다 리프 수가 더 많이 나오는걸 볼 수 있음.)

### 3. 정리

결국 이 문제는 "트리를 직접 구성"하는 문제가 아니라 "트리 구성을 결정하는 두 개의 정수(`a`, `b`)를 완전 탐색하고, 정해진 정수 안에서 예산을 채우는 방법을 그리디로 정하는" 문제로 바꿔서 풀이함. 탐색 범위를 줄이는 관찰(분배도는 순서 무관, 자유도는 두 정수뿐)과, 그 안에서 남는 예산을 어느 층에 먼저/얼마나 배정할지에 대한 그리디 문제로의 판단이 핵심
