package kr.kro.colla.exception.exception.redis;

public class EmbeddedRedisPortException extends RuntimeException {

    public EmbeddedRedisPortException() {
        super("Embedded Redis를 실행할 수 있는 Port가 없습니다.");
    }
}
