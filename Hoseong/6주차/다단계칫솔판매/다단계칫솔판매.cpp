#include <string>
#include <vector>
#include <unordered_map>

using namespace std;

vector<int> solution(vector<string> enroll, vector<string> referral, vector<string> seller, vector<int> amount) {
    // 1. 트리 구조(부모 노드)와 수익을 기록할 해시맵 초기화
    unordered_map<string, string> parent;
    unordered_map<string, int> profit;
    
    for (int i = 0; i < enroll.size(); ++i) {
        parent[enroll[i]] = referral[i];
        profit[enroll[i]] = 0;
    }
    
    // 2. 각 판매 건에 대해 수익 분배 계산
    for (int i = 0; i < seller.size(); ++i) {
        string current_name = seller[i];
        int current_money = amount[i] * 100; // 칫솔 개당 100원
        
        // 추천인이 없거나("-" 즉, center) 분배할 금액이 없을 때까지 거슬러 올라감
        while (current_name != "-" && current_money > 0) {
            int distribute = current_money / 10;          // 상위로 넘길 10%
            int mine = current_money - distribute;        // 자신이 가질 90% (원 단위 절사 보정)
            
            profit[current_name] += mine;                 // 이익 누적
            
            // 상위 추천인으로 타겟 변경
            current_name = parent[current_name];
            current_money = distribute;
        }
    }
    
    // 3. enroll 순서대로 수익금 배열 생성
    vector<int> answer;
    for (int i = 0; i < enroll.size(); ++i) {
        answer.push_back(profit[enroll[i]]);
    }
    
    return answer;
}