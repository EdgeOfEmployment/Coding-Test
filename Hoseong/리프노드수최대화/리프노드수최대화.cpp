#include <bits/stdc++.h>
using namespace std;

long long D, S;

map<tuple<long long,long long,long long>, long long> memo;


long long dfs(long long nodes, long long split, long long used)
{
    if (used > D || split > S)
        return 0;


    auto key = make_tuple(nodes, split, used);

    if (memo.count(key))
        return memo[key];


    // 현재 노드를 모두 리프로 종료
    long long ret = nodes;


    // 분배 노드 사용
    if (used + nodes <= D)
    {
        for (long long mul : {2LL,3LL})
        {
            long long child = nodes * mul;
            long long nextSplit = split * mul;


            if (nextSplit > S)
                continue;


            /*
                child 중 일부만 다음 분배 노드가 된다.
                나머지는 리프
            */
            for (long long next = 0; next <= child; next++)
            {
                long long leaves = child - next;

                if (next == 0)
                {
                    ret = max(ret, leaves);
                }
                else
                {
                    ret = max(
                        ret,
                        leaves + dfs(
                            next,
                            nextSplit,
                            used + nodes
                        )
                    );
                }
            }
        }
    }


    return memo[key] = ret;
}


int solution(int dist_limit, int split_limit)
{
    D = dist_limit;
    S = split_limit;

    /*
      root의 자식 1개가 최초 후보
    */
    return dfs(1,1,0);
}