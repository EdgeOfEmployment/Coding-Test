function solution(fees, records) {
    const [basicTime, basicFee, unitTime, unitFee] = fees;

    const parking = new Map(); // 현재 주차장 내 차량 관리 (차량번호 -> 입차시간)
    const totalTimes = new Map(); // 차량별 누적 주차 시간 관리 (차량번호 -> 누적시간)

    // 1. 입출차 기록 처리
    for (const record of records) {
        const [timeStr, car, action] = record.split(' ');
        const [hh, mm] = timeStr.split(':').map(Number);
        const time = hh * 60 + mm; // 분 단위로 변환

        if (action === 'IN') {
            parking.set(car, time);
        } else {
            // "OUT"
            const inTime = parking.get(car);
            parking.delete(car);

            const parkedTime = time - inTime;
            totalTimes.set(car, (totalTimes.get(car) || 0) + parkedTime);
        }
    }

    // 2. 출차 기록이 없는 차량 처리 (23:59 출차 간주)
    const endOfDay = 23 * 60 + 59; // 1439분
    for (const [car, inTime] of parking.entries()) {
        const parkedTime = endOfDay - inTime;
        totalTimes.set(car, (totalTimes.get(car) || 0) + parkedTime);
    }

    // 3. 차량 번호 오름차순 정렬 및 요금 계산
    const sortedCars = Array.from(totalTimes.keys()).sort();
    const result = [];

    for (const car of sortedCars) {
        const time = totalTimes.get(car);

        if (time <= basicTime) {
            // 기본 시간 이내라면 기본 요금만 청구
            result.push(basicFee);
        } else {
            // 초과 시간에 대한 요금 계산 (올림 처리)
            const extraTime = time - basicTime;
            const extraFee = Math.ceil(extraTime / unitTime) * unitFee;
            result.push(basicFee + extraFee);
        }
    }

    return result;
}
