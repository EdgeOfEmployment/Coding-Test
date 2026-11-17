#include <vector>
#include <stack>
#include <algorithm>

using namespace std;

class Solution {
public:
    vector<int> dailyTemperatures(vector<int>& temperatures) {
        int size = temperatures.size();
        vector<int> result(size, 0);

        // 이중 for문 O(N^2) test case 마지막 꺼 통과 못함....
        // for(int i = 0; i < size-1; i++)
        // {
        //     int currentTemperature = temperatures[i];
        //     for(int j = i+1; j < size; j++)
        //     {
        //         if(currentTemperature < temperatures[j])
        //         {
        //             //cout << i << "," << j << ","<< currentTemperature << "," << temperatures[j] << endl; 
        //             result[i] = j-i;
        //             break;
        //         }
        //     }
        // }
        
        stack<int> st;

        for (int i = 0; i < size; i++) {
            // 현재 기온이 스택 맨 위에 있는 날의 기온보다 높다면
            while (!st.empty() && temperatures[st.top()] < temperatures[i]) {
                int prevIndex = st.top();
                st.pop();
                result[prevIndex] = i - prevIndex;
            }
            st.push(i);
        }

        return result;
    }
};