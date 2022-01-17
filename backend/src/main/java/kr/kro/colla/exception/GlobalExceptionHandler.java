package kr.kro.colla.exception;

import kr.kro.colla.exception.exception.NotFoundException;
import kr.kro.colla.exception.exception.auth.UnauthorizedException;
import kr.kro.colla.exception.exception.user.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionHandleResponse> handleBindException (BindException e) {
        FieldError fieldError = e.getFieldError();
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.BAD_REQUEST.value(), fieldError.getField() + " : " + fieldError.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionHandleResponse> handleHttpClientErrorException (HttpClientErrorException e) {
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionHandleResponse> handleUnauthorizedException (UnauthorizedException e) {
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionHandleResponse> handleNotFoundException (NotFoundException e) {
        ExceptionHandleResponse response = new ExceptionHandleResponse(e.getStatusCode().value(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionHandleResponse> handleFileUploadException (FileUploadException e) {
        ExceptionHandleResponse response = new ExceptionHandleResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
