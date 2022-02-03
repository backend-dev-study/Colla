package kr.kro.colla.exception.exception.task.tag;

public class InvalidTagFormatException extends RuntimeException {

    public InvalidTagFormatException() {
        super("잘못된 형식의 태그 리스트입니다.");
    }

}
