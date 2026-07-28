# [level 2] 선인장 숨기기 - 468379

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468379)

### 성능 요약

메모리: 279 MB, 시간: 254.76 ms

### 구분

코딩테스트 연습 > 2025 카카오 하반기 2차

### 채점결과

정확성: 100.0<br/>합계: 100.0 / 100.0

### 제출 일자

2026년 07월 25일 10:21:02

### 문제 설명

<p><code>m</code>개의 행과 <code>n</code>개의 열로 구성된 격자가 주어지며, 이는 사막 지도를 나타냅니다. 사막 지도의 가장 왼쪽 위칸 좌표는 <code>(0, 0)</code>, 오른쪽 아래칸 좌표는 <code>(m-1, n-1)</code>입니다. 이 사막 어딘가에 가로 <code>w</code>, 세로 <code>h</code> 크기의 선인장 구역을 조성하려 합니다. 선인장 구역은 격자 축에 맞춘 연속된 <code>w</code> × <code>h</code> 크기의 부분 격자이며, 회전할 수 없습니다.</p>

<p>비구름은 미리 정해진 순서대로 격자의 여러 칸에 비를 뿌립니다. 이때 빗방울이 처음으로 선인장 구역에 포함된 칸에 떨어졌을 때, 그 시점을 선인장이 처음으로 비를 맞는 순간으로 기록합니다. 당신은 선인장이 가능한 한 늦게 비를 맞도록, 선인장 구역의 위치를 정하려고 합니다.</p>

<ul>
<li>선인장이 비를 맞지 않도록 선인장 구역의 위치를 정할 수 있다면 해당 위치가 가장 우선됩니다.</li>
<li>가능한 늦게 비를 맞는 선인장 구역 후보가 여러 개라면 그중 가장 위쪽 행, 그래도 여러 개면 가장 왼쪽 열에 위치한 구역을 선택합니다.</li>
</ul>

<p>격자의 세로 길이와 가로 길이를 나타내는 정수 <code>m</code>, <code>n</code>, 선인장 구역의 세로 길이와 가로 길이를 나타내는 정수 <code>h</code>, <code>w</code>, 그리고 빗방울이 떨어지는 순서대로 칸의 좌표를 담은 2차원 정수 배열 <code>drops</code>가 매개변수로 주어집니다. 주어진 조건을 만족하는 선인장 구역에 포함된 가장 왼쪽 위칸의 좌표를 정수 배열로 return 하도록 solution 함수를 완성해 주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>1 ≤ <code>m</code>, <code>n</code> ≤ 500,000</li>
<li>1 ≤ <code>m</code> × <code>n</code> ≤ 500,000</li>
<li>1 ≤ <code>h</code> ≤ <code>m</code></li>
<li>1 ≤ <code>w</code> ≤ <code>n</code></li>
<li>1 ≤ <code>drops</code>의 길이 ≤ <code>m</code> × <code>n</code>

<ul>
<li><code>drops[i]</code>는 [<code>r</code>, <code>c</code>] 형태입니다.</li>
<li><code>drops[i]</code>는 <code>i + 1</code>번째로 떨어진 빗방울의 좌표를 의미합니다.</li>
<li>0 ≤ <code>r</code> &lt; <code>m</code></li>
<li>0 ≤ <code>c</code> &lt; <code>n</code></li>
<li><code>drops</code>의 모든 원소는 서로 다른 칸을 나타냅니다.</li>
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
<td><code>m ≤ 50</code>, <code>n ≤ 50</code></td>
</tr>
<tr>
<td>#2</td>
<td>70%</td>
<td>추가 제한 없음</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>m</th>
<th>n</th>
<th>h</th>
<th>w</th>
<th>drops</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>4</td>
<td>5</td>
<td>2</td>
<td>2</td>
<td>[[0, 0], [3, 1], [1, 3], [2, 4], [1, 1], [2, 2], [2, 3], [0, 4]]</td>
<td>[2, 2]</td>
</tr>
<tr>
<td>3</td>
<td>3</td>
<td>1</td>
<td>1</td>
<td>[[0, 0], [0, 1], [0, 2], [1, 0]]</td>
<td>[1, 1]</td>
</tr>
<tr>
<td>4</td>
<td>6</td>
<td>3</td>
<td>4</td>
<td>[[1, 2]]</td>
<td>[0, 0]</td>
</tr>
<tr>
<td>4</td>
<td>6</td>
<td>1</td>
<td>2</td>
<td>[[0, 1], [0, 3], [0, 5], [1, 1], [1, 3], [1, 5], [2, 1], [2, 3], [2, 5], [3, 1], [3, 3], [3, 5]]</td>
<td>[3, 4]</td>
</tr>
<tr>
<td>2</td>
<td>2</td>
<td>2</td>
<td>2</td>
<td>[[0, 0], [0, 1], [1, 1], [1, 0]]</td>
<td>[0, 0]</td>
</tr>
<tr>
<td>4</td>
<td>4</td>
<td>3</td>
<td>1</td>
<td>[[2, 0], [1, 3], [3, 2], [0, 1]]</td>
<td>[0, 2]</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong></p>

