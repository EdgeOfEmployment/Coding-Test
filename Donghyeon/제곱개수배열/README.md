# [level 3] 제곱 개수 배열 - 468380

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468380)

### 성능 요약

메모리: 67.7 MB, 시간: 44.32 ms

### 구분

코딩테스트 연습 > 2025 카카오 하반기 2차

### 채점결과

정확성: 100.0<br/>합계: 100.0 / 100.0

### 제출 일자

2026년 07월 28일 17:24:08

### 문제 설명

<p>1이상의 정수로 이루어진 길이가 <code>N</code>인 1차원 정수 배열 <code>arr</code>가 주어집니다. 다음 규칙에 따라 배열 <code>brr</code>를 만듭니다.</p>

<ul>
<li>배열 <code>arr</code>의 인덱스 순서대로 <code>arr[i]</code>를 배열 <code>brr</code>에 연속으로 <code>arr[i]</code>개씩 추가합니다.</li>
</ul>

<p>예를 들어, <code>arr</code>가 [2, 1, 5]이면 <code>brr</code>는 [2, 2, 1, 5, 5, 5, 5, 5]입니다.</p>

<p><code>brr</code>의 부분 배열<sup id="fnref1"><a href="#fn1">1</a></sup> 구간의 양 끝을 나타내는 <code>l</code>, <code>r</code>이 주어질 때 아래 2가지를 구하려고 합니다.</p>

<ol>
<li><code>K</code> = <code>brr</code>의 <code>l</code>번째 원소부터 <code>r</code>번째 원소까지의 합 = <code>brr[l-1] + brr[l] + brr[l+1] + ... + brr[r-2] + brr[r-1]</code></li>
<li><code>C</code> = 길이가 <code>r - l + 1</code>인 <code>brr</code>의 부분 배열 중 합이 <code>K</code>인 부분 배열의 개수</li>
</ol>

<p>1차원 정수 배열 <code>arr</code>와 구간의 양 끝을 나타내는 정수 <code>l</code>, <code>r</code>이 매개변수로 주어집니다. 이때, 1차원 정수 배열 <code>[K, C]</code>를 return 하도록 solution 함수를 완성해 주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>1 ≤ <code>arr</code>의 길이 = <code>N</code> ≤ 100,000

<ul>
<li>1 ≤ <code>arr[i]</code> ≤ 100,000</li>
<li><code>brr</code>의 모든 원소의 합 ≤ 10<sup>15</sup></li>
</ul></li>
<li>1 ≤ <code>l</code> ≤ <code>r</code> ≤ <code>arr</code>의 모든 원소의 합

<ul>
<li><code>l</code>번째 원소부터 <code>r</code>번째 원소까지의 합을 구해야 합니다.</li>
</ul></li>
</ul>

<hr>

<h5>테스트 케이스 구성 안내</h5>

<p>아래는 테스트 케이스 구성을 나타냅니다. 각 그룹은 하나 이상의 하위 그룹으로 이루어져 있으며, 하위 그룹의 모든 테스트 케이스를 통과하면 해당 그룹에 할당된 점수를 획득할 수 있습니다.</p>
<table class="table">
        <thead><tr>
<th>그룹</th>
<th>총점</th>
<th>테스트 케이스 그룹 설명</th>
</tr>
</thead>
        <tbody><tr>
<td>#1</td>
<td>5%</td>
<td><code>l = r</code></td>
</tr>
<tr>
<td>#2</td>
<td>15%</td>
<td><code>N</code> ≤ 100, <code>arr[i]</code> ≤ 10</td>
</tr>
<tr>
<td>#3</td>
<td>35%</td>
<td>정답이 <code>C = 1</code>인 테스트 케이스만 주어집니다.</td>
</tr>
<tr>
<td>#4</td>
<td>45%</td>
<td>추가 제한 사항 없음</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>arr</th>
<th>l</th>
<th>r</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>[3, 2, 3, 1, 1]</td>
<td>5</td>
<td>7</td>
<td>[8, 2]</td>
</tr>
<tr>
<td>[2, 2, 2]</td>
<td>2</td>
<td>2</td>
<td>[2, 6]</td>
</tr>
<tr>
<td>[8, 8, 6, 5, 2, 9, 8, 4, 3, 10]</td>
<td>25</td>
<td>27</td>
<td>[15, 3]</td>
</tr>
<tr>
<td>[70195, 25471, 7389, 58187, 18454, 90532, 97667, 17148, 91636, 2810]</td>
<td>126058</td>
<td>462933</td>
<td>[27554327568, 1]</td>
</tr>
<tr>
<td>[16952, 70276, 16771, 37992, 87549, 54906, 36718, 20478, 57088, 27916, 51509, 83422, 51707, 18807, 80859, 2673, 37734, 93380]</td>
<td>149845</td>
<td>228204</td>
<td>[6860339640, 9190]</td>
</tr>
<tr>
<td>[49134, 86806, 94548, 88849, 95022, 28334, 16637, 79487, 23773, 7314, 47370, 50269, 36573, 9415, 44674, 28096]</td>
<td>61242</td>
<td>88535</td>
<td>[2369282964, 59513]</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong>  </p>

