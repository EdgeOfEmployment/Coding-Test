# [level 3] 카카오 앱 정리하기 - 468374

[문제 링크](https://school.programmers.co.kr/learn/courses/30/lessons/468374)

### 성능 요약

메모리: 57.3 MB, 시간: 34.48 ms

### 구분

코딩테스트 연습 > 2025 카카오 하반기 1차

### 채점결과

정확성: 100.0<br/>합계: 100.0 / 100.0

### 제출 일자

2026년 07월 21일 16:14:25

### 문제 설명

<p><code>N</code> x <code>M</code> 크기의 격자판 위에 카카오 앱들이 존재합니다. 격자에서 가장 왼쪽 위칸은 1행 1열, 가장 오른쪽 아래칸은 <code>N</code>행 <code>M</code>열입니다. 모든 앱들은 정사각형 모양이며 크기는 제각각 다를 수 있습니다.</p>

<p>예를 들어, <code>N</code> = 6, <code>M</code> = 8일 때 아래 그림과 같이 앱들이 배치되어 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/89808f9e-45f4-4a7c-8a6b-ca04b1feed91/kakao_app_00.jpg" title="" alt="kakao_app_00.jpg"></p>

<ul>
<li>6개의 앱이 격자판 위에 존재합니다.</li>
</ul>

<p>당신은 해당 앱들 중 하나를 골라 상하좌우 중 한 방향으로 한 칸 밀 수 있습니다. 이때, 해당 방향에 다른 앱이 존재한다면 해당 앱도 같은 방향으로 한 칸 밀려납니다. 위 예시에서 카카오톡 앱을 오른쪽으로 한 칸 밀면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/acfd35fe-38d9-4287-bca1-da9662e5a942/kakao_app_01.jpg" title="" alt="kakao_app_01.jpg"></p>

<ul>
<li>빨간색으로 표시된 카카오톡 앱을 오른쪽으로 한 칸 밉니다.</li>
<li>초록색으로 표시된 4개의 앱들은 카카오톡에 의해 영향을 받은 앱들을 나타냅니다.</li>
</ul>

<p>만약 앱이 격자 밖으로 이동하게 된다면, 해당 앱은 격자 반대편으로 이어져 나오게 됩니다. 이때, 크기가 2x2 이상인 앱은 한 칸이라도 격자 밖으로 이동하게 되면 반대편으로 이동하게 됩니다. 격자 반대편으로 이동할 때 해당 위치에 다른 앱이 있다면 그 앱도 같은 방향으로 밀려납니다. 위 예시에서 카카오톡 앱을 오른쪽으로 한 칸 밀면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/c7cfe11c-eb92-4c78-977b-396e9ec32605/kakao_app_02.jpg" title="" alt="kakao_app_02.jpg"></p>

<ul>
<li>카카오 뱅크앱이 격자 밖으로 이동하게 되어 격자 반대편으로 이동하게 됩니다.</li>
<li>카카오 웹툰앱(w)의 일부가 격자 밖으로 이동하게 되어 격자 반대편으로 이동하게 됩니다. 이 과정에서 카카오 뮤직 앱이 오른쪽으로 밀리게 됩니다. </li>
</ul>

<p>격자와 앱의 위치 정보를 담은 2차원 정수 배열 <code>board</code>와 앱의 이동 명령을 순서대로 담은 2차원 정수 배열 <code>commands</code>가 매개변수로 주어집니다. 주어진 규칙에 따라 앱을 이동시킨 후 앱의 위치 정보를 2차원 정수 배열에 담아 return 하도록 solution 함수를 완성해 주세요.</p>

<hr>

<h5>제한사항</h5>

<ul>
<li>2 ≤ <code>board</code>의 길이 = <code>N</code> ≤ 10

<ul>
<li>2 ≤ <code>board[i]</code>의 길이 = <code>M</code> ≤ 10</li>
<li>0 ≤ <code>board[i][j]</code> ≤ 100</li>
<li><code>board[i][j]</code>는 앱의 유일한 ID를 나타내며, <code>board</code>의 <code>i+1</code>번째 행 <code>j+1</code>번째 열에 ID가 <code>board[i][j]</code>인 앱이 존재함을 나타냅니다. 각 앱의 ID는 1번부터 <code>앱의 가짓수</code>번까지 번호가 붙어 있습니다.</li>
<li><code>board[i][j]</code>가 같은 원소면 정사각형 모양으로 붙어 있습니다.</li>
<li><code>board[i][j]</code>가 0이면 해당 격자는 빈 칸임을 나타냅니다.</li>
</ul></li>
<li>1 ≤ <code>commands</code>의 길이 ≤ 1,000

<ul>
<li><code>commands[i]</code>는 [<code>ID</code>, <code>arrow</code>]형태로 ID가 <code>ID</code>인 앱을 <code>arrow</code>방향으로 한 칸 움직임을 나타냅니다.</li>
<li>1 ≤ <code>ID</code> ≤ <code>board[i][j]</code>의 최댓값</li>
<li>1 ≤ <code>arrow</code> ≤ 4</li>
<li>1은 오른쪽, 2는 아래쪽, 3은 왼쪽, 4는 위쪽 방향을 나타냅니다.</li>
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
<td>1x1 크기의 앱 1개만 존재합니다.</td>
</tr>
<tr>
<td>#2</td>
<td>10%</td>
<td>2x2 크기의 앱 1개만 존재합니다.</td>
</tr>
<tr>
<td>#3</td>
<td>15%</td>
<td>모든 앱의 크기가 1x1입니다.</td>
</tr>
<tr>
<td>#4</td>
<td>20%</td>
<td>앱이 격자 밖으로 이동하는 명령이 주어지지 않습니다.</td>
</tr>
<tr>
<td>#5</td>
<td>50%</td>
<td>추가 제한 사항 없음</td>
</tr>
</tbody>
      </table>
<hr>

<h5>입출력 예</h5>
<table class="table">
        <thead><tr>
<th>board</th>
<th>commands</th>
<th>result</th>
</tr>
</thead>
        <tbody><tr>
<td>[[0, 2, 2, 0, 0, 0, 0, 0], [0, 2, 2, 0, 0, 4, 4, 0], [0, 3, 3, 3, 1, 4, 4, 0], [0, 3, 3, 3, 0, 0, 0, 0], [0, 3, 3, 3, 5, 5, 6, 0], [0, 0, 0, 0, 5, 5, 0, 0]]</td>
<td>[[3, 1], [3, 1]]</td>
<td>[[0, 0, 2, 2, 0, 0, 0, 0], [4, 4, 2, 2, 0, 0, 0, 0], [4, 4, 0, 3, 3, 3, 1, 0], [0, 0, 0, 3, 3, 3, 0, 0], [6, 0, 0, 3, 3, 3, 5, 5], [0, 0, 0, 0, 0, 0, 5, 5]]</td>
</tr>
<tr>
<td>[[0, 9, 1, 1, 6, 0, 0, 0], [2, 2, 1, 1, 0, 0, 0, 0], [2, 2, 3, 4, 4, 4, 0, 0], [5, 0, 0, 4, 4, 4, 7, 0], [0, 0, 0, 4, 4, 4, 8, 8], [0, 0, 0, 0, 0, 0, 8, 8]]</td>
<td>[[2, 1], [3, 1], [9, 2], [4, 1]]</td>
<td>[[8, 8, 0, 1, 1, 6, 0, 0], [8, 8, 0, 1, 1, 0, 0, 0], [4, 4, 4, 9, 3, 0, 0, 0], [4, 4, 4, 7, 2, 2, 0, 0], [4, 4, 4, 0, 2, 2, 0, 0], [0, 5, 0, 0, 0, 0, 0, 0]]</td>
</tr>
<tr>
<td>[[1, 1, 0], [1, 1, 0]]</td>
<td>[[1, 4], [1, 3], [1, 2]]</td>
<td>[[0, 1, 1], [0, 1, 1]]</td>
</tr>
</tbody>
      </table>
<h5>입출력 예 설명</h5>

<p><strong>입출력 예 #1</strong></p>

<ul>
<li>문제 예시와 같습니다.</li>
</ul>

<p><strong>입출력 예 #2</strong></p>

<p>아래 그림과 같이 앱들이 배치되어 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/b87c343d-5041-410b-ac9b-f29c9cead08e/kakao_app_2_0.jpg" title="" alt="kakao_app_2_0.jpg"></p>

<p>2번 앱(카카오 뮤직)을 오른쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/aacb94b3-d90e-469d-afb6-cc9332434a55/kakao_app_2_1.jpg" title="" alt="kakao_app_2_1.jpg"></p>

<p>3번 앱(카카오 페이)을 오른쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/ce00e0d3-5987-4579-9c70-54e4eeba8d40/kakao_app_2_2.jpg" title="" alt="kakao_app_2_2.jpg"></p>

<p>9번 앱(카카오톡)을 아래쪽으로 한 칸 움직이면 아래와 같이 됩니다. 카카오 웹툰 앱이 위쪽으로 이동하게 되어 카카오톡 앱이 아래로 한 칸 더 밀립니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/a8beb83c-6bf5-4e24-a450-5ed21d535fad/kakao_app_2_3.jpg" title="" alt="kakao_app_2_3.jpg"></p>

<p>4번 앱(카카오 페이지)을 오른쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/71ab123a-613d-4f36-95dd-33766cb63f7c/kakao_app_2_4.jpg" title="" alt="kakao_app_2_4.jpg"></p>

<p><strong>입출력 예 #3</strong></p>

<p>아래 그림과 같이 앱이 배치되어 있습니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/37393a7e-78cc-4bd1-bf2a-0a2ac732ced7/kakao_app_3_0.jpg" title="" alt="kakao_app_3_0.jpg"></p>

<p>1번 앱(프로그래머스)을 위쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/ba46512f-177e-4721-9cb5-ad18cf80ee34/kakao_app_3_1.jpg" title="" alt="kakao_app_3_1.jpg"></p>

<p>1번 앱(프로그래머스)을 왼쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/ea5787bc-594a-4da7-b17c-7fdb778ea2d4/kakao_app_3_2.jpg" title="" alt="kakao_app_3_2.jpg"></p>

<p>1번 앱(프로그래머스)을 아래쪽으로 한 칸 움직이면 아래와 같이 됩니다.</p>

<p><img src="https://grepp-programmers.s3.ap-northeast-2.amazonaws.com/production/presigned_urls/79427532-2a82-405f-b0be-b106d8ab8ec3/kakao_app_3_3.jpg" title="" alt="kakao_app_3_3.jpg"></p>

## 문제 요약

N×M 격자 위에 정사각형 모양의 앱들이 놓여 있다. 커맨드마다 앱 하나를 상하좌우 한 방향으로 1칸 민다.

- 미는 방향에 다른 앱이 있으면 그 앱도 같은 방향으로 밀린다 (연쇄 push).
- 앱이 격자 밖으로 나가게 되면 반대편 끝으로 이어져 나온다 (wrap). 이때 반대편에 다른 앱이 있으면 그 앱도 같은 방향으로 밀린다.

## 처음에 시도했던 방식과 실패 이유

초기 접근은 "각 앱이 최종적으로 몇 칸(`steps`) 밀릴지를 계산하고, 두 앱의 목표 칸이 겹치면 우선순위 규칙(누가 이기고 누가 한 칸 더 밀리는지)으로 조정"하는 방식이었다.

이 우선순위 규칙(예: "wrap한 쪽이 이긴다", "스텝이 큰 쪽이 이긴다", "부모-자식 관계에서 자식이 이긴다" 등)을 공식 예제 3개만 보고 역으로 추론해서 만들었는데, 문제는:

- 예제에 없는 상황(한 커맨드 안에서 **여러 앱이 동시에, 서로 무관하게 wrap**하며 충돌하는 밀집 보드)에서 규칙이 안 맞았다.
- 대칭적인 비교 규칙(예: "스텝 큰 쪽이 이긴다")은 두 앱이 서로 자리를 계속 맞바꾸는 **무한 진동**을 일으킬 수 있었다.
- 즉, "미래를 추측해서 조정"하는 방식 자체가 근본적으로 불안정했다.

## 최종 방식: 미래를 추측하지 않고, 실제 이동 과정을 그대로 시뮬레이션

핵심 발상 전환: 어떤 앱이 몇 칸 밀릴지 미리 계산하려 하지 말고, 문제에 적힌 물리적 과정을 **한 칸씩, 여러 웨이브로 나눠서 그대로 재현**한다.

### 웨이브(wave) 단위 시뮬레이션

한 커맨드는 다음을 겹침이 사라질 때까지 반복하는 것으로 처리한다.

1. **연쇄 확장(closure)**: 지금 이동해야 하는 앱들의 목표 칸(1칸 이동했을 때 도착할 칸)에 다른 앱이 있으면, 그 앱도 이번 웨이브의 이동 대상에 포함시킨다. 단, **지금 이미 나와 겹쳐 있는** 앱은 제외한다 — 그 앱은 내가 미는 대상이 아니라, 오히려 나를 밀고 있는(내 자리에 올라탄) 쪽이기 때문이다.
2. **동시 이동**: 이번 웨이브의 이동 대상 전원을 **동시에 정확히 1칸** 이동시킨다. 이동 중 격자 밖으로 나가면 반대편으로 wrap하고, 몇 번째 웨이브에 wrap했는지 기록해 둔다.
3. **겹침 검사**: 이동이 끝난 뒤 두 앱의 영역이 겹치는지 확인한다.
    - 겹치는 경우는 항상 누군가 **wrap해서 도착**했기 때문에 생긴다 (같은 방향으로 동시에 움직이는 앱들끼리는 간격이 보존되고, 정지해 있던 앱과의 충돌은 1단계 closure에서 이미 흡수되므로).
    - 문제 규칙 그대로: **더 최근에 wrap해서 도착한 앱이 자리를 지키고**, 원래 있던(또는 덜 최근에 도착한) 앱이 다음 웨이브에 한 번 더 밀린다.
4. 겹침이 전혀 없으면 커맨드 종료. 겹침이 있으면 밀려난 앱들을 시드로 다시 1단계부터 반복.

이 방식의 장점은 "누가 이기는지"를 임의로 정할 필요가 없다는 것이다. 문제 문장이 이미 답을 주고 있다 — _"격자 반대편으로 이동할 때 해당 위치에 다른 앱이 있다면 그 앱도 같은 방향으로 밀려납니다"_. wrap해서 도착한 쪽이 항상 밀어내는 쪽이라는 규칙은 애매함이 없고, 예제에 없던 복잡한 상황에서도 자연스럽게 확장된다.

### 구현 세부사항

- 앱 위치는 커맨드 처리 중에는 `{id, r, c, size}` 형태의 맵으로만 관리하고, 2차원 배열(`board`)에는 반영하지 않는다. 웨이브 도중에는 앱끼리 일시적으로 겹칠 수 있어서 board로는 표현이 안 되기 때문이다. 모든 커맨드 처리가 끝난 뒤 마지막에 한 번만 board를 재구성한다.
- 셀 점유 여부는 매 웨이브마다 `grid[r*M+c] = [점유 중인 id들]` 형태로 다시 만들어서 확인한다 (겹침을 허용하는 구조).
- 무한 루프 방지용 가드(`guard`)를 넣어 두었지만, 정상적으로는 웨이브 수가 격자 둘레 근처에서 수렴하므로 실제로 걸릴 일은 없다.
