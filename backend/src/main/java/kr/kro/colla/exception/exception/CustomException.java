package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private HttpStatus httpStatus;

    public CustomException (HttpStatus httpStatus, String message) {
        super(message);
        System.out.println(message+" "+ httpStatus);

        this.httpStatus = httpStatus;
    }
}
