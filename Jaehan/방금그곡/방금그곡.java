import java.util.*;

class Solution {
    public String solution(String m, String[] musicinfos) {
        // 음 : C, C#, D, D#, E, F, F#, G, G#, A, A#, B 12개
        // 각 음은 1분에 1개씩 재생되므로 악보길이를 알아야하며 구한 악보 길이로 반복된 멜로디를 구해야함
        // 1. 악보 길이가 실제 재생된 시간보다 짧을 때 -> 전체 멜로디를 다 알 수 있음
        // 2. 악보길이가 실제 재생된 시간보다 길 때 -> 네오가 알 수 있는 멜로디는 실제 재생된 시간만큼 잘린 길이
        
        String answer = "(None)";
        int maxPlayTime = -1; // 재생 시간이 가장 긴 음악을 찾기 위한 변수

        // 멜로디 m의 #음들을 한 글자 소문자로 치환
        m = replaceNote(m);

        for (String music : musicinfos) {
            String[] info = music.split(",");
            int playTime = changeToMin(info[0], info[1]); // 총 재생시간
            String songName = info[2];                     // 곡 이름
            String melody = replaceNote(info[3]);          // 악보 (#음 치환 적용)

            // 악보를 재생 시간(playTime)만큼 순환 반복(확장)하여 실제 재생된 멜로디 생성
            StringBuilder totalMelody = new StringBuilder();
            for (int i = 0; i < playTime; i++) {
                totalMelody.append(melody.charAt(i % melody.length()));
            }

            // 기억한 멜로디가 실제 재생된 멜로디에 포함되어 있는지 확인
            if (totalMelody.toString().contains(m)) {
                // 재생 시간이 더 긴 음악 우선 (시간이 같으면 먼저 입력된 음악 유지)
                if (playTime > maxPlayTime) {
                    maxPlayTime = playTime;
                    answer = songName;
                }
            }
        }

        return answer;
    }

    // 실제 재생된 시간을 분으로 변환하여 최종 재생시간을 반환할 함수
    private int changeToMin(String time1, String time2) {
        String startHour = time1.split(":")[0];
        String startMin = time1.split(":")[1];

        String endHour = time2.split(":")[0];
        String endMin = time2.split(":")[1];

        int startHourInt = Integer.parseInt(startHour) * 60;
        int startMinInt = Integer.parseInt(startMin);
        int totalStart = startHourInt + startMinInt;

        int endHourInt = Integer.parseInt(endHour) * 60;
        int endMinInt = Integer.parseInt(endMin);
        int totalEnd = endHourInt + endMinInt;

        return totalEnd - totalStart;
    }

    // #이 붙은 2글자 음을 1글자 소문자로 변환하는 함수
    private String replaceNote(String str) {
        return str.replace("C#", "c")
                  .replace("D#", "d")
                  .replace("F#", "f")
                  .replace("G#", "g")
                  .replace("A#", "a")
                  .replace("B#", "b");
    }
}