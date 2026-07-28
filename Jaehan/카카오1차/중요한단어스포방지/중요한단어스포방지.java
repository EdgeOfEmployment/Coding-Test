import java.util.*;

class Solution {
    // 1. 한 단어가 여러 스포 구간에 걸쳐 있을 수 있음 -> 겹치는 구간 중 "가장 나중에 클릭되는
    //    구간"(배열에서 제일 뒤 인덱스, 이미 왼쪽->오른쪽 클릭 순서로 정렬돼서 옴) 시점에 완전히 공개됨
    // 2. 겹치는 구간이 하나도 없는 단어 = 스포 방지 구간 밖에서 등장한 단어 (조건 2 위반용으로 미리 모아둠)
    // 3. 중요한 단어 조건: 스포 단어 O, 밖에서 등장한 적 X, 이전에 공개된 스포 단어와 중복 X
    // 4. 같은 시점에 여러 단어가 동시에 공개되면 왼쪽부터 순서대로 판정 (동일 텍스트 중복 처리 때문에 순서 중요)
    public int solution(String message, int[][] spoiler_ranges) {
        List<int[]> words = new ArrayList<>(); // [start, end] inclusive
        List<String> texts = new ArrayList<>();
        splitWords(message, words, texts);

        int wordCount = words.size();
        int rangeCount = spoiler_ranges.length;

        Set<String> outsideTexts = new HashSet<>();
        int[] lastRangeOf = new int[wordCount];
        Arrays.fill(lastRangeOf, -1);

        for (int i = 0; i < wordCount; i++) {
            int ws = words.get(i)[0];
            int we = words.get(i)[1];
            int last = -1;
            for (int r = 0; r < rangeCount; r++) {
                int rs = spoiler_ranges[r][0];
                int re = spoiler_ranges[r][1];
                if (rs <= we && ws <= re) {
                    last = r;
                }
            }
            if (last == -1) {
                outsideTexts.add(texts.get(i));
            } else {
                lastRangeOf[i] = last;
            }
        }

        // 구간 인덱스별로 "이 구간이 클릭되는 순간 완전히 공개되는 단어들"을 모아둠
        List<List<Integer>> wordsByLastRange = new ArrayList<>();
        for (int r = 0; r < rangeCount; r++) {
            wordsByLastRange.add(new ArrayList<>());
        }
        for (int i = 0; i < wordCount; i++) {
            if (lastRangeOf[i] != -1) {
                wordsByLastRange.get(lastRangeOf[i]).add(i);
            }
        }

        Set<String> revealedTexts = new HashSet<>();
        int answer = 0;

        for (int r = 0; r < rangeCount; r++) {
            for (int idx : wordsByLastRange.get(r)) { // already left to right
                String text = texts.get(idx);
                if (outsideTexts.contains(text) || revealedTexts.contains(text)) {
                    continue;
                }
                revealedTexts.add(text);
                answer++;
            }
        }

        return answer;
    }

    private void splitWords(String message, List<int[]> words, List<String> texts) {
        int n = message.length();
        int i = 0;
        while (i < n) {
            if (message.charAt(i) == ' ') {
                i++;
                continue;
            }
            int j = i;
            while (j < n && message.charAt(j) != ' ') {
                j++;
            }
            words.add(new int[]{i, j - 1});
            texts.add(message.substring(i, j));
            i = j;
        }
    }
}
