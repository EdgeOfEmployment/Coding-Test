import java.util.*;

class Solution {
    // 원판의 이동을 남기기 위해 리스트 작성
    List<int[]>list = new ArrayList<>();

    public int[][] solution(int n) {
        int[][] answer = {};

        hanoi(n, 1, 2, 3);

        // 리스트에 저장한걸 이차원 배열로 바꾸기
        return list.toArray(new int[list.size()][]);
    }

    public void hanoi(int n, int start, int mid, int end){
        if(n==1){
            list.add(new int[]{start, end});
        }else{
            // 출발점에 있던 작은 원판을 중간으로
            hanoi(n-1, start, end, mid);
            // 처음에 있던 큰 원판을 마지막으로
            list.add(new int[]{start, end});
            // 중간에 있던 작은 원판을 마지막으로
            hanoi(n-1, mid, start, end);
        }
    }
}