import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 10,  // 동시 사용자 수
    duration: '30s', // 30초 동안 부하 테스트
};

const BASE_URL = 'http://spring-boot-app:8080'; // 서버 주소 (배포 환경에 맞게 변경)

// 🎯 Banner API 테스트
export default function () {
    testBannerAPI();
    sleep(1); // 각 요청 사이에 1초 대기
}

// 🎯 Banner API 테스트 함수
function testBannerAPI() {
    let url = `${BASE_URL}/api/v1/banners`;

    let payload = JSON.stringify({
        itemName: "스킨케어",
        itemConcept: "촉촉한",
        itemCategory: "화장품",
        addInformation: "3일간만 진행되는 이벤트",
        imageId: 1
    });

    let params = {
        headers: { 'Content-Type': 'application/json' }
    };

    let res = http.post(url, payload, params);

    check(res, {
        'Banner API 응답 상태가 200인가?': (r) => r.status === 200 || r.status === 201
    });
}
