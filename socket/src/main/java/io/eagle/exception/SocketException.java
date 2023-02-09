package io.eagle.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SocketException extends RuntimeException{
    private final String message;
}
