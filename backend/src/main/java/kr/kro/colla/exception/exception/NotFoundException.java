package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private HttpStatus statusCode;

    public NotFoundException(String message){
        super(message);
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
