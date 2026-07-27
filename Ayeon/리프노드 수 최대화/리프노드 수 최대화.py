def solution(dist_limit, split_limit):
    max_leaves = 1
    
    # y: 3자식 분배 노드 층의 개수 (3^20 > 10^9)
    for y in range(23):
        if 3**y > split_limit:
            break
            
        # x: 2자식 분배 노드 층의 개수 (2^30 > 10^9)
        for x in range(32):
            base_leaves = (2**x) * (3**y)
            if base_leaves > split_limit:
                break
                
            # 완전히 채워진 트리의 분배 노드 수 계산
            # 3자식 층 y개를 먼저 채우고, 그 아래에 2자식 층 x개를 채운다고 가정
            nodes_3 = (3**y - 1) // 2
            nodes_2 = (3**y) * (2**x - 1)
            full_dist_nodes = nodes_3 + nodes_2
            
            # 1. 이미 꽉 채운 트리가 dist_limit을 만족하는 경우
            if full_dist_nodes <= dist_limit:
                max_leaves = max(max_leaves, base_leaves)
                
                # [핵심] 만약 dist_limit이 남았다면, 맨 아래 레벨의 리프 중 일부를 더 쪼갤 수 있음
                # 다음 레벨은 2자식으로 쪼개는 게 이득일지, 3자식으로 쪼개는 게 이득일지 둘 다 확인
                rem = dist_limit - full_dist_nodes
                
                # Case A: 맨 아래 리프 중 일부를 '2자식 분배 노드'로 쪼갬 (단 split_limit을 넘지 않아야 함)
                if base_leaves * 2 <= split_limit:
                    # 쪼갤 수 있는 최대 노드 수는 현재 리프 수(base_leaves)와 남은 개수(rem) 중 작은 값
                    add_nodes = min(base_leaves, rem)
                    # 2자식 노드 1개당 리프 노드는 1개씩 순증가함 (1개 사라지고 2개 생기므로)
                    max_leaves = max(max_leaves, base_leaves + add_nodes)
                    
                # Case B: 맨 아래 리프 중 일부를 3자식 분배 노드로 쪼갬
                if base_leaves * 3 <= split_limit:
                    add_nodes = min(base_leaves, rem)
                    # 3자식 노드 1개당 리프 노드는 2개씩 순증가함 (1개 사라지고 3개 생기므로)
                    max_leaves = max(max_leaves, base_leaves + add_nodes * 2)
            
            # 2. 꽉 채우기엔 dist_limit이 부족한 경우
            else:
                # 3자식 층 y개는 다 채울 수 있는지 확인
                if nodes_3 <= dist_limit:
                    rem = dist_limit - nodes_3
                    # 현재 3자식만 채운 상태의 리프 수는 3^y 개임
                    # 그 아래 레벨(2자식 층)을 다 채우진 못하고 rem개만큼만 부분적으로 2자식 노드를 배치하는 경우
                    # 단, 이 경우도 다음 쪼개진 리프의 분배도(3^y * 2)가 split_limit 이하인 경우에만 가능
                    if (3**y) * 2 <= split_limit:
                        max_leaves = max(max_leaves, 3**y + rem)
                        
    return max_leaves
