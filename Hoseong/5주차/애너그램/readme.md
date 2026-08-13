hash map

sort, vector 그대로 return

```
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>

using namespace std;

class Solution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        unordered_map<string, vector<string>> anagramMap;

        for (const string& s : strs) {
            string key = s;
            sort(key.begin(), key.end());
            anagramMap[key].push_back(s);
        }

        vector<vector<string>> result;
        result.reserve(anagramMap.size());

        for (auto& [key, group] : anagramMap) {
            result.push_back(move(group));
        }

        return result;
    }
};
```
