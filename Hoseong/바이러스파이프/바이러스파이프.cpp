#include <string>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;

struct Edge {
    int to;
    int type;
};

int max_infected = 0;
int N, K;
vector<vector<Edge>> adj;

// [BFS] 현재 감염된 상태에서 특정 타입의 파이프를 열었을 때의 감염 전파를 시뮬레이션
vector<bool> spread(const vector<bool>& current_infected, int type) {
    vector<bool> next_infected = current_infected;
    queue<int> q;
    
    // 현재 감염되어 있는 모든 노드를 큐의 시작점으로 추가
    for (int i = 1; i <= N; ++i) {
        if (current_infected[i]) {
            q.push(i);
        }
    }
    
    // 연결된 파이프가 매개변수로 주어진 type과 일치할 때만 큐에 넣으며 전파
    while (!q.empty()) {
        int curr = q.front();
        q.pop();
        
        for (const auto& edge : adj[curr]) {
            if (edge.type == type && !next_infected[edge.to]) {
                next_infected[edge.to] = true;
                q.push(edge.to);
            }
        }
    }
    
    return next_infected;
}

// [DFS] 백트래킹을 이용한 조합 탐색
void dfs(int step, const vector<bool>& current_infected, int current_count) {
    max_infected = max(max_infected, current_count);
    
    // 조기 종료 조건: 모든 노드가 감염되었거나, 최대 행동 수 K에 도달했을 때
    if (current_count == N || step == K) {
        return;
    }
    
    // 1(A), 2(B), 3(C) 종류의 파이프를 각각 열어보기
    for (int type = 1; type <= 3; ++type) {
        vector<bool> next_infected = spread(current_infected, type);
        
        // 새로 감염된 노드의 개수 세기
        int next_count = 0;
        for (int i = 1; i <= N; ++i) {
            if (next_infected[i]) next_count++;
        }
        
        // 가지치기: 실제로 새로 감염된 배양체가 존재하는 경우에만 깊이 탐색 진행
        if (next_count > current_count) {
            dfs(step + 1, next_infected, next_count);
        }
    }
}

int solution(int n, int infection, vector<vector<int>> edges, int k) {
    // 전역 변수 초기화
    N = n;
    K = k;
    max_infected = 0;
    adj.assign(n + 1, vector<Edge>());
    
    // 인접 리스트 생성 (양방향 그래프)
    for (const auto& edge : edges) {
        int u = edge[0];
        int v = edge[1];
        int type = edge[2];
        adj[u].push_back({v, type});
        adj[v].push_back({u, type});
    }
    
    // 최초 감염 상태 초기화
    vector<bool> start_infected(n + 1, false);
    start_infected[infection] = true;
    
    // DFS 백트래킹 시작 (0단계, 최초 상태, 감염 수 1)
    dfs(0, start_infected, 1);
    
    return max_infected;
}