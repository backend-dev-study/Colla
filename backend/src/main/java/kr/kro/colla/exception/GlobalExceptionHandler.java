package kr.kro.colla.exception;

import kr.kro.colla.exception.exception.auth.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionHandleResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        FieldError fieldError = e.getFieldError();
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.BAD_REQUEST.value(), fieldError.getField() + " : " + fieldError.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionHandleResponse> handleHttpClientErrorException(HttpClientErrorException e){
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionHandleResponse> handleUnauthorizedException(UnauthorizedException e) {
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

}
