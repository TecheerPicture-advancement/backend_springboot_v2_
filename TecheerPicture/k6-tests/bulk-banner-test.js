import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 10,             // 동시 사용자 수
    duration: '30s'      // 테스트 시간
};

const BASE_URL = 'http://spring-boot-app:8080'; // Docker 내 컨테이너 이름 사용

export default function () {
    testBulkBannerAPI();
    sleep(1);
}

function testBulkBannerAPI() {
    const url = `${BASE_URL}/api/v1/banners/collection`;

    const payload = JSON.stringify({
        requests: [
            {
                itemName: "선크림",
                itemConcept: "자연스러운",
                itemCategory: "화장품",
                addInformation: "봄 한정",
                imageId: 2
            },
            {
                itemName: "립밤",
                itemConcept: "매트한",
                itemCategory: "화장품",
                addInformation: "1+1 이벤트",
                imageId: 2
            },
            {
                itemName: "마스크팩",
                itemConcept: "진정 케어",
                itemCategory: "화장품",
                addInformation: "피부 자극 최소화",
                imageId: 2
            }
        ]
    });

    const params = {
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const res = http.post(url, payload, params);

    check(res, {
        '배너 bulk API 응답 상태가 201인가?': (r) => r.status === 201,
        '응답에 배열 데이터가 포함되어 있는가?': (r) => r.body.includes('[')
    });
}
