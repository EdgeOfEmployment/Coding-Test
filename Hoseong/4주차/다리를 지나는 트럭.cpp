#include <string>
#include <vector>
#include <queue>

using namespace std;

int solution(int bridge_length, int weight, vector<int> truck_weights) {
    queue<int> bridge;
    
    // 처음에는 다리가 비어있음
    for (int i = 0; i < bridge_length; i++)
        bridge.push(0);

    int time = 0;
    int bridgeWeight = 0;
    int idx = 0;

    while (!bridge.empty()) {
        time++;

        // 한 칸 이동 (맨 앞이 다리에서 나감)
        bridgeWeight -= bridge.front();
        bridge.pop();

        // 아직 대기 트럭이 있다면
        if (idx < truck_weights.size()) {
            if (bridgeWeight + truck_weights[idx] <= weight) {
                bridge.push(truck_weights[idx]);
                bridgeWeight += truck_weights[idx];
                idx++;
            } else {
                bridge.push(0);
            }
        }

        // 모든 트럭이 올라간 뒤에는 빈칸만 빠져나감
        if (idx == truck_weights.size() && bridgeWeight == 0)
            break;
    }

    return time;
}