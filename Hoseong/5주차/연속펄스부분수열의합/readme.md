못풀었음
AI

누적합

```
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

long long solution(vector<int> sequence) {
    long long max_sum = 0; // 누적합의 최댓값 (초기 누적합 0 포함)
    long long min_sum = 0; // 누적합의 최솟값 (초기 누적합 0 포함)
    long long cur_sum = 0; // 현재까지의 누적합

    for (int i = 0; i < sequence.size(); i++) {
        // Pulse A: 인덱스가 짝수면 1, 홀수면 -1
        long long pulse = (i % 2 == 0) ? 1 : -1;

        // 펄스가 적용된 값을 현재 누적합에 더함
        cur_sum += sequence[i] * pulse;

        // 누적합의 최댓값과 최솟값을 갱신
        max_sum = max(max_sum, cur_sum);
        min_sum = min(min_sum, cur_sum);
    }

    // 최댓값과 최솟값의 차이가 만들 수 있는 가장 큰 연속 펄스 부분 수열의 합
    return max_sum - min_sum;
}
```
