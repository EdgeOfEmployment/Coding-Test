def solution(sequence):
    # 1로 시작하는 펄스 수열을 곱한 배열 ([1, -1, 1, ...])
    seq1 = [s * (1 if i % 2 == 0 else -1) for i, s in enumerate(sequence)]
    
    # -1로 시작하는 펄스 수열을 곱한 배열 ([-1, 1, -1, ...])
    seq2 = [s * (-1 if i % 2 == 0 else 1) for i, s in enumerate(sequence)]
    
    # 카데인 알고리즘 정의
    def kadane(arr):
        max_sum = arr[0]     # 0번 인덱스를 초기 최댓값으로 설정
        current_sum = arr[0] # 0번 인덱스를 초기 부분 수열 합으로 설정
        
        # 1번 인덱스부터 끝까지 순회 (0번은 이미 포함됨)
        for x in arr[1:]:
            current_sum = max(x, current_sum + x)
            max_sum = max(max_sum, current_sum)
            
        return max_sum

    # 두 배열 중 더 큰 연속 부분 수열의 합을 반환
    return max(kadane(seq1), kadane(seq2))