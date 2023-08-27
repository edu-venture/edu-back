package com.bit.eduventure.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NULL_TOKEN(1000, "헤더에 토큰이 없습니다."),
    UNKNOWN_ERROR(1001, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(1002, "변조된 토큰입니다."),
    EXPIRED_TOKEN(1003, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1004, "변조된 토큰입니다."),
    ACCESS_DENIED(1005, "권한이 없습니다."),
    NULL_POINT(2000, "NULL 값입니다."),
    NO_SUCH_ELEMENT(2001,"찾는 데이터가 없습니다."),
    CLASS_CAST(2002, "데이터 타입이 틀립니다."),
    STACK_OVER_FLOW(2003, "스택 오버 플로우. 백엔드 문제일 확률 높음"),
    DATA_FORMAT(2004, "입력한 데이터 형식이 잘못 되었습니다. 아마 프론트 문제 확률 높음"),
    RUN_TIME(2005, "런타임 오류입니다. 서버 콘솔창을 확인해보세요."),
    MAKE_SIGNATURE(3000, "NCP API 요청 시 생성할 SIGNATURE 오류입니다."),
    EXCEPTION(9999, "예외입니다. 백엔드에서 세부화하든 프론트 문제든 난 몰라");

    private int code;
    private String message;
}
