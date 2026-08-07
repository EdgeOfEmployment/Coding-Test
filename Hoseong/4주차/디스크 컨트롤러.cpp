#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

struct Job {
    int request;
    int duration;
    int idx;
};

struct cmp {
    bool operator()(Job a, Job b) {
        if (a.duration != b.duration)
            return a.duration > b.duration;
        if (a.request != b.request)
            return a.request > b.request;
        return a.idx > b.idx;
    }
};

int solution(vector<vector<int>> jobs) {

    vector<Job> arr;

    for (int i = 0; i < jobs.size(); i++) {
        arr.push_back({jobs[i][0], jobs[i][1], i});
    }

    sort(arr.begin(), arr.end(), [](Job a, Job b) {
        return a.request < b.request;
    });

    priority_queue<Job, vector<Job>, cmp> pq;

    int time = 0;
    int idx = 0;
    int total = 0;

    while (idx < arr.size() || !pq.empty()) {

        while (idx < arr.size() && arr[idx].request <= time) {
            pq.push(arr[idx]);
            idx++;
        }

        if (!pq.empty()) {

            Job cur = pq.top();
            pq.pop();

            time += cur.duration;
            total += time - cur.request;
        }
        else {
            time = arr[idx].request;
        }
    }

    return total / jobs.size();
}