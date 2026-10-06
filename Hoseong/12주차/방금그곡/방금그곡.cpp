#include <string>
#include <vector>
#include <sstream>

using namespace std;

// 시각(HH:MM)을 분 단위로 변환
int toMinutes(const string& t) {
    return stoi(t.substr(0, 2)) * 60 + stoi(t.substr(3, 2));
}

// '#'이 붙은 음을 소문자 한 글자로 치환
string convert(const string& s) {
    string res;
    for (size_t i = 0; i < s.size(); i++) {
        if (i + 1 < s.size() && s[i + 1] == '#') {
            res += (char)(s[i] + 32);
            i++;
        } else {
            res += s[i];
        }
    }
    return res;
}

string solution(string m, vector<string> musicinfos) {
    string answer = "(None)";
    int maxTime = -1;
    string target = convert(m);

    for (const string& info : musicinfos) {
        vector<string> tok;
        stringstream ss(info);
        string part;
        while (getline(ss, part, ',')) tok.push_back(part);

        int playTime = toMinutes(tok[1]) - toMinutes(tok[0]);
        string title = tok[2];
        string score = convert(tok[3]);

        string played;
        for (int i = 0; i < playTime; i++)
            played += score[i % score.size()];

        if (played.find(target) != string::npos && playTime > maxTime) {
            maxTime = playTime;
            answer = title;
        }
    }
    return answer;
}
