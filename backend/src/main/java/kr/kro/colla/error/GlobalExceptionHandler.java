package kr.kro.colla.error;

import kr.kro.colla.error.exception.auth.AuthPermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        FieldError fieldError = e.getFieldError();
        ErrorResponse response = new ErrorResponse(400, fieldError.getField()+" : "+fieldError.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthPermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthPermissionDeniedException(AuthPermissionDeniedException e){
        ErrorResponse response = new ErrorResponse(400, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
