#include <string>
#include <vector>
#include <algorithm>
#include <cstring>
#include <climits>

using namespace std;

extern int excavate(int);

// 구간 [L, R]을 처리하기 위한 메모이제이션 테이블
long long memo[205][205];
int best_choice[205][205]; // 각 구간 [L, R]에서 먼저 파야 할 최적의 열

// 구간 [L, R]을 해결하는 데 드는 최악의 최소 비용을 구하는 함수 (인터벌 DP)
long long solve(int L, int R, const vector<int>& depth) {
    if (L > R) return 0;
    // [수정 2순위] 기저 사례: 단 한 칸만 남았을 때, 그 칸을 파야 하므로 비용은 depth[L-1]입니다.
    if (L == R) return depth[L - 1]; 
    
    if (memo[L][R] != -1) return memo[L][R];
    
    long long min_cost = LLONG_MAX; // [수정 5순위] 안전한 초기화
    int choice = L;
    
    for (int i = L; i <= R; i++) {
        long long cost_i = depth[i - 1];
        
        // i번 열을 팠을 때 결과가 왼쪽(-1) 또는 오른쪽(1) 중 최악의 경우를 선택
        // i번 열을 이미 팠으므로 남은 구간은 각각 [L, i-1]과 [i+1, R]이 됩니다.
        long long left_cost = (L <= i - 1) ? solve(L, i - 1, depth) : 0;
        long long right_cost = (i + 1 <= R) ? solve(i + 1, R, depth) : 0;
        
        long long worst_case = cost_i + max(left_cost, right_cost);
        
        // [수정 4순위] Tie-break 조건 추가 (비용이 같다면 depth가 더 작은 곳을 우선 선택하여 Adaptive 공격에 대비)
        if (worst_case < min_cost) {
            min_cost = worst_case;
            choice = i;
        } else if (worst_case == min_cost) {
            if (depth[i - 1] < depth[choice - 1]) {
                choice = i;
            }
        }
    }
    
    best_choice[L][R] = choice;
    return memo[L][R] = min_cost;
}

int solution(vector<int> depth, int money) {
    int w = depth.size();
    
    memset(memo, -1, sizeof(memo));
    memset(best_choice, 0, sizeof(best_choice));
    
    // DP 테이블 채우기
    solve(1, w, depth);
    
    // 실제 로봇과 인터랙션하며 탐색 수행
    int L = 1, R = w;
    while (L <= R) {
        int target;
        
        // [수정 1순위] L == R일 때 무조건 return하지 않고 명시적으로 excavate를 호출해 0을 확인합니다.
        if (L == R) {
            target = L;
        } else {
            target = best_choice[L][R];
            if (target == 0 || target < L || target > R) target = L; // 예외 방어
        }
        
        int result = excavate(target);
        
        if (result == 0) {
            return target; // 반드시 0을 반환받아야 정답 처리됨
        } else if (result == -1) {
            R = target - 1;
        } else {
            L = target + 1;
        }
    }
    
    return L;
}