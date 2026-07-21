function solution(message, spoiler_ranges) {
    var answer = 0;

    // 구간에 겹치는 단어와 안겹치는 단어 분리
    // 겹치는 단어 중 안겹치는 단어들에 없으면 answer++

    // 1. 단어와 해당 단어의 시작/끝 인덱스 추출
    let wordsInfo = [];
    let currentIndex = 0;
    let words = message.split(' ');

    for (let word of words) {
        let startIdx = currentIndex;
        let endIdx = currentIndex + word.length - 1;
        wordsInfo.push({ word, start: startIdx, end: endIdx });

        // 다음 단어의 시작 인덱스 = 현재 단어 길이 + 공백(1칸)
        currentIndex += word.length + 1;
    }

    let normalWords = new Set(); // 일반 구간에 등장한 단어 목록
    let spoilerCandidates = []; // 스포 방지 구간에 걸친 단어들

    // 2. 스포 방지 단어와 일반 단어 분류 (겹침 판별)
    for (let { word, start, end } of wordsInfo) {
        let isSpoiler = false;

        for (let [s, e] of spoiler_ranges) {
            // 단어의 구간[start, end]와 스포 방지 구간[s, e]이 겹치는지 확인
            if (start <= e && end >= s) {
                isSpoiler = true;
                break;
            }
        }

        if (isSpoiler) {
            spoilerCandidates.push(word); // 왼쪽부터 순서대로 담기
        } else {
            normalWords.add(word); // 스포가 전혀 없는 일반 단어로 등록
        }
    }

    let revealedWords = new Set(); // 이전에 공개 처리된 중요 단어 기록

    // 3. 조건 검사 및 카운트
    for (let word of spoilerCandidates) {
        // [조건 2] 스포 방지 구간이 아닌 곳에 등장한 적이 없어야 함
        if (!normalWords.has(word)) {
            // [조건 3] 이전에 공개된 스포 방지 단어와 중복되지 않아야 함
            if (!revealedWords.has(word)) {
                answer++;
                revealedWords.add(word); // 공개된 중요 단어로 등록
            }
        }
    }

    return answer;
}
