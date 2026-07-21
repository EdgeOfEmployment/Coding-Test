import java.util.*;

public class 중요한단어를스포방지 {
    public int solution(String message, int[][] spoiler_ranges) {
        int answer = 0;

        List<String> textWords = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();

        int len = message.length();
        int start = -1;

        // 단어 및 인덱스 추출
        for (int i = 0; i < len; i++) {
            char ch = message.charAt(i);
            if (ch != ' ') {
                if (start == -1) {
                    start = i;
                }
               // 지금 글자가 문장의 맨 끝이거나, 바로 다음 글자가 공백이라면? (단어가 끝났다는 뜻)
                if (i == len - 1 || message.charAt(i + 1) == ' ') {
                    textWords.add(message.substring(start, i + 1));
                    starts.add(start);
                    ends.add(i);
                    start = -1; //단어 하나 끝났으니 다음 단어 찾으러 갈 준비
                }
            }
        }

        Set<String> blacklist = new HashSet<>();
        int totalWords = textWords.size();

        // 스포일러 구역과 안겹치는 단어 조사
        for (int i = 0; i < totalWords; i++) {
            boolean isSpoilerWord = false;

            // 이 단어가 혹시 지정된 스포일러 구역들과 단 1글자라도 겹치는지 전부 대조해봄
            for (int[] range : spoiler_ranges) {
                if (starts.get(i) <= range[1] && ends.get(i) >= range[0]) {
                    isSpoilerWord = true;
                    break;
                }
            }
            if (!isSpoilerWord) {
                blacklist.add(textWords.get(i)); //blacklist 주머니 안에는 대놓고 노출되었던 평범한 단어들이 모두 격리
            }
        }

        // 스포일러 구간 순회하며 카운트
        Set<String> visitedSpoilers = new HashSet<>();

        for (int[] range : spoiler_ranges) {
            for (int i = 0; i < totalWords; i++) {
                if (starts.get(i) <= range[1] && ends.get(i) >= range[0]) {
                    String word = textWords.get(i);

                    if (blacklist.contains(word)) continue;
                    if (visitedSpoilers.contains(word)) continue;

                    answer++;
                    visitedSpoilers.add(word);
                }
            }
        }
        return answer;
    }
}
