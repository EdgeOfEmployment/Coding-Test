import java.util.*;

class Solution {
    
    // 기둥과 보 구조물 상태를 저장할 배열 또는 집합
    // 여기서는 3차원 배열을 사용하여 [x][y][종류]로 표현 (0: 기둥, 1: 보)
    boolean[][] pillar;
    boolean[][] bo;

    public int[][] solution(int n, int[][] build_frame) {
        // 규칙
        // 기둥은 바닥 위에 있거나 보의 한쪽 끝 부분 위에 있거나, 또는 다른 기둥 위에 있어야 합니다. --> 기둥이 보 중간에 오는 경우는 없음
        // 보는 한쪽 끝 부분이 기둥 위에 있거나, 또는 양쪽 끝 부분이 다른 보와 동시에 연결되어 있어야 합니다.
        // 설치 시 가능 여부는 설치 전 좌우 좌표를 체크해야함
        // 삭제 시 가능 여부를 판단하기 위해선 해당 좌표에 대한 삭제 연산 후 좌우 좌표의 연산 결과가 규칙에 유효한지 체크해야함
        
        // [x,y,a,b] => [가로 좌표, 세로 좌표, (0 = 기둥, 1 = 보), (0 = 삭제, 1 = 설치)]
        // return => [x,y,a] 형태
        // -> x좌표 기준으로 오름차순 정렬하며, x좌표가 같을 경우 y좌표 기준으로 오름차순 정렬 + x, y좌표가 모두 같은 경우 기둥이 보보다 앞에 위치
        
        pillar = new boolean[n + 3][n + 3];
        bo = new boolean[n + 3][n + 3];

        for (int[] frame : build_frame) {
            int x = frame[0];
            int y = frame[1];
            int a = frame[2]; // a: 0(기둥), 1(보)
            int b = frame[3]; // b: 0(삭제), 1(설치)

            if (b == 1) { // 설치
                if (a == 0) { // 기둥 설치
                    if (canInstallPillar(x, y)) {
                        pillar[x][y] = true;
                    }
                } else { // 보 설치
                    if (canInstallBo(x, y)) {
                        bo[x][y] = true;
                    }
                }
            } else { // 삭제
                if (a == 0) { // 기둥 삭제
                    pillar[x][y] = false; // 일단 삭제해 봄
                    if (!isValid(n)) {
                        pillar[x][y] = true; // 불가능하면 원상복구
                    }
                } else { // 보 삭제
                    bo[x][y] = false; // 일단 삭제해 봄
                    if (!isValid(n)) {
                        bo[x][y] = true; // 불가능하면 원상복구
                    }
                }
            }
        }

        // 결과 리스트 생성
        List<int[]> resultList = new ArrayList<>();
        for (int x = 0; x <= n; x++) {
            for (int y = 0; y <= n; y++) {
                if (pillar[x][y]) resultList.add(new int[]{x, y, 0});
                if (bo[x][y]) resultList.add(new int[]{x, y, 1});
            }
        }

        // 정렬 조건 적용: x 오름차순, y 오름차순, 구조물 종류(기둥 0, 보 1) 오름차순
        resultList.sort((o1, o2) -> {
            if (o1[0] != o2[0]) return Integer.compare(o1[0], o2[0]);
            if (o1[1] != o2[1]) return Integer.compare(o1[1], o2[1]);
            return Integer.compare(o1[2], o2[2]);
        });

        int[][] answer = new int[resultList.size()][3];
        for (int i = 0; i < resultList.size(); i++) {
            answer[i] = resultList.get(i);
        }

        return answer;
    }

    // 기둥 설치 가능 여부 확인
    private boolean canInstallPillar(int x, int y) {
        // 1. 바닥 위인 경우 (y == 0)
        if (y == 0) return true;
        // 2. 다른 기둥 위인 경우
        if (y > 0 && pillar[x][y - 1]) return true;
        // 3. 보의 한쪽 끝 부분 위인 경우 (왼쪽 보가 있거나, 바로 아래에 보가 있는 경우)
        if ((x > 0 && bo[x - 1][y]) || bo[x][y]) return true;
        return false;
    }

    // 보 설치 가능 여부 확인
    private boolean canInstallBo(int x, int y) {
        // 1. 한쪽 끝 부분이 기둥 위에 있는 경우 (아래에 기둥이 있거나, 오른쪽 아래에 기둥이 있는 경우)
        if ((y > 0 && pillar[x][y - 1]) || (y > 0 && pillar[x + 1][y - 1])) return true;
        // 2. 양쪽 끝 부분이 다른 보와 동시에 연결되어 있는 경우
        if (x > 0 && bo[x - 1][y] && bo[x + 1][y]) return true;
        return false;
    }

    // 전체 구조물이 규칙을 만족하는지 검사
    private boolean isValid(int n) {
        for (int x = 0; x <= n; x++) {
            for (int y = 0; y <= n; y++) {
                if (pillar[x][y] && !canInstallPillar(x, y)) return false;
                if (bo[x][y] && !canInstallBo(x, y)) return false;
            }
        }
        return true;
    }
}