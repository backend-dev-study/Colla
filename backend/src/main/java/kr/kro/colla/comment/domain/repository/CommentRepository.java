package kr.kro.colla.comment.domain.repository;

import kr.kro.colla.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
