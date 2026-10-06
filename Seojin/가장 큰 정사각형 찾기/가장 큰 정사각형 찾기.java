class Solution
{
    public int solution(int [][]board)
    {
        int answer = board[0][0];
        int[][] w = new int[board.length][board[0].length];
        
        for (int i=0;i<board.length;i++){
            w[i][0]=board[i][0];
        }
        
        for (int i=0;i<board[0].length;i++){
            w[0][i]=board[0][i];
        }
        
        for (int i=1;i<board.length;i++){
            for (int j=1;j<board[0].length;j++){
                if (board[i][j]==0){
                    w[i][j] = 0;
                }
                else {
                    int cur = Math.min(w[i-1][j],w[i][j-1]);
                    cur = Math.min(cur,w[i-1][j-1]);
                    w[i][j]=cur+1;
                }
                
            }
        }
        
        int res = answer;
        for (int i=1;i<board.length;i++){
            for (int j=1;j<board[0].length;j++){
                res = Math.max(res,w[i][j]);
            }
        }
        
        answer =res*res;

        return answer;
    }
}
