#include <string>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

int solution(vector<vector<int>> cost, vector<vector<int>> hint) {
    int n = cost.size();
    long long min_total_cost = LLONG_MAX;
    
    int num_bundles = n - 1; // 마지막 스테이지를 제외한 각 스테이지마다 번들 존재
    int total_masks = 1 << num_bundles;
    
    // 1. 모든 힌트 번들 구매 조합(비트마스크) 탐색
    for (int mask = 0; mask < total_masks; ++mask) {
        long long current_cost = 0;
        vector<int> ticket_count(n + 1, 0); // 각 힌트권 보유 개수 (1번 ~ n번)
        
        // 2. 번들 구매 비용 및 획득 힌트권 계산
        for (int i = 0; i < num_bundles; ++i) {
            if ((mask >> i) & 1) {
                current_cost += hint[i][0]; // 번들 구매 가격 추가
                for (size_t j = 1; j < hint[i].size(); ++j) {
                    int ticket_type = hint[i][j];
                    if (ticket_type >= 1 && ticket_type <= n) {
                        ticket_count[ticket_type]++;
                    }
                }
            }
        }
        
        // 3. 각 스테이지 클리어 비용 계산
        for (int i = 0; i < n; ++i) {
            int stage_num = i + 1;
            int available_tickets = ticket_count[stage_num];
            int max_allowed_tickets = cost[i].size() - 1; // 최대 사용 가능한 힌트권 수 (n - 1)
            int tickets_to_use = min(available_tickets, max_allowed_tickets);
            
            current_cost += cost[i][tickets_to_use];
        }
        
        // 4. 최소 비용 갱신
        min_total_cost = min(min_total_cost, current_cost);
    }
    
    return (int)min_total_cost;
}