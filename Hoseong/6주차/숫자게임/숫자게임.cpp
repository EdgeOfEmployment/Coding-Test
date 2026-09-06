#include <vector>
#include <algorithm>

using namespace std;

int solution(vector<int> A, vector<int> B) {
    int answer = 0;
    
    // 1. 두 팀의 숫자를 모두 오름차순으로 정렬
    sort(A.begin(), A.end());
    sort(B.begin(), B.end());
    
    int a_idx = 0;
    int b_idx = 0;
    
    // 2. 투 포인터를 사용하여 배열 탐색
    while (a_idx < A.size() && b_idx < B.size()) {
        // B팀의 숫자가 A팀의 숫자보다 커서 이길 수 있는 경우
        if (B[b_idx] > A[a_idx]) {
            answer++;
            a_idx++; // 다음 A팀 숫자로 넘어감
            b_idx++; // 다음 B팀 숫자로 넘어감
        } 
        // B팀의 숫자가 A팀의 숫자보다 작거나 같아 질 수밖에 없는 경우
        else {
            b_idx++; // 현재 B팀 숫자는 A팀의 큰 숫자에 "버리는 카드"로 사용
        }
    }
    
    return answer;
}