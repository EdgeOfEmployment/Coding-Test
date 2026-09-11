from itertools import product

def solution(users, emoticons):
    discount_rates = [10, 20, 30, 40]
    best_plus = 0
    best_revenue = 0
    
    # 가능한 모든 이모티콘 할인율 조합 생성 (중복 순열)
    for discounts in product(discount_rates, repeat=len(emoticons)):
        current_plus = 0
        current_revenue = 0
        
        # 각 사용자별로 구매 비용 계산
        for user_req_discount, user_budget in users:
            user_spend = 0
            
            for i, discount in enumerate(discounts):
                # 사용자가 원하는 할인율 이상일 경우 구매
                if discount >= user_req_discount:
                    # 할인된 가격을 누적
                    user_spend += emoticons[i] * (100 - discount) // 100
            
            # 구매 비용 합이 예산(가격) 이상이면 이모티콘 플러스 가입
            if user_spend >= user_budget:
                current_plus += 1
            else:
                current_revenue += user_spend
                
        # 1. 가입자 수를 최대로
        if current_plus > best_plus:
            best_plus = current_plus
            best_revenue = current_revenue
        # 2. 가입자 수가 같다면 판매액을 최대로
        elif current_plus == best_plus:
            if current_revenue > best_revenue:
                best_revenue = current_revenue
                
    return [best_plus, best_revenue]