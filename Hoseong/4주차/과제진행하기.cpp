#include <string>
#include <vector>
#include <algorithm>
#include <stack>

using namespace std;

struct Task {
    string name;
    int start;
    int play;
};

int toMinute(string s) {
    return stoi(s.substr(0, 2)) * 60 + stoi(s.substr(3, 2));
}

vector<string> solution(vector<vector<string>> plans) {

    vector<Task> tasks;

    for (auto &p : plans) {
        tasks.push_back({
            p[0],
            toMinute(p[1]),
            stoi(p[2])
        });
    }

    sort(tasks.begin(), tasks.end(), [](Task &a, Task &b) {
        return a.start < b.start;
    });

    vector<string> answer;
    stack<Task> st;

    for (int i = 0; i < tasks.size() - 1; i++) {

        Task cur = tasks[i];
        int gap = tasks[i + 1].start - cur.start;

        if (cur.play > gap) {
            cur.play -= gap;
            st.push(cur);
        }
        else {

            answer.push_back(cur.name);

            int remain = gap - cur.play;

            while (!st.empty() && remain > 0) {

                Task t = st.top();
                st.pop();

                if (t.play <= remain) {
                    remain -= t.play;
                    answer.push_back(t.name);
                }
                else {
                    t.play -= remain;
                    st.push(t);
                    remain = 0;
                }
            }
        }
    }

    answer.push_back(tasks.back().name);

    while (!st.empty()) {
        answer.push_back(st.top().name);
        st.pop();
    }

    return answer;
}