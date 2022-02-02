package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenException extends RuntimeException {
    private HttpStatus statusCode;

    public ForbiddenException(String message){
        super(message);
        this.statusCode = HttpStatus.FORBIDDEN;
    }
}