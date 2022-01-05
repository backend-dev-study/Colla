package kr.kro.colla.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionHandleResponse {
    private final int status;
    private final String message;
}
