package io.eagle.exception;

import io.eagle.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3Exception extends RuntimeException{
    private final ErrorCode errorCode;
}