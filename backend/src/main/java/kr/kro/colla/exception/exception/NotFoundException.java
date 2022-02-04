package kr.kro.colla.exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(String message) { super(HttpStatus.NOT_FOUND, message); }
}
