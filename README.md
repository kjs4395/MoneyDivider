# MoneyDivider
머니뿌리기 구현

## Table Structure [[ERD](./money_divider_erd.png)][[SQL](./money_divider_init.sql)]
```
- money
- money_divide_info
- room
- room_member
- user
```

## Project Requirements
```
- Gradle 6.5
- Spring boot 2.3.6
- MySQL 5.7
- jOOQ 3.13
- Redis
- Lombok
```

## 테스트시 실행 순서
1. Redis 실행 (localhost:6379)
1. MySQL 실행 (localhost:3306)
1. MySQL 사용자 생성 (admin/1234)
1. MySQL 테이블 생성 (*첨부된 [SQL](./money_divider_init.sql) 파일 참조)
1. jOOQ 빌드 (`jooq > generateJooq` Gradle task 실행)
1. [http](./src/http) 파일을 참고하여 API 호출
1. API 호출 순서
    1. 사용자 생성
    1. 룸 생성
    1. 룸 입장
    1. 뿌리기 정보 생성
    1. 뿌린 금액 받기
    1. 뿌리기 정보 조회
        - token에 특수문자가 포함되므로 URL 인코딩 해서 전달 필요

## API 구성
| url | method | 의미 | http파일링크
|---|:---:|:---:|:---:|
| /money | POST | 뿌리기 생성|[http파일](./src/http/MoneyRequestTest.http)
| /money/divide | PUT |뿌린 금액 받기|[http파일](./src/http/MoneyDivideTest.http)
| /money/{token} | GET |  뿌리기 정보 조회|[http파일](./src/http/MoneyInfoTest.http)
| /room | POST |  룸 생성|[http파일](./src/http/RoomTest.http)
| /room/member | POST |  룸 입장|[http파일](./src/http/RoomMemberTest.http)
| /user | POST |  사용자 생성|[http파일](./src/http/UserTest.http)


## 핵심문제해결전략
 1. token 생성 규칙
    1. Ascii 코드 기반 출력 가능한 문자열로 구성
    2. token 생성기 내부에 미리 만들어둔 문자 테이블을 기반으로 랜덤하게 token 생성
    3. 나노타임을 시드값으로 사용해 만든 랜덤값을 인덱스로 사용해 랜덤한 문자 3개를 뽑아 token 으로 사용
 2. 뿌리기 생성 규칙
    1. java 랜덤 함수를 기반으로 뿌릴 돈을 랜덤하게 할당
    2. 뿌린 금액을 받아갈 seq 정보 redis에 저장
    3. 랜덤하게 할당된 금액에 대해서는 DB로 관리 
 3. 응답 규칙
    1. ApiResponse로 실행결과를 명확히 알 수 있도록 모든 요청에 동일한 응답 전송
    2. 예외 상황일 시 ApiException을 통해 정의된 응답값으로 반환


## 상황별 응답 코드
| 예외상황 | exception | 에러코드
|---|:---:|:---:|
|성공|SUCCESS|200|
|기타 서버 에러|FAIL|500|
|유효하지 않은 토큰|INVALID_TOKEN|10001|
|유효하지 않은 사용자 생성 요청|INVALID_USER_REQUEST|10002|
|유효하지 않은 룸 생성 요청|INVALID_ROOM_REQUEST|10003|
|유효하지 않음 룸 입장 요청|INVALID_JOIN_ROOM_REQUEST|10004|
|유효하지 않은 뿌리기 요청|INVALID_MONEY_REQUEST|10005|
|ID 정보가 없는 경우|INVALID_MONEY_REQUEST_ID|10006|
|유효하지 않은 뿌린 금액 받기 요청|INVALID_MONEY_DIVIDE_REQUEST|10007|
|유효하지 않은 뿌리기 정보|MONEY_EXPIRED|20001|
|조회 가능한 뿌리기 정보가 없는 경우|MONEY_NOT_FOUND|20002|
|뿌린 금액 받기 에러|MONEY_RECEIVE_FAIL|20003|
|받을 수 있는 금액이 없는 경우|EMPTY_NEXT_MONEY_SEQ|20003|
|자신이 뿌리기한 건을 자신이 받으려 할 때|NOT_ALLOWED_DIVIDE_BY_OWNER|20004|
|뿌리기가 호출된 대화방에 속하지 않은 사용자 요청|NOT_IN_ROOM_MEMBER|20005|
|이미 한 번 받아간 사용자 요청|ALREADY_RECEIVE_USER|20006|
