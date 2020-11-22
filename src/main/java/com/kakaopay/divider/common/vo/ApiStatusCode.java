package com.kakaopay.divider.common.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created By kjs4395 on 2020-11-22
 */
@Getter
@RequiredArgsConstructor
public enum ApiStatusCode {
    SUCCESS(200, "성공"),
    FAIL(500, "기타 서버 에러"),

    // validation 에러
    INVALID_TOKEN(10001, "유효하지 않은 토큰입니다."),
    INVALID_USER_REQUEST(10002, "유효하지 않은 사용자 생성 요청입니다."),
    INVALID_ROOM_REQUEST(10003, "유효하지 않은 룸 생성 요청입니다."),
    INVALID_JOIN_ROOM_REQUEST(10004, "유효하지 않음 룸 입장 요청입니다."),
    INVALID_MONEY_REQUEST(10005, "유효하지 않은 뿌리기 요청입니다."),
    INVALID_MONEY_REQUEST_ID(1006, "ID 정보가 없습니다."),
    INVALID_MONEY_DIVIDE_REQUEST(10007, "유효하지 않은 뿌린 금액 받기 요청입니다."),

    // 뿌리기 에러
    MONEY_EXPIRED(20001, "유효하지 않은 뿌리기입니다."),
    MONEY_NOT_FOUND(20002, "조회 가능한 뿌리기 정보가 없습니다."),
    MONEY_RECEIVE_FAIL(20003, "뿌린 금액을 받는데 실패했습니다."),
    EMPTY_NEXT_MONEY_SEQ(20003, "받을 수 있는 금액이 없습니다."),
    NOT_ALLOWED_DIVIDE_BY_OWNER(20004, "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    NOT_IN_ROOM_MEMBER(20005, "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    ALREADY_RECEIVE_USER(20006, "이미 받아간 사용자입니다."),
    ;

    private final int code;
    private final String mesage;
}
