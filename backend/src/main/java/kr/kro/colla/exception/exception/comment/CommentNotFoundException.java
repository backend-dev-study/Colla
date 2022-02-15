package kr.kro.colla.exception.exception.comment;

import kr.kro.colla.exception.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        super("해당하는 댓글을 찾을 수 없습니다.");
    }
}
