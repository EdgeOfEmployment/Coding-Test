완탐 안될듯

일단 그리디

트리 만들고, 끝에서 부터 끄고 키고

연결은 다 되어 있음

결론: N이 매우 큰 트리 형태의 네트워크(그래프) 구조 문제는 리프 노드부터 잘라내며 들어가는 그리디로 풀어야 한다.


```
#include <string>
#include <vector>
#include <queue>

using namespace std;

int solution(int n, vector<vector<int>> lighthouse) {
    vector<vector<int>> adj(n + 1); // 인접 리스트
    vector<int> degree(n + 1, 0);   // 각 노드의 연결 차수

    // 1. 그래프 구성 및 차수 계산
    for (const auto& edge : lighthouse) {
        int u = edge[0];
        int v = edge[1];
        adj[u].push_back(v);
        adj[v].push_back(u);
        degree[u]++;
        degree[v]++;
    }

    // 2. 리프 노드(차수가 1인 노드)를 큐에 삽입
    queue<int> q;
    for (int i = 1; i <= n; i++) {
        if (degree[i] == 1) {
            q.push(i);
        }
    }

    vector<bool> light_on(n + 1, false); // 등대가 켜졌는지 여부
    vector<bool> visited(n + 1, false);  // 큐에서 꺼내어 방문 처리했는지 여부

    // 3. 큐를 이용해 리프 노드부터 안쪽으로 탐색
    while (!q.empty()) {
        int u = q.front();
        q.pop();

        visited[u] = true;

        // 아직 방문하지 않은 부모(안쪽) 노드를 찾음
        int parent = -1;
        for (int v : adj[u]) {
            if (!visited[v]) {
                parent = v;
                break;
            }
        }

        // 더 이상 연결된 미방문 노드가 없다면 종료(마지막 노드)
        if (parent == -1) continue;

        // 현재 등대(리프)가 꺼져있다면, 연결된 간선을 커버하기 위해 부모를 켬
        if (!light_on[u]) {
            light_on[parent] = true;
        }

        // 부모 노드의 차수를 1 줄이고, 새롭게 리프가 되면 큐에 삽입
        degree[parent]--;
        if (degree[parent] == 1) {
            q.push(parent);
        }
    }

    // 4. 불이 켜진 등대의 총 개수 집계
    int answer = 0;
    for (int i = 1; i <= n; i++) {
        if (light_on[i]) {
            answer++;
        }
    }

    return answer;
}
```