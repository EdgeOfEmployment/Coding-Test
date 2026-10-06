class Solution
{
    public int solution(int [][]board){
        int answer = 0;
        int n = board.length;
        int m = board[0].length;

        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(board[i][j] == 1){
                    if(i>0 && j>0){
                        // 오른쪽 아래 칸을 기준으로 위, 왼쪽, 왼쪽 위 확인
                        int leftup = board[i-1][j-1];
                        int up = board[i-1][j];
                        int left = board[i][j-1];

                        int min = Math.min(Math.min(leftup, up), left);

                        board[i][j] = min + 1;


                    }
                    answer = Math.max(answer, board[i][j]);
                }
            }

        }

        return answer * answer;
    }
}