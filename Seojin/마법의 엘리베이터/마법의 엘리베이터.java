class Solution {
    int ans = Integer.MAX_VALUE;
    void btr(String storey, int sum, int cnt,int flag){
        if (ans<sum) return;
        if (cnt == 0){
            if (flag == 1){
                sum++;
            }
            ans = Math.min(ans,sum);
            return;
        }
        int cur = storey.charAt(cnt-1)-'0';
        if (flag == 1){
            if (cur == 9){
                cur = 0;
                btr(storey,sum+cur, cnt-1,1);
                btr(storey,sum+(10-cur), cnt-1,1); 
            }
            else {
                cur++;
                btr(storey,sum+cur, cnt-1,0);
                btr(storey,sum+(10-cur), cnt-1,1);
            }
        }
        else {
            btr(storey,sum+cur, cnt-1,0);
            btr(storey,sum+(10-cur), cnt-1,1); 
        }

        
    }
    public int solution(int storey) {
        int answer = 0;
        String s_storey = String.valueOf(storey);

        btr(s_storey,0,s_storey.length(),0);
        
        answer = ans;
        return answer;
    }
}
