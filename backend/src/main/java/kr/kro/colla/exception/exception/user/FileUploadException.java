package kr.kro.colla.exception.exception.user;

public class FileUploadException extends RuntimeException {

    public FileUploadException () {
        super("파일 업로드에 실패하였습니다.");
    }

}
