#include <string>
#include <vector>
#include <queue>

using namespace std;

vector<int> solution(int n, vector<vector<int>> roads, vector<int> sources, int destination) {
    vector<int> answer;

    vector<vector<int>> graph(n + 1);
    for (auto& r : roads) {
        int a = r[0], b = r[1];
        graph[a].push_back(b);
        graph[b].push_back(a);
    }

    vector<int> dist(n + 1, -1);
    dist[destination] = 0;
    queue<int> q;
    q.push(destination);

    while (!q.empty()) {
        int cur = q.front();
        q.pop();
        for (int next : graph[cur]) {
            if (dist[next] == -1) {
                dist[next] = dist[cur] + 1;
                q.push(next);
            }
        }
    }

    for (int s : sources) {
        answer.push_back(dist[s]);
    }

    return answer;
}