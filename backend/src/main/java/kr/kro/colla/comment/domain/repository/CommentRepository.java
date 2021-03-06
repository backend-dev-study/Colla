package kr.kro.colla.comment.domain.repository;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.task.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.task = :task order by c.createAt asc")
    List<Comment> findAll(@Param("task") Task task);

}
