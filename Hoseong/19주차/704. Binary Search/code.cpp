#include <vector>
#include <algorithm>

using namespace std;

class Solution {
public:
    int search(vector<int>& nums, int target) {
        // O(nlogn) 이미 정렬된 nums임 필요없음
        // 영어를 잘하자..
        // sort(nums.begin(), nums.end());

        // O(n)
        // for(int i = 0; i < nums.size(); i++)
        // {
        //     if(nums[i] == target)
        //         return i;
        // }
        

        int left = 0;
        int right = static_cast<int>(nums.size()) - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid; 
            } else if (nums[mid] < target) {
                left = mid + 1; 
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }
};