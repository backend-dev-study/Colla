package kr.kro.colla.error.exception.auth;

public class AuthPermissionDeniedException extends RuntimeException{

    public AuthPermissionDeniedException(){ super("permission denied"); }
}
