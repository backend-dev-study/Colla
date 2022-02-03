package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenException extends CustomException {

    public ForbiddenException(String message) { super(HttpStatus.FORBIDDEN, message); }
}