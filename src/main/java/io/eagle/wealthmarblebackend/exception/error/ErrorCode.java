package io.eagle.wealthmarblebackend.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Global
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터입니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    // User

    // Security


    // Vacation
    VACATION_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 공모 ID 입니다."),
    VACATION_PERIOD_INVALID(HttpStatus.BAD_REQUEST,"공모 기간이 올바르지 않습니다."),

    // Order


    // Transaction


    // Price Info


    ;
    private final HttpStatus httpStatus;
    private final String message;
}
