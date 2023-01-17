package io.eagle.wealthmarblebackend.exception;

import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
