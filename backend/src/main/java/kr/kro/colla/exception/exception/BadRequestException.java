package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends CustomException {
    public BadRequestException(String message) { super(HttpStatus.BAD_REQUEST, message); }
}