<p>아래 그림은 <code>4</code> × <code>5</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/ee647a0c-6d56-433f-88e5-a4de759e437e/trs_ex1_1.png" title="" alt="trs_ex1_1.png"></p>

<p>노란색으로 표시된 구역을 선인장 구역으로 두면, 6번째로 비가 떨어질 때 선인장이 처음 비를 맞게 되며, 이보다 더 늦게 젖도록 하는 배치는 존재하지 않습니다. 따라서 노란색 구역의 가장 왼쪽 위 좌표인 <code>[2, 2]</code>를 return 해야 합니다.</p>

<p><strong>입출력 예 #2</strong></p>

<p>아래 그림은 <code>3</code> × <code>3</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/617cd348-ee36-437f-b6dc-c86e86135277/trs_ex2.png" title="" alt="trs_ex2.png"></p>

<p>모든 빗방울이 떨어질 때까지 좌표가 (1, 1), (1, 2), (2, 0), (2, 1), (2, 2)인 칸은 젖지 않습니다. 이 5칸 어디에나 <code>1</code> × <code>1</code> 크기의 선인장 구역을 놓을 수 있지만, 그중 가장 위쪽 행, 그리고 가장 왼쪽 열에 해당하는 좌표는 (1, 1)입니다. 따라서 <code>[1, 1]</code>을 return 해야 합니다.</p>

<p><strong>입출력 예 #3</strong></p>

<p>아래 그림은 <code>4</code> × <code>6</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/34650a98-15ea-46a5-a24b-9c6b65c41683/trs_ex3.png" title="" alt="trs_ex3.png"></p>

<p>선인장 구역을 어디에 배치하더라도 첫 번째 빗방울만에 구역이 젖습니다. 따라서 가장 위쪽 행, 그중에서도 가장 왼쪽 열에 위치하는 좌표인 <code>[0, 0]</code>을 return 해야 합니다.</p>

<p><strong>입출력 예 #4</strong></p>

<p>아래 그림은 <code>4</code> × <code>6</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/2b2d9522-116e-44d4-9e69-5204ed83d73b/trs_ex4.png" title="" alt="trs_ex4.png"></p>

<p>따라서 <code>[3, 4]</code>를 return 해야 합니다.</p>

<p><strong>입출력 예 #5</strong></p>

<p>아래 그림은 <code>2</code> × <code>2</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/f7d5b500-dc06-43ec-999b-edfc6dfdd815/trs_ex5.png" title="" alt="trs_ex5.png"></p>

<p>따라서 <code>[0, 0]</code>을 return 해야 합니다.</p>

<p><strong>입출력 예 #6</strong></p>

<p>아래 그림은 <code>4</code> × <code>4</code> 크기의 지도 격자입니다. 각 칸의 큰 숫자는 빗방울이 떨어지는 순서를, 작은 숫자는 좌표를 나타냅니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/ac8d15f6-05f6-4323-9daa-3fd8951e6ae2/trs_ex6.png" title="" alt="trs_ex6.png"></p>

<p>따라서 <code>[0, 2]</code>를 return 해야 합니다.</p>

> 출처: 프로그래머스 코딩 테스트 연습, https://school.programmers.co.kr/learn/challenges

## 풀이

### 문제 유형 파악

`m × n` 격자에서 `h × w` 크기의 부분 격자를 골라, 그 안에 포함된 가장 빠른 빗방울 시각을 최대한 늦추는 문제다. 즉 각 `(i, j)`를 좌상단으로 하는 `h × w` 부분 격자마다 "그 영역 안에서 가장 먼저 비를 맞는 시점"(영역 내 최솟값, 비가 안 오면 무한대)을 구하고, 그 값이 가장 큰(=가장 늦게 젖는) 좌상단 좌표를 찾으면 된다. 값이 같은 후보가 여럿이면 위쪽 행, 그다음 왼쪽 열이 우선이다.

