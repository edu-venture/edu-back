package com.bit.eduventure.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NULL_TOKEN(1000, "헤더에 토큰이 없습니다."),
    UNKNOWN_ERROR(1003, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(1004, "변조된 토큰입니다."),
    EXPIRED_TOKEN(1005, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(1006, "변조된 토큰입니다."),
    ACCESS_DENIED(1007, "권한이 없습니다."),
    NO_SUCH_EXCEPTION(1008,"저장된 내용이 없습니다."),
    CLASS_CAST_EXCETPION(1009, "요청하신 내용의 타입이 틀립니다."),
    EXCEPTION(9999, "요청 오류 입니다.");

    private int code;
    private String message;
}
