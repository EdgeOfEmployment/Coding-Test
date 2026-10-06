#include <string>
#include <vector>
#include <algorithm>

using namespace std;

// 돌 곡괭이 기준 피로도 계산
int getFatigue(const string& m) {
    if (m == "diamond") return 25;
    if (m == "iron") return 5;
    return 1;
}

int solution(vector<int> picks, vector<string> minerals) {
    int answer = 0;

    // 1. 보유한 곡괭이로 캘 수 있는 최대 광물 개수 제한
    int maxMine = (picks[0] + picks[1] + picks[2]) * 5;
    if (minerals.size() > maxMine) {
        minerals.resize(maxMine);
    }

    // 2. 5개씩 묶어서 그룹(chunk) 생성 및 피로도 합산
    vector<pair<int, vector<string>>> chunks;
    for (size_t i = 0; i < minerals.size(); i += 5) {
        vector<string> chunk;
        int fatigue = 0;

        for (size_t j = i; j < i + 5 && j < minerals.size(); j++) {
            chunk.push_back(minerals[j]);
            fatigue += getFatigue(minerals[j]);
        }
        chunks.push_back({fatigue, chunk});
    }

    // 3. 돌 곡괭이 기준 피로도가 높은 그룹 순으로 내림차순 정렬
    sort(chunks.begin(), chunks.end(), [](const auto& a, const auto& b) {
        return a.first > b.first;
    });

    // 4. 피로도가 큰 그룹부터 좋은 곡괭이(다이아 -> 철 -> 돌) 우선 할당
    for (const auto& c : chunks) {
        int pIndex = -1;
        if (picks[0] > 0) pIndex = 0;      // 다이아 곡괭이
        else if (picks[1] > 0) pIndex = 1; // 철 곡괭이
        else if (picks[2] > 0) pIndex = 2; // 돌 곡괭이
        else break;                        // 사용할 곡괭이가 없음

        picks[pIndex]--;

        // 선택된 곡괭이로 해당 묶음 캐기
        for (const string& m : c.second) {
            if (pIndex == 0) { // 다이아 곡괭이
                answer += 1;
            } else if (pIndex == 1) { // 철 곡괭이
                if (m == "diamond") answer += 5;
                else answer += 1;
            } else { // 돌 곡괭이
                if (m == "diamond") answer += 25;
                else if (m == "iron") answer += 5;
                else answer += 1;
            }
        }
    }

    return answer;
}