<p>brr는 [3, 3, 3, 2, <code>2, 3, 3</code>, 3, 1, 1]입니다. 5~7번째 원소로 이루어진 부분 배열의 합은 8(=<code>K</code>)입니다.<br>
[3, <code>3, 3, 2</code>, 2, 3, 3, 3, 1, 1] 또한 부분 배열의 합이 8이며 길이가 3입니다. <br>
위 두 경우 외에는 합이 8이고 길이가 3인 부분 배열은 존재하지 않습니다.<br>
[3, 3, 3, 2, 2, 3, <code>3, 3, 1, 1</code>]은 부분 배열의 합이 8이지만 길이가 4이므로 <code>C</code>로 세지 않습니다.</p>

<p>따라서 [8, 2]를 return 해야 합니다.</p>

<p><strong>입출력 예 #2</strong>  </p>

<p>brr는 [2, <code>2</code>, 2, 2, 2, 2]입니다. 2~2번째 원소로 이루어진 부분 배열의 합은 2(=<code>K</code>)입니다. 합이 2이고 길이가 1인 부분 배열의 개수는 6개입니다.</p>

<p>따라서 [2, 6]을 return 해야 합니다.</p>

<p><strong>입출력 예 #3</strong>  </p>

<p>[15, 3]을 return 해야 합니다.</p>

<p><strong>입출력 예 #4</strong>  </p>

<p>[27554327568, 1]을 return 해야 합니다.</p>

<p><strong>입출력 예 #5</strong>  </p>

<p>[6860339640, 9190]을 return 해야 합니다.</p>

<p><strong>입출력 예 #6</strong>  </p>

<p>[2369282964, 59513]을 return 해야 합니다.</p>

<div class="footnotes">
<hr>
<ol>

<li id="fn1">
<p>부분 배열이란 주어진 배열에서 연속된 원소들로 이루어진 배열을 의미합니다.&nbsp;<a href="#fnref1">↩</a></p>
</li>

</ol>
</div>

> 출처: 프로그래머스 코딩 테스트 연습, https://school.programmers.co.kr/learn/challenges

## 풀이

### 문제 유형 파악과 이름의 의미

`arr[i]`를 `arr[i]`번 반복해서 `brr`를 만들기 때문에, `arr[i]`가 만드는 블록 하나의 합은 항상 `arr[i] × arr[i] = arr[i]²`이 된다. 문제 이름("제곱 개수 배열")도 여기서 나온 것으로 보인다. `brr`의 길이는 `arr` 원소의 합만큼(최대 100,000 × 100,000 = 1×10¹⁰ 수준) 커질 수 있어서, **`brr`를 실제 배열로 펼치면 안 된다**는 게 이 문제의 핵심 제약이다.

### 처음 시도와 실패 이유 (주석 처리된 코드)

처음에는 `arr`를 순회하며 `brr`를 실제 배열로 펼친 뒤, 길이 `len = r - l + 1`짜리 윈도우를 투 포인터로 한 칸씩 옮기며 합을 비교하는 방식으로 접근했다.

```js
arr.forEach(n => { for (let i = 0; i < n; i++) brr.push(n); });
// 이후 길이 len짜리 윈도우를 한 칸씩 옮기며 temp를 갱신, K와 비교해 C++
```

`brr`의 길이가 최대 1×10¹⁰ 수준까지 커질 수 있어서, 배열을 만드는 순간 메모리 초과이고, 모든 윈도우를 한 칸씩 순회하는 것도 시간 초과다. `l = r`인 아주 작은 케이스(테스트 케이스 그룹 #1)만 겨우 통과할 수 있는 수준이었다.

### 최종 접근: `brr`를 펼치지 않고 압축된 블록 단위로만 다루기

`brr`는 사실 "값이 `arr[i]`이고 길이가 `arr[i]`인 블록"이 `N`개 이어 붙은 구조(런렝스 인코딩)다. 이 블록 구조를 유지한 채로 `K`와 `C`를 각각 구했다.

**1단계: `K` 계산 — 임의 구간 합을 `O(log N)`에 구하기**

- `prefix_len[i]`: 앞의 `i`개 블록이 차지하는 `brr` 상의 총 길이
- `prefix_sum[i]`: 앞의 `i`개 블록의 합 (= `arr[0]² + arr[1]² + ... + arr[i-1]²`)

`getSum(idx)`는 "`brr`의 앞 `idx`개 원소의 합"을 구하는 함수다. `prefix_len` 위에서 이분 탐색으로 `idx`가 속한 블록을 찾은 뒤, 그 블록 이전까지의 누적 합(`prefix_sum[block]`)에 해당 블록 안에서 걸치는 부분(`(idx - prefix_len[block]) * arr[block]`, 블록 안은 전부 같은 값이므로 개수 × 값)만 더해서 `O(log N)`에 계산한다. `K = getSum(r) - getSum(l - 1)`로 1-based 구간 `[l, r]`의 합을 구한다.

**2단계: `C` 계산 — 구간별로 등차수열이 되는 성질 이용**

길이가 `L = r - l + 1`인 윈도우를 시작 위치 `s`(0-based, `0 ≤ s ≤ total_len - L`)만큼씩 오른쪽으로 옮긴다고 하면, 윈도우 합은 `s`가 1 증가할 때마다 "왼쪽에서 빠지는 값"(`s`가 속한 블록의 값)만큼 빼고 "오른쪽에서 새로 들어오는 값"(`s + L`이 속한 블록의 값)만큼 더해진다. 즉, **윈도우의 왼쪽 끝과 오른쪽 끝이 각각 같은 블록 안에 머무는 동안은 그 변화량(`delta`)이 일정**하므로, 그 구간 동안 윈도우 합은 등차수열을 이룬다.

- 왼쪽 끝이 블록 경계를 넘는 지점(`prefix_len[i]`)과, 오른쪽 끝(`s + L`)이 블록 경계를 넘는 지점(`prefix_len[i] - L`)들을 전부 모아 `cuts`로 정렬하면, 인접한 두 `cuts` 사이 구간에서는 `delta`가 항상 일정하게 유지된다(각 블록 경계마다 최대 2개씩만 생기므로 `cuts`는 최대 `O(N)`개).
- 각 구간마다 현재 왼쪽/오른쪽 블록 값(`v_left`, `v_right`)으로 `delta = v_right - v_left`를 구하고,
    - `delta === 0`이면 구간 내내 윈도우 합이 동일하므로, 그 합이 `K`와 같다면 구간 길이(`len_interval`)만큼을 통째로 `C`에 더한다.
    - `delta !== 0`이면 등차수열의 일반항 `current_sum + t × delta = K`를 만족하는 정수 `t`를 구해서(`(K - current_sum)`이 `delta`로 나누어떨어지는지 확인), 그 `t`가 현재 구간 길이 범위 안에 있으면 정확히 그 한 지점에서만 합이 `K`가 되므로 `C`에 1을 더한다.
    - 구간을 넘어갈 때는 `current_sum += len_interval * delta`로 다음 구간이 시작되는 시점의 윈도우 합을 갱신해 나간다.
- 각 `cuts` 지점에서 윈도우 좌/우 끝이 속한 블록 인덱스는, `s_start`가 단조 증가하므로 매번 이분 탐색으로 새로 찾지 않고 투 포인터(`getBlockLeft`, `getBlockRight`)로 상각 `O(1)`에 추적한다.

이렇게 하면 `cuts`를 정렬하는 `O(N log N)`과 이후 투 포인터로 구간을 훑는 `O(N)`만으로 `C`를 전부 구할 수 있다. `brr`를 한 번도 실제로 펼치지 않고, `arr` 크기(최대 100,000)에만 비례하는 시간·공간으로 문제를 해결했다.
