/*
#include <iostream>
#include <vector>
#include <string>
#include <utility> 

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    // 1. size: 입력 횟수, find: 검색 횟수
    int size, find;
    cin >> size >> find;

    // 2. 메모 저장
    vector<pair<string, string>> arr(size);
    for (int i = 0; i < size; i++) {
        cin >> arr[i].first >> arr[i].second;
    }

    // 버퍼 비우기
    cin.ignore();
    
    // find 반복
    for (int i = 0; i < find; i++) {
        string line;
        getline(cin, line);

        // O(n)탐색
        for (int index = 0; index < size; index++) {            
            if (arr[index].first == line) {
                // cout << arr[index].first << arr[index].second << '\n';
                cout << arr[index].second << '\n';
            }
        }
    }

    return 0;
}
*/

#include <iostream>
#include <string>
#include <unordered_map>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int size, find;
    cin >> size >> find;

    // 1. 해시 맵에 (사이트 주소, 비밀번호) 저장
    unordered_map<string, string> memo;
    string site, pw;
    for (int i = 0; i < size; i++) {
        cin >> site >> pw;
        memo[site] = pw;
    }
    
    // 버퍼 비우기
    cin.ignore();

    // 2. 비밀번호 검색 및 출력 (공백이 없으므로 cin 사용 가능)
    for (int i = 0; i < find; i++) {
        cin >> site;
        cout << memo[site] << '\n';
    }

    return 0;
}