### 처음 시도와 실패 이유 (주석 처리된 코드)

처음에는 가능한 모든 좌상단 좌표 `(i, j)`에 대해 내부 `h × w` 칸을 전부 순회하며 최솟값을 직접 구하는 브루트 포스로 접근했다.

```js
for (let p = i; p < i + h; p++) {
    for (let q = j; q < j + w; q++) {
        if (graph[p][q] != 0 && min > graph[p][q]) min = graph[p][q];
    }
}
```

이 방식은 좌상단 후보 수 `O(m × n)`에 내부 탐색 `O(h × w)`가 곱해져 최악의 경우 `O(m × n × h × w)`가 된다. 제한사항의 테스트 케이스 그룹 #1(`m, n ≤ 50`)에서는 통과하지만, `m × n ≤ 500,000`까지 커지고 `h`, `w`도 함께 커질 수 있는 전체 케이스에서는 시간 초과가 날 수밖에 없는 구조였다.

### 최종 접근: 2차원 슬라이딩 윈도우 최솟값 (모노토닉 큐)

핵심 아이디어는 "`h × w` 영역의 최솟값"을 후보 좌표마다 매번 새로 구하지 않고, **가로 방향 윈도우 최솟값을 먼저 구해둔 뒤, 그 결과에 세로 방향 윈도우 최솟값을 한 번 더 적용**하는 2단계 분리다. 각 단계는 모노토닉 큐(단조 큐)를 이용한 슬라이딩 윈도우 최솟값 기법으로 처리하면, 윈도우 크기와 무관하게 전체를 `O(격자 크기)`에 끝낼 수 있다.

1. **격자 초기화**: 비가 오지 않는 칸은 어떤 실제 낙하 순번보다도 큰 `Infinity`로 채워, "비를 맞지 않는 영역"이 항상 최댓값 후보로 자연스럽게 선택되도록 한다.
2. **가로 방향 윈도우 최솟값(`rowMin`)**: 각 행마다 길이 `w`짜리 윈도우를 오른쪽으로 밀면서, 윈도우 안의 최솟값을 모노토닉 큐로 유지한다.
    - 큐에는 "값이 오름차순이 되도록" 열 인덱스만 저장한다. 새로 들어오는 값보다 크거나 같은 값들은 답이 될 수 없으므로 뒤에서부터 제거한 뒤 새 인덱스를 넣는다. 그러면 큐의 맨 앞(`head`)이 항상 현재 윈도우의 최솟값이 된다.
    - 윈도우 범위(`j - w`)를 벗어난 인덱스는 앞에서 제거한다. 이때 `shift()`로 배열 앞을 실제로 지우면 `O(n)`이 걸리므로, `head`라는 포인터만 앞으로 옮겨 논리적으로만 건너뛰는 방식으로 매 연산을 상각 `O(1)`로 유지했다.
3. **세로 방향 윈도우 최솟값(`rectMin`)**: 2에서 구한 `rowMin`을 입력으로 놓고, 이번엔 각 열마다 길이 `h`짜리 윈도우를 아래로 밀면서 같은 방식(모노토닉 큐)으로 최솟값을 구한다. `rowMin[i][j]`가 "`i`행에서 가로로 `w`칸의 최솟값"이었으므로, 그 위에 세로로 `h`칸의 최솟값을 한 번 더 씌운 `rectMin[i][j]`는 결국 `(i, j)`를 좌상단으로 하는 `h × w` 영역 전체의 최솟값이 된다.
4. **최적 좌표 탐색**: `rectMin`을 행 우선(위→아래), 열 우선(왼쪽→오른쪽) 순서로 순회하면서, **이전 최댓값보다 "엄격히 클 때만"(`>`)** 갱신한다. `>=`가 아니라 `>`를 쓰기 때문에, 값이 같은 후보를 나중에 만나도 갱신되지 않고 먼저 만난(=더 위쪽, 그다음 더 왼쪽) 좌표가 그대로 유지되어 tie-break 조건이 자연스럽게 만족된다.

이렇게 하면 가로/세로 각 단계가 전체 칸 수에 비례하는 `O(m × n)`으로 끝나서, `h`, `w`가 얼마나 크든 상관없이 항상 빠르게 답을 구할 수 있다.
