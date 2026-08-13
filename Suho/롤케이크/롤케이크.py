from collections import Counter
def solution(topping):
    #해시맵 + set
    answer = 0
    #set사용해서 없애기?
    #둘로 나누고, counter했을때, key값의 갯수가 같아야함.
    right =Counter(topping)

    left = set()

    for current_topping in topping:
        # 왼쪽으로 이동
        left.add(current_topping)
        # 오른쪽에서 감소
        right[current_topping]-=1
        
        if right[current_topping] ==0:
            del right[current_topping]
        # 양쪽의 종류 수 비교
        if len(left) == len(right):
            answer+=1
    
    return